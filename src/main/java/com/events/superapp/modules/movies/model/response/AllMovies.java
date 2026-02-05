package com.events.superapp.modules.movies.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AllMovies {
    public Long id;
    public String title;
    private String contentRating;
    private String posterUrl;
    private String genre;
    private String imdb;
}
