package booking

import "sync"

type ConcurrentStore struct {
	bookings map[string]Booking
	sync.RWMutex
}

func NewConcurrentStore() *ConcurrentStore {
	return &ConcurrentStore{
		bookings: map[string]Booking{},
	}
}

func (s *ConcurrentStore) Book(b Booking) (Booking, error) {
	s.Lock() //travas de escrita para garantir que apenas uma goroutine possa modificar o mapa de reservas por vez
	defer s.Unlock()

	if _, exists := s.bookings[b.SeatID]; exists {
		return b, ErrSeatAlreadyBooked
	}

	s.bookings[b.SeatID] = b
	return b, nil
}

func (s *ConcurrentStore) ListBookings(movieID string) []Booking {
	s.RLock() //travas de leitura para permitir múltiplas leituras simultâneas
	defer s.RUnlock()

	var result []Booking
	for _, booking := range s.bookings {
		if booking.MovieID == movieID {
			result = append(result, booking)
		}
	}

	return result
}
