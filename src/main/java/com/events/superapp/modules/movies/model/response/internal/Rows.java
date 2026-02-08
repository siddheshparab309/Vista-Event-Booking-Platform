package com.events.superapp.modules.movies.model.response.internal;

import java.util.List;

public record Rows (
        String rowLabel,
        List<SeatInfo> seats
){}
