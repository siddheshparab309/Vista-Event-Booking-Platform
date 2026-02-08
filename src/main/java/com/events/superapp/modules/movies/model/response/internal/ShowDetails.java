package com.events.superapp.modules.movies.model.response.internal;

import java.math.BigDecimal;

public record ShowDetails(
        Long showtimeId,
        String time,
        BigDecimal price,
        Integer version
) {}
