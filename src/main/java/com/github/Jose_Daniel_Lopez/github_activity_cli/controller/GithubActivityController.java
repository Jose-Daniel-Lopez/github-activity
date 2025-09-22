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

@RestController
@RequestMapping("/api")
public class GithubActivityController {

    private final GitHubApiService gitHubApiService;
    private final EventProcessingService eventProcessingService;

    @Autowired
    public GithubActivityController(GitHubApiService gitHubApiService, EventProcessingService eventProcessingService) {
        this.gitHubApiService = gitHubApiService;
        this.eventProcessingService = eventProcessingService;
    }

    // ========== HEALTH CHECK ==========

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    // ========== GENERAL ACTIVITY ENDPOINTS ==========

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

    @GetMapping("/commits/{username}")
    public Object getCommitEvents(@PathVariable String username) {
        // Note: GitHub doesn't provide a direct API for all-time commits by a user
        // We can only get commits from recent events (last 90 days) or from specific repositories
        // This endpoint will continue to use the events API as it's the only available source
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> pushEvents = eventProcessingService.filterEventsByType(events, "PushEvent");
            List<CommitEventDto> commitEvents = eventProcessingService.processCommitEvents(pushEvents);
            return handleEmptyResult(commitEvents, "The specified user has no recent commit events (last 90 days).");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    @GetMapping("/pushes/{username}")
    public Object getPushEvents(@PathVariable String username) {
        // Note: GitHub doesn't provide a direct API for all-time pushes by a user
        // This endpoint will continue to use the events API (last 90 days)
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> pushEvents = eventProcessingService.filterEventsByType(events, "PushEvent");
            List<PushEventDto> pushEventDtos = eventProcessingService.processPushEvents(pushEvents);
            return handleEmptyResult(pushEventDtos, "The specified user has no recent push events (last 90 days).");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

    @GetMapping("/issues/{username}")
    public Object getIssueEvents(@PathVariable String username) {
        // Note: GitHub doesn't provide a direct API for all issues created by a user across all repositories
        // This endpoint will continue to use the events API (last 90 days)
        try {
            GitHubEvent[] events = gitHubApiService.fetchUserEvents(username);
            List<GitHubEvent> issueEvents = eventProcessingService.filterEventsByType(events, "IssuesEvent");
            List<IssueEventDto> issueEventDtos = eventProcessingService.processIssueEvents(issueEvents);
            return handleEmptyResult(issueEventDtos, "The specified user has no recent issue events (last 90 days).");
        } catch (Exception e) {
            throw gitHubApiService.handleGitHubApiException(e, username);
        }
    }

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
     * Helper method to handle empty results with appropriate messages
     */
    private Object handleEmptyResult(List<?> resultList, String emptyMessage) {
        if (resultList.isEmpty()) {
            return emptyMessage;
        }
        return resultList;
    }
}
