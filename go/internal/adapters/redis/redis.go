package redis

import (
	"context"
	"log"

	goredis "github.com/redis/go-redis/v9"
)

func NewClient(addr string) *goredis.Client {
	rbd := goredis.NewClient(&goredis.Options{
		Addr: addr,
	})

	if err := rbd.Ping(context.Background()).Err(); err != nil {
		log.Fatalf("failed to connect to Redis: %v", err)
	}
	log.Printf("connected to Redis at %s", addr)

	return rbd
}
