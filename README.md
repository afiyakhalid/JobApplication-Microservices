🚀 Job Application Microservices Project

A complete backend system for a job board application, built using a modern microservices architecture with Spring Boot, Docker, and Eureka.

🏛️ Architecture Overview

The system is designed with a service-oriented approach, separating concerns into distinct microservices. These are supported by common infrastructure patterns like a service registry, an API gateway, and a configuration server.

Core Microservices

Job Service (jobms): Responsible for all CRUD (Create, Read, Update, Delete) operations related to job postings.

Company Service (companyms): Manages company profiles, including details that can be associated with job postings.

Review Service (reviewms): Handles user reviews for companies.

Supporting Services (Infrastructure)

Service Registry (servicereg): Uses Spring Cloud Netflix Eureka to allow services to register themselves and discover others dynamically.

API Gateway: (Concept discussed) Acts as the single entry point for all client requests, routing them to the appropriate microservice.

Config Server: (Concept discussed) Provides centralized configuration management for all microservices.

**** > A diagram showing the relationship between the Gateway, Service Registry, Config Server, and the core microservices.

🛠️ Tech Stack & Key Patterns

This project leverages the Spring ecosystem and other modern tools to address the challenges of microservice development.

Framework: Spring Boot 3

Interservice Communication: Spring Cloud OpenFeign

Service Discovery: Spring Cloud Netflix Eureka

Database: PostgreSQL

Distributed Tracing: Micrometer & Zipkin

Health Monitoring: Spring Boot Actuator

Asynchronous Communication: Spring AMQP with RabbitMQ

Containerization: Docker and Docker Compose

Design Patterns: Data Transfer Object (DTO)

⚙️ Setup and Running the Project

Follow these steps to build and run the entire application using Docker.

✅ Prerequisites

Java 21 (or newer)

Apache Maven

Docker and Docker Compose

1. Build Docker Images for Each Service

Each microservice needs to be packaged as a Docker image. Navigate into the root directory of each service and run the Maven command.

Note: The pom.xml for each service has been configured to use the gcr.io/buildpacks/builder:v1 builder to ensure a stable build process.

# Example for the Service Registry
cd servicereg
./mvnw spring-boot:build-image "-Dspring-boot.build-image.imageName=your-dockerhub-username/servicereg"

# Example for the Job Service
cd ../jobms
./mvnw spring-boot:build-image "-Dspring-boot.build-image.imageName=your-dockerhub-username/jobms"

# --- Repeat this process for all other services ---


Important: Replace your-dockerhub-username with your actual Docker Hub username.

2. Configure for Docker Environment

The project uses Spring Profiles to manage environment-specific configurations.

application.properties: Contains settings for local development (using localhost).

application-docker.properties: Overrides settings for the containerized environment, using Docker service names (jobms, zipkin, etc.) for communication.

3. Run the Entire Application with Docker Compose

Once all images are built, start the entire system with a single command from the root directory containing your docker-compose.yml file.

docker-compose up


This command will spin up the entire ecosystem, including:

A virtual network for your services.

Containers for PostgreSQL, RabbitMQ, Zipkin, and all your microservices.

The docker Spring profile will be automatically activated.
