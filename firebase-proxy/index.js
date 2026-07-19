/**
 * Taro AI — Secure Gemini Proxy (Firebase Cloud Function, gen2)
 *
 * Responsibilities:
 *  1. AUTH   — verify the caller's Firebase ID token (Bearer <token>).
 *  2. ACCOUNTING / QUOTA — meter usage per user in Firestore and enforce a
 *              free-tier daily limit (premium users get a high cap).
 *  3. PROXY  — forward the request to the Gemini API using a server-side key
 *              (Secret Manager), so the key never ships inside the APK.
 */
const { onRequest } = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin");
const axios = require("axios");

admin.initializeApp();
const db = admin.firestore();

// ---- Config -------------------------------------------------------------
const FREE_DAILY_LIMIT = parseInt(process.env.FREE_DAILY_LIMIT || "20", 10);
const PREMIUM_DAILY_LIMIT = parseInt(process.env.PREMIUM_DAILY_LIMIT || "500", 10);
// gemini-3.5-flash does not exist; transparently map legacy names to a real model.
const MODEL_ALIASES = {
  "gemini-3.5-flash": "gemini-2.5-flash",
  "gemini-3.5-flash-latest": "gemini-2.5-flash",
  "gemini-flash": "gemini-2.5-flash",
};
const GEMINI_HOST = "https://generativelanguage.googleapis.com";

function todayUTC() {
  return new Date().toISOString().slice(0, 10); // YYYY-MM-DD
}

/**
 * Atomically record one usage unit for a user and enforce the daily quota.
 * Returns { allowed, tier, dayCount, limit }.
 */
async function meterUsage(uid, email) {
  const ref = db.collection("users").doc(uid);
  return db.runTransaction(async (tx) => {
    const snap = await tx.get(ref);
    const now = admin.firestore.FieldValue.serverTimestamp();
    const day = todayUTC();
    let data = snap.exists ? snap.data() : null;

    if (!data) {
      data = { email: email || null, tier: "free", totalReadings: 0, day, dayCount: 0, createdAt: now };
    }
    if (data.day !== day) { data.day = day; data.dayCount = 0; }

    const tier = data.tier === "premium" ? "premium" : "free";
    const limit = tier === "premium" ? PREMIUM_DAILY_LIMIT : FREE_DAILY_LIMIT;

    if (data.dayCount >= limit) {
      return { allowed: false, tier, dayCount: data.dayCount, limit };
    }

    tx.set(ref, {
      email: email || data.email || null,
      tier,
      day,
      dayCount: (data.dayCount || 0) + 1,
      totalReadings: (data.totalReadings || 0) + 1,
      updatedAt: now,
      ...(snap.exists ? {} : { createdAt: now }),
    }, { merge: true });

    return { allowed: true, tier, dayCount: (data.dayCount || 0) + 1, limit };
  });
}

exports.secureGeminiProxy = onRequest(
  { cors: true, region: "us-central1", memory: "256MiB", timeoutSeconds: 60, secrets: ["GEMINI_API_KEY"] },
  async (req, res) => {
    // Health check (no auth) so deploys are easy to verify.
    if (req.method === "GET" && (req.path === "/" || req.path === "")) {
      return res.status(200).json({ status: "ok", service: "taro-secure-gemini-proxy" });
    }

    // 1. AUTH
    const authHeader = req.headers.authorization || "";
    if (!authHeader.startsWith("Bearer ")) {
      return res.status(401).json({ error: "Unauthorized: missing Bearer token" });
    }
    let decoded;
    try {
      decoded = await admin.auth().verifyIdToken(authHeader.slice(7));
    } catch (e) {
      logger.warn("Token verification failed", e.message);
      return res.status(401).json({ error: "Unauthorized: invalid or expired token" });
    }
    const uid = decoded.uid;
    const email = decoded.email || null;

    // 2. ACCOUNTING / QUOTA
    let usage;
    try {
      usage = await meterUsage(uid, email);
    } catch (e) {
      logger.error("Accounting transaction failed", e);
      return res.status(500).json({ error: "Accounting error" });
    }
    if (!usage.allowed) {
      logger.log(`[QUOTA] uid=${uid} tier=${usage.tier} hit daily limit ${usage.limit}`);
      return res.status(429).json({
        error: "Daily free reading limit reached. Upgrade to Premium for more.",
        tier: usage.tier, limit: usage.limit, used: usage.dayCount,
      });
    }

    // 3. PROXY to Gemini
    const geminiApiKey = process.env.GEMINI_API_KEY;
    if (!geminiApiKey) {
      logger.error("GEMINI_API_KEY secret not configured");
      return res.status(500).json({ error: "Server missing API key configuration" });
    }
    let targetPath = req.path && req.path !== "/" ? req.path
      : "/v1beta/models/gemini-2.5-flash:generateContent";
    for (const [legacy, real] of Object.entries(MODEL_ALIASES)) {
      targetPath = targetPath.replace(legacy, real);
    }
    const geminiUrl = `${GEMINI_HOST}${targetPath}?key=${geminiApiKey}`;

    try {
      const response = await axios({
        method: req.method,
        url: geminiUrl,
        headers: { "Content-Type": "application/json" },
        data: req.body,
        timeout: 55000,
      });
      res.set("X-Taro-Usage", `${usage.dayCount}/${usage.limit}`);
      res.set("X-Taro-Tier", usage.tier);
      return res.status(response.status).send(response.data);
    } catch (error) {
      const status = error.response?.status || 500;
      const errorData = error.response?.data || { error: error.message };
      logger.error("Gemini forwarding failed", status, error.response?.data || error.message);
      return res.status(status).json(errorData);
    }
  }
);
