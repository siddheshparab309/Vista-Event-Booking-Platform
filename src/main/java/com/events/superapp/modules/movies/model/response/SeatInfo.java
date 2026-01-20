package com.events.superapp.modules.movies.model.response;

import java.math.BigDecimal;

public record SeatInfo(
        String seatId,
        String status,
        BigDecimal price,
        String type
) {
}
