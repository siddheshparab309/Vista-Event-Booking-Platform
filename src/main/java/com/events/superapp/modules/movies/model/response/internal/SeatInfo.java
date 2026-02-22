package com.events.superapp.modules.movies.model.response.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatInfo {
    private String seatId;
    private String status;
    private BigDecimal price;
    private String type;
}
