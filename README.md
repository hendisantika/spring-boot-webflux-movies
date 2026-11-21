# spring-boot-webflux-movies

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-webflux-movies/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-webflux-movies/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-7-47A248?logo=mongodb)
![Dependabot](https://img.shields.io/badge/Dependabot-enabled-025E8C?logo=dependabot)

Spring Boot WebFlux Movies on Micro Services — a reactive (Project Reactor) movie catalogue split into three
independent Spring Boot applications.

## Architecture

```
                  ┌──────────────────────────┐
  client ───────► │  movies-service  :8083   │
                  └────────────┬─────────────┘
                     WebClient │ (retry on 5xx)
              ┌────────────────┴────────────────┐
              ▼                                 ▼
┌──────────────────────────┐      ┌──────────────────────────┐
│ movies-info-service :8081│      │movies-review-service:8082│
│  annotated controllers   │      │  functional router       │
└────────────┬─────────────┘      └────────────┬─────────────┘
             └───────────────┬─────────────────┘
                             ▼
                    MongoDB :27017 (db: movie)
```

| Service                 | Port | Description                                                                 |
|-------------------------|------|-----------------------------------------------------------------------------|
| `movies-info-service`   | 8081 | CRUD + SSE stream for movie information (`@RestController`)                  |
| `movies-review-service` | 8082 | CRUD + SSE stream for movie reviews (functional `RouterFunction` endpoints) |
| `movies-service`        | 8083 | Aggregates movie info + reviews from the two services above via `WebClient` |

## Tech Stack

- Java 25
- Spring Boot 4.1 (WebFlux, Reactive MongoDB, Validation, WebClient)
- Project Reactor
- MongoDB 7
- Lombok
- Testing: JUnit 5, `WebTestClient`, `StepVerifier`, Mockito, Testcontainers 2 (MongoDB), WireMock
- Maven (each service has its own Maven Wrapper)
- GitHub Actions & Dependabot

## Prerequisites

- JDK 25
- Maven 3.9+ (or use the bundled `./mvnw`)
- Docker (for MongoDB and for running the Testcontainers based tests)

## Run MongoDB

Using Docker Compose (recommended):

```shell
docker compose up -d
```

Or with plain Docker:

```shell
docker run -d --rm --name mongodb -p 27017:27017 -v /tmp/mongo-data:/data/db mongo:7-jammy
```

Both services connect to `localhost:27017`, database `movie`. Override the location with the `MONGODB_HOST` and
`MONGODB_PORT` environment variables, e.g. `MONGODB_HOST=mongodb` when running inside a Docker network.

## Run the Services

Start each service in its own terminal:

```shell
cd movies-info-service && ./mvnw spring-boot:run
cd movies-review-service && ./mvnw spring-boot:run
cd movies-service && ./mvnw spring-boot:run
```

`movies-info-service` and `movies-review-service` seed sample data on startup.

`movies-service` reaches the other two services through the URLs configured in
`movies-service/src/main/resources/application.yml`:

```yaml
restClient:
  moviesInfoUrl: http://localhost:8081/v1/movieinfos
  reviewsUrl: http://localhost:8082/v1/reviews
```

## API Endpoints

### movies-info-service (`:8081`)

| Method | Path                         | Description                                    |
|--------|------------------------------|------------------------------------------------|
| POST   | `/v1/movieinfos`             | Create movie info                              |
| GET    | `/v1/movieinfos?year={year}` | List movie infos (optional `year` filter)      |
| GET    | `/v1/movieinfos/{id}`        | Get movie info by id                           |
| PUT    | `/v1/movieinfos/{id}`        | Update movie info                              |
| DELETE | `/v1/movieinfos/{id}`        | Delete movie info                              |
| GET    | `/v1/movieinfos/stream`      | Server-Sent Events stream of new movie infos   |

### movies-review-service (`:8082`)

| Method | Path                                  | Description                              |
|--------|---------------------------------------|------------------------------------------|
| POST   | `/v1/reviews`                         | Create review                            |
| GET    | `/v1/reviews?movieInfoId={movieInfoId}` | List reviews (optional movie filter)   |
| PUT    | `/v1/reviews/{id}`                    | Update review                            |
| DELETE | `/v1/reviews/{id}`                    | Delete review                            |
| GET    | `/v1/reviews/stream`                  | Server-Sent Events stream of new reviews |

### movies-service (`:8083`)

| Method | Path                    | Description                                     |
|--------|-------------------------|-------------------------------------------------|
| GET    | `/v1/movies/{movieId}`  | Movie info combined with its reviews            |
| GET    | `/v1/movies/stream`     | Server-Sent Events stream of movie infos        |

Example:

```shell
curl http://localhost:8081/v1/movieinfos
curl -N http://localhost:8082/v1/reviews/stream
```

## Running the Tests

Docker must be running because the integration tests start a MongoDB container with Testcontainers.

```shell
cd movies-info-service && ./mvnw test
cd movies-review-service && ./mvnw test
cd movies-service && ./mvnw test   # external services are stubbed with WireMock
```

## CI/CD

- **GitHub Actions** — [`.github/workflows/maven.yml`](.github/workflows/maven.yml) builds and tests every service in
  parallel (matrix job) on JDK 25 (Temurin) for each push and pull request to `main`. On `main` it also submits the
  Maven dependency graph so Dependabot alerts are accurate.
- **Dependabot** — [`.github/dependabot.yml`](.github/dependabot.yml) keeps dependencies up to date:
  - Maven (all three services, daily) with grouped updates for Spring Boot, Testcontainers, test libraries, Maven
    plugins and remaining minor/patch bumps, so the services stay on the same versions
  - GitHub Actions (weekly, grouped)
  - Docker Compose MongoDB image (weekly)

## Author

Hendi Santika — [s.id/hendisantika](https://s.id/hendisantika) · Telegram [@hendisantika34](https://t.me/hendisantika34)
