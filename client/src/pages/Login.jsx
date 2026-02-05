import React from 'react';
import { GoogleLogin } from '@react-oauth/google';
import api from '../api/axios';

const Login = ({ onLoginSuccess }) => {
  const handleGoogleSuccess = async (credentialResponse) => {
    try {
      await api.post('/auth/google', {
        token: credentialResponse.credential 
      });
      onLoginSuccess(); 
    } catch (err) {
      console.error("Auth failed:", err.response?.data || err.message);
      alert("Login failed. Check Backend Logs.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-950 p-4">
      <div className="bg-slate-900 p-10 rounded-xl border border-slate-800 w-full max-w-md text-center shadow-2xl">
        <h1 className="text-4xl font-bold text-indigo-500 mb-2 italic tracking-tighter">
          VISTA
        </h1>
        <p className="text-slate-400 mb-8 uppercase tracking-[0.2em] text-[10px] font-black">
          Movies • Sports • Comedy • Meetups
        </p>
        <div className="flex justify-center">
          <GoogleLogin
            onSuccess={handleGoogleSuccess}
            onError={() => console.log('Google Login Failed')}
            theme="filled_blue"
            shape="pill"
          />
        </div>
        <p className="mt-8 text-[9px] text-slate-600 font-bold uppercase tracking-[0.3em]">
          Secure SSO via Google Cloud
        </p>
      </div>
    </div>
  );
};

export default Login;