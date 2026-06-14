# Spring Boot PostgreSQL CRUD Demo

A demo REST API built with Spring Boot 3.2.2 and PostgreSQL, providing basic User CRUD operations. The project includes Docker Compose for local full-stack orchestration and Kubernetes manifests for local cluster deployment.

## Project Structure

```
├── Back-End/              # Spring Boot application source code (Java 21, Maven)
├── Docker/                # Dockerfile, docker-compose files for containerized deployment
├── Kubernetes/            # Kubernetes Deployment and Service manifests
├── Postman Collections/   # Postman collection for testing API endpoints
└── README.md
```

| Directory | Description |
|-----------|-------------|
| `Back-End/` | Contains the Spring Boot Maven project (`demo/`) with the User CRUD REST API. |
| `Docker/` | Dockerfile for the application image, full-stack `docker-compose.yml`, and a standalone PostgreSQL compose file. |
| `Kubernetes/` | YAML manifests to deploy the application and PostgreSQL on a local Kubernetes cluster. |
| `Postman Collections/` | Exported Postman collection with pre-configured requests for all API endpoints. |

## Prerequisites

- **Java 21** — required to build the application
- **Maven** — used for building the Spring Boot project
- **Docker** and **Docker Compose** — for running the containerized stack locally

Optional (for Kubernetes deployment):

- **kubectl** — Kubernetes command-line tool
- **Minikube** or **kind** — local Kubernetes cluster

## Running with Docker Compose

1. Build the application JAR:

   ```bash
   cd Back-End/demo
   mvn clean package -DskipTests
   ```

2. Copy the JAR to the Docker build context:

   ```bash
   cp Back-End/demo/target/demo-0.0.1-SNAPSHOT.jar Docker/dockerfile/DemoUserService/
   ```

3. Start the full stack (PostgreSQL + Application):

   ```bash
   cd Docker
   docker-compose up --build
   ```

4. The API will be available at `http://localhost:8080`.

5. To stop the services:

   ```bash
   docker-compose down
   ```

## Running with Kubernetes

1. Build the Docker image locally:

   ```bash
   cd Docker/dockerfile/DemoUserService
   docker build -t demouserimg:latest .
   ```

   > If using Minikube, run `eval $(minikube docker-env)` first so the image is available to the cluster.

2. Apply the Kubernetes manifests:

   ```bash
   kubectl apply -f Kubernetes/
   ```

3. Wait for pods to become ready:

   ```bash
   kubectl get pods -w
   ```

4. Access the application:

   ```bash
   minikube service demo-user-service
   ```

   Or, if using kind or another cluster, use the NodePort assigned to the `demo-user-service` Service on port 8080.

5. To tear down:

   ```bash
   kubectl delete -f Kubernetes/
   ```

## API Endpoints

All endpoints are served on port **8080** under the base path `/psql/user`.

| Method | Path | Description |
|--------|------|-------------|
| PUT | `/psql/user/add` | Add a new user |
| PUT | `/psql/user/update` | Update an existing user |
| GET | `/psql/user/isUserExist/{username}` | Check if a user exists |
| GET | `/psql/user/listUser` | Retrieve all users |
| DELETE | `/psql/user/delete/{username}` | Delete a user by username |
