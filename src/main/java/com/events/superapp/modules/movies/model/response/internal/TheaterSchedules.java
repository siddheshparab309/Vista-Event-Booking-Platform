package com.events.superapp.modules.movies.model.response.internal;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheaterSchedules {
    private Long theaterId;
    private String theaterName;
    private String address;
    private List<ShowDetails> showTimes;
}