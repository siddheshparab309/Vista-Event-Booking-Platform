package com.events.superapp.modules.ticketing.service;

import com.events.superapp.common.repository.UserRepository;
import com.events.superapp.modules.movies.entity.ShowtimeSeat;
import com.events.superapp.modules.movies.repository.ShowtimeSeatRepository;
import com.events.superapp.modules.ticketing.entity.Bookings;
import com.events.superapp.modules.ticketing.model.request.BookingRequest;
import com.events.superapp.modules.ticketing.model.response.BookingConfirmation;
import com.events.superapp.modules.ticketing.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketingService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BookingRepository bookingRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final UserRepository userRepository;

    private static final String LOCK_PREFIX = "seat_lock_";

    @Transactional
    public BookingConfirmation reserveSeats(BookingRequest request, String sessionId) {
        if (request.selectedSeatIds() == null || request.selectedSeatIds().isEmpty()) {
            throw new RuntimeException("No seats selected!");
        }

        if (sessionId == null || sessionId.isBlank()) {
            throw new RuntimeException("Invalid session detected!");
        }

        String showtimeId = request.showtimeId().toString();

        for (String seatLabel : request.selectedSeatIds()) {
            String lockKey = LOCK_PREFIX + showtimeId + "_" + seatLabel;
            Boolean acquired = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, sessionId, Duration.ofMinutes(10));

            if (Boolean.FALSE.equals(acquired)) {
                throw new RuntimeException("Seat " + seatLabel + " is currently held.");
            }
        }

        var seats = showtimeSeatRepository
                .findAvailableSeats(request.showtimeId(), request.selectedSeatIds());

        if (seats.size() != request.selectedSeatIds().size()) {
            releaseRedisLocks(showtimeId, request.selectedSeatIds());
            throw new RuntimeException("Some seats are no longer available in the database.");
        }

        var currentUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var booking = new Bookings();
        booking.setBookingReference(generateBookingReference());
        booking.setBookingStatus("TEMP_HOLD");
        booking.setBookingTime(LocalDateTime.now());
        booking.setUser(currentUser);
        booking.setShowtimeSeats(seats);

        var total = seats.stream()
                .map(ShowtimeSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        booking.setTotalAmount(total);

        return mapToResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingConfirmation confirmBooking(Long bookingId, String sessionId) {
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found."));

        if (!"TEMP_HOLD".equals(booking.getBookingStatus())) {
            throw new RuntimeException("Booking already processed.");
        }

        String showtimeId = booking.getShowtime().getId().toString();
        for (ShowtimeSeat ss : booking.getShowtimeSeats()) {
            var label = ss.getSeat().getSeatRow() + "-" + ss.getSeat().getSeatNumber();
            var lockKey = LOCK_PREFIX + showtimeId + "_" + label;
            var lockedBy = redisTemplate.opsForValue().get(lockKey);

            if (lockedBy == null || !lockedBy.equals(sessionId)) {
                throw new RuntimeException("Reservation for seat " + label + " has expired.");
            }
        }

        booking.setBookingStatus("CONFIRMED");
        booking.getShowtimeSeats().forEach(ss -> ss.setStatus("BOOKED"));

        var labels = booking.getShowtimeSeats().stream()
                .map(ss -> ss.getSeat().getSeatRow() + "-" + ss.getSeat().getSeatNumber())
                .toList();
        releaseRedisLocks(showtimeId, labels);

        return mapToResponse(bookingRepository.save(booking));
    }

    private String generateBookingReference() {
        return "VST-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
                + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private BookingConfirmation mapToResponse(Bookings booking) {
        var labels = booking.getShowtimeSeats().stream()
                .map(ss -> ss.getSeat().getSeatRow() + "-" + ss.getSeat().getSeatNumber())
                .toList();

        return new BookingConfirmation(
                booking.getBookingReference(),
                booking.getShowtime().getTheater().getName(),
                booking.getShowtime().getMovie().getTitle(),
                booking.getShowtime().getStartTime(),
                labels,
                booking.getTotalAmount(),
                "VISTA-QR-" + booking.getBookingReference()
        );
    }

    private void releaseRedisLocks(String showtimeId, List<String> seatLabels) {
        var keys = seatLabels.stream()
                .map(label -> LOCK_PREFIX + showtimeId + "_" + label)
                .toList();
        redisTemplate.delete(keys);
    }
}