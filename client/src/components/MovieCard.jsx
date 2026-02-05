import React from 'react';

const MovieCard = ({ title, genre, distance, image }) => {
  return (
    <div className="bg-slate-900 border border-slate-800 rounded-lg overflow-hidden group hover:border-indigo-500 transition-all duration-200">
      <div className="aspect-[2/3] relative overflow-hidden">
        <img src={image} alt={title} className="w-full h-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-transparent to-transparent" />
      </div>
      <div className="p-4">
        <p className="text-[10px] font-bold text-indigo-400 uppercase tracking-wider">{genre}</p>
        <h3 className="text-sm font-bold text-white truncate mt-1">{title}</h3>
        <div className="flex items-center gap-1 mt-2 text-xs text-slate-500">
          <span>{distance}</span>
        </div>
      </div>
    </div>
  );
};

export default MovieCard;