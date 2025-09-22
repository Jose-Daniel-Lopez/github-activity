package com.github.Jose_Daniel_Lopez.github_activity_cli.controller;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.model.Repo;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.GitHubApiService;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.EventProcessingService;
import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.StarEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GithubActivityController.class)
class GithubActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitHubApiService gitHubApiService;

    @MockitoBean
    private EventProcessingService eventProcessingService;

    @Test
    @DisplayName("Should return OK for health check")
    void shouldReturnOkForHealthCheck() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("Should return formatted activity for valid user")
    void shouldReturnFormattedActivityForValidUser() throws Exception {
        // Given
        String username = "testuser";
        GitHubEvent event1 = createMockEvent("PushEvent", "user/repo1");
        GitHubEvent event2 = createMockEvent("WatchEvent", "user/repo2");
        GitHubEvent[] mockEvents = {event1, event2};

        when(gitHubApiService.fetchUserEvents(username)).thenReturn(mockEvents);

        // When & Then
        mockMvc.perform(get("/api/activity/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should return message when user has no activity")
    void shouldReturnMessageWhenUserHasNoActivity() throws Exception {
        // Given
        String username = "inactiveuser";
        when(gitHubApiService.fetchUserEvents(username)).thenReturn(new GitHubEvent[0]);

        // When & Then
        mockMvc.perform(get("/api/activity/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().string("The specified user has no activity events."));
    }

    @Test
    @DisplayName("Should handle user not found error")
    void shouldHandleUserNotFoundError() throws Exception {
        // Given
        String username = "nonexistentuser";
        RuntimeException exception = new RuntimeException("User not found: " + username);

        when(gitHubApiService.fetchUserEvents(username)).thenThrow(new RuntimeException("API Error"));
        when(gitHubApiService.handleGitHubApiException(any(), anyString())).thenReturn(exception);

        // When & Then
        mockMvc.perform(get("/api/activity/{username}", username))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Should return star events for valid user")
    void shouldReturnStarEventsForValidUser() throws Exception {
        // Given
        String username = "testuser";
        Object[] mockStarredRepos = {new Object(), new Object()};
        List<StarEventDto> mockStarEvents = List.of(
                new StarEventDto("repo1", "owner1", "2025-09-22T10:00:00Z"),
                new StarEventDto("repo2", "owner2", "2025-09-22T11:00:00Z")
        );

        when(gitHubApiService.fetchUserStarredRepos(username)).thenReturn(mockStarredRepos);
        when(eventProcessingService.processStarredRepos(mockStarredRepos)).thenReturn(mockStarEvents);

        // When & Then
        mockMvc.perform(get("/api/stars/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should return message when user has no starred repos")
    void shouldReturnMessageWhenUserHasNoStarredRepos() throws Exception {
        // Given
        String username = "testuser";
        when(gitHubApiService.fetchUserStarredRepos(username)).thenReturn(new Object[0]);
        when(eventProcessingService.processStarredRepos(any())).thenReturn(new ArrayList<>());

        // When & Then
        mockMvc.perform(get("/api/stars/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().string("The specified user has no starred repositories."));
    }

    private GitHubEvent createMockEvent(String type, String repoName) {
        GitHubEvent event = new GitHubEvent();
        event.setType(type);
        event.setCreatedAt("2025-09-22T10:30:00Z");

        Repo repo = new Repo(repoName);
        event.setRepo(repo);

        return event;
    }
}
