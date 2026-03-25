# Spring Boot Caching Demo

A focused project demonstrating caching concepts in Spring Boot using **Caffeine** (in-memory) and **Redis** (distributed).

---

## What this project covers

| Concept | Description |
|---|---|
| `@Cacheable` | Cache method result on first call, return cached on subsequent calls |
| `@CachePut` | Always execute method and update cache |
| `@CacheEvict` | Remove entry from cache |
| `@Caching` | Combine multiple cache operations on one method |
| Caffeine | Fast in-memory cache with TTL and size limits |
| Redis | Distributed cache shared across servers |
| Rate Limiting | Limit API requests per IP using Redis counters |

---

## Tech Stack

- Java 21
- Spring Boot 4.x
- Spring Cache
- Caffeine
- Redis
- Spring Data Redis
- MySQL

---

## Rate Limiting

All `/api/**` endpoints are rate limited to **5 requests per minute per IP**.

Exceed the limit and you get:
```
HTTP 429 Too Many Requests
"Too many requests — try again in 1 minute"
```

Check remaining counter in Redis:
```bash
redis-cli get "rate:127.0.0.1"
redis-cli ttl "rate:127.0.0.1"
```

---

## Caching Flow

```
First call  → cache miss → hits database → stores in cache
Second call → cache hit  → returns from cache (no DB call)
TTL expires → cache miss → hits database → stores in cache again
```

### Cache Configuration

| Cache | TTL | Max Size | Provider |
|---|---|---|---|
| `products` | 10 minutes | 500 entries | Caffeine / Redis |
| `products-all` | 5 minutes | 10 entries | Caffeine / Redis |

---

## Switching Between Caffeine and Redis

**Caffeine (in-memory):**
```yaml
spring:
  cache:
    type: caffeine
```

**Redis (distributed):**
```yaml
spring:
  cache:
    type: redis
  redis:
    host: localhost
    port: 6379
```

Zero changes needed in service code — annotations work the same for both.

---

## Verify Caching Works

Add this temporarily to your service method:
```java
@Cacheable(value = "products", key = "#id")
public ProductDTO getProduct(Long id) {
    System.out.println(">>> CACHE MISS — hitting database for id: " + id);
    ...
}
```

- First call → prints the message (cache miss)
- Second call → nothing printed (cache hit, method skipped)

For Redis, inspect cached data directly:
```bash
redis-cli keys *
redis-cli get "products::1"
```