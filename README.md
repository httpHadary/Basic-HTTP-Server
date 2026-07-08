# Java Micro Backend Framework

> **We use backend frameworks every day. But have you ever wondered how they're built?**

This project is my attempt to answer that question.

Java Micro Backend Framework is a lightweight HTTP framework built completely from scratch using Java's standard library. It implements many of the core concepts behind modern backend frameworks—including routing, middleware, request/response abstractions, authentication, and static file serving—to provide a hands-on understanding of how web frameworks work internally.

> **Note:** This is an educational project built to explore backend fundamentals. It is **not intended for production use**.

---

# Table of Contents

* [Features](#features)
* [Project Architecture](#project-architecture)
* [Getting Started](#getting-started)
* [Available Routes](#available-routes)
* [Routing](#routing)

  * Dynamic Path Parameters
  * Query Parameters
* [Request Object](#request-object)
* [Response Object](#response-object)
* [Middleware](#middleware)

  * Logging
  * Authentication
  * CORS
  * Exception Handling
* [Static File Server](#static-file-server)
* [Project Structure](#project-structure)
* [Future Improvements](#future-improvements)

---

# Features

* Built directly on raw TCP sockets
* HTTP/1.0 & HTTP/1.1 support
* Keep-Alive connections
* Concurrent requests handling (Virtual Threads)
* Custom routing system
* Dynamic path parameters
* Query parameter parsing
* Middleware pipeline
* Authentication middleware
* Logging middleware
* Exception middleware
* CORS middleware
* Static file server
* File CRUD operations
* JSON response support
* MIME type detection
* HEAD request support
* Clean Request & Response abstractions
* Zero external libraries

---

# Project Architecture

```text
                     Client
                        │
                        ▼
                 TCP Connection
                        │
                        ▼
              Parse HTTP Request
                        │
                        ▼
                  Resolve Route
             ┌──────────┴──────────┐
             │                     │
             ▼                     ▼
      Static File Server      Route Found
                                     │
                                     ▼
                            Middleware Chain
                                     │
     ┌────────────────────────────────────────────────────┐
     │ Exception → Logging → CORS → Authentication │
     └────────────────────────────────────────────────────┘
                                     │
                                     ▼
                               Route Handler
                                     │
                                     ▼
                              HTTP Response
```

---

# Getting Started

Clone the repository:

```bash
git clone https://github.com/<your-username>/<repository>.git
cd <repository>
```

Run the server:

```bash
java WebServer --directory files
```

The server will start on:

```
http://localhost:5555
```

---

# Available Routes

| Method | Endpoint               | Description                                  |
| ------ | ---------------------- | -------------------------------------------- |
| GET    | `/hello`               | Returns a plain text response                |
| GET    | `/echo/{message}`      | Echoes the supplied message                  |
| GET    | `/user-agent`          | Returns the client's User-Agent              |
| GET    | `/search?query=&page=` | Demonstrates query parameter parsing         |
| GET    | `/files/{filename}`    | Downloads a file *(Authentication required)* |
| POST   | `/files/{filename}`    | Creates a new file                           |
| PUT    | `/files/{filename}`    | Updates an existing file                     |
| DELETE | `/files/{filename}`    | Deletes a file                               |
| HEAD   | `/files/{filename}`    | Returns response headers only                |

If no route matches, the framework attempts to serve a static file from the `public/` directory.

---

# Routing

Routes are registered with the router by specifying an HTTP method and a URL pattern.

Example:

```java
router.get("/hello", Handlers::helloHandler, false);

router.post("/files/{filename}", Handlers::createFileHandler, false);
```

Each route consists of:

```
HTTP_METHOD + URL Pattern + Handler + Authentication Flag
```

---

## Dynamic Path Parameters

Path parameters allow parts of the URL to be captured dynamically.

Example:

```
GET /files/{filename}
```

Request:

```
GET /files/readme.txt
```

Inside the handler:

```java
String filename = request.getPathParameter("filename");
```

---

## Query Parameters

Query parameters are automatically parsed into a map.

Request:

```
GET /search?query=java&page=2
```

Inside the handler:

```java
String query = request.getQueryParameters().getOrDefault("query", "");
String page = request.getQueryParameters().getOrDefault("page", "1");
```

---

# Request Object

Every incoming request is represented by a `Request` object.

It provides access to:

* HTTP method
* URL
* HTTP version
* Headers
* Body
* Path parameters
* Query parameters
* Client IP
* Matched route

Examples:

```java
request.getVerb();

request.getURL();

request.getRequestHeaders();

request.getRequestBody();

request.getPathParameter("filename");

request.getQueryParameters();

request.getClientIP();
```

---

# Response Object

Handlers build responses using the `Response` abstraction.

Plain text:

```java
response.text("Hello World");
```

JSON:

```java
response.json(Map.of(
    "message", "Hello World"
));
```

Custom status code:

```java
response.setStatusCode("404");
response.text("Not Found");
```

Response headers:

```java
response.addResponseHeader("Content-Type", "text/plain");
```

---

# Middleware

Every request flows through a configurable middleware pipeline before reaching the handler.

```
Request
    │
    ▼
Exception Middleware
    │
    ▼
Logging Middleware
    │
    ▼
CORS Middleware
    │
    ▼
Authentication Middleware
    │
    ▼
Route Handler
    │
    ▼
Response
```

Middleware registration:

```java
use(new ExceptionMiddleware());
use(new LoggingMiddleware());
use(new CorsMiddleware());
use(new AuthenticationMiddleware());
```

Current middleware includes:

* Exception handling
* Structured request logging
* CORS support
* Token-based authentication

---

# Static File Server

When no registered route matches an incoming request, the framework attempts to serve a file from the `public/` directory.

Examples:

```
GET /
```

serves

```
public/index.html
```

while

```
GET /css/styles.css
```

serves

```
public/css/styles.css
```

The framework automatically determines the appropriate MIME type based on the file extension.

---

# Project Structure

```
src
├── middleware
│   ├── AuthenticationMiddleware
│   ├── CorsMiddleware
│   ├── ExceptionMiddleware
│   ├── LoggingMiddleware
│   ├── Middleware
│   └── MiddlewareChain
│
├── serialization
│   └── JsonSerializer
│
└── webServer
    ├── WebServer
    ├── Router
    ├── Route
    ├── Request
    ├── Response
    ├── RouteHandler
    ├── StaticFileServer
    └── Handlers
```

---

# Future Improvements

Although the project has achieved its educational goals, there are many directions it could be extended:

* Recursive JSON serialization
* Automatic HEAD → GET fallback
* Better HTTP request validation
* Additional MIME types
* HTTP caching
* Chunked Transfer Encoding
* GZIP compression
* TLS / HTTPS support

---

## 🎯 Project Goal

This project wasn't built to replace Spring Boot or other mature frameworks.

It was built to answer a simple question:

> **"How do backend frameworks actually work?"**

The best way to understand that wasn't by reading documentation—it was by building one.

---

## Final Thoughts

Modern backend frameworks make web development incredibly productive, but they also abstract away a tremendous amount of engineering.

Building this micro backend framework from scratch was an opportunity to explore those internals—from parsing raw HTTP requests to designing a middleware pipeline and routing system—and gain a deeper understanding of the technologies we use every day.

---


If you found this project interesting or have suggestions for improvements, feel free to open an issue or leave a ⭐.
