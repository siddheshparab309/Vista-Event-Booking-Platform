package com.events.superapp.modules.ticketing.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookingRequest(
        @NotNull
        Long showtimeId,
        @NotEmpty
        List<String> selectedSeatIds,
        @NotBlank
        String email,
        @NotBlank
        String mobileNumber,
        String paymentToken
) {
}
