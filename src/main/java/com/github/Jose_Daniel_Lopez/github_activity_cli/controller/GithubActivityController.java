package com.github.Jose_Daniel_Lopez.github_activity_cli.controller;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.EventFormatter;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.GitHubApiService;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.EventProcessingService;
import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for fetching and formatting GitHub user activity.
 * <p>
 * All endpoints return either a list of processed events or a descriptive message if no events are found.
 * Note: GitHub's Events API only returns activity from the last 90 days. Some endpoints (e.g., commits, issues)
 * are limited by this constraint — there is no official API for historical/all-time data across repositories.
 * </p>
 * <p><strong>Design Note:</strong> Returns {@code Object} to allow flexible response (List or String message).
 * Consider using {@code ResponseEntity<?>} in future for explicit HTTP status control.</p>
 */
@RestController
@RequestMapping("/api")
public class GithubActivityController {

    private final GitHubApiService gitHubApiService;
    private final EventProcessingService eventProcessingService;

    /**
     * Constructor-based dependency injection for required services.
     *
     * @param gitHubApiService       service to fetch raw events from GitHub API
     * @param eventProcessingService service to filter and transform events into DTOs
     */
    @Autowired
    public GithubActivityController(GitHubApiService gitHubApiService, EventProcessingService eventProcessingService) {
        this.gitHubApiService = gitHubApiService;
        this.eventProcessingService = eventProcessingService;
    }

    // ========== HEALTH CHECK ==========

    /**
     * Simple health check endpoint.
     *
     * @return "OK" if service is running
     */
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    // ========== GENERAL ACTIVITY ENDPOINTS ==========

    /**
     * Fetches and formats all recent public events for a GitHub user.
     * <p><strong>Note:</strong> Only events from the last 90 days are available via GitHub API.</p>
     *
     * @param username GitHub username
     * @return List of formatted event strings, or message if no events found
     * @throws RuntimeException wrapped GitHub API exceptions via {@link GitHubApiService#handleGitHubApiException}
     */
    @GetMapping("/activity/{username}")
    public Object getActivity(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<String> formattedEvents = new ArrayList<>();
            if (events != null) {
                for (GitHubEvent event : events) {
                    formattedEvents.add(EventFormatter.format(event));
                }
            }
            return handleEmptyResult(formattedEvents, "The specified user has no activity events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    // ========== TOTAL ACTIVITY ENDPOINTS ==========

    /**
     * Fetches all repositories starred by the user.
     *
     * @param username GitHub username
     * @return List of {@link StarEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/stars/{username}")
    public Object getStarEvents(@PathVariable String username) {
        try {
            Object[] starredRepos = gitHubApiService.fetchUserStarredRepos(username);
            List<StarEventDto> starEvents = eventProcessingService.processStarredRepos(starredRepos);
            return handleEmptyResult(starEvents, "The specified user has no starred repositories.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches all public repositories owned by the user.
     *
     * @param username GitHub username
     * @return List of {@link RepositoryDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/repositories/{username}")
    public Object getUserRepositories(@PathVariable String username) {
        try {
            Object[] repos = gitHubApiService.fetchUserRepositories(username);
            List<RepositoryDto> repositories = eventProcessingService.processRepositories(repos);
            return handleEmptyResult(repositories, "The specified user has no public repositories.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    // ========== RECENT ACTIVITY ENDPOINTS (LAST 90 DAYS) ==========

    /**
     * Fetches recent commit events (from PushEvents) for the user.
     * <p><strong>Note:</strong> GitHub does not provide an all-time commit history API.
     * This uses PushEvents from the last 90 days as the only available source.</p>
     *
     * @param username GitHub username
     * @return List of {@link CommitEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/commits/{username}")
    public Object getCommitEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> pushEvents = eventProcessingService.filterEventsByType(events, "PushEvent");
            List<CommitEventDto> commitEvents = eventProcessingService.processCommitEvents(pushEvents);
            return handleEmptyResult(commitEvents, "The specified user has no recent commit events (last 90 days).");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches recent push events for the user (last 90 days).
     *
     * @param username GitHub username
     * @return List of {@link PushEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/pushes/{username}")
    public Object getPushEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> pushEvents = eventProcessingService.filterEventsByType(events, "PushEvent");
            List<PushEventDto> pushEventDtos = eventProcessingService.processPushEvents(pushEvents);
            return handleEmptyResult(pushEventDtos, "The specified user has no recent push events (last 90 days).");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches recent issue events created by the user (last 90 days).
     * <p><strong>Note:</strong> GitHub does not provide an all-time cross-repo issue history API.</p>
     *
     * @param username GitHub username
     * @return List of {@link IssueEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/issues/{username}")
    public Object getIssueEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> issueEvents = eventProcessingService.filterEventsByType(events, "IssuesEvent");
            List<IssueEventDto> issueEventDtos = eventProcessingService.processIssueEvents(issueEvents);
            return handleEmptyResult(issueEventDtos, "The specified user has no recent issue events (last 90 days).");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches repository fork events triggered by the user.
     *
     * @param username GitHub username
     * @return List of {@link ForkEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/forks/{username}")
    public Object getForkEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> forkEvents = eventProcessingService.filterEventsByType(events, "ForkEvent");
            List<ForkEventDto> forkEventDtos = eventProcessingService.processForkEvents(forkEvents);
            return handleEmptyResult(forkEventDtos, "The specified user has no fork events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches pull request events involving the user.
     *
     * @param username GitHub username
     * @return List of {@link PullRequestEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/pulls/{username}")
    public Object getPullRequestEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> prEvents = eventProcessingService.filterEventsByType(events, "PullRequestEvent");
            List<PullRequestEventDto> prEventDtos = eventProcessingService.processPullRequestEvents(prEvents);
            return handleEmptyResult(prEventDtos, "The specified user has no pull request events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches release events published by the user.
     *
     * @param username GitHub username
     * @return List of {@link ReleaseEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/releases/{username}")
    public Object getReleaseEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> releaseEvents = eventProcessingService.filterEventsByType(events, "ReleaseEvent");
            List<ReleaseEventDto> releaseEventDtos = eventProcessingService.processReleaseEvents(releaseEvents);
            return handleEmptyResult(releaseEventDtos, "The specified user has no release events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches issue comment events by the user.
     *
     * @param username GitHub username
     * @return List of {@link IssueCommentEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/comments/{username}")
    public Object getIssueCommentEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> commentEvents = eventProcessingService.filterEventsByType(events, "IssueCommentEvent");
            List<IssueCommentEventDto> commentEventDtos = eventProcessingService.processCommentEvents(commentEvents);
            return handleEmptyResult(commentEventDtos, "The specified user has no issue comment events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches repository publicization events by the user.
     *
     * @param username GitHub username
     * @return List of {@link PublicEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/public/{username}")
    public Object getPublicEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> publicEvents = eventProcessingService.filterEventsByType(events, "PublicEvent");
            List<PublicEventDto> publicEventDtos = eventProcessingService.processPublicEvents(publicEvents);
            return handleEmptyResult(publicEventDtos, "The specified user has no public events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches branch/tag delete events by the user.
     *
     * @param username GitHub username
     * @return List of {@link DeleteEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/delete/{username}")
    public Object getDeleteEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> deleteEvents = eventProcessingService.filterEventsByType(events, "DeleteEvent");
            List<DeleteEventDto> deleteEventDtos = eventProcessingService.processDeleteEvents(deleteEvents);
            return handleEmptyResult(deleteEventDtos, "The specified user has no delete events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches repository/branch creation events by the user.
     *
     * @param username GitHub username
     * @return List of {@link CreateEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/create/{username}")
    public Object getCreateEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> createEvents = eventProcessingService.filterEventsByType(events, "CreateEvent");
            List<CreateEventDto> createEventDtos = eventProcessingService.processCreateEvents(createEvents);
            return handleEmptyResult(createEventDtos, "The specified user has no create events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    /**
     * Fetches repository collaborator addition events.
     *
     * @param username GitHub username
     * @return List of {@link MemberEventDto}, or message if none found
     * @throws RuntimeException wrapped GitHub API exceptions
     */
    @GetMapping("/member/{username}")
    public Object getMemberEvents(@PathVariable String username) {
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> memberEvents = eventProcessingService.filterEventsByType(events, "MemberEvent");
            List<MemberEventDto> memberEventDtos = eventProcessingService.processMemberEvents(memberEvents);
            return handleEmptyResult(memberEventDtos, "The specified user has no member events.");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    // ========== HELPER METHODS ==========

    /**
     * Returns the given list if non-empty, otherwise returns the provided empty message.
     * <p><strong>Contract:</strong> Never returns {@code null}.</p>
     *
     * @param resultList    the list of results to check
     * @param emptyMessage  message to return if list is empty
     * @return the list if not empty, otherwise the empty message
     */
    private Object handleEmptyResult(List<?> resultList, String emptyMessage) {
        if (resultList.isEmpty()) {
            return emptyMessage;
        }
        return resultList;
    }
}