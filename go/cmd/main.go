package main

import (
	"Cinema/internal/adapters/redis"
	"Cinema/internal/booking"
	"encoding/json"
	"log"
	"net/http"
)

func main() {
	mux := http.NewServeMux()

	mux.HandleFunc("GET /movies", listMovies)

	mux.Handle("GET /", http.FileServer(http.Dir("static")))

	store := booking.NewRedisStore(redis.NewClient("localhost:6379"))
	svc := booking.NewService(store)

	bookingHandler := booking.NewHandler(svc)

	mux.HandleFunc("GET /movies/{movieID}/seats", bookingHandler.ListSeats)
	mux.HandleFunc("POST /movies/{movieID}/seats/{seatID}/hold", bookingHandler.HoldSeat)

	mux.HandleFunc("PUT /sessions/{sessionID}/confirm", bookingHandler.ConfirmSession)
	mux.HandleFunc("DELETE /sessions/{sessionID}", bookingHandler.ReleaseSession)

	if err := http.ListenAndServe(":8080", mux); err != nil {
		log.Fatal(err)
	}
}

var movies = []movieResponse{
	{
		ID:          "1",
		Title:       "The Matrix",
		Rows:        5,
		SeatsPerRow: 10,
	},
	{
		ID:          "2",
		Title:       "Inception",
		Rows:        5,
		SeatsPerRow: 10,
	},
}

func listMovies(w http.ResponseWriter, r *http.Request) {
	WriteJson(w, http.StatusOK, movies)
}

func WriteJson(w http.ResponseWriter, status int, responses any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	json.NewEncoder(w).Encode(responses)
}

type movieResponse struct {
	ID          string `json:"id"`
	Title       string `json:"title"`
	Rows        int    `json:"rows"`
	SeatsPerRow int    `json:"seats_per_row"`
}
