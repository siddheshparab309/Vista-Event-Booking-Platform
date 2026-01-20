package com.events.superapp.modules.movies.model.request;

public record FilterByLocation(
        Double latitude,
        Double longitude,
        Double radius
) {
}
