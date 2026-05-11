# Spring Boot Learning Project

A hands-on Spring Boot demo exploring observability, messaging, code quality, and database tooling.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4.5 |
| Database | MySQL 8.4, PostgreSQL 17 |
| Connection Pool | HikariCP |
| Messaging | Apache Kafka (KRaft mode) + Protobuf |
| Cache | Redis Stack |
| Observability | Datadog APM, Micrometer, OpenTelemetry (OTLP) |
| Code Quality | SonarQube, Spotless (Google Java Format) |
| AWS Local | LocalStack |
| Connection Proxy | PgBouncer |
| Testing | JUnit 5, Testcontainers |

## Project Structure

```
spring-demo/
├── app/                        # Main Spring Boot application
│   └── src/main/java/com/example/demo/
│       ├── controller/         # REST controllers
│       ├── service/            # Business logic
│       ├── repository/         # Data access
│       ├── model/              # JPA entities
│       ├── dto/                # Data transfer objects
│       ├── consumer/           # Kafka consumers
│       ├── configuration/      # Bean/datasource config
│       ├── aop/                # Aspects (DB connection monitoring)
│       ├── eventlisteners/     # Hibernate & custom events
│       ├── filter/             # Request logging filter
│       ├── exception/          # Global exception handling
│       └── proto/              # Generated Protobuf classes
├── datadog/                    # Datadog agent config
├── pgbouncer/                  # PgBouncer config
├── docker-compose.yml
└── build.gradle
```

## Prerequisites

- Java 17+
- Docker & Docker Compose
- Gradle (wrapper included)

## Running Locally

Start all infrastructure services:

```bash
docker compose up -d kafka mysql redis
```

Run the application:

```bash
./gradlew :app:bootRun
```

The app starts on `http://localhost:8080` with virtual threads enabled and a 100MB heap limit.

## API Reference

### Books

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/books?page=0&size=10` | List all books (paginated) |
| `GET` | `/book/{id}` | Get book by ID |
| `GET` | `/book?name=<name>` | Search books by name |
| `POST` | `/books` | Create a book |
| `PUT` | `/book/{id}` | Update a book |
| `DELETE` | `/book/{id}` | Delete a book |
| `POST` | `/book/{bookId}/review` | Add a review to a book |

**Create book body:**
```json
{ "title": "Clean Code", "author": "Robert Martin", "publicationDate": "2008-08-01" }
```

**Add review body:**
```json
{ "comment": "Great read", "rating": 5 }
```

### Kafka

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/send` | Send JSON message to Kafka |
| `POST` | `/send-protobuf` | Send Protobuf message to Kafka |

**Send JSON body:**
```json
{ "topic": "my-topic", "message": "hello" }
```

**Send Protobuf body:**
```json
{ "id": "1", "username": "alice", "email": "alice@example.com", "message": "hello", "type": "INFO" }
```

- JSON consumer reads from `my-topic`
- Protobuf consumer reads from `user-protobuf-topic`

### Diagnostics

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/` | Hello World |
| `GET` | `/health` | Health check |
| `GET` | `/test-sleep?seconds=1` | Test DB connection pool (no transaction) |
| `GET` | `/test-sleep-1?seconds=1` | Test DB connection pool (with transaction) |
| `GET` | `/trigger-oom` | Trigger OOM for heap dump testing |

### Actuator

Exposed endpoints: `health`, `info`, `metrics`, `prometheus`

```
GET /actuator/health
GET /actuator/metrics
GET /actuator/prometheus
```

## Infrastructure Services

| Service | Port | UI |
|---|---|---|
| Kafka | 9092 | Kafka UI → http://localhost:8890 |
| MySQL | 3306 | — |
| PostgreSQL | 5432 | — |
| Redis Stack | 6379 | RedisInsight → http://localhost:8001 |
| SonarQube | 9000 | http://localhost:9000 |
| LocalStack | 4566 | http://localhost:8080 |
| Datadog Agent | 8125 (StatsD), 8126 (APM) | — |

> **Note:** PgBouncer and PostgreSQL both map to port 5432 in `docker-compose.yml`. Run them exclusively, not simultaneously.

## Code Quality

```bash
# Check Google Java Style compliance
./gradlew spotlessCheck

# Auto-apply formatting
./gradlew spotlessApply
```

### SonarQube

Start SonarQube (already defined in docker-compose.yml):

```bash
docker compose up -d sonarqube
```

1. Open http://localhost:9000 and log in with `admin/admin`
2. Create a project and generate an API token
3. Set the token in `app/build.gradle` under `sonar.token`
4. Run the analysis:

```bash
./gradlew sonar
```

## Protobuf

Proto files live in `app/src/main/protobuf/`. Generated Java classes are committed under `app/src/main/java/com/example/demo/proto/`.

To regenerate after editing a `.proto` file:

**Install the compiler:**
```bash
# macOS
brew install protobuf
```

**Regenerate:**
```bash
cd app
protoc --java_out=src/main/java --proto_path=src/main/protobuf src/main/protobuf/*.proto
```

Current proto files:
- `user_message.proto` — `UserMessage` with `id`, `username`, `email`, `message`, `timestamp`, `type` (INFO / WARNING / ERROR)

## Observability

The app ships metrics to three backends simultaneously:

| Backend | Config key | Default |
|---|---|---|
| Datadog | `management.metrics.export.datadog.*` | US5 region |
| Prometheus | `/actuator/prometheus` | Always enabled |
| OpenTelemetry (OTLP) | `management.metrics.export.otlp.*` | `localhost:4318` |

Set your real Datadog API key in `application.properties` before enabling the Datadog exporter.

## Testing

```bash
./gradlew test
```

Integration tests use Testcontainers to spin up a real MySQL instance — no mocks needed.

## JVM Tuning (bootRun)

The app runs with these JVM flags for OOM testing:

```
-Xmx100m
-XX:MaxDirectMemorySize=32m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=build/dump.hprof
```

Hit `GET /trigger-oom` to exercise the heap dump path.
