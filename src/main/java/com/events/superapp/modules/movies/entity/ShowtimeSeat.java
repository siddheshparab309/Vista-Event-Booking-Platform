package com.events.superapp.modules.movies.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "showtime_seats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Showtime showtime;

    @ManyToOne
    private Seat seat;

    private String status;
    private BigDecimal price;

    @Version
    private Integer version;
}