package com.events.superapp.modules.ticketing.repository;


import com.events.superapp.modules.ticketing.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
}
