package com.events.superapp.modules.movies.model.response.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rows {
    private String rowLabel;
    private List<SeatInfo> seats;
}
