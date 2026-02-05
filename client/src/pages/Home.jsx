import React, { useState, useEffect } from 'react';
import { fetchMovies, fetchEvents } from '../services/apiService';
import MovieCard from '../components/MovieCard';

const Home = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('Movies');

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      const response = activeTab === 'Movies' ? await fetchMovies() : await fetchEvents();
      setItems(response.data);
      setLoading(false);
    };
    loadData();
  }, [activeTab]);

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex gap-4 mb-8">
        {['Movies', 'Events', 'Sports'].map(tab => (
          <button 
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-6 py-2 rounded-full font-bold transition-all ${
              activeTab === tab ? 'bg-red-500 text-white shadow-lg' : 'bg-gray-100 text-gray-600'
            }`}
          >
            {tab}
          </button>
        ))}
      </div>

      {loading ? (
        <div className="flex justify-center py-20">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-red-500"></div>
        </div>
      ) : (
        <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-6">
          {items.map(item => (
            <MovieCard 
              key={item.id} 
              title={item.title} 
              genre={item.genre || item.type} 
              distance={item.distance} 
              image={item.image} 
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default Home;