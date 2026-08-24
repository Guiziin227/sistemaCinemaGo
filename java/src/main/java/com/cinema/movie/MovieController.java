package com.cinema.movie;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/movies")
public class MovieController {
  private static final List<MovieResponse> MOVIES = List.of(
      new MovieResponse("1", "The Matrix", 5, 10), new MovieResponse("2", "Inception", 5, 10));
  @GetMapping public List<MovieResponse> list() { return MOVIES; }
  public record MovieResponse(String id, String title, int rows, int seatsPerRow) {}
}
