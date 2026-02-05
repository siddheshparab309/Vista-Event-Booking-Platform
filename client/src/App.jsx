import { useState, useEffect } from 'react';
import api from './api/axios';
import Navbar from "./components/Navbar"; 
import LogoutModal from "./components/LogoutModal";
import Home from "./pages/Home"; 
import Login from "./pages/Login";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    api.get('/auth/verifySession')
      .then(() => setIsLoggedIn(true))
      .catch(() => setIsLoggedIn(false));
  }, []);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      {isLoggedIn ? (
        <div className="flex flex-col min-h-screen">
          <Navbar onLogout={() => setIsModalOpen(true)} />
          <main className="flex-1 p-8">
            <Home />
          </main>
          <LogoutModal 
            isOpen={isModalOpen} 
            onClose={() => setIsModalOpen(false)} 
            onConfirm={() => setIsLoggedIn(false)} 
          />
        </div>
      ) : (
        <Login onLoginSuccess={() => setIsLoggedIn(true)} />
      )}
    </div>
  );
}

export default App;