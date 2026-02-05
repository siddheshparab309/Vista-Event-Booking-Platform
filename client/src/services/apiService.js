import { MOCK_MOVIES, MOCK_EVENTS } from '../mock/data.js';

const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

export const fetchMovies = async (cityId) => {
  await sleep(800); 
  return { data: MOCK_MOVIES };
};

export const fetchEvents = async (cityId) => {
  await sleep(800);
  return { data: MOCK_EVENTS };
};