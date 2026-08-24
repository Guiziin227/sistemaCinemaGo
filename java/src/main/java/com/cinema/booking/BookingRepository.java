package com.cinema.booking;
import java.util.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
@Repository
public class BookingRepository {
  private final JdbcClient jdbc;
  public BookingRepository(JdbcClient jdbc) { this.jdbc = jdbc; }
  public Optional<Booking> hold(Booking b) {
    return jdbc.sql("""
      INSERT INTO bookings (id, movie_id, seat_id, user_id, status, expires_at)
      VALUES (:id, :movieId, :seatId, :userId, 'HELD', :expiresAt)
      ON CONFLICT (movie_id, seat_id) DO UPDATE SET id=EXCLUDED.id, user_id=EXCLUDED.user_id,
        status='HELD', expires_at=EXCLUDED.expires_at, updated_at=NOW()
      WHERE bookings.status='HELD' AND bookings.expires_at <= NOW()
      RETURNING id, movie_id, seat_id, user_id, status, expires_at
      """).param("id", b.id()).param("movieId", b.movieId()).param("seatId", b.seatId())
      .param("userId", b.userId())
      .param("expiresAt", OffsetDateTime.ofInstant(b.expiresAt(), ZoneOffset.UTC))
      .query(Booking.class).optional();
  }
  public List<Booking> listActive(String movieId) {
    return jdbc.sql("""
      SELECT id, movie_id, seat_id, user_id, status, expires_at FROM bookings
      WHERE movie_id=:movieId AND (status='CONFIRMED' OR expires_at > NOW()) ORDER BY seat_id
      """)
      .param("movieId", movieId).query(Booking.class).list();
  }
  public Optional<Booking> findActiveForUpdate(UUID id) {
    return jdbc.sql("""
      SELECT id, movie_id, seat_id, user_id, status, expires_at FROM bookings
      WHERE id=:id AND (status='CONFIRMED' OR expires_at > NOW()) FOR UPDATE
      """)
      .param("id", id).query(Booking.class).optional();
  }
  public Booking confirm(UUID id) {
    return jdbc.sql("""
      UPDATE bookings SET status='CONFIRMED', expires_at=NULL, updated_at=NOW() WHERE id=:id
      RETURNING id, movie_id, seat_id, user_id, status, expires_at
      """)
      .param("id", id).query(Booking.class).single();
  }
  public void delete(UUID id) { jdbc.sql("DELETE FROM bookings WHERE id=:id").param("id", id).update(); }
}
