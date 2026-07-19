# Firebase GenAI / Gemini API Secure Proxy

This directory contains a complete, deployable Node.js Firebase Cloud Function that acts as a secure gateway/proxy to Google's GenAI / Gemini API. 

## Features
- **Google Account Authentication**: Validates Google/Firebase Social Sign-In JWTs via `firebase-admin` on every request.
- **Server-Side Secrets**: Keeps your actual `GEMINI_API_KEY` hidden securely on the server side instead of exposing it in your Android APK.
- **Dynamic Endpoint Handling**: Seamlessly forwards content generation, vision, and dynamic model queries.

---

## 🚀 Deployment Instructions

### Prerequisites
1. Install the [Firebase CLI](https://firebase.google.com/docs/cli):
   ```bash
   npm install -g firebase-tools
   ```
2. Log in to your Firebase account:
   ```bash
   firebase login
   ```

### 1. Initialize Firebase
In this folder (`/firebase-proxy`), run:
```bash
firebase use --add YOUR_PROJECT_ID
```
Replace `YOUR_PROJECT_ID` with your real Firebase Project ID.

### 2. Configure Your Secret Key
We use Firebase Environment Configuration or Cloud Secret Manager to keep the actual Gemini API key safe.

Run the following command to securely bind the API key for your functions:
```bash
firebase functions:secrets:set GEMINI_API_KEY=your_actual_gemini_api_key
```

### 3. Deploy
Deploy the proxy function with a single command:
```bash
firebase deploy --only functions
```

Once deployed, the terminal will print your secure URL (e.g., `https://securegeminiproxy-YOUR_SUBDOMAIN.run.app`).

---

## 📱 Android Integration Setup

1. Log in to the application using **Google Sign-In**.
2. Tap the **Settings icon (⚙️)** in the top bar.
3. Enter your deployed function URL in the **Custom Gateway URL** field:
   - Example: `https://securegeminiproxy-YOUR_SUBDOMAIN.run.app`
4. Leave the local **API Key field blank**—the server handles your actual API key secret on your behalf!
5. Now, all your content generation and photo scans will pass through this authenticated gateway!
