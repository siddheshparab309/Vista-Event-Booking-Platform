package com.events.superapp.modules.movies.model.response.internal;

import java.util.List;

public record TheaterSchedules(
        Long theaterId,
        String theaterName,
        String address,
        Double distanceKm,
        List<ShowDetails> showTimes
) {}
