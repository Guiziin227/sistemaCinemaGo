package com.cinema.booking;
import static com.cinema.booking.BookingExceptions.*;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(SeatAlreadyBooked.class) ResponseEntity<Map<String,String>> conflict(RuntimeException e) { return error(HttpStatus.CONFLICT,e.getMessage()); }
  @ExceptionHandler(SessionNotFound.class) ResponseEntity<Map<String,String>> missing(RuntimeException e) { return error(HttpStatus.NOT_FOUND,e.getMessage()); }
  @ExceptionHandler(SessionForbidden.class) ResponseEntity<Map<String,String>> forbidden(RuntimeException e) { return error(HttpStatus.FORBIDDEN,e.getMessage()); }
  @ExceptionHandler({MethodArgumentNotValidException.class,IllegalArgumentException.class}) ResponseEntity<Map<String,String>> bad(Exception e) { return error(HttpStatus.BAD_REQUEST,"invalid request"); }
  @ExceptionHandler(IllegalStateException.class) ResponseEntity<Map<String,String>> state(IllegalStateException e) { return error(HttpStatus.CONFLICT,e.getMessage()); }
  private ResponseEntity<Map<String,String>> error(HttpStatus s,String m) { return ResponseEntity.status(s).body(Map.of("error",m)); }
}
