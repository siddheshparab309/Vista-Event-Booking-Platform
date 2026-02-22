package com.events.superapp.modules.events.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TicketmasterResponse(
        @JsonProperty("_embedded") EmbeddedEvents embedded
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EmbeddedEvents(
            List<EventDto> events
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EventDto(
            String name,
            String info,
            List<Classification> classifications,
            @JsonProperty("_embedded")
            EmbeddedVenues embedded
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record EmbeddedVenues(
            List<Venue> venues
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Venue(
            String name,
            Location location,
            City city
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Location(String latitude, String longitude) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record City(String name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Classification(Segment segment) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Segment(String name) {}
}