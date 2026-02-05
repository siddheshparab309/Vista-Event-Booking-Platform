package com.events.superapp.modules.movies.repository;

import com.events.superapp.modules.movies.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
}
