package com.events.superapp.modules.movies.model.response.internal;

import java.util.List;

public record SeatMap(
        Long showtimeId,
        String movieTitle,
        String theaterName,
        String startTime,
        Integer version,
        List<Rows> rows
) {}
