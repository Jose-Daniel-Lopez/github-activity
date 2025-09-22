package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubApiServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private GitHubApiService gitHubApiService;

    @BeforeEach
    void setUp() {
        gitHubApiService = new GitHubApiService(webClient);
    }

    @Test
    @DisplayName("Should fetch user events successfully")
    void shouldFetchUserEventsSuccessfully() {
        // Given
        String username = "testuser";
        GitHubEvent[] mockEvents = new GitHubEvent[]{new GitHubEvent(), new GitHubEvent()};

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(GitHubEvent[].class)).thenReturn(Mono.just(mockEvents));

        // When
        GitHubEvent[] result = gitHubApiService.fetchUserEvents(username);

        // Then
        assertNotNull(result);
        assertEquals(2, result.length);
        verify(requestHeadersUriSpec).uri("https://api.github.com/users/testuser/events");
    }

    @Test
    @DisplayName("Should fetch user starred repositories successfully")
    void shouldFetchUserStarredReposSuccessfully() {
        // Given
        String username = "testuser";
        Object[] mockRepos = new Object[]{new Object(), new Object()};

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object[].class)).thenReturn(Mono.just(mockRepos));

        // When
        Object[] result = gitHubApiService.fetchUserStarredRepos(username);

        // Then
        assertNotNull(result);
        assertEquals(2, result.length);
        verify(requestHeadersUriSpec).uri("https://api.github.com/users/testuser/starred");
    }

    @Test
    @DisplayName("Should fetch user repositories successfully")
    void shouldFetchUserRepositoriesSuccessfully() {
        // Given
        String username = "testuser";
        Object[] mockRepos = new Object[]{new Object(), new Object()};

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object[].class)).thenReturn(Mono.just(mockRepos));

        // When
        Object[] result = gitHubApiService.fetchUserRepositories(username);

        // Then
        assertNotNull(result);
        assertEquals(2, result.length);
        verify(requestHeadersUriSpec).uri("https://api.github.com/users/testuser/repos");
    }

    @Test
    @DisplayName("Should handle 404 error correctly")
    void shouldHandle404ErrorCorrectly() {
        // Given
        String username = "nonexistentuser";
        WebClientResponseException notFoundException = WebClientResponseException.create(
                404, "Not Found", null, null, null);

        // When
        RuntimeException result = gitHubApiService.handleGitHubApiException(notFoundException, username);

        // Then
        assertEquals("User not found: nonexistentuser", result.getMessage());
    }

    @Test
    @DisplayName("Should handle other HTTP errors correctly")
    void shouldHandleOtherHttpErrorsCorrectly() {
        // Given
        String username = "testuser";
        WebClientResponseException serverError = WebClientResponseException.create(
                500, "Internal Server Error", null, null, null);

        // When
        RuntimeException result = gitHubApiService.handleGitHubApiException(serverError, username);

        // Then
        assertEquals("GitHub API error: 500 INTERNAL_SERVER_ERROR", result.getMessage());
    }

    @Test
    @DisplayName("Should handle unexpected exceptions correctly")
    void shouldHandleUnexpectedExceptionsCorrectly() {
        // Given
        String username = "testuser";
        Exception unexpectedException = new RuntimeException("Network timeout");

        // When
        RuntimeException result = gitHubApiService.handleGitHubApiException(unexpectedException, username);

        // Then
        assertEquals("Unexpected error: Network timeout", result.getMessage());
    }
}
