package com.events.superapp.modules.movies.model.response;

public record AllMovies(
        Long id,
        String title,
        String rating,
        String posterUrl,
        String genre
) {
}
