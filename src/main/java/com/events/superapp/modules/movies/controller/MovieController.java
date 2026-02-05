package com.events.superapp.modules.movies.controller;

import com.events.superapp.modules.movies.model.response.AllMovies;
import com.events.superapp.modules.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieController {
    private final MovieService movieService;
    @PostMapping("/getAllMovies")
    public ResponseEntity<List<AllMovies>> handleGoogleLogin(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok().body(movieService.getAllMovies());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(List.of(new AllMovies()));
        }
    }
}
