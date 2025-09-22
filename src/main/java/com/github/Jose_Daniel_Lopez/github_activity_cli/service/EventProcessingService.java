package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.*;
import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for processing and transforming raw GitHub events into typed DTOs.
 * <p>
 * This class handles:
 * <ul>
 *   <li>Filtering events by type</li>
 *   <li>Extracting and validating data from untyped {@code payload} maps</li>
 *   <li>Converting event data into presentation-ready DTOs</li>
 * </ul>
 * </p>
 * <p><strong>Assumptions:</strong>
 * <ul>
 *   <li>GitHub event {@code payload} is deserialized as {@code Map<?, ?>}.</li>
 *   <li>Repository names follow {@code "owner/repo"} format.</li>
 * </ul>
 * </p>
 * <p><strong>Thread Safety:</strong> This service is stateless and thread-safe. Safe to inject and reuse.</p>
 * <p><strong>Performance Note:</strong> For very large event lists, consider parallelStream() — but not needed for GitHub's 300-event limit.</p>
 *
 * @since 1.0
 */
@Service
public class EventProcessingService {

    /**
     * Extracts repository name and owner from a full repository name in "owner/repo" format.
     * <p><strong>Example:</strong> Input: {@code "spring-projects/spring-boot"} → Returns: {@code ["spring-boot", "spring-projects"]}</p>
     * <p><strong>Note:</strong> If format is invalid, returns input as repoName and {@code null} as repoOwner.</p>
     *
     * @param fullRepoName the full repository name (e.g., "owner/repo")
     * @return array where [0] = repoName, [1] = repoOwner
     */
    public String[] extractRepoInfo(String fullRepoName) {
        if (fullRepoName != null && fullRepoName.contains("/")) {
            String[] parts = fullRepoName.split("/", 2); // Limit to 2 parts for safety
            return new String[]{parts[1], parts[0]}; // [repoName, repoOwner]
        }
        return new String[]{fullRepoName, null};
    }

    /**
     * Safely extracts a string value from a map, returning {@code null} if key is missing or value is not a String.
     *
     * @param map the map to extract from
     * @param key the key to look up
     * @return the string value, or {@code null} if not present or not a String
     */
    public String getStringValue(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value instanceof String ? (String) value : null;
    }

    /**
     * Safely extracts an integer value from a map, returning 0 if key is missing or value is not a Number.
     *
     * @param map the map to extract from
     * @param key the key to look up
     * @return the integer value, or 0 if not present or not a Number
     */
    public int getIntValue(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    /**
     * Filters an array of GitHub events by event type.
     *
     * @param events    array of GitHub events (may be null)
     * @param eventType the event type to filter by (e.g., "PushEvent")
     * @return list of events matching the type; never null
     */
    public List<GitHubEvent> filterEventsByType(GitHubEvent[] events, String eventType) {
        List<GitHubEvent> filteredEvents = new ArrayList<>();
        if (events != null) {
            for (GitHubEvent event : events) {
                if (eventType.equals(event.getType())) {
                    filteredEvents.add(event);
                }
            }
        }
        return filteredEvents;
    }

    /**
     * Processes PushEvents and converts them to {@link CommitEventDto}.
     * <p><strong>Note:</strong> Nearly identical to {@link #processPushEvents} — consider consolidation if business logic permits.</p>
     *
     * @param pushEvents list of PushEvent GitHub events
     * @return list of {@link CommitEventDto}
     */
    public List<CommitEventDto> processCommitEvents(List<GitHubEvent> pushEvents) {
        List<CommitEventDto> commitEvents = new ArrayList<>();
        for (GitHubEvent event : pushEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            int commitCount = 0;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object size = map.get("size");
                if (size instanceof Number) {
                    commitCount = ((Number) size).intValue();
                }
            }
            commitEvents.add(new CommitEventDto(repoInfo[0], repoInfo[1], commitCount, event.getCreatedAt()));
        }
        return commitEvents;
    }

    /**
     * Processes PushEvents and converts them to {@link PushEventDto}.
     * <p><strong>Note:</strong> Nearly identical to {@link #processCommitEvents} — consider consolidation if business logic permits.</p>
     *
     * @param pushEvents list of PushEvent GitHub events
     * @return list of {@link PushEventDto}
     */
    public List<PushEventDto> processPushEvents(List<GitHubEvent> pushEvents) {
        List<PushEventDto> pushEventDtos = new ArrayList<>();
        for (GitHubEvent event : pushEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            int commitCount = 0;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object size = map.get("size");
                if (size instanceof Number) {
                    commitCount = ((Number) size).intValue();
                }
            }
            pushEventDtos.add(new PushEventDto(repoInfo[0], repoInfo[1], commitCount, event.getCreatedAt()));
        }
        return pushEventDtos;
    }

    /**
     * Processes IssuesEvents and converts them to {@link IssueEventDto}.
     *
     * @param issueEvents list of IssuesEvent GitHub events
     * @return list of {@link IssueEventDto}
     */
    public List<IssueEventDto> processIssueEvents(List<GitHubEvent> issueEvents) {
        List<IssueEventDto> issueEventDtos = new ArrayList<>();
        for (GitHubEvent event : issueEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String issueTitle = null;
            String action = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object issueObj = map.get("issue");
                if (issueObj instanceof Map<?, ?> issueMap) {
                    issueTitle = getStringValue(issueMap, "title");
                }
                action = getStringValue(map, "action");
            }
            issueEventDtos.add(new IssueEventDto(repoInfo[0], repoInfo[1], issueTitle, action, event.getCreatedAt()));
        }
        return issueEventDtos;
    }

    /**
     * Processes starred repositories and converts them to {@link StarEventDto}.
     * <p><strong>Note:</strong> GitHub's starred repos API does not return {@code starred_at} in basic response.
     * Consider using GraphQL or enhanced REST if timestamp is needed.</p>
     *
     * @param starredRepos array of starred repository objects from GitHub API
     * @return list of {@link StarEventDto} (with placeholder timestamp)
     */
    public List<StarEventDto> processStarredRepos(Object[] starredRepos) {
        List<StarEventDto> starEvents = new ArrayList<>();
        if (starredRepos != null) {
            for (Object repoObj : starredRepos) {
                if (repoObj instanceof Map<?, ?> repo) {
                    String fullName = getStringValue(repo, "full_name");
                    String[] repoInfo = extractRepoInfo(fullName);
                    starEvents.add(new StarEventDto(repoInfo[0], repoInfo[1], "Unknown"));
                }
            }
        }
        return starEvents;
    }

    /**
     * Processes repositories and converts them to {@link RepositoryDto}.
     *
     * @param repos array of repository objects from GitHub API
     * @return list of {@link RepositoryDto}
     */
    public List<RepositoryDto> processRepositories(Object[] repos) {
        List<RepositoryDto> repositories = new ArrayList<>();
        if (repos != null) {
            for (Object repoObj : repos) {
                if (repoObj instanceof Map<?, ?> repo) {
                    String name = getStringValue(repo, "name");
                    String fullName = getStringValue(repo, "full_name");
                    String description = getStringValue(repo, "description");
                    String language = getStringValue(repo, "language");
                    int stargazersCount = getIntValue(repo, "stargazers_count");
                    int forksCount = getIntValue(repo, "forks_count");
                    String createdAt = getStringValue(repo, "created_at");
                    String updatedAt = getStringValue(repo, "updated_at");

                    repositories.add(new RepositoryDto(name, fullName, description, language,
                            stargazersCount, forksCount, createdAt, updatedAt));
                }
            }
        }
        return repositories;
    }

    /**
     * Processes ForkEvents and converts them to {@link ForkEventDto}.
     *
     * @param forkEvents list of ForkEvent GitHub events
     * @return list of {@link ForkEventDto}
     */
    public List<ForkEventDto> processForkEvents(List<GitHubEvent> forkEvents) {
        List<ForkEventDto> forkEventDtos = new ArrayList<>();
        for (GitHubEvent event : forkEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String forkedRepoName = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object forkeeObj = map.get("forkee");
                if (forkeeObj instanceof Map<?, ?> forkeeMap) {
                    forkedRepoName = getStringValue(forkeeMap, "full_name");
                }
            }
            forkEventDtos.add(new ForkEventDto(repoInfo[0], repoInfo[1], forkedRepoName, event.getCreatedAt()));
        }
        return forkEventDtos;
    }

    /**
     * Processes PullRequestEvents and converts them to {@link PullRequestEventDto}.
     *
     * @param prEvents list of PullRequestEvent GitHub events
     * @return list of {@link PullRequestEventDto}
     */
    public List<PullRequestEventDto> processPullRequestEvents(List<GitHubEvent> prEvents) {
        List<PullRequestEventDto> prEventDtos = new ArrayList<>();
        for (GitHubEvent event : prEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String prTitle = null;
            String action = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object prObj = map.get("pull_request");
                if (prObj instanceof Map<?, ?> prMap) {
                    prTitle = getStringValue(prMap, "title");
                }
                action = getStringValue(map, "action");
            }
            prEventDtos.add(new PullRequestEventDto(repoInfo[0], repoInfo[1], prTitle, action, event.getCreatedAt()));
        }
        return prEventDtos;
    }

    /**
     * Processes ReleaseEvents and converts them to {@link ReleaseEventDto}.
     *
     * @param releaseEvents list of ReleaseEvent GitHub events
     * @return list of {@link ReleaseEventDto}
     */
    public List<ReleaseEventDto> processReleaseEvents(List<GitHubEvent> releaseEvents) {
        List<ReleaseEventDto> releaseEventDtos = new ArrayList<>();
        for (GitHubEvent event : releaseEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String releaseName = null;
            String action = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object releaseObj = map.get("release");
                if (releaseObj instanceof Map<?, ?> releaseMap) {
                    releaseName = getStringValue(releaseMap, "name");
                }
                action = getStringValue(map, "action");
            }
            releaseEventDtos.add(new ReleaseEventDto(repoInfo[0], repoInfo[1], releaseName, action, event.getCreatedAt()));
        }
        return releaseEventDtos;
    }

    /**
     * Processes IssueCommentEvents and converts them to {@link IssueCommentEventDto}.
     *
     * @param commentEvents list of IssueCommentEvent GitHub events
     * @return list of {@link IssueCommentEventDto}
     */
    public List<IssueCommentEventDto> processCommentEvents(List<GitHubEvent> commentEvents) {
        List<IssueCommentEventDto> commentEventDtos = new ArrayList<>();
        for (GitHubEvent event : commentEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String commentBody = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object commentObj = map.get("comment");
                if (commentObj instanceof Map<?, ?> commentMap) {
                    commentBody = getStringValue(commentMap, "body");
                }
            }
            commentEventDtos.add(new IssueCommentEventDto(repoInfo[0], repoInfo[1], commentBody, event.getCreatedAt()));
        }
        return commentEventDtos;
    }

    /**
     * Processes PublicEvents and converts them to {@link PublicEventDto}.
     *
     * @param publicEvents list of PublicEvent GitHub events
     * @return list of {@link PublicEventDto}
     */
    public List<PublicEventDto> processPublicEvents(List<GitHubEvent> publicEvents) {
        List<PublicEventDto> publicEventDtos = new ArrayList<>();
        for (GitHubEvent event : publicEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            publicEventDtos.add(new PublicEventDto(repoInfo[0], repoInfo[1], event.getCreatedAt()));
        }
        return publicEventDtos;
    }

    /**
     * Processes DeleteEvents and converts them to {@link DeleteEventDto}.
     *
     * @param deleteEvents list of DeleteEvent GitHub events
     * @return list of {@link DeleteEventDto}
     */
    public List<DeleteEventDto> processDeleteEvents(List<GitHubEvent> deleteEvents) {
        List<DeleteEventDto> deleteEventDtos = new ArrayList<>();
        for (GitHubEvent event : deleteEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String refType = null;
            String ref = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                refType = getStringValue(map, "ref_type");
                ref = getStringValue(map, "ref");
            }
            deleteEventDtos.add(new DeleteEventDto(repoInfo[0], repoInfo[1], refType, ref, event.getCreatedAt()));
        }
        return deleteEventDtos;
    }

    /**
     * Processes CreateEvents and converts them to {@link CreateEventDto}.
     *
     * @param createEvents list of CreateEvent GitHub events
     * @return list of {@link CreateEventDto}
     */
    public List<CreateEventDto> processCreateEvents(List<GitHubEvent> createEvents) {
        List<CreateEventDto> createEventDtos = new ArrayList<>();
        for (GitHubEvent event : createEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String refType = null;
            String ref = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                refType = getStringValue(map, "ref_type");
                ref = getStringValue(map, "ref");
            }
            createEventDtos.add(new CreateEventDto(repoInfo[0], repoInfo[1], refType, ref, event.getCreatedAt()));
        }
        return createEventDtos;
    }

    /**
     * Processes MemberEvents and converts them to {@link MemberEventDto}.
     *
     * @param memberEvents list of MemberEvent GitHub events
     * @return list of {@link MemberEventDto}
     */
    public List<MemberEventDto> processMemberEvents(List<GitHubEvent> memberEvents) {
        List<MemberEventDto> memberEventDtos = new ArrayList<>();
        for (GitHubEvent event : memberEvents) {
            String[] repoInfo = extractRepoInfo(event.getRepo() != null ? event.getRepo().getName() : null);
            String memberLogin = null;
            String action = null;
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object memberObj = map.get("member");
                if (memberObj instanceof Map<?, ?> memberMap) {
                    memberLogin = getStringValue(memberMap, "login");
                }
                action = getStringValue(map, "action");
            }
            memberEventDtos.add(new MemberEventDto(repoInfo[0], repoInfo[1], memberLogin, action, event.getCreatedAt()));
        }
        return memberEventDtos;
    }
}