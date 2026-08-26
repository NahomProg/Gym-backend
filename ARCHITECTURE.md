# Gym Backend Architecture

## Layers

- controller: HTTP endpoints only.
- dto: request and response models.
- service: business-operation interfaces.
- service/impl: transactional business logic.
- repository: Spring Data JPA persistence interfaces.
- mapper: entity-to-response mapping interfaces and implementations.
- entity: JPA domain model.
- security: JWT and authentication logic.
- security/filter: JWT authentication and rate limiting filters.
- config: Spring configuration and beans.
- exception: API exception types and global error handling.
- resources/db/migration: Flyway database migrations.

## Database ownership

Flyway owns schema creation and changes. Hibernate no longer runs `ddl-auto`.

Add future schema changes as:

`V2__description.sql`

Never edit an already-applied migration in a shared environment.

## JWT

JWT uses HS256, which is HMAC with SHA-256. The signing secret must contain at least 32 UTF-8 bytes.

## Transactions

Write operations use `@Transactional`. Service implementations use class-level `@Transactional(readOnly = true)` for read operations, with write methods overriding it.

## JSON

Spring's configured `ObjectMapper` is injected where filters need to produce JSON responses. This avoids manually building JSON strings.

## CI/CD

GitHub Actions runs:

1. Maven tests.
2. Maven package.
3. Uploads the generated JAR as an artifact.

The Dockerfile can package the built JAR for deployment.
