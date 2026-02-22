package com.events.superapp.modules.movies.model.response.internal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowDetails {
    private Long showtimeId;
    private String time;
    private java.math.BigDecimal price;
    private Integer version;
}