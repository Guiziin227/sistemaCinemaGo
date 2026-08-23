package booking

import "errors"

var (
	ErrSeatAlreadyBooked = errors.New("Seat is already booked")
)

type Booking struct {
	ID      string
	MovieID string
	SeatID  string
	UserID  string
	Status  string
}

type BookingStore interface {
	Book(b Booking) (Booking, error)
	ListBookings(movieID string) []Booking
}
