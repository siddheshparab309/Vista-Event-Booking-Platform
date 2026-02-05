package com.events.superapp.modules.movies.service;

import com.events.superapp.modules.movies.model.response.AllMovies;
import com.events.superapp.modules.movies.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<AllMovies> getAllMovies() {
        return movieRepository.findAll().stream().map( movie -> new AllMovies(
                movie.getId(),
                movie.getTitle(),
                movie.getRating().name(),
                (String) movie.getMetadata().getOrDefault("posterUrl", "placeholder.jpg"),
                movie.getGenre(),
                movie.getImdbRating()
                )
        ).toList();
    }

}
