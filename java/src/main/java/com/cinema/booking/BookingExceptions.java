package com.cinema.booking;
final class BookingExceptions {
  private BookingExceptions() {}
  static final class SeatAlreadyBooked extends RuntimeException { SeatAlreadyBooked() { super("seat is already taken"); } }
  static final class SessionNotFound extends RuntimeException { SessionNotFound() { super("session not found or expired"); } }
  static final class SessionForbidden extends RuntimeException { SessionForbidden() { super("session belongs to another user"); } }
}
