# Requirements Document

## Introduction

Restructure a PostgreSQL Spring Boot demo project to improve organization, fix broken Docker configurations, add Kubernetes manifests for local testing, and rewrite the README to accurately describe the project. The restructuring standardizes port 8080 and database credentials (admin/foo123) across all environments.

## Glossary

- **Project**: The PostgreSQL Spring Boot demo repository located at the workspace root
- **Back-End**: The renamed directory (formerly "Spring Source Code") containing the Spring Boot application source code
- **Docker_Setup**: The Docker directory containing Dockerfile, docker-compose files, and related configurations
- **Kubernetes_Manifests**: A new directory containing Kubernetes Deployment and Service YAML files for local cluster testing
- **Application**: The Spring Boot demo application that provides a User CRUD REST API on port 8080
- **PostgreSQL_Service**: The PostgreSQL database instance used by the Application
- **Postman_Collection**: The exported Postman collection file containing API request definitions

## Requirements

### Requirement 1: Directory Renaming

**User Story:** As a developer, I want the source code directory to have a clear and conventional name, so that the project structure is immediately understandable.

#### Acceptance Criteria

1. WHEN the project restructuring is applied, THE Project SHALL contain a top-level directory named "Back-End" that holds the Spring Boot application source tree previously located under "Spring Source Code".
2. WHEN the project restructuring is applied, THE Project SHALL retain the complete directory structure and all files from "Spring Source Code/demo" under "Back-End/demo" without modification to file contents (except where other requirements mandate changes).
3. WHEN the project restructuring is applied, THE Project SHALL remove the "Spring Source Code" directory entirely.

### Requirement 2: Dockerfile Correction

**User Story:** As a developer, I want the Dockerfile to use the correct Java 21 base image and properly package the application, so that I can build and run the Application container successfully.

#### Acceptance Criteria

1. THE Docker_Setup SHALL use `eclipse-temurin:21-jre-alpine` as the base image in the Dockerfile for the Application.
2. THE Docker_Setup SHALL copy the built JAR artifact into the container image at a defined location.
3. THE Docker_Setup SHALL define an ENTRYPOINT that starts the Application JAR using `java -jar`.
4. WHEN the Docker image is built, THE Docker_Setup SHALL produce a container that exposes port 8080 for the Application.

### Requirement 3: Docker Compose Fix for Application Service

**User Story:** As a developer, I want docker-compose.yml to correctly orchestrate the Application container, so that I can run the full stack locally with a single command.

#### Acceptance Criteria

1. THE Docker_Setup SHALL configure docker-compose.yml to map host port 8080 to container port 8080 for the Application service.
2. THE Docker_Setup SHALL remove references to port 8100 from docker-compose.yml.
3. THE Docker_Setup SHALL remove Kafka-related and gRPC-related configurations from docker-compose.yml.
4. THE Docker_Setup SHALL configure the Application service to depend on the PostgreSQL_Service in docker-compose.yml.
5. THE Docker_Setup SHALL configure the Application service build context to reference the corrected Dockerfile location.

### Requirement 4: Docker Compose PostgreSQL Credentials

**User Story:** As a developer, I want consistent database credentials across all configurations, so that the Application can connect to PostgreSQL without credential mismatches.

#### Acceptance Criteria

1. THE Docker_Setup SHALL set POSTGRES_USER to "admin" in docker-compose-postgresql.yml.
2. THE Docker_Setup SHALL set POSTGRES_PASSWORD to "foo123" in docker-compose-postgresql.yml.
3. THE Docker_Setup SHALL set POSTGRES_DB to "psql_user" in docker-compose-postgresql.yml.

### Requirement 5: Application Properties Update

**User Story:** As a developer, I want application.properties to use credentials and connection settings that match the Docker and Kubernetes environments, so that the Application connects to PostgreSQL correctly in containerized deployments.

#### Acceptance Criteria

1. THE Application SHALL set `spring.datasource.username` to "admin" in application.properties.
2. THE Application SHALL set `spring.datasource.password` to "foo123" in application.properties.
3. THE Application SHALL set `spring.datasource.url` to use the Docker Compose service name "postgres" as the hostname in application.properties (jdbc:postgresql://postgres:5432/psql_user).

### Requirement 6: Kubernetes Manifests

**User Story:** As a developer, I want basic Kubernetes manifests for the Application and PostgreSQL, so that I can deploy and test the stack on a local Kubernetes cluster (Minikube or kind).

#### Acceptance Criteria

1. WHEN the project restructuring is applied, THE Project SHALL contain a top-level directory named "Kubernetes" holding all Kubernetes manifest files.
2. THE Kubernetes_Manifests SHALL include a Deployment resource for the Application that specifies one replica, container port 8080, and the Application Docker image name.
3. THE Kubernetes_Manifests SHALL include a Service resource for the Application of type NodePort that routes traffic to port 8080.
4. THE Kubernetes_Manifests SHALL include a Deployment resource for the PostgreSQL_Service that specifies one replica, container port 5432, and environment variables for database name "psql_user", user "admin", and password "foo123".
5. THE Kubernetes_Manifests SHALL include a Service resource for the PostgreSQL_Service of type ClusterIP named "postgres" that routes traffic to port 5432.
6. THE Kubernetes_Manifests SHALL configure the Application Deployment to set the datasource URL environment variable pointing to the PostgreSQL_Service Kubernetes Service name on port 5432.

### Requirement 7: Postman Collection Verification

**User Story:** As a developer, I want the Postman collection to remain functional after restructuring, so that I can test the API endpoints without manual edits.

#### Acceptance Criteria

1. THE Postman_Collection SHALL use "localhost" as the host and "8080" as the port for all API requests.
2. THE Postman_Collection SHALL retain all five existing API requests (Add User, Update User, User Exist, Retrieve User List, Delete User) with their current paths under "/psql/user".

### Requirement 8: README Rewrite

**User Story:** As a developer, I want the README to describe the actual project, its structure, and how to run it, so that new contributors can onboard without confusion.

#### Acceptance Criteria

1. WHEN the project restructuring is applied, THE Project SHALL contain a README.md at the repository root that describes the project as a Spring Boot PostgreSQL CRUD demo.
2. THE Project SHALL include a "Project Structure" section in README.md that lists and describes the Back-End, Docker, Kubernetes, and Postman Collections directories.
3. THE Project SHALL include a "Prerequisites" section in README.md listing Java 21, Maven, Docker, and optionally kubectl and Minikube/kind.
4. THE Project SHALL include a "Running with Docker Compose" section in README.md with step-by-step instructions to start the PostgreSQL_Service and Application.
5. THE Project SHALL include a "Running with Kubernetes" section in README.md with step-by-step instructions to apply manifests and access the Application.
6. THE Project SHALL include an "API Endpoints" section in README.md that lists all five User CRUD endpoints with their HTTP methods and paths.
