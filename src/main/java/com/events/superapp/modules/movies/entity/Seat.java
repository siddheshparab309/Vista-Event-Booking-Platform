package com.events.superapp.modules.movies.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Theater theater;
}
