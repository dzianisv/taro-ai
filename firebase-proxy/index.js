const { onRequest } = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin");
const axios = require("axios");

admin.initializeApp();

/**
 * secureGeminiProxy - Verifies Firebase Auth ID Token from Authorization header,
 * then securely proxies the request to the official Gemini API.
 */
exports.secureGeminiProxy = onRequest({ cors: true }, async (req, res) => {
  // 1. Authenticate with Google Account (Verify ID Token)
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    logger.error("Missing or malformed Authorization header.");
    return res.status(401).json({ error: "Unauthorized: Missing Authorization Bearer token" });
  }

  const idToken = authHeader.split("Bearer ")[1];
  let decodedToken;
  try {
    decodedToken = await admin.auth().verifyIdToken(idToken);
    logger.log(`[AUTH SUCCESS] User Email: ${decodedToken.email}, UID: ${decodedToken.uid}`);
  } catch (error) {
    logger.error("Token verification failed:", error);
    return res.status(401).json({ error: "Unauthorized: Invalid or expired token" });
  }

  // 2. Extract configuration
  const geminiApiKey = process.env.GEMINI_API_KEY;
  if (!geminiApiKey) {
    logger.error("Server configuration error: GEMINI_API_KEY environment variable is not set.");
    return res.status(500).json({ error: "Internal Server Error: Server is missing its API key configuration" });
  }

  // Determine target API endpoint path
  // Standard format: /v1beta/models/gemini-3.5-flash:generateContent
  const targetPath = req.path || "/v1beta/models/gemini-3.5-flash:generateContent";
  const geminiUrl = `https://generativelanguage.googleapis.com${targetPath}?key=${geminiApiKey}`;

  try {
    logger.log(`Proxying request to Gemini API: ${targetPath}`);
    
    const response = await axios({
      method: req.method,
      url: geminiUrl,
      headers: {
        "Content-Type": "application/json"
      },
      data: req.body
    });

    return res.status(response.status).send(response.data);
  } catch (error) {
    logger.error("Gemini API proxy forwarding failed:", error.response?.data || error.message);
    const status = error.response?.status || 500;
    const errorData = error.response?.data || { error: error.message };
    return res.status(status).json(errorData);
  }
});
