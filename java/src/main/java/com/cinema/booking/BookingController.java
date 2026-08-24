package com.cinema.booking;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping
public class BookingController {
  private final BookingService service;
  public BookingController(BookingService service) { this.service=service; }
  @GetMapping("/movies/{movieId}/seats") public List<SeatResponse> list(@PathVariable String movieId) {
    return service.listActive(movieId).stream().map(b -> new SeatResponse(b.seatId(),b.userId(),true,b.status()==BookingStatus.CONFIRMED)).toList();
  }
  @PostMapping("/movies/{movieId}/seats/{seatId}/hold") @ResponseStatus(HttpStatus.CREATED)
  public SessionResponse hold(@PathVariable String movieId,@PathVariable String seatId,@Valid @RequestBody UserRequest r) { return SessionResponse.from(service.hold(movieId,seatId,r.userId())); }
  @PutMapping("/sessions/{id}/confirm") public SessionResponse confirm(@PathVariable UUID id,@Valid @RequestBody UserRequest r) { return SessionResponse.from(service.confirm(id,r.userId())); }
  @DeleteMapping("/sessions/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void release(@PathVariable UUID id,@Valid @RequestBody UserRequest r) { service.release(id,r.userId()); }
  public record UserRequest(@NotBlank String userId) {}
  public record SeatResponse(String seatId,String userId,boolean booked,boolean confirmed) {}
  public record SessionResponse(UUID sessionId,String movieId,String seatId,String userId,String status,Instant expiresAt) {
    static SessionResponse from(Booking b) { return new SessionResponse(b.id(),b.movieId(),b.seatId(),b.userId(),b.status().name().toLowerCase(),b.expiresAt()); }
  }
}
