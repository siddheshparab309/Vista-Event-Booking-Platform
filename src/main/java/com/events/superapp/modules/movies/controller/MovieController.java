package com.events.superapp.modules.movies.controller;

import com.events.superapp.modules.movies.model.response.internal.AllMovies;
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
    @GetMapping("/getAllMovies")
    public ResponseEntity<List<AllMovies>> getAllMovies() {
        try {
            return ResponseEntity.ok().body(movieService.getAllMovies());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of(new AllMovies()));
        }
    }
}
