package com.events.superapp.modules.movies.repository;

import com.events.superapp.modules.movies.entity.ShowtimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {
    List<ShowtimeSeat> findByShowtimeId(Long showtimeId);
    @Query("SELECT ss FROM ShowtimeSeat ss " +
            "JOIN FETCH ss.seat s " +
            "WHERE ss.showtime.id = :showtimeId " +
            "AND CONCAT(s.seatRow, '-', s.seatNumber) IN :seatLabels " +
            "AND ss.status != 'BOOKED'")
    List<ShowtimeSeat> findAvailableSeats(
            @Param("showtimeId") Long showtimeId,
            @Param("seatLabels") List<String> seatLabels
    );
}