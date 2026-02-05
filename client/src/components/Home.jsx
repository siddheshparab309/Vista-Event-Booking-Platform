import React from 'react';
import MovieCard from '../components/MovieCard';

export default function Home() {
  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-white">Recommended Movies</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <MovieCard 
          title="Top Gun: Maverick" 
          genre="Action/Drama" 
          distance="2.5 miles" 
          image="https://images.unsplash.com/photo-1436491865332-7a61a109c0f3?q=80&w=2070" 
        />
      </div>
    </div>
  );
}