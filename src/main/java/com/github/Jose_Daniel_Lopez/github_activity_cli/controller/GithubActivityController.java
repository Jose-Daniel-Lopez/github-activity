package com.github.Jose_Daniel_Lopez.github_activity_cli.controller;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.service.EventFormatter;
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
    public List<String> getActivity(@PathVariable String username) {
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
    public int getTotalCommits(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();

            int totalCommits = 0;

            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("PushEvent".equals(event.getType())) {
                        Object payload = event.getPayload();
                        if (payload instanceof java.util.Map<?, ?> map) {
                            Object size = map.get("size");
                            if (size instanceof Number) {
                                totalCommits += ((Number) size).intValue();
                            }
                        }
                    }
                }
            }

            return totalCommits;

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
    public int getTotalPushes(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();

            int totalPushes = 0;

            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("PushEvent".equals(event.getType())) {
                        totalPushes++;
                    }
                }
            }

            return totalPushes;

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
    public int getTotalIssues(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();
            int totalIssues = 0;
            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("IssuesEvent".equals(event.getType())) {
                        totalIssues++;
                    }
                }
            }
            return totalIssues;
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
    public int getTotalStars(@PathVariable String username) {
        String url = "https://api.github.com/users/" + username + "/events";

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            GitHubEvent[] events = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();

            int totalStars = 0;

            if (events != null) {
                for (GitHubEvent event : events) {
                    if ("WatchEvent".equals(event.getType())) {
                        totalStars++;
                    }
                }
            }

            return totalStars;

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
