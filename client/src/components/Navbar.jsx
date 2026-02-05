import React, { useState, useEffect, useRef } from 'react';
import { Search, LogOut, ChevronDown, Loader2, Crosshair } from 'lucide-react';
import api from '../api/axios';

const Navbar = ({ onLogout }) => {
  const [location, setLocation] = useState('Arlington, VA');
  const [showSearch, setShowSearch] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [isDetecting, setIsDetecting] = useState(false);
  const dropdownRef = useRef(null);
  const isProcessingSync = useRef(false);
  const lastSyncedCity = useRef(null);

  const syncCityToBackend = async (cityName) => {
    if (isProcessingSync.current || cityName === lastSyncedCity.current) return;

    try {
      isProcessingSync.current = true;
      await api.put('/auth/updateCity', { city: cityName });
      lastSyncedCity.current = cityName;
      console.log("Backend updated successfully for:", cityName);
    } catch (err) {
      console.error("Backend sync failed:", err);
    } finally {
      isProcessingSync.current = false;
    }
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowSearch(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleDetectLocation = () => {
    if (!navigator.geolocation || isDetecting) return;
    
    setIsDetecting(true);
    navigator.geolocation.getCurrentPosition(async (pos) => {
      try {
        const { latitude, longitude } = pos.coords;
        const res = await fetch(`https://nominatim.openstreetmap.org/reverse?lat=${latitude}&lon=${longitude}&format=json`);
        const data = await res.json();
        const city = data.address.city || data.address.town || "Arlington";
        const state = data.address.state_code || "VA";
        
        setLocation(`${city}, ${state}`);
        syncCityToBackend(city); 
        setShowSearch(false);
      } catch (err) { 
        console.error("Detection failed:", err); 
      } finally { 
        setIsDetecting(false); 
      }
    }, () => setIsDetecting(false), { maximumAge: 60000, timeout: 5000 });
  };

  useEffect(() => {
    const delayDebounce = setTimeout(async () => {
      if (searchQuery.length > 2) {
        const res = await fetch(`https://nominatim.openstreetmap.org/search?q=${searchQuery}&format=json&addressdetails=1&limit=5&countrycodes=us`);
        const data = await res.json();
        setSearchResults(data);
      } else { 
        setSearchResults([]); 
      }
    }, 400);
    return () => clearTimeout(delayDebounce);
  }, [searchQuery]);

  return (
    <nav className="bg-slate-900 border-b border-slate-800 px-6 py-4 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto flex items-center justify-between gap-8">
        <div className="flex items-center gap-6">
          <h1 className="text-2xl font-black italic text-indigo-500">VISTA</h1>
          
          <div className="relative" ref={dropdownRef}>
            <button 
              onClick={() => setShowSearch(!showSearch)}
              className="flex items-center gap-2 text-slate-300 hover:text-indigo-400 transition-colors"
            >
              <span className="text-sm font-bold">{location}</span>
              <ChevronDown size={14} className={`transition-transform ${showSearch ? 'rotate-180' : ''}`} />
            </button>

            {showSearch && (
              <div className="absolute top-12 left-0 w-72 bg-slate-800 border border-slate-700 rounded-lg shadow-2xl p-4 z-50">
                <button 
                  onClick={handleDetectLocation}
                  disabled={isDetecting}
                  className="w-full flex items-center justify-center gap-2 p-2 mb-3 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white rounded-md text-sm font-bold transition-colors"
                >
                  {isDetecting ? <Loader2 size={16} className="animate-spin" /> : <Crosshair size={16} />}
                  Use Current Location
                </button>

                <div className="relative border-t border-slate-700 pt-3">
                  <Search className="absolute left-2 top-6 text-slate-500" size={14} />
                  <input 
                    autoFocus
                    placeholder="Search city..."
                    className="w-full bg-slate-900 border border-slate-700 rounded-md py-2 pl-8 pr-3 text-xs text-white outline-none focus:border-indigo-500"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                  />
                </div>

                <div className="mt-2 max-h-40 overflow-y-auto">
                  {searchResults.map((result) => (
                    <button
                      key={result.place_id}
                      onClick={() => {
                        const city = result.address.city || result.address.town || result.display_name.split(',')[0];
                        setLocation(`${city}, ${result.address.state || ''}`);
                        syncCityToBackend(city);
                        setShowSearch(false);
                      }}
                      className="w-full text-left p-2 text-xs text-slate-300 hover:bg-slate-700 rounded-md truncate"
                    >
                      {result.display_name}
                    </button>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>

        <div className="flex-1 max-w-md relative hidden sm:block">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-500" size={16} />
          <input 
            type="text" 
            placeholder="Search Movies, Events..."
            className="w-full bg-slate-800 border border-slate-700 rounded-lg py-2 pl-10 pr-4 text-sm focus:outline-none focus:border-indigo-500"
          />
        </div>

        <button onClick={onLogout} className="p-2 bg-slate-800 hover:bg-red-900/20 text-slate-400 hover:text-red-500 rounded-lg border border-slate-700 transition-colors">
          <LogOut size={20} />
        </button>
      </div>
    </nav>
  );
};

export default Navbar;