# Cinema Booking — Java + PostgreSQL

Aplicação migrada para Java 21 e Spring Boot. O PostgreSQL substitui o Redis e garante apenas uma reserva por assento com uma restrição única e `UPSERT` atômico. Bloqueios não confirmados expiram em 2 minutos.

```bash
docker compose up --build
```

Abra <http://localhost:8080>. Para parar, execute `docker compose down`. Use `docker compose down -v` somente se quiser apagar também os dados.

Endpoints: `GET /movies`, `GET /movies/{movieId}/seats`, `POST /movies/{movieId}/seats/{seatId}/hold`, `PUT /sessions/{sessionId}/confirm` e `DELETE /sessions/{sessionId}`.
