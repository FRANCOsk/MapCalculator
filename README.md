# Map Calculator

A Spring Boot REST API that calculates a land route between two countries by applying Dijkstra's shortest-path algorithm to country-border data.

## Highlights

- REST endpoint for route calculation
- Graph-based path finding with JGraphT
- Country data loaded from a classpath JSON resource
- Case-insensitive ISO 3166-1 alpha-3 country codes
- Constructor-based dependency injection
- Automated unit and application-context tests
- Maven build, GitHub Actions CI, and Dependabot configuration

## Technology stack

- Java 17
- Spring Boot 3.5
- Spring Web
- Jackson
- JGraphT
- JUnit 5
- Maven

## API

```http
GET /routing/{origin}/{destination}
```

Both parameters use three-letter country codes.

Example:

```http
GET /routing/CZE/ITA
```

Successful response:

```json
{
  "route": ["CZE", "AUT", "ITA"]
}
```

The API returns `400 Bad Request` when a country code is unknown, both codes are the same, or no land route exists.

## Run locally

Requirements:

- JDK 17 or newer
- Maven 3.6.3 or newer

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080` by default.

## Build and test

```bash
./mvnw clean verify
```

## Project structure

```text
src/main/java       application and routing logic
src/main/resources  configuration and country-border data
src/test/java       automated tests
```

## Security and dependency maintenance

Dependabot checks Maven and GitHub Actions dependencies every week. Pull requests are validated by the CI workflow before they are merged.

## Notes

The route calculation is based exclusively on land borders contained in `countries.json`; sea and air connections are intentionally excluded.
