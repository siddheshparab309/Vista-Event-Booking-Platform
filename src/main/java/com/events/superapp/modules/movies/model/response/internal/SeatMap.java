package com.events.superapp.modules.movies.model.response.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatMap {
    private Long showtimeId;
    private String movieTitle;
    private String theaterName;
    private String startTime;
    private Integer version;
    private List<Rows> rows;
}
