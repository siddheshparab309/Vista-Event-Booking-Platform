package com.events.superapp.modules.ticketing.controller;

import com.events.superapp.modules.ticketing.model.request.BookingRequest;
import com.events.superapp.modules.ticketing.model.response.BookingConfirmation;
import com.events.superapp.modules.ticketing.service.TicketingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticketing")
public class TicketingController {

    private final TicketingService ticketingService;

    @PostMapping("/reserve")
    public ResponseEntity<BookingConfirmation> reserveSeats(
            @RequestBody BookingRequest request, HttpServletRequest servletRequest
    ) {
        var sessionId = servletRequest.getSession().getId();
        return ResponseEntity.ok(ticketingService.reserveSeats(request, sessionId));
    }

    @PostMapping("/confirmTkt/{bookingId}")
    public ResponseEntity<BookingConfirmation> confirmBooking(
            @PathVariable Long bookingId, HttpServletRequest servletRequest
    ) {
        var sessionId = servletRequest.getSession().getId();
        return ResponseEntity.ok(ticketingService.confirmBooking(bookingId, sessionId));
    }

}