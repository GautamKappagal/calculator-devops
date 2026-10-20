# Calculator DevOps

An end-to-end CI/CD project that packages a Spring Boot Calculator REST API into a Docker image and deploys it using Jenkins and Docker Compose.

The project demonstrates version control, automated testing, container image publishing, multi-container orchestration, health checks, and deployment verification.

## Table of Contents

- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [API Reference](#api-reference)
- [Prerequisites](#prerequisites)
- [Build and Test Locally](#build-and-test-locally)
- [Run with Docker](#run-with-docker)
- [Run with Docker Compose](#run-with-docker-compose)
- [CI/CD Pipeline](#cicd-pipeline)
- [Jenkins Configuration](#jenkins-configuration)
- [Project Structure](#project-structure)

## Architecture

The application is a Spring Boot REST API. Jenkins automates the build, tests, Docker image creation, publishing, and deployment. Docker Compose runs the application alongside an Nginx reverse proxy.

```text
Developer pushes code
        |
        v
      GitHub
        |
        v
      Jenkins
        |
        +--> Checkout
        +--> Build with Maven
        +--> Run tests and publish JUnit results
        +--> Build Docker image
        +--> Push image to Docker Hub
        +--> Deploy with Docker Compose
        +--> Verify health and API response
        |
        v
 Docker Compose deployment
        |
        v
  Nginx :8088 --> Calculator API :8080
```

## Technology Stack

- **Language:** Java 17
- **Application framework:** Spring Boot
- **Build tool:** Maven
- **Testing:** JUnit 5 and Spring MockMvc
- **CI/CD:** Jenkins Declarative Pipeline
- **Containerization:** Docker multi-stage build
- **Orchestration:** Docker Compose
- **Reverse proxy:** Nginx
- **Image registry:** Docker Hub

## API Reference

The API accepts numeric query parameters `a` and `b` and returns a JSON response.

| Operation | Endpoint |
|---|---|
| Addition | `GET /api/v1/add?a=2&b=3` |
| Subtraction | `GET /api/v1/subtract?a=5&b=2` |
| Multiplication | `GET /api/v1/multiply?a=5&b=2` |
| Division | `GET /api/v1/divide?a=10&b=4` |
| Health | `GET /actuator/health` |

### Example successful response

```json
{
  "operation": "add",
  "a": 2,
  "b": 3,
  "result": 5
}
```

Invalid or missing parameters and division by zero return HTTP `400 Bad Request` with a structured JSON error body.

The health endpoint reports the Spring Boot application's health status.

## Prerequisites

For local development:

- Java 17
- Maven
- Git

For containerized execution:

- Docker Engine or Docker Desktop
- Docker Compose v2

For the automated pipeline:

- Jenkins with Git, Java, Maven, and Docker available to its build agent
- A Docker Hub account and a Jenkins credential for registry authentication
- Email configuration in Jenkins for build notifications
- A configured GitHub webhook or SCM polling trigger

## Build and Test Locally

Clone the repository and enter the project directory:

```bash
git clone https://github.com/GautamKappagal/calculator-devops.git
cd calculator-devops
```

Build the application:

```bash
mvn clean package
```

Run the tests:

```bash
mvn test
```

The tests cover calculator operations, division-by-zero handling, and HTTP-level behaviour using MockMvc. Maven produces JUnit XML reports under `target/surefire-reports/`.

Run the application using the executable JAR generated in the `target/` directory:

```bash
java -jar target/mini-project-0.0.1-SNAPSHOT.jar
```

The API will be available on port `8080` by default.

Example request:

```bash
curl "http://localhost:8080/api/v1/add?a=2&b=3"
```

Check application health:

```bash
curl -i http://localhost:8080/actuator/health
```

## Run with Docker

Build the image locally:

```bash
docker build -t calculator:local .
```

Run the container with port `8080` mapped to the host:

```bash
docker run --rm -p 8080:8080 calculator:local
```

In another terminal, test the API:

```bash
curl "http://localhost:8080/api/v1/add?a=2&b=3"
curl -i http://localhost:8080/actuator/health
```

The Dockerfile uses a multi-stage build. Maven compiles the application in the build stage, while the runtime stage contains the executable JAR and Java runtime. The application runs as a non-root user.

## Run with Docker Compose

The Compose configuration starts the calculator service and an Nginx reverse proxy.

From the repository root, run:

```bash
IMAGE_TAG=latest docker compose up -d
```

The API is accessible through Nginx on host port `8088`.

Test the API:

```bash
curl "http://localhost:8088/api/v1/add?a=2&b=3"
curl "http://localhost:8088/api/v1/divide?a=10&b=4"
curl -i http://localhost:8088/actuator/health
```

Test division by zero:

```bash
curl -i "http://localhost:8088/api/v1/divide?a=1&b=0"
```

Check the services and their health:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs --tail=100
```

Stop the stack:

```bash
docker compose down
```

The Compose configuration defines a dedicated network, restart policies, environment variables, and health checks for the application and Nginx.

## CI/CD Pipeline

The declarative pipeline is defined in `Jenkinsfile`. It contains the following stages:

1. **Checkout:** Retrieves the latest source from GitHub.
2. **Build:** Packages the application using Maven.
3. **Test:** Runs the test suite and publishes JUnit XML results. Test failures fail the pipeline.
4. **Docker Build:** Builds the application image and tags it with the Jenkins build number, as well as `latest`.
5. **Push:** Authenticates to Docker Hub using Jenkins-managed credentials and pushes the image tags.
6. **Deploy:** Deploys the image using Docker Compose, waits for health, and checks the health endpoint and a sample API request.

The pipeline is configured to send email notifications for successful and failed builds. Concurrent pipeline runs are disabled to avoid overlapping deployments.

## Jenkins Configuration

The Jenkins environment must be configured before running the pipeline:

- Configure the job to use this repository and its `Jenkinsfile`.
- Configure a GitHub webhook or SCM polling so code changes trigger a build automatically.
- Add Docker Hub credentials in Jenkins Credentials using the ID `dockerhub-creds`.
- Configure the Jenkins email system and recipient settings.
- Ensure the Jenkins agent can run Maven, Java, Docker, Docker Compose, and `curl`.
- Ensure the agent can access the Docker daemon and deploy the Compose stack.

The current pipeline contains local machine paths for Java and Docker. These paths must exist on the machine running Jenkins. They are specific to the current local setup and may need to be configured differently on another machine.

## Project Structure

```text
calculator-devops/
├── src/
│   ├── main/java/          # REST API and calculator logic
│   ├── main/resources/     # Spring Boot configuration
│   └── test/java/          # Unit and integration tests
├── nginx/
│   └── nginx.conf          # Nginx reverse-proxy configuration
├── Dockerfile              # Multi-stage image build
├── docker-compose.yml      # Application and Nginx services
├── Jenkinsfile             # Declarative CI/CD pipeline
├── pom.xml                 # Maven dependencies and build
├── .dockerignore           # Docker build-context exclusions
├── .gitignore              # Git exclusions
└── README.md
```

## Repository Links

- **GitHub:** https://github.com/GautamKappagal/calculator-devops
- **Docker Hub:** https://hub.docker.com/r/gautamkappagal10/calculator
