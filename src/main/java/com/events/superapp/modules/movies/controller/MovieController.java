package com.events.superapp.modules.movies.controller;

import com.events.superapp.modules.movies.model.response.internal.MovieDetailResponse;
import com.events.superapp.modules.movies.model.response.internal.MovieDetails;
import com.events.superapp.modules.movies.model.response.internal.SeatMap;
import com.events.superapp.modules.movies.model.response.internal.TheaterSchedules;
import com.events.superapp.modules.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/allMovies")
    public ResponseEntity<List<MovieDetails>> getAllMovies() {
        try {
            return ResponseEntity.ok().body(movieService.getAllMovies());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of(new MovieDetails()));
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<MovieDetailResponse> getMovieDetails(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(movieService.getMovieDetails(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MovieDetailResponse());
        }
    }

    @GetMapping("/{movieId}/showtimes")
    public ResponseEntity<List<TheaterSchedules>> getMovieSchedules(@PathVariable Long movieId) {
        try {
            return ResponseEntity.ok().body(movieService.getSchedulesForMovie(movieId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of(new TheaterSchedules()));
        }
    }

    @GetMapping("/showtimes/{showtimeId}/seats")
    public ResponseEntity<SeatMap> getSeatmapForTheater(@PathVariable Long movieId) {
        try {
            return ResponseEntity.ok().body(movieService.getSeatMap(movieId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SeatMap());
        }
    }

}
