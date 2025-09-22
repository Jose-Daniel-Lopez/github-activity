package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.*;
import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.model.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventProcessingServiceTest {

    private EventProcessingService eventProcessingService;

    @BeforeEach
    void setUp() {
        eventProcessingService = new EventProcessingService();
    }

    @Test
    @DisplayName("Should extract repo info correctly from full repo name")
    void shouldExtractRepoInfoCorrectly() {
        // When
        String[] result = eventProcessingService.extractRepoInfo("spring-projects/spring-boot");

        // Then
        assertEquals("spring-boot", result[0]); // repo name
        assertEquals("spring-projects", result[1]); // repo owner
    }

    @Test
    @DisplayName("Should handle invalid repo name format")
    void shouldHandleInvalidRepoNameFormat() {
        // When
        String[] result = eventProcessingService.extractRepoInfo("invalid-format");

        // Then
        assertEquals("invalid-format", result[0]);
        assertNull(result[1]);
    }

    @Test
    @DisplayName("Should handle null repo name")
    void shouldHandleNullRepoName() {
        // When
        String[] result = eventProcessingService.extractRepoInfo(null);

        // Then
        assertNull(result[0]);
        assertNull(result[1]);
    }

    @Test
    @DisplayName("Should extract string value from map correctly")
    void shouldExtractStringValueFromMapCorrectly() {
        // Given
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 123);

        // When & Then
        assertEquals("value1", eventProcessingService.getStringValue(map, "key1"));
        assertNull(eventProcessingService.getStringValue(map, "key2")); // not a string
        assertNull(eventProcessingService.getStringValue(map, "nonexistent"));
    }

    @Test
    @DisplayName("Should extract int value from map correctly")
    void shouldExtractIntValueFromMapCorrectly() {
        // Given
        Map<String, Object> map = new HashMap<>();
        map.put("key1", 42);
        map.put("key2", "not-a-number");

        // When & Then
        assertEquals(42, eventProcessingService.getIntValue(map, "key1"));
        assertEquals(0, eventProcessingService.getIntValue(map, "key2")); // not a number
        assertEquals(0, eventProcessingService.getIntValue(map, "nonexistent"));
    }

    @Test
    @DisplayName("Should filter events by type correctly")
    void shouldFilterEventsByTypeCorrectly() {
        // Given
        GitHubEvent pushEvent = createEvent("PushEvent", "user/repo1");
        GitHubEvent issueEvent = createEvent("IssuesEvent", "user/repo2");
        GitHubEvent anotherPushEvent = createEvent("PushEvent", "user/repo3");
        GitHubEvent[] events = {pushEvent, issueEvent, anotherPushEvent};

        // When
        List<GitHubEvent> result = eventProcessingService.filterEventsByType(events, "PushEvent");

        // Then
        assertEquals(2, result.size());
        assertEquals("PushEvent", result.get(0).getType());
        assertEquals("PushEvent", result.get(1).getType());
    }

    @Test
    @DisplayName("Should handle null events array in filter")
    void shouldHandleNullEventsArrayInFilter() {
        // When
        List<GitHubEvent> result = eventProcessingService.filterEventsByType(null, "PushEvent");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should process commit events correctly")
    void shouldProcessCommitEventsCorrectly() {
        // Given
        GitHubEvent pushEvent = createPushEvent("user/test-repo", 3, "2025-09-22T10:30:00Z");
        List<GitHubEvent> pushEvents = List.of(pushEvent);

        // When
        List<CommitEventDto> result = eventProcessingService.processCommitEvents(pushEvents);

        // Then
        assertEquals(1, result.size());
        CommitEventDto commitEvent = result.get(0);
        assertEquals("test-repo", commitEvent.getRepoName());
        assertEquals("user", commitEvent.getRepoOwner());
        assertEquals(3, commitEvent.getCommitCount());
        assertEquals("2025-09-22T10:30:00Z", commitEvent.getCreatedAt());
    }

    @Test
    @DisplayName("Should process push events correctly")
    void shouldProcessPushEventsCorrectly() {
        // Given
        GitHubEvent pushEvent = createPushEvent("owner/repository", 5, "2025-09-22T11:00:00Z");
        List<GitHubEvent> pushEvents = List.of(pushEvent);

        // When
        List<PushEventDto> result = eventProcessingService.processPushEvents(pushEvents);

        // Then
        assertEquals(1, result.size());
        PushEventDto pushEventDto = result.get(0);
        assertEquals("repository", pushEventDto.getRepoName());
        assertEquals("owner", pushEventDto.getRepoOwner());
        assertEquals(5, pushEventDto.getCommitCount());
        assertEquals("2025-09-22T11:00:00Z", pushEventDto.getCreatedAt());
    }

    @Test
    @DisplayName("Should process issue events correctly")
    void shouldProcessIssueEventsCorrectly() {
        // Given
        GitHubEvent issueEvent = createIssueEvent("owner/repo", "Bug report", "opened", "2025-09-22T12:00:00Z");
        List<GitHubEvent> issueEvents = List.of(issueEvent);

        // When
        List<IssueEventDto> result = eventProcessingService.processIssueEvents(issueEvents);

        // Then
        assertEquals(1, result.size());
        IssueEventDto issueEventDto = result.get(0);
        assertEquals("repo", issueEventDto.getRepoName());
        assertEquals("owner", issueEventDto.getRepoOwner());
        assertEquals("Bug report", issueEventDto.getIssueTitle());
        assertEquals("opened", issueEventDto.getAction());
        assertEquals("2025-09-22T12:00:00Z", issueEventDto.getCreatedAt());
    }

    @Test
    @DisplayName("Should process starred repos correctly")
    void shouldProcessStarredReposCorrectly() {
        // Given
        Map<String, Object> repo1 = new HashMap<>();
        repo1.put("full_name", "facebook/react");

        Map<String, Object> repo2 = new HashMap<>();
        repo2.put("full_name", "microsoft/vscode");

        Object[] starredRepos = {repo1, repo2};

        // When
        List<StarEventDto> result = eventProcessingService.processStarredRepos(starredRepos);

        // Then
        assertEquals(2, result.size());

        StarEventDto star1 = result.get(0);
        assertEquals("react", star1.getRepoName());
        assertEquals("facebook", star1.getRepoOwner());
        assertEquals("Unknown", star1.getStarredAt());

        StarEventDto star2 = result.get(1);
        assertEquals("vscode", star2.getRepoName());
        assertEquals("microsoft", star2.getRepoOwner());
    }

    @Test
    @DisplayName("Should handle null starred repos array")
    void shouldHandleNullStarredReposArray() {
        // When
        List<StarEventDto> result = eventProcessingService.processStarredRepos(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle events with null repo")
    void shouldHandleEventsWithNullRepo() {
        // Given
        GitHubEvent eventWithNullRepo = new GitHubEvent();
        eventWithNullRepo.setType("PushEvent");
        eventWithNullRepo.setCreatedAt("2025-09-22T10:30:00Z");
        eventWithNullRepo.setRepo(null);

        Map<String, Object> payload = new HashMap<>();
        payload.put("size", 2);
        eventWithNullRepo.setPayload(payload);

        List<GitHubEvent> events = List.of(eventWithNullRepo);

        // When
        List<CommitEventDto> result = eventProcessingService.processCommitEvents(events);

        // Then
        assertEquals(1, result.size());
        CommitEventDto commitEvent = result.get(0);
        assertNull(commitEvent.getRepoName());
        assertNull(commitEvent.getRepoOwner());
        assertEquals(2, commitEvent.getCommitCount());
    }

    @Test
    @DisplayName("Should handle events with invalid payload")
    void shouldHandleEventsWithInvalidPayload() {
        // Given
        GitHubEvent eventWithInvalidPayload = createEvent("PushEvent", "user/repo");
        eventWithInvalidPayload.setPayload("invalid-payload"); // not a Map

        List<GitHubEvent> events = List.of(eventWithInvalidPayload);

        // When
        List<CommitEventDto> result = eventProcessingService.processCommitEvents(events);

        // Then
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getCommitCount()); // should default to 0
    }

    // Helper methods
    private GitHubEvent createEvent(String type, String repoName) {
        GitHubEvent event = new GitHubEvent();
        event.setType(type);
        event.setCreatedAt("2025-09-22T10:30:00Z");
        event.setRepo(new Repo(repoName));
        return event;
    }

    private GitHubEvent createPushEvent(String repoName, int commitCount, String createdAt) {
        GitHubEvent event = new GitHubEvent();
        event.setType("PushEvent");
        event.setCreatedAt(createdAt);
        event.setRepo(new Repo(repoName));

        Map<String, Object> payload = new HashMap<>();
        payload.put("size", commitCount);
        event.setPayload(payload);

        return event;
    }

    private GitHubEvent createIssueEvent(String repoName, String issueTitle, String action, String createdAt) {
        GitHubEvent event = new GitHubEvent();
        event.setType("IssuesEvent");
        event.setCreatedAt(createdAt);
        event.setRepo(new Repo(repoName));

        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put("title", issueTitle);

        Map<String, Object> payload = new HashMap<>();
        payload.put("issue", issueMap);
        payload.put("action", action);
        event.setPayload(payload);

        return event;
    }
}
