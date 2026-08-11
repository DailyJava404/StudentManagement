# Student Management API

A robust RESTful API built with Spring Boot for managing student records. This project includes secure JWT authentication, standardized responses, comprehensive lifecycle logging, and third-party API integrations.

## 🚀 Features

*   **Student CRUD Operations:** Full create, read, update, and delete functionality for student entities.
*   **JWT Authentication:** Secure API endpoints requiring Bearer token authorization.
*   **Relational Database:** Custom entity mapping and relational database schema.
*   **Standardized Responses:** Uniform API response structure for both success and failure states.
*   **Input Validation:** Strict parameter and payload validation using Spring Boot `@Valid`.
*   **Global Exception Handling:** Centralized `@ControllerAdvice` to gracefully handle errors and exceptions.
*   **File-Based Logging:** Automated logging of HTTP requests, responses, and error traces into dedicated log files.
*   **External API Integration:** Third-party service calls implemented via WebClient / FeignClient with built-in request/response interceptor logging.

## 🛠️ Tech Stack

*   **Framework:** Spring Boot (Java)
*   **Database:** SQL Server
*   **Security:** Spring Security & JWT (JSON Web Tokens)
*   **HTTP Clients:** Spring Cloud OpenFeign
*   **Logging:** SLF4J & Logback
*   **Build Tool:** Maven

## 📋 Prerequisites

Before running the project, ensure you have the following installed:
*   Java 17 or higher
*   Maven or Gradle
*   SQL Server
