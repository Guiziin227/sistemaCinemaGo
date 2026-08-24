package com.cinema.booking;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
  static final Instant NOW=Instant.parse("2026-01-01T12:00:00Z");
  @Mock BookingRepository repository;
  @Test void holdsForTwoMinutes() {
    when(repository.hold(any())).thenAnswer(i -> Optional.of(i.getArgument(0)));
    Booking b=new BookingService(repository,Clock.fixed(NOW,ZoneOffset.UTC)).hold("1","A1","user");
    assertThat(b.expiresAt()).isEqualTo(NOW.plusSeconds(120)); assertThat(b.status()).isEqualTo(BookingStatus.HELD);
  }
  @Test void rejectsOccupiedSeat() {
    when(repository.hold(any())).thenReturn(Optional.empty());
    assertThatThrownBy(() -> new BookingService(repository,Clock.fixed(NOW,ZoneOffset.UTC)).hold("1","A1","user"))
      .isInstanceOf(BookingExceptions.SeatAlreadyBooked.class);
  }
}
