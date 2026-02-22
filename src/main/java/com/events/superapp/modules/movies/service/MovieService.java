package com.events.superapp.modules.movies.service;

import com.events.superapp.common.entity.BaseEvent;
import com.events.superapp.modules.movies.entity.*;
import com.events.superapp.modules.movies.model.response.internal.*;
import com.events.superapp.modules.movies.repository.MovieRepository;
import com.events.superapp.modules.movies.repository.ShowtimeRepository;
import com.events.superapp.modules.movies.repository.ShowtimeSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    public List<MovieDetails> getAllMovies() {
        return movieRepository.findAll().stream().filter(BaseEvent::isActive).map(movie -> new MovieDetails(
                movie.getId(),
                movie.getTitle(),
                movie.getRating().name(),
                (String) movie.getMetadata().getOrDefault("posterUrl", "placeholder.jpg"),
                movie.getGenre(),
                movie.getImdbRating(),
                movie.getMetadata()
                )
        ).toList();
    }

    public MovieDetailResponse getMovieDetails(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        MovieDetailResponse resp = new MovieDetailResponse();
        resp.setId(movie.getId());
        resp.setTitle(movie.getTitle());
        resp.setGenre(movie.getGenre());
        resp.setPosterUrl(movie.getPosterUrl());
        resp.setImdb(movie.getImdbRating());
        resp.setMovieMetadata(movie.getMetadata());
        return resp;
    }

    public List<TheaterSchedules> getSchedulesForMovie(Long movieId) {
        List<Showtime> showtimes = showtimeRepository.findByMovieId(movieId);

        return showtimes.stream()
                .collect(Collectors.groupingBy(Showtime::getTheater))
                .entrySet().stream()
                .map(entry -> {
                    Theater theater = entry.getKey();
                    List<Showtime> theaterShows = entry.getValue();
                    List<ShowDetails> details = theaterShows.stream()
                            .map(s -> ShowDetails.builder()
                                    .showtimeId(s.getId())
                                    .time(s.getStartTime().toString())
                                    .price(s.getBasePrice())
                                    .version(0)
                                    .build())
                            .toList();
                    return TheaterSchedules.builder()
                            .theaterId(theater.getId())
                            .theaterName(theater.getName())
                            .address(theater.getAddress())
                            .showTimes(details).build();
                }).toList();
    }

    public SeatMap getSeatMap(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow();
        List<ShowtimeSeat> seats = showtimeSeatRepository.findByShowtimeId(showtimeId);
        Map<String, List<SeatInfo>> groupedSeats = seats.stream()
                .map(st -> new SeatInfo(
                        st.getSeat().getSeatRow() + st.getSeat().getSeatNumber(),
                        st.getStatus(),
                        st.getPrice(),
                        st.getSeat().getType()
                )).collect(Collectors.groupingBy(si -> si.getSeatId().substring(0, 1)));

        List<Rows> rows = groupedSeats.entrySet().stream()
                .map(e -> new Rows(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(Rows::getRowLabel)).toList();

        return new SeatMap(
                showtime.getId(),
                showtime.getMovie().getTitle(),
                showtime.getTheater().getName(),
                showtime.getStartTime().toString(),
                0,
                rows
        );
    }

}
