package com.events.superapp.modules.movies.repository;

import com.events.superapp.modules.movies.entity.ShowtimeSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {
}