# **Online Book Store**

## Overview

This project is a backend application for managing a bookstore, developed using the Spring Boot framework. It provides a structured and extensible system for handling book-related operations such as creation, retrieval, updating, and deletion. The application follows established software engineering principles, including layered architecture and separation of concerns.

The system is designed as a RESTful service that can be integrated with various front-end clients or used independently for API-based interactions. It demonstrates practical usage of Spring Boot, Spring Data JPA, and relational database management systems in building scalable web applications.

## **Objectives**

The main objectives of this project are:

* To implement a backend system for bookstore management
* To demonstrate CRUD operations using RESTful principles
* To apply layered architecture (Controller, Service, Repository)
* To integrate a relational database using JPA/Hibernate
* To provide a foundation for further extensions such as authentication, ordering systems, or recommendation modules

**Functional Capabilities**
The application supports the following functionality:

* Management of book entities (create, read, update, delete)
* Retrieval of books by identifier
* Listing all available books in the system
* Persistent storage using a relational database
* Structured API endpoints for external interaction

**Technology Stack**
* Programming Language: Java
* Framework: Spring Boot
* Data Access: Spring Data JPA (Hibernate)
* Database: MySQL or PostgreSQL (configurable)
* Build Tool: Maven
* API Format: REST (JSON)

## **Setup and Execution**
Prerequisites
Ensure the following software is installed:
* Java Development Kit (JDK) 17 or higher
* Maven 3.6 or higher
* MySQL or PostgreSQL database server
* Git

## **Docker**

The project includes Docker support for running both the Spring Boot application and the MySQL database in containers. Docker configuration is provided through three files: Dockerfile, docker-compose.yml, and .env.template.

The Docker setup starts two services:

* db — a MySQL container used as the application database
* app — the Spring Boot application container

The application container depends on the database container and starts only after MySQL passes its health check. Database connection settings are injected through environment variables, and the Spring Boot container connects to MySQL using the internal Docker service name db.

## Getting Started

### Clone the repository

```bash
git clone https://github.com/IvanShevchenko1/Spring-Boot-Book-Store.git
cd Spring-Boot-Book-Store
```

---

## Running the Application

### Using Docker (recommended)

```bash
cp .env.template .env
./mvnw clean package
docker compose up --build
```

Application will be available at:

```
http://localhost:<SPRING_LOCAL_PORT>
```

| Variable | Description | Example |
|-----------|--------------|---------|
| `SPRING_LOCAL_PORT` | Port on your host (localhost) that exposes the Spring Boot app | `8088` |
| `SPRING_DOCKER_PORT` | Internal port used inside the container by the Spring Boot app | `8080` |
| `DEBUG_PORT` | Optional remote debugging port for the Spring container | `5005` |

| Variable | Description | Example |
|-----------|--------------|---------|
| `MYSQLDB_LOCAL_PORT` | Port on your host for accessing MySQL | `3306` |
| `MYSQLDB_DOCKER_PORT` | Internal MySQL port inside the container | `3306` |
| `MYSQLDB_HOST` | Hostname that Spring Boot connects to inside the Docker network (usually the service name) | `db` |
| `MYSQLDB_DATABASE` | Name of the database to be created | `book_store_db` |
| `MYSQLDB_USER` | Application database username (non-root) | `bookuser` |
| `MYSQLDB_PASSWORD` | Password for the above user | `bookpass` |
| `MYSQL_ROOT_PASSWORD` | Root user password (for MySQL initialization) | `supersecretroot` |

## **API Overview**
The application exposes a set of RESTful APIs for managing core bookstore functionality, including authentication, books, categories, shopping cart operations, and orders. The API follows standard HTTP conventions and supports JSON for both requests and responses.

Authentication is handled using JWT (JSON Web Token). Public endpoints are available for user registration and login, while all other endpoints require a valid token provided in the Authorization: Bearer <token> header. Access to certain operations, such as creating or deleting books and categories or updating order status, is restricted to users with administrative privileges.

The API is organized into logical groups:

* Authentication API — user registration and login
* Books API — CRUD operations for books
* Categories API — management of book categories
* Shopping Cart API — managing user cart items
* Orders API — order creation and tracking

## ER Diagram

<img width="1902" height="2840" alt="book_store_db@localhost  2" src="https://github.com/user-attachments/assets/d95387d6-98a1-4a17-a6b0-cf6a96249a51" />

## API Endpoints by Role

### Public Endpoints

| Method | Endpoint | Description |
|--------|--------|-------------|
| POST | /auth/registration | Register new user |
| POST | /auth/login | Authenticate user |

---

### USER Endpoints

#### Books & Categories

| Method | Endpoint | Description |
|--------|--------|-------------|
| GET | /books | Get all books |
| GET | /books/{id} | Get book by ID |
| GET | /categories | Get all categories |
| GET | /categories/{id}/books | Get books by category |

#### Shopping Cart

| Method | Endpoint | Description |
|--------|--------|-------------|
| GET | /carts | Get current user cart |
| POST | /carts | Add item to cart |
| PUT | /carts/{id} | Update cart item quantity |
| DELETE | /carts/{id} | Remove item from cart |

#### Orders

| Method | Endpoint | Description |
|--------|--------|-------------|
| GET | /orders | Get user orders |
| POST | /orders | Create order |
| GET | /orders/{id}/items | Get order items |
| GET | /orders/{orderId}/items/{itemId} | Get specific item |

---

### ADMIN Endpoints

#### Books

| Method | Endpoint | Description |
|--------|--------|-------------|
| POST | /books | Create book |
| PUT | /books/{id} | Update book |
| DELETE | /books/{id} | Delete book |

#### Categories

| Method | Endpoint | Description |
|--------|--------|-------------|
| POST | /categories | Create category |
| PUT | /categories/{id} | Update category |
| DELETE | /categories/{id} | Delete category |

#### Orders

| Method | Endpoint | Description |
|--------|--------|-------------|
| PATCH | /orders/{id} | Update order status |

---

## **Swagger Documentation**

The project includes integrated Swagger (OpenAPI) documentation, which provides an interactive interface for exploring and testing all available API endpoints.

After starting the application, Swagger UI can be accessed at:

http://localhost:8080/swagger-ui/index.html

Swagger allows you to:

* View all available endpoints and their descriptions
* Inspect request and response models (DTOs)
* Execute API calls directly from the browser
* Authenticate using a JWT token via the "Authorize" button
