package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.model.Repo;
import com.github.Jose_Daniel_Lopez.github_activity_cli.util.AnsiColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventFormatterTest {

    @Test
    @DisplayName("Should format PushEvent correctly")
    void shouldFormatPushEventCorrectly() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("PushEvent");
        event.setCreatedAt("2025-09-22T10:30:00Z");

        Repo repo = new Repo("user/test-repo");
        event.setRepo(repo);

        Map<String, Object> payload = new HashMap<>();
        payload.put("size", 3);
        event.setPayload(payload);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("Pushed 3 commits to user/test-repo"));
        assertTrue(result.contains("2025-09-22T10:30:00Z"));
        assertTrue(result.contains(AnsiColor.BOLD_GREEN));
    }

    @Test
    @DisplayName("Should format WatchEvent (star) correctly")
    void shouldFormatWatchEventCorrectly() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("WatchEvent");

        Repo repo = new Repo("user/awesome-repo");
        event.setRepo(repo);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("Starred user/awesome-repo"));
        assertTrue(result.contains(AnsiColor.BOLD_YELLOW));
    }

    @Test
    @DisplayName("Should format IssuesEvent correctly")
    void shouldFormatIssuesEventCorrectly() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("IssuesEvent");

        Repo repo = new Repo("user/project");
        event.setRepo(repo);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("Opened an issue in user/project"));
        assertTrue(result.contains(AnsiColor.BOLD_PURPLE));
    }

    @Test
    @DisplayName("Should format ForkEvent correctly")
    void shouldFormatForkEventCorrectly() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("ForkEvent");

        Repo repo = new Repo("original/repo");
        event.setRepo(repo);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("Forked original/repo"));
        assertTrue(result.contains(AnsiColor.BOLD_BLUE));
    }

    @Test
    @DisplayName("Should format PullRequestEvent correctly")
    void shouldFormatPullRequestEventCorrectly() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("PullRequestEvent");

        Repo repo = new Repo("user/repository");
        event.setRepo(repo);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("Created a pull request in user/repository"));
        assertTrue(result.contains(AnsiColor.CYAN));
    }

    @Test
    @DisplayName("Should handle unknown event types with default formatting")
    void shouldHandleUnknownEventTypes() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("UnknownEvent");

        Repo repo = new Repo("user/repo");
        event.setRepo(repo);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("UnknownEvent on user/repo"));
        assertTrue(result.contains(AnsiColor.WHITE));
    }

    @Test
    @DisplayName("Should handle null event gracefully")
    void shouldHandleNullEvent() {
        // When
        String result = EventFormatter.format(null);

        // Then
        assertEquals(AnsiColor.RED + "Unknown event" + AnsiColor.RESET, result);
    }

    @Test
    @DisplayName("Should handle event with null repo gracefully")
    void shouldHandleEventWithNullRepo() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("PushEvent");
        event.setRepo(null);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertEquals(AnsiColor.RED + "Unknown event" + AnsiColor.RESET, result);
    }

    @Test
    @DisplayName("Should handle PushEvent with invalid payload gracefully")
    void shouldHandlePushEventWithInvalidPayload() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("PushEvent");
        event.setCreatedAt("2025-09-22T10:30:00Z");

        Repo repo = new Repo("user/test-repo");
        event.setRepo(repo);

        // Invalid payload
        event.setPayload("invalid");

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("Pushed 0 commits to user/test-repo"));
    }

    @Test
    @DisplayName("Should handle PushEvent with null payload gracefully")
    void shouldHandlePushEventWithNullPayload() {
        // Given
        GitHubEvent event = new GitHubEvent();
        event.setType("PushEvent");
        event.setCreatedAt("2025-09-22T10:30:00Z");

        Repo repo = new Repo("user/test-repo");
        event.setRepo(repo);

        event.setPayload(null);

        // When
        String result = EventFormatter.format(event);

        // Then
        assertTrue(result.contains("Pushed 0 commits to user/test-repo"));
    }
}
