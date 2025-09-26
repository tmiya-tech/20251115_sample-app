# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a multi-module Maven project demonstrating two different Spring Boot TODO application implementations:
- `sample-web`: Traditional Spring MVC with JPA and blocking I/O
- `sample-webflux`: Reactive implementation using WebFlux and R2DBC

Both modules share the same API design and database schema but use different technology stacks.

## Development Commands

### Build and Package
```bash
# Build all modules
mvn clean package

# Build specific module
mvn clean package -pl sample-web
mvn clean package -pl sample-webflux
```

### Running Applications
```bash
# Start PostgreSQL database
docker-compose up -d

# Run sample-web (blocking)
mvn spring-boot:run -pl sample-web

# Run sample-webflux (reactive)
mvn spring-boot:run -pl sample-webflux
```

### Testing
```bash
# Run all tests
mvn test

# Run tests for specific module
mvn test -pl sample-web
mvn test -pl sample-webflux
```

### Database
- PostgreSQL database runs on port 5432
- Default credentials: postgres/secret
- Schema is auto-initialized via schema.sql

## Architecture

### Project Structure
- Root POM acts as parent for multi-module setup
- Each module is independently deployable Spring Boot application
- Shared package structure: `com.example.sample.{web|webflux}.todo`

### Module Differences
- **sample-web**: Uses Spring Web + JPA + PostgreSQL driver
- **sample-webflux**: Uses Spring WebFlux + R2DBC + R2DBC PostgreSQL

### Common Layer Structure
Both modules follow the same layered architecture:
- `controller/`: REST controllers exposing `/api/todos` endpoints
- `service/`: Business logic layer
- `repository/`: Data access layer (JPA vs R2DBC)
- `entity/`: Database entities
- `dto/`: Data transfer objects for API contracts

### API Endpoints
Both applications expose identical REST APIs:
- `POST /api/todos` - Create todo
- `GET /api/todos/{id}` - Get single todo
- `GET /api/todos` - Get paginated todos (max 100 per page)
- `PUT /api/todos/{id}` - Update todo
- `PATCH /api/todos/{id}/complete` - Mark todo as complete
- `DELETE /api/todos/{id}` - Delete todo

## Java Version
Both modules use Java 24 as specified in their POM files.
