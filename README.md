# Job Application Microservices Platform

A production-ready job board application built with Spring Boot microservices architecture, demonstrating modern cloud-native patterns and best practices.

## Overview

This project implements a complete microservices ecosystem for managing job postings, companies, and reviews. It showcases enterprise patterns including service discovery, distributed tracing, inter-service communication, and containerized deployment.

## Architecture

The system consists of three core business services and three supporting infrastructure services:

**Business Services**
- **Job Service** - Manages job postings (CRUD operations)
- **Company Service** - Handles company profiles and information
- **Review Service** - Manages company reviews and ratings

**Infrastructure Services**
- **Service Registry (Eureka)** - Service discovery and registration
- **API Gateway** - Single entry point for client requests, routes to appropriate services
- **Config Server** - Centralized configuration management

Each service communicates through REST APIs using OpenFeign clients, with service discovery handled automatically by Eureka.

## Key Features

- **Microservices Architecture** - Independent services with single responsibilities
- **Service Discovery** - Dynamic service registration and lookup with Eureka
- **Inter-Service Communication** - Type-safe REST clients using OpenFeign
- **Distributed Tracing** - Request tracing across services with Zipkin and Micrometer
- **Asynchronous Messaging** - Event-driven communication using RabbitMQ
- **Health Monitoring** - Built-in health checks and metrics with Spring Actuator
- **Containerization** - Full Docker support with Docker Compose orchestration
- **Environment Profiles** - Separate configurations for local and Docker environments

## Tech Stack

- **Framework**: Spring Boot 3.5.6
- **Language**: Java 21
- **Service Discovery**: Spring Cloud Netflix Eureka
- **API Communication**: Spring Cloud OpenFeign
- **Database**: PostgreSQL
- **Message Broker**: RabbitMQ (Spring AMQP)
- **Tracing**: Micrometer + Zipkin
- **Monitoring**: Spring Boot Actuator
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven

## Getting Started

### Prerequisites

- Java 21 or newer
- Apache Maven 3.6+
- Docker and Docker Compose
- Docker Hub account (for pushing images)

### Building the Services

Each microservice needs to be built as a Docker image. Navigate to each service directory and run:

```bash
# Service Registry
cd servicereg
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=yourusername/servicereg

# Job Service
cd ../jobms
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=yourusername/jobms

# Company Service
cd ../companyms
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=yourusername/companyms

# Review Service
cd ../reviewms
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=yourusername/reviewms
```

Replace `yourusername` with your Docker Hub username.

### Running the Application

Start the entire system with a single command:

```bash
docker-compose up
```

This will start:
- PostgreSQL database
- RabbitMQ message broker
- Zipkin tracing server
- Eureka service registry
- All microservices

**Access Points:**
- Eureka Dashboard: http://localhost:8761
- Zipkin UI: http://localhost:9411
- Job Service: http://localhost:8082
- Company Service: http://localhost:8083
- Review Service: http://localhost:8084
- RabbitMQ Management: http://localhost:15672 (guest/guest)

### Configuration

The project uses Spring Profiles for environment-specific settings:

- `application.properties` - Local development configuration (uses localhost)
- `application-docker.properties` - Docker environment configuration (uses service names)

When running with Docker Compose, the `docker` profile is automatically activated.

## Service Details

### Job Service (Port 8082)
Manages job postings with full CRUD operations. Communicates with Company Service to fetch company details for job listings.

### Company Service (Port 8083)
Handles company information and profiles. Used by Job Service to enrich job data with company details.

### Review Service (Port 8084)
Manages user reviews and ratings for companies. Integrates with Company Service for validation.

### Service Registry (Port 8761)
Eureka server that maintains a registry of all running service instances. Services register themselves on startup and discover other services dynamically.

## Monitoring & Observability

**Health Checks**: Each service exposes health endpoints via Spring Actuator at `/actuator/health`

**Distributed Tracing**: All requests are traced across services using Micrometer and visualized in Zipkin UI

**Metrics**: Service metrics are available at `/actuator/metrics`

**Service Status**: Monitor all registered services through Eureka dashboard

## Design Patterns

- **API Gateway Pattern** - Single entry point for all client requests
- **Service Registry Pattern** - Dynamic service discovery
- **Circuit Breaker Pattern** - Fault tolerance for inter-service calls
- **Database Per Service** - Each service has its own database schema
- **DTO Pattern** - Data transfer objects for API contracts
- **Event-Driven Architecture** - Asynchronous communication via message queues

## Development

### Local Development
For local development without Docker, use the default profile which connects to localhost instances of PostgreSQL, RabbitMQ, and Zipkin.

### Adding New Services
1. Create Spring Boot application with required dependencies
2. Configure Eureka client in application.properties
3. Add service-specific configuration
4. Build Docker image using Maven
5. Add service definition to docker-compose.yml

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.
