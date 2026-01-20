package com.events.superapp.modules.movies.model.response;

import java.util.List;

public record SeatMap(
        // Metadata to show at the top of the seat map screen
        Long showtimeId,
        String movieTitle,
        String theaterName,
        String startTime,
        Integer version,
        List<Rows> rows
) {}
