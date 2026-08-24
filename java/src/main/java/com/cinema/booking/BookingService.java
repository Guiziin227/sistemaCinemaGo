package com.cinema.booking;
import static com.cinema.booking.BookingExceptions.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class BookingService {
  static final Duration HOLD_DURATION = Duration.ofMinutes(2);
  private final BookingRepository repository; private final Clock clock;
  @Autowired public BookingService(BookingRepository repository) { this(repository, Clock.systemUTC()); }
  BookingService(BookingRepository repository, Clock clock) { this.repository=repository; this.clock=clock; }
  @Transactional public Booking hold(String movieId, String seatId, String userId) {
    Booking b = new Booking(UUID.randomUUID(), movieId, seatId, userId, BookingStatus.HELD, clock.instant().plus(HOLD_DURATION));
    return repository.hold(b).orElseThrow(SeatAlreadyBooked::new);
  }
  @Transactional(readOnly=true) public List<Booking> listActive(String movieId) { return repository.listActive(movieId); }
  @Transactional public Booking confirm(UUID id, String userId) {
    Booking b=ownedSession(id,userId); return b.status()==BookingStatus.CONFIRMED ? b : repository.confirm(id);
  }
  @Transactional public void release(UUID id, String userId) {
    Booking b=ownedSession(id,userId);
    if (b.status()==BookingStatus.CONFIRMED) throw new IllegalStateException("confirmed bookings cannot be released");
    repository.delete(id);
  }
  private Booking ownedSession(UUID id, String userId) {
    Booking b=repository.findActiveForUpdate(id).orElseThrow(SessionNotFound::new);
    if (!b.userId().equals(userId)) throw new SessionForbidden(); return b;
  }
}
