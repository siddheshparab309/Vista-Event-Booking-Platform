package com.events.superapp.modules.movies.scheduler;

import com.events.superapp.modules.movies.entity.*;
import com.events.superapp.modules.movies.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class LoadTheatersAndShowtimesScheduler implements CommandLineRunner {

    @Value("${SERP_API_KEY}")
    private String serpApiKey;

    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Pattern US_ADDR_PATTERN = Pattern.compile(",\\s*([^,]+),\\s*([A-Z]{2})\\s*(\\d{5})");

    public LoadTheatersAndShowtimesScheduler(
            TheaterRepository theaterRepository,
            MovieRepository movieRepository,
            ShowtimeRepository showtimeRepository,
            ShowtimeSeatRepository showtimeSeatRepository
    ) {
        this.theaterRepository = theaterRepository;
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
        this.showtimeSeatRepository = showtimeSeatRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Job to load theaters and showtimes in db started...");
        loadTheatersAndShowtimes();
    }

    private void loadTheatersAndShowtimes() {
        var targetCities = Arrays.asList(
                "Arlington, VA", "Los Angeles, CA", "New York, NY", "Chicago, IL",
                "Austin, TX", "Dallas, TX", "Atlanta, GA", "Miami, FL",
                "Philadelphia, PA", "Seattle, WA", "Boston, MA", "Phoenix, AZ"
        );
        for (String city : targetCities) {
            try {
                String url = "https://serpapi.com/search.json?engine=google&q=movie+theaters+near+"
                        + city.replace(" ", "+") + "&api_key=" + serpApiKey;

                JsonNode root = objectMapper.readTree(restTemplate.getForObject(url, String.class));
                JsonNode results = root.get("local_results");

                if (results != null && results.isArray()) {
                    List<Theater> cityBatch = new ArrayList<>();
                    for (JsonNode node : results) {
                        if (node.has("gps_coordinates")) {
                            Theater t = mapToTheater(node);
                            cityBatch.add(t);
                        }
                    }

                    if (!cityBatch.isEmpty()) {
                        List<Theater> savedTheaters = theaterRepository.saveAll(cityBatch);
                        savedTheaters.forEach(this::generateShowtimesAndInventory);
                        log.info("Successfully synced {} theaters for {}", savedTheaters.size(), city);
                    }
                }
                log.info("Job to load theaters and showtimes in db ended...");
            } catch (Exception e) {
                log.error("Failed to sync for {}: {}", city, e.getMessage());
            }
        }
    }

    private Theater mapToTheater(JsonNode node) {
        String name = node.get("title").asText();
        String rawAddress = node.get("address").asText();

        Theater theater = theaterRepository.findByName(name).orElse(new Theater());
        theater.setName(name);
        theater.setAddress(rawAddress);
        theater.setLatitude(node.get("gps_coordinates").get("latitude").asDouble());
        theater.setLongitude(node.get("gps_coordinates").get("longitude").asDouble());

        Matcher matcher = US_ADDR_PATTERN.matcher(rawAddress);
        if (matcher.find()) {
            theater.setCity(matcher.group(1).trim());
            theater.setState(matcher.group(2).trim());
            theater.setZip(matcher.group(3).trim());
        }

        if (theater.getSeats() == null || theater.getSeats().isEmpty()) {
            List<Seat> physicalSeats = new ArrayList<>();
            for (char row = 'A'; row <= 'E'; row++) {
                for (int i = 1; i <= 10; i++) {
                    Seat seat = new Seat();
                    seat.setSeatRow(String.valueOf(row));
                    seat.setSeatNumber(String.valueOf(i));
                    seat.setType("STANDARD");
                    seat.setTheater(theater);
                    physicalSeats.add(seat);
                }
            }
            theater.setSeats(physicalSeats);
        }
        return theater;
    }

    private void generateShowtimesAndInventory(Theater theater) {
        movieRepository.findAll().stream().limit(2).forEach(movie -> {
            Showtime showtime = new Showtime();
            showtime.setMovie(movie);
            showtime.setTheater(theater);
            showtime.setStartTime(LocalDateTime.now().plusHours(4));
            showtime.setBasePrice(new BigDecimal("15.00"));
            showtime = showtimeRepository.save(showtime);

            if (theater.getSeats() != null) {
                List<ShowtimeSeat> inventory = new ArrayList<>();
                for (Seat physicalSeat : theater.getSeats()) {
                    ShowtimeSeat ss = new ShowtimeSeat();
                    ss.setShowtime(showtime);
                    ss.setSeat(physicalSeat);
                    ss.setStatus("AVAILABLE");
                    ss.setPrice(showtime.getBasePrice());
                    inventory.add(ss);
                }
                showtimeSeatRepository.saveAll(inventory);
            }
        });
    }
}