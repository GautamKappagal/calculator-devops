# Calculator DevOps

A Spring Boot REST calculator with a Jenkins CI/CD pipeline that builds, tests, publishes a Docker image and deploys it with Docker Compose.

## Build, test, run
```bash
mvn clean package        # Build the executable JAR
mvn test                 # Run unit and MockMvc tests
java -jar target/mini-project-0.0.1-SNAPSHOT.jar   # run on http://localhost:8080
```

## API
`GET /api/v1/{add|subtract|multiply|divide}?a=<number>&b=<number>`

```bash
curl "http://localhost:8080/api/v1/add?a=2&b=3"
```
Invalid or missing parameters and division by zero return HTTP 400 with a JSON error body. Health: `/actuator/health`.

## Docker Compose
```bash
IMAGE_TAG=latest docker compose up -d     # App + Nginx, available on http://localhost:8088
```