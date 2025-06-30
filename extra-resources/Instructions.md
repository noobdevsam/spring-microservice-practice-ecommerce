# Spring_Boot_Microservices_E-Commerce_Application

## Microservice Architecture Overview

---

### 1. API Gateway

- All client requests go through the API Gateway, which routes them to the appropriate microservice.
- **Exposed endpoints:** `/customers`, `/products`, `/orders`.

### 2. Microservices

- Each microservice is a Spring Boot application, running in Docker containers.
- **Customer Service:** Manages customer data, uses MongoDB for storage.
- **Product Service:** Manages product data, uses its own database.
- **Order Service:** Handles order creation and management, uses its own database.
- **Payment Service:** Processes payments, uses its own database.
- **Notification Service:** Sends notifications (e.g., email), uses MongoDB for storage.

### 3. Service Interactions

- API Gateway forwards requests to the respective services.
- Order Service interacts with Product Service to fetch product details.
- Payment Service processes payments for orders.
- Both Order and Payment services send asynchronous confirmation messages via Kafka (message broker) to the Notification
  Service.
- Notification Service sends out notifications and logs them.

### 4. Infrastructure Services

- **Eureka Server:** Service discovery for all microservices.
- **Config Server:** Centralized configuration management for all services.
- **Kafka:** Message broker for asynchronous communication.
- **Zipkin:** Distributed tracing for monitoring and debugging microservice interactions.

### 5. Databases

- Each service has its own database (MongoDB or other), ensuring data isolation and autonomy.

### 6. Observability

- All services are integrated with Zipkin for distributed tracing, enabling end-to-end request tracking.

---

## Implementation Steps

1. Set up Config Server and Eureka Server.
2. Develop each microservice with its own database and REST endpoints.
3. Integrate services with Eureka for discovery and Config Server for configuration.
4. Implement API Gateway for routing and security.
5. Set up Kafka for asynchronous messaging.
6. Integrate Zipkin for tracing.
7. Containerize all components with Docker.
8. Deploy and test the full system.

---

This structure ensures scalability, maintainability, and resilience for the e-commerce application.
