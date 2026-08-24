package com.cinema.booking;
import java.time.Instant;
import java.util.UUID;
public record Booking(UUID id, String movieId, String seatId, String userId, BookingStatus status, Instant expiresAt) {}
