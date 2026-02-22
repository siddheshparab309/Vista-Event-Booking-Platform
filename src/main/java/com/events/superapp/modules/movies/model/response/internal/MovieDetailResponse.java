package com.events.superapp.modules.movies.model.response.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailResponse extends MovieDetails {
    private List<TheaterSchedules> theaters;
}

