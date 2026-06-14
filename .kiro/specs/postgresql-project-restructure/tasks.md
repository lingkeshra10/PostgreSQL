# Implementation Plan: PostgreSQL Project Restructure

## Overview

Restructure the PostgreSQL Spring Boot demo project by renaming directories, fixing Docker configurations, adding Kubernetes manifests, standardizing credentials, and rewriting the README. All changes are organizational and configuration-focused — no application logic is modified.

## Tasks

- [x] 1. Rename source directory and clean up stale configs
  - [x] 1.1 Rename "Spring Source Code" directory to "Back-End"
    - Move/rename the `Spring Source Code/` directory to `Back-End/` at the project root
    - Verify the complete directory structure (`Back-End/demo/pom.xml`, `Back-End/demo/src/...`) is preserved
    - Remove the old `Spring Source Code/` directory if the move leaves it behind
    - _Requirements: 1.1, 1.2, 1.3_

  - [x] 1.2 Remove stale Docker configs under `Docker/otherservice/`
    - Delete `Docker/otherservice/DemoUserService/` and any incorrect configs within it
    - Ensure no references to the removed path remain in compose files
    - _Requirements: 3.3_

- [x] 2. Fix Dockerfile for the Application
  - [x] 2.1 Rewrite `Docker/dockerfile/DemoUserService/Dockerfile`
    - Replace contents with a correct Dockerfile using `eclipse-temurin:21-jre-alpine` base image
    - Set WORKDIR to `/opt/app`
    - COPY the JAR artifact as `app.jar`
    - EXPOSE port 8080
    - Define ENTRYPOINT `["java", "-jar", "app.jar"]`
    - _Requirements: 2.1, 2.2, 2.3, 2.4_

- [x] 3. Fix Docker Compose files
  - [x] 3.1 Rewrite `Docker/docker-compose.yml` for full-stack orchestration
    - Define `postgres` service with image `postgres:latest`, credentials (admin/foo123), database `psql_user`, port 5432, and a named volume `postgres_data`
    - Define `demo` service with build context `./dockerfile/DemoUserService`, port mapping 8080:8080, environment variables for datasource (URL, username, password), and `depends_on: postgres`
    - Remove all Kafka, gRPC, and port 8100 references
    - Use compose file version `3.9`
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5_

  - [x] 3.2 Update `Docker/docker-compose-postgresql.yml` credentials
    - Set `POSTGRES_USER` to `admin`
    - Set `POSTGRES_PASSWORD` to `foo123`
    - Set `POSTGRES_DB` to `psql_user`
    - Retain existing volume mount structure for standalone PostgreSQL usage
    - _Requirements: 4.1, 4.2, 4.3_

- [x] 4. Update application properties
  - [x] 4.1 Update `Back-End/demo/src/main/resources/application.properties`
    - Set `spring.datasource.url` to `jdbc:postgresql://postgres:5432/psql_user`
    - Set `spring.datasource.username` to `admin`
    - Set `spring.datasource.password` to `foo123`
    - Keep `spring.datasource.driver-class-name=org.postgresql.Driver`
    - Keep `spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect`
    - _Requirements: 5.1, 5.2, 5.3_

- [x] 5. Checkpoint - Verify Docker setup
  - Ensure all Docker files are syntactically correct and consistent. Ask the user if questions arise.

- [x] 6. Create Kubernetes manifests
  - [x] 6.1 Create `Kubernetes/app-deployment.yaml`
    - Define a Deployment named `demo-user-service` with 1 replica
    - Set container image to `demouserimg:latest`, containerPort 8080
    - Configure environment variables: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
    - _Requirements: 6.2, 6.6_

  - [x] 6.2 Create `Kubernetes/app-service.yaml`
    - Define a Service of type NodePort for `demo-user-service`
    - Route traffic to port 8080
    - _Requirements: 6.3_

  - [x] 6.3 Create `Kubernetes/postgres-deployment.yaml`
    - Define a Deployment named `postgres` with 1 replica
    - Set container image to `postgres:latest`, containerPort 5432
    - Configure environment variables: `POSTGRES_DB=psql_user`, `POSTGRES_USER=admin`, `POSTGRES_PASSWORD=foo123`
    - _Requirements: 6.4_

  - [x] 6.4 Create `Kubernetes/postgres-service.yaml`
    - Define a Service named `postgres` of type ClusterIP
    - Route traffic to port 5432
    - _Requirements: 6.5_

- [x] 7. Verify Postman collection
  - [x] 7.1 Verify `Postman Collections/Microservice PSQL.postman_collection.json`
    - Confirm all requests use `localhost` as host and `8080` as port
    - Confirm all five endpoints are present (Add User, Update User, User Exist, Retrieve User List, Delete User) with paths under `/psql/user`
    - No modification expected — only verification
    - _Requirements: 7.1, 7.2_

- [x] 8. Rewrite README.md
  - [x] 8.1 Create new `README.md` at project root
    - Write project description (Spring Boot + PostgreSQL CRUD demo)
    - Add "Project Structure" section listing Back-End, Docker, Kubernetes, and Postman Collections directories
    - Add "Prerequisites" section listing Java 21, Maven, Docker, and optionally kubectl + Minikube/kind
    - Add "Running with Docker Compose" section with step-by-step instructions
    - Add "Running with Kubernetes" section with step-by-step instructions
    - Add "API Endpoints" section listing all five User CRUD endpoints with HTTP methods and paths
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5, 8.6_

- [x] 9. Final checkpoint - Validate full restructure
  - Ensure all files are in correct locations, credentials are consistent across all configs, and README accurately reflects the project. Ask the user if questions arise.

## Notes

- No property-based tests are included — this is a configuration/infrastructure restructuring task
- All credential values are standardized: database=`psql_user`, user=`admin`, password=`foo123`
- The Postman collection is verified but not modified (it already uses correct host/port)
- Checkpoints ensure incremental validation of the restructuring

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1", "1.2"] },
    { "id": 1, "tasks": ["2.1", "3.1", "3.2"] },
    { "id": 2, "tasks": ["4.1"] },
    { "id": 3, "tasks": ["6.1", "6.2", "6.3", "6.4", "7.1"] },
    { "id": 4, "tasks": ["8.1"] }
  ]
}
```
