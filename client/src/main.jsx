import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { GoogleOAuthProvider } from '@react-oauth/google'
import './index.css'
import App from './App.jsx'

const GOOGLE_SSO_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID;
console.log("My Client ID is:", import.meta.env.VITE_GOOGLE_CLIENT_ID);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <GoogleOAuthProvider clientId={GOOGLE_SSO_ID}>
    <App />
    </GoogleOAuthProvider>
  </StrictMode>,
)
