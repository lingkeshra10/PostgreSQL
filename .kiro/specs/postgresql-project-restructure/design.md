# Design Document

## Overview

This design describes the restructuring of a Spring Boot PostgreSQL CRUD demo project. The changes are purely organizational and configuration-focused: renaming a directory, fixing Docker and Docker Compose files, adding Kubernetes manifests, standardizing credentials, and rewriting the README. No application logic is modified.

## Architecture

The restructured project follows a clear separation of concerns across top-level directories:

```
PostgreSQL/
├── Back-End/                    # Spring Boot application (renamed from "Spring Source Code")
│   └── demo/
│       ├── pom.xml
│       └── src/main/...
├── Docker/
│   ├── dockerfile/DemoUserService/Dockerfile
│   ├── docker-compose.yml       # Full stack: app + postgres
│   └── docker-compose-postgresql.yml  # Standalone PostgreSQL
├── Kubernetes/
│   ├── app-deployment.yaml
│   ├── app-service.yaml
│   ├── postgres-deployment.yaml
│   └── postgres-service.yaml
├── Postman Collections/
│   └── Microservice PSQL.postman_collection.json
└── README.md
```

The application is a stateless REST API backed by PostgreSQL. It exposes CRUD operations for User entities at `/psql/user/*` on port 8080.

## Components

### 1. Back-End Directory (renamed)

The existing Spring Boot application moves from `Spring Source Code/` to `Back-End/` with no changes to file content (except `application.properties` per Requirement 5).

**Source structure preserved:**
- `demo/pom.xml` — Maven build (Spring Boot 3.2.2, Java 21)
- `demo/src/main/java/com/postgresql/demo/` — Application code (controller, entity, modal, repository, service)
- `demo/src/main/resources/application.properties` — Updated connection settings

### 2. Dockerfile (corrected)

**File:** `Docker/dockerfile/DemoUserService/Dockerfile`

```dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /opt/app

COPY demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

Key decisions:
- `eclipse-temurin:21-jre-alpine` matches the Java 21 requirement from pom.xml
- JAR name matches the Maven artifact: `demo-0.0.1-SNAPSHOT.jar`
- Single JAR copy + ENTRYPOINT replaces the broken volume-mount + shell-script approach

### 3. Docker Compose — Full Stack

**File:** `Docker/docker-compose.yml`

```yaml
version: '3.9'
services:

  postgres:
    image: postgres:latest
    container_name: postgres_db
    restart: always
    environment:
      POSTGRES_DB: psql_user
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: foo123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  demo:
    build:
      context: ./dockerfile/DemoUserService
    image: demouserimg
    restart: always
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/psql_user
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: foo123
    depends_on:
      - postgres

volumes:
  postgres_data:
```

Key decisions:
- Removed Kafka, gRPC, and port 8100 references
- Added `depends_on: postgres` for startup ordering
- Environment variables override application.properties for containerized context
- Named volume replaces host-mounted data directory for portability

### 4. Docker Compose — PostgreSQL Only

**File:** `Docker/docker-compose-postgresql.yml`

```yaml
version: '3.9'
services:

  postgres:
    image: postgres:latest
    container_name: postgres_db
    restart: always
    environment:
      POSTGRES_DB: psql_user
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: foo123
    ports:
      - "5432:5432"
    volumes:
      - ./data/postgresInitDB:/docker-entrypoint-initdb.d
      - ./data/postgres:/var/lib/postgresql/data
```

Only credential values change here — structure remains the same.

### 5. Application Properties

**File:** `Back-End/demo/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/psql_user
spring.datasource.username=admin
spring.datasource.password=foo123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

The hostname changes from `localhost` to `postgres` to match the Docker Compose service name. When running locally outside Docker, developers can override via environment variables or a local profile.

### 6. Kubernetes Manifests

All manifests target a local cluster (Minikube/kind). No production concerns (secrets management, PVCs with storage classes, etc.) are addressed — this is for dev/test only.

**File:** `Kubernetes/app-deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: demo-user-service
  labels:
    app: demo-user-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: demo-user-service
  template:
    metadata:
      labels:
        app: demo-user-service
    spec:
      containers:
        - name: demo-user-service
          image: demouserimg:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_DATASOURCE_URL
              value: "jdbc:postgresql://postgres:5432/psql_user"
            - name: SPRING_DATASOURCE_USERNAME
              value: "admin"
            - name: SPRING_DATASOURCE_PASSWORD
              value: "foo123"
```

**File:** `Kubernetes/app-service.yaml`

```yaml
apiVersion: v1
kind: Service
metadata:
  name: demo-user-service
spec:
  type: NodePort
  selector:
    app: demo-user-service
  ports:
    - port: 8080
      targetPort: 8080
      protocol: TCP
```

**File:** `Kubernetes/postgres-deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
  labels:
    app: postgres
spec:
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
        - name: postgres
          image: postgres:latest
          ports:
            - containerPort: 5432
          env:
            - name: POSTGRES_DB
              value: "psql_user"
            - name: POSTGRES_USER
              value: "admin"
            - name: POSTGRES_PASSWORD
              value: "foo123"
```

**File:** `Kubernetes/postgres-service.yaml`

```yaml
apiVersion: v1
kind: Service
metadata:
  name: postgres
spec:
  type: ClusterIP
  selector:
    app: postgres
  ports:
    - port: 5432
      targetPort: 5432
      protocol: TCP
```

### 7. README.md

The README is rewritten from scratch to describe the actual project — not generic PostgreSQL documentation. It includes:
- Project description (Spring Boot + PostgreSQL CRUD demo)
- Project structure overview (Back-End, Docker, Kubernetes, Postman Collections)
- Prerequisites (Java 21, Maven, Docker, optionally kubectl + Minikube/kind)
- Running with Docker Compose (step-by-step)
- Running with Kubernetes (step-by-step)
- API Endpoints table (all five CRUD operations)

### 8. Postman Collection (unchanged)

The existing collection already uses `localhost:8080` for all requests and has all five endpoints. No modifications needed — only verification that it remains correct after restructuring.

## Data Models

No changes to application data models. The existing `User` entity and its associated modals (`AddUserModal`, `UpdateUserModal`, `ResponseModal`) remain unchanged.

## Error Handling

No changes to error handling. The restructuring is purely organizational. The existing controller error handling (duplicate username/email checks, user-not-found responses) remains intact.

## Credential Standardization

All configuration files converge on:
| Setting | Value |
|---------|-------|
| Database name | `psql_user` |
| Username | `admin` |
| Password | `foo123` |
| App port | `8080` |
| DB port | `5432` |
| DB hostname (containerized) | `postgres` |

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system — essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

This project restructuring is an infrastructure/configuration task. All acceptance criteria are static configuration checks (smoke tests) or structural verifications (example tests). There are no pure functions with varying inputs, no serialization logic being written, and no algorithmic behavior to validate across a range of inputs.

**Property-based testing is not appropriate for this feature** because:
- The changes are declarative configurations (Dockerfiles, YAML manifests, properties files)
- There is no application logic being added or modified
- Behavior does not vary meaningfully with input — configurations are either correct or incorrect
- All validations are one-shot structural checks (file exists, field has expected value)

The correct testing approach for this feature is:
- **Smoke tests**: Verify configuration files contain expected values (credentials, ports, image names)
- **Example-based tests**: Verify README structure contains expected sections, Postman collection has expected endpoints
- **Integration test**: Run `docker-compose up` and verify the application responds on port 8080

No correctness properties are generated for this feature.
