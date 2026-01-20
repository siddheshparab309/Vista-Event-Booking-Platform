package com.events.superapp.modules.movies.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seats")
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatNumber;
    private String seatRow;
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    private Theatre theater;
}
