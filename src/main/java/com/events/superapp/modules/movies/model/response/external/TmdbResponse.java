package com.events.superapp.modules.movies.model.response.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbResponse(List<MovieDetails> results) {
    public record MovieDetails(
            Long id,
            String title,
            String overview,
            Boolean adult,
            @JsonProperty("poster_path")
            String posterPath,
            @JsonProperty("vote_average")
            Double voteAverage,
            @JsonProperty("genre_ids")
            List<Integer> genreIds
    ) {}
}