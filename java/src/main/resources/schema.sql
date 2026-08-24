CREATE TABLE IF NOT EXISTS bookings (
  id UUID PRIMARY KEY,
  movie_id VARCHAR(100) NOT NULL,
  seat_id VARCHAR(20) NOT NULL,
  user_id VARCHAR(100) NOT NULL,
  status VARCHAR(20) NOT NULL CHECK (status IN ('HELD','CONFIRMED')),
  expires_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT uq_booking_movie_seat UNIQUE (movie_id,seat_id),
  CONSTRAINT ck_booking_expiration CHECK ((status='HELD' AND expires_at IS NOT NULL) OR (status='CONFIRMED' AND expires_at IS NULL))
);
CREATE INDEX IF NOT EXISTS idx_bookings_movie ON bookings(movie_id);
CREATE INDEX IF NOT EXISTS idx_bookings_expiration ON bookings(expires_at) WHERE status='HELD';
