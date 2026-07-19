# GCP AI Gateway Deployment Guide

This guide provides the complete source code and step-by-step instructions to deploy a secure **AI Gateway** on Google Cloud Platform (GCP) using **Google Cloud Run** and **Google Secret Manager**. 

Deploying an AI Gateway keeps your Gemini API Key secure in the cloud, preventing reverse-engineering or extraction from your decompiled Android APK.

---

## 🏗️ 1. Architecture Overview

1. **Android App**: Sends standard Gemini API payload format to your custom Cloud Run gateway URL (e.g., `https://taro-gateway-abcde-uc.a.run.app`).
2. **Cloud Run Gateway**: Automatically retrieves your Gemini API Key from Google Secret Manager securely at runtime.
3. **Gemini API**: Receives the proxied request from the Gateway, processes it, and returns the mystical reading back to your app.

---

## 💻 2. Gateway Source Code (Node.js & Express)

Create a clean directory on your machine (e.g. `taro-gateway/`) and place the following three files inside it:

### File 1: `package.json`
```json
{
  "name": "taro-ai-gateway",
  "version": "1.0.0",
  "description": "Secure Gemini API Gateway for Taro Android App",
  "main": "server.js",
  "scripts": {
    "start": "node server.js"
  },
  "dependencies": {
    "express": "^4.19.2",
    "axios": "^1.7.2"
  },
  "engines": {
    "node": ">=18.0.0"
  }
}
```

### File 2: `server.js`
```javascript
const express = require('express');
const axios = require('axios');
const app = express();

app.use(express.json({ limit: '10mb' }));

// Health Check Endpoint
app.get('/', (req, res) => {
  res.status(200).send('🔮 Oracle Gateway is active and listening to the cosmos.');
});

// Proxy Endpoint for Gemini generateContent
app.post('/v1beta/models/:modelAndAction', async (req, res) => {
  try {
    const modelAndAction = req.params.modelAndAction; // e.g. "gemini-3.5-flash:generateContent"
    
    // 1. Fetch the API Key from environment variables (populated securely via GCP Secret Manager)
    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) {
      return res.status(500).json({ error: 'Gateway configuration error: GEMINI_API_KEY is missing.' });
    }

    // 2. Forward payload to official Google Generative Language endpoint
    const response = await axios.post(
      `https://generativelanguage.googleapis.com/v1beta/models/${modelAndAction}?key=${apiKey}`,
      req.body,
      {
        headers: { 'Content-Type': 'application/json' },
        timeout: 45000 // 45 seconds timeout
      }
    );

    // 3. Return the response back to the Android client
    res.status(response.status).json(response.data);
  } catch (error) {
    console.error('Proxy Error:', error.message);
    if (error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json({ error: 'Gateway failed to reach Gemini: ' + error.message });
    }
  }
}

// Start Server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`🔮 Gateway server started on port ${PORT}`);
});
```

### File 3: `Dockerfile`
```dockerfile
FROM node:18-alpine
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install --only=production
COPY . .
EXPOSE 8080
CMD [ "npm", "start" ]
```

---

## 🚀 3. Step-by-Step Deployment Instructions

Follow these commands in your terminal to deploy to GCP. Make sure you have installed and authenticated the [Google Cloud SDK (gcloud CLI)](https://cloud.google.com/sdk).

### Step 3.1: Authenticate and Set Project
```bash
# Log in to your GCP Account
gcloud auth login

# Set active GCP Project ID (replace with your project ID)
gcloud config set project YOUR_GCP_PROJECT_ID
```

### Step 3.2: Enable Services
```bash
# Enable required services on your GCP Project
gcloud services enable run.googleapis.com secretmanager.googleapis.com artifactregistry.googleapis.com
```

### Step 3.3: Store your API Key in GCP Secret Manager
```bash
# Create the Secret in Secret Manager
gcloud secrets create taro-gemini-key --replication-policy="automatic"

# Insert your actual Gemini API Key (replace AIzaSy... with your real key)
echo -n "YOUR_ACTUAL_GEMINI_API_KEY" | gcloud secrets versions add taro-gemini-key --data-file=-
```

### Step 3.4: Deploy to Cloud Run
Deploy the app directory directly to Cloud Run. It will automatically build a secure Docker container, push it to Artifact Registry, and host it serverlessly:

```bash
gcloud run deploy taro-ai-gateway \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-secrets="GEMINI_API_KEY=taro-gemini-key:latest"
```

Once deployment completes, the terminal will print a service URL similar to:
`https://taro-ai-gateway-abcde-uc.a.run.app`

---

## 📱 4. How to configure the Android app

1. Open your **Taro App** on your device.
2. Tap the **Settings (Gear Icon)** in the top-right corner of the Home screen.
3. In the **Oracle Settings** dialog:
   * Keep the **Gemini API Key** field empty (since your key is securely stored inside GCP Secret Manager!).
   * Set **Custom Gateway URL** to:
     ```
     https://taro-ai-gateway-abcde-uc.a.run.app/
     ```
     *(Note: Please ensure the URL has a trailing slash `/` as it acts as the base URL).*
4. Tap **Save**.
5. Draw a card—the app will securely route your mystical Tarot readings via your secure Cloud Run AI Gateway!
