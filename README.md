# GitHub Activity API

A Spring Boot REST API application that fetches and displays recent GitHub user activity. This project provides a web service with REST endpoints that can be consumed by web applications, mobile apps, or other services.

## Features

- **Comprehensive Activity Tracking**: Fetches various types of GitHub user activities including commits, issues, pull requests, stars, forks, releases, and more
- **RESTful API**: Clean REST endpoints for programmatic access
- **Reactive Architecture**: Built with Spring WebFlux for non-blocking I/O operations
- **GitHub API Integration**: Direct integration with GitHub's public Events API
- **Error Handling**: Robust error handling for API failures and user not found scenarios

## Prerequisites

- Java 23 or higher
- Maven 3.6+ (or use the included Maven wrapper)
- Internet connection for GitHub API access

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Jose-Daniel-Lopez/github-activity-cli.git
   cd github-activity-cli
   ```

2. Build the project:
   ```bash
   ./mvnw clean compile
   ```

## Running the Application

Start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The application will start on port 8080 by default. You can change this in `src/main/resources/application.properties`.

## API Endpoints

The API provides the following endpoints under the `/api` base path:

### Health Check

- `GET /api/health` - Service health check

### General Activity

- `GET /api/activity/{username}` - All recent public events for a user

### Specific Activity Types

- `GET /api/commits/{username}` - Recent commit events (PushEvents)
- `GET /api/pushes/{username}` - Recent push events
- `GET /api/issues/{username}` - Recent issue creation events
- `GET /api/pulls/{username}` - Pull request events
- `GET /api/forks/{username}` - Repository fork events
- `GET /api/stars/{username}` - Starred repositories
- `GET /api/repositories/{username}` - User's public repositories
- `GET /api/releases/{username}` - Release publication events
- `GET /api/comments/{username}` - Issue comment events
- `GET /api/public/{username}` - Repository publicization events
- `GET /api/delete/{username}` - Branch/tag deletion events
- `GET /api/create/{username}` - Repository/branch creation events
- `GET /api/member/{username}` - Collaborator addition events

## Usage Examples

### Using curl

Get all recent activity for a user:

```bash
curl http://localhost:8080/api/activity/octocat
```

Get recent commits:

```bash
curl http://localhost:8080/api/commits/octocat
```

Get starred repositories:

```bash
curl http://localhost:8080/api/stars/octocat
```

### Response Format

The API returns JSON responses. For activity endpoints, you'll receive arrays of DTOs with structured data. For example, a commit event response:

```json
[
  {
    "repoName": "my-repo",
    "repoOwner": "octocat",
    "commitCount": 3,
    "pushedAt": "2025-04-01T12:34:56Z"
  }
]
```

If no activity is found, the API returns a descriptive message string.

### Error Handling

- **404 Not Found**: User does not exist on GitHub
- **Other API Errors**: GitHub API rate limits or server errors
- **Invalid Requests**: Malformed usernames or endpoints

## Project Structure

```
src/main/java/com/github/Jose_Daniel_Lopez/github_activity_cli/
├── GithubActivityApplication.java          # Main Spring Boot application
├── config/
│   └── WebClientConfig.java               # WebClient bean configuration
├── controller/
│   └── GithubActivityController.java      # REST API endpoints
├── dto/                                   # Data Transfer Objects
│   ├── CommitEventDto.java
│   ├── IssueEventDto.java
│   └── ... (other event DTOs)
├── model/                                 # Domain models
│   ├── GitHubEvent.java
│   └── Repo.java
├── service/                               # Business logic services
│   ├── GitHubApiService.java             # GitHub API client
│   ├── EventProcessingService.java       # Event filtering/processing
│   └── EventFormatter.java               # Event formatting utilities
└── util/
    └── AnsiColor.java                    # ANSI color utilities
```

## Key Components

- **GitHubApiService**: Handles HTTP requests to GitHub's API using WebFlux WebClient
- **EventProcessingService**: Filters and transforms raw GitHub events into structured DTOs
- **EventFormatter**: Provides utilities for formatting event data
- **WebClientConfig**: Configures the reactive HTTP client with proper headers

## Limitations

- **90-Day Window**: GitHub's Events API only provides activity from the last 90 days
- **Public Activity Only**: Only public events are accessible without authentication
- **Rate Limiting**: Subject to GitHub's API rate limits for unauthenticated requests
- **No Historical Data**: Cannot fetch activity older than 90 days

## Future Enhancements

- **Authentication**: Support for GitHub personal access tokens for private repos and higher rate limits
- **Caching**: Add caching layer to reduce API calls
- **Pagination**: Support for paginated results
- **Webhooks**: Real-time activity monitoring via GitHub webhooks
- **Rate Limiting**: Implement request throttling and user quotas

## Testing

The project includes comprehensive test coverage with unit tests, integration tests, and controller tests.

### Test Structure

```text
src/test/java/com/github/Jose_Daniel_Lopez/github_activity_cli/
├── GithubActivityCliApplicationTests.java    # Main application context tests
├── controller/
│   └── GithubActivityControllerTest.java     # REST controller tests
├── integration/
│   └── GithubActivityIntegrationTest.java    # End-to-end integration tests
└── service/
    ├── EventFormatterTest.java               # Event formatting tests
    ├── EventProcessingServiceTest.java       # Event processing logic tests
    └── GitHubApiServiceTest.java             # GitHub API client tests
```

### Running Tests

Run all tests:

```bash
./mvnw test
```

Run tests with coverage report:

```bash
./mvnw test jacoco:report
```

Run specific test class:

```bash
./mvnw test -Dtest=GithubActivityControllerTest
```

## Building for Production

Create a JAR file:

```bash
./mvnw clean package
```

Run the JAR:

```bash
java -jar target/github-activity-cli-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project currently has no specified license. Please check with the project maintainer for usage terms.

## Technologies Used

- **Spring Boot 3.5.5**: Framework for building the application
- **Spring WebFlux**: Reactive web framework
- **WebClient**: Reactive HTTP client
- **Lombok**: Code generation library
- **Maven**: Build tool
- **Java 23**: Programming language

## API Rate Limits

When using without authentication, GitHub allows 60 requests per hour per IP address. For higher limits, consider implementing GitHub authentication in future versions.
