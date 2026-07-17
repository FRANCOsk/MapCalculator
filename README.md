# MapCalculator

MapCalculator is a Spring Boot application for experimenting with map and graph calculations. The project uses Java 17, Spring Web, Spring Data JPA, H2 and JGraphT.

## Technology stack

- Java 17
- Spring Boot 3
- Maven
- Spring Web and Web Services
- Spring Data JPA
- H2 in-memory database
- JGraphT

## Requirements

- JDK 17 or newer
- Maven 3.9+ or the included Maven wrapper, when available

## Build and test

```bash
mvn clean verify
```

## Run locally

```bash
mvn spring-boot:run
```

The application starts with the configuration stored under `src/main/resources`.

## Continuous integration

GitHub Actions validates every pull request and push to the default branch by compiling the project and running the complete Maven test lifecycle.

## Production notes

The bundled H2 database is intended for development and demonstrations. For production use, configure an external database, externalized secrets, structured logging, health checks and environment-specific application profiles.

## License

No license has been declared. Add an explicit license before reusing this project outside its current scope.
