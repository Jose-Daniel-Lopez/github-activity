package com.github.Jose_Daniel_Lopez.github_activity_cli.controller;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.EventFormatter;
import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.StarEventDto;
import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.CommitEventDto;
import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.PushEventDto;
import com.github.Jose_Daniel_Lopez.github_activity_cli.dto.IssueEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GithubActivityController {

    private final RestTemplate restTemplate;

    @Autowired
    public GithubActivityController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/activity/{username}")
    public Object getActivity(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();
            List<String> formattedEvents = new ArrayList<>();
            if (events != null) {
                for (GitHubEvent event : events) {
                    formattedEvents.add(EventFormatter.format(event));
                }
            }
            if (formattedEvents.isEmpty()) {
                return "The specified user has no activity events.";
            }
            return formattedEvents;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("User not found: " + username);
            } else {
                throw new RuntimeException("GitHub API error: " + e.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/commits/{username}")
    public Object getCommitEvents(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();
            List<CommitEventDto> commitEvents = new ArrayList<>();
            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("PushEvent".equals(event.getType())) {
                        String repoName = event.getRepo() != null ? event.getRepo().getName() : null;
                        String repoOwner = null;
                        if (repoName != null && repoName.contains("/")) {
                            repoOwner = repoName.split("/")[0];
                        }
                        int commitCount = 0;
                        Object payload = event.getPayload();
                        if (payload instanceof java.util.Map<?, ?> map) {
                            Object size = map.get("size");
                            if (size instanceof Number) {
                                commitCount = ((Number) size).intValue();
                            }
                        }
                        String pushedAt = event.getCreatedAt();
                        commitEvents.add(new CommitEventDto(repoName, repoOwner, commitCount, pushedAt));
                    }
                }
            }
            if (commitEvents.isEmpty()) {
                return "The specified user has no commit events.";
            }
            return commitEvents;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("User not found: " + username);
            } else {
                throw new RuntimeException("GitHub API error: " + e.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/pushes/{username}")
    public Object getPushEvents(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();
            List<PushEventDto> pushEvents = new ArrayList<>();
            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("PushEvent".equals(event.getType())) {
                        String repoName = event.getRepo() != null ? event.getRepo().getName() : null;
                        String repoOwner = null;
                        if (repoName != null && repoName.contains("/")) {
                            repoOwner = repoName.split("/")[0];
                        }
                        int commitCount = 0;
                        Object payload = event.getPayload();
                        if (payload instanceof java.util.Map<?, ?> map) {
                            Object size = map.get("size");
                            if (size instanceof Number) {
                                commitCount = ((Number) size).intValue();
                            }
                        }
                        String pushedAt = event.getCreatedAt();
                        pushEvents.add(new PushEventDto(repoName, repoOwner, commitCount, pushedAt));
                    }
                }
            }
            if (pushEvents.isEmpty()){
                return "The specified user has no push events.";
            }
            return pushEvents;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("User not found: " + username);
            } else {
                throw new RuntimeException("GitHub API error: " + e.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/issues/{username}")
    public Object getIssueEvents(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();
            List<IssueEventDto> issueEvents = new ArrayList<>();
            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("IssuesEvent".equals(event.getType())) {
                        String repoName = event.getRepo() != null ? event.getRepo().getName() : null;
                        String repoOwner = null;
                        if (repoName != null && repoName.contains("/")) {
                            repoOwner = repoName.split("/")[0];
                        }
                        String issueTitle = null;
                        String action = null;
                        Object payload = event.getPayload();
                        if (payload instanceof java.util.Map<?, ?> map) {
                            Object issueObj = map.get("issue");
                            if (issueObj instanceof java.util.Map<?, ?> issueMap) {
                                Object titleObj = issueMap.get("title");
                                if (titleObj instanceof String) {
                                    issueTitle = (String) titleObj;
                                }
                            }
                            Object actionObj = map.get("action");
                            if (actionObj instanceof String) {
                                action = (String) actionObj;
                            }
                        }
                        String occurredAt = event.getCreatedAt();
                        issueEvents.add(new IssueEventDto(repoName, repoOwner, issueTitle, action, occurredAt));
                    }
                }
            }
            if (issueEvents.isEmpty()) {
                return "The specified user has no opened issues.";
            }
            return issueEvents;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("User not found: " + username);
            } else {
                throw new RuntimeException("GitHub API error: " + e.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/stars/{username}")
    public Object getStarEvents(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();
            List<StarEventDto> starEvents = new ArrayList<>();
            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("WatchEvent".equals(event.getType())) {
                        String repoName = event.getRepo() != null ? event.getRepo().getName() : null;
                        String repoOwner = null;
                        if (repoName != null && repoName.contains("/")) {
                            repoOwner = repoName.split("/")[0];
                        }
                        String starredAt = event.getCreatedAt();
                        starEvents.add(new StarEventDto(repoName, repoOwner, starredAt));
                    }
                }
            }
            if (starEvents.isEmpty()) {
                return "The specified user has no starred repositories.";
            }
            return starEvents;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new RuntimeException("User not found: " + username);
            } else {
                throw new RuntimeException("GitHub API error: " + e.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }
}
