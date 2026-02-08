package com.events.superapp.modules.movies.scheduler;

import com.events.superapp.common.entity.ContentRating;
import com.events.superapp.modules.movies.entity.Movie;
import com.events.superapp.modules.movies.model.response.external.TmdbGenreMapper;
import com.events.superapp.modules.movies.model.response.external.TmdbResponse;
import com.events.superapp.modules.movies.repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LoadMoviesScheduler implements CommandLineRunner {
    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${TMDB_ACCESS_TOKEN}")
    private String tmdbToken;

    public LoadMoviesScheduler(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Job to sync movies started...");
        syncMovies();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void syncMovies() {
        String trendingUrl = "https://api.themoviedb.org/3/trending/movie/day?language=en-US";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tmdbToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TmdbResponse> response = restTemplate.exchange(
                    trendingUrl, HttpMethod.GET, entity, TmdbResponse.class);
            if (response.getBody() != null && response.getBody().results() != null) {
                for (var dto : response.getBody().results()) {
                    processSingleMovie(dto, entity);
                }
                log.info("Successfully synced {} movies from TMDB.", response.getBody().results().size());
            }
        } catch (Exception e) {
            log.error("Movie sync failed. Reason: {}", e.getMessage());
        }
    }

    private void processSingleMovie(TmdbResponse.MovieDetails movieDetails, HttpEntity<String> entity) {
        try {
            Movie movie = movieRepository.findByTitle(movieDetails.title()).orElse(new Movie());
            movie.setTitle(movieDetails.title());
            movie.setDescription(movieDetails.overview());
            movie.setPosterUrl("https://image.tmdb.org/t/p/w500" + movieDetails.posterPath());
            movie.setImdbRating(String.valueOf(movieDetails.voteAverage()));
            movie.setActive(true);
            movie.setRating(movieDetails.adult() != null && movieDetails.adult() ? ContentRating.R : ContentRating.PG13);
            String detailsUrl = "https://api.themoviedb.org/3/movie/" + movieDetails.id() + "?append_to_response=credits";

            JsonNode details = restTemplate.exchange(detailsUrl, HttpMethod.GET, entity, JsonNode.class).getBody();

            if (details != null) {
                movie.setDurationMinutes(details.get("runtime").asInt());
                Map<String, Object> meta = new HashMap<>();
                meta.put("cast", details.get("credits").get("cast"));
                meta.put("crew", details.get("credits").get("crew"));
                movie.setMetadata(meta);
            }

            if (movieDetails.genreIds() != null && !movieDetails.genreIds().isEmpty()) {
                String genreString = movieDetails.genreIds().stream()
                        .map(TmdbGenreMapper::getGenreNameById)
                        .collect(Collectors.joining(", "));
                movie.setGenre(genreString);
            }
            movieRepository.save(movie);
        } catch (Exception e) {
            log.error("Error processing for movie '{}': {}", movieDetails.title(), e.getMessage());
        }
    }

}