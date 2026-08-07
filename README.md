# Food Delivery Backend

A REST API for a food-delivery platform, built with Java and Spring Boot. The project currently covers customer authentication, restaurant menus, shopping carts, and order checkout.

## Features

- User registration and JWT-based login
- Authenticated user profile retrieval and updates
- Restaurant and address registration
- Menu categories and menu items
- Customer-facing nested restaurant menus
- One active cart per customer
- Cart items restricted to a single restaurant
- Add, view, update, and remove cart items
- Checkout that converts an active cart into an order
- Request validation and consistent API error responses
- Optimistic locking on carts and cart items

## Technology

- Java 21
- Spring Boot 4.1
- Spring MVC
- Spring Security OAuth2 Resource Server
- Spring Data JPA and Hibernate
- PostgreSQL
- Maven Wrapper

## Architecture

The application follows a conventional layered structure:

```text
HTTP request
    -> Controller
    -> Service (transactions and business rules)
    -> Repository
    -> PostgreSQL

Entity <-> Mapper <-> Request/Response DTO
```

Main packages are under `com.sahithya.fooddeliverybackend`:

- `controller` — HTTP endpoints
- `service` — use cases, transactions, and business rules
- `repository` — Spring Data JPA access
- `entity` — persistent domain objects
- `dto` — API request and response contracts
- `mapper` — entity/DTO transformations
- `config` — security, JWT, and password configuration
- `exception` — domain errors and centralized HTTP error handling

## Prerequisites

- JDK 21
- PostgreSQL
- No system Maven installation is required; the repository includes Maven Wrapper.

## Configuration

Create the local database:

```sql
CREATE DATABASE food_delivery_db;
```

Set these environment variables before starting the application:

| Variable | Required | Default | Description |
| --- | --- | --- | --- |
| `DB_PASSWORD` | Yes | — | PostgreSQL password |
| `JWT_SECRET` | Yes | — | HMAC signing secret; use at least 32 random bytes |
| `DB_URL` | No | `jdbc:postgresql://localhost:5432/food_delivery_db` | JDBC URL |
| `DB_USERNAME` | No | `postgres` | PostgreSQL user |
| `JPA_DDL_AUTO` | No | `update` | Hibernate schema strategy for local development |
| `JPA_SHOW_SQL` | No | `true` | Print generated SQL |

The included `.env.example` is a reference. Spring Boot does not automatically load `.env`; export the values in your shell or add them to the IntelliJ run configuration.

Example terminal setup:

```bash
export DB_PASSWORD='your-local-postgres-password'
export JWT_SECRET='replace-with-a-random-secret-at-least-32-bytes-long'
```

Never commit real credentials or JWT secrets.

## Run locally

```bash
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080` by default.

## Tests

With the required environment variables and PostgreSQL available:

```bash
./mvnw test
```

## Authentication

Register a customer:

```http
POST /users/register
Content-Type: application/json

{
  "name": "Sample User",
  "email": "user@example.com",
  "password": "strong-password"
}
```

Log in:

```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "strong-password"
}
```

Use the returned token for protected endpoints:

```http
Authorization: Bearer <access-token>
```

## API overview

All routes except registration and login currently require a bearer token.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/users/register` | Register a user |
| `POST` | `/auth/login` | Obtain a JWT access token |
| `GET` | `/users/me` | Get the authenticated user |
| `GET` | `/users/{id}` | Get a user by ID |
| `PATCH` | `/users/{id}` | Update a user |
| `POST` | `/restaurants` | Register a restaurant |
| `POST` | `/restaurants/{restaurantId}/categories` | Create a menu category |
| `GET` | `/restaurants/{restaurantId}/menu` | Get the nested restaurant menu |
| `POST` | `/categories/{categoryId}/items` | Create a menu item |
| `GET` | `/cart` | Get the active cart |
| `POST` | `/cart/item` | Add an item to the cart |
| `PATCH` | `/cart/items/{cartItemId}` | Change an item quantity |
| `DELETE` | `/cart/items/{cartItemId}` | Remove an item from the cart |
| `POST` | `/orders/checkout` | Checkout the active cart |

## Core domain model

```text
User
Restaurant -> Address
Restaurant -> MenuCategory -> MenuItem
User -> Cart -> CartItem -> MenuItem
User -> Order -> OrderItem
```

## Current development notes

This is an actively developed learning project. Before production use, the main next steps are:

- Add role- and ownership-based authorization
- Replace automatic schema updates with Flyway or Liquibase migrations
- Add service, controller, repository, and integration test coverage
- Add OpenAPI/Swagger documentation
- Add containerized local development
- Add production profiles, observability, and deployment configuration

## Security

If you discover a security issue, do not open a public issue containing credentials or exploit details. Remove exposed secrets immediately and rotate them at their source.
