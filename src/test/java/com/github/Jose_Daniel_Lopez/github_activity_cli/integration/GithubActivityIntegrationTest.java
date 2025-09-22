package com.github.Jose_Daniel_Lopez.github_activity_cli.integration;

import com.github.Jose_Daniel_Lopez.github_activity_cli.service.GitHubApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class GithubActivityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubApiService gitHubApiService;

    @Test
    @DisplayName("Integration test - Should handle full application flow for health check")
    void shouldHandleFullApplicationFlowForHealthCheck() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("Integration test - Should handle user not found scenario")
    void shouldHandleUserNotFoundScenario() throws Exception {
        // Given
        String username = "nonexistentuser";
        RuntimeException exception = new RuntimeException("User not found: " + username);

        when(gitHubApiService.fetchUserEvents(username))
                .thenThrow(new RuntimeException("GitHub API error"));
        when(gitHubApiService.handleGitHubApiException(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.eq(username)))
                .thenReturn(exception);

        // When & Then
        mockMvc.perform(get("/api/activity/{username}", username))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Integration test - Should handle empty activity response")
    void shouldHandleEmptyActivityResponse() throws Exception {
        // Given
        String username = "emptyuser";
        when(gitHubApiService.fetchUserEvents(username))
                .thenReturn(new com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent[0]);

        // When & Then
        mockMvc.perform(get("/api/activity/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().string("The specified user has no activity events."));
    }
}
