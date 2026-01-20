package com.events.superapp.modules.movies.model.response;

import java.util.List;

public record MovieDetailResponse(
        Long movieId,
        String title,
        List<TheaterSchedules> theaters
) {}

