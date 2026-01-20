package com.events.superapp.modules.ticketing.entity;

import com.events.superapp.common.entity.User;
import com.events.superapp.modules.movies.entity.Seat;
import com.events.superapp.modules.movies.entity.Showtime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingReference;

    @ManyToOne
    private User user;

    @ManyToOne
    private Showtime showtime;

    @ManyToMany
    @JoinTable(name = "booking_seats")
    private List<Seat> seats;

    private BigDecimal totalAmount;
    private LocalDateTime bookingTime;
    private String bookingStatus;
}