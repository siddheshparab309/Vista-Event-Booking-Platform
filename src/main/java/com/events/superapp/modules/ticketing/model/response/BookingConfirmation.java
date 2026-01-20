package com.events.superapp.modules.ticketing.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingConfirmation(
        String bookingId,
        String theaterName,
        String movieTitle,
        LocalDateTime startTime,
        List<String> confirmedSeats,
        BigDecimal totalAmount,
        String qrCode
) {}
