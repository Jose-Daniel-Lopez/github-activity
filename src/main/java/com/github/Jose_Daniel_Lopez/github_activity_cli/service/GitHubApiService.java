package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubApiService {

    private final RestTemplate restTemplate;

    @Autowired
    public GitHubApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Creates HTTP headers with User-Agent for GitHub API requests
     */
    public HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SpringBootGitHubCLI/1.0");
        return new HttpEntity<>(headers);
    }

    /**
     * Fetches user events from GitHub API
     */
    public GitHubEvent[] fetchUserEvents(String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        HttpEntity<String> entity = createHttpEntity();
        return restTemplate.exchange(url, HttpMethod.GET, entity, GitHubEvent[].class).getBody();
    }

    /**
     * Fetches user starred repositories from GitHub API
     */
    public Object[] fetchUserStarredRepos(String username) {
        String url = "https://api.github.com/users/" + username + "/starred";
        HttpEntity<String> entity = createHttpEntity();
        return restTemplate.exchange(url, HttpMethod.GET, entity, Object[].class).getBody();
    }

    /**
     * Fetches user repositories from GitHub API
     */
    public Object[] fetchUserRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        HttpEntity<String> entity = createHttpEntity();
        return restTemplate.exchange(url, HttpMethod.GET, entity, Object[].class).getBody();
    }

    /**
     * Handles common GitHub API exceptions
     */
    public RuntimeException handleGitHubApiException(Exception e, String username) {
        if (e instanceof HttpClientErrorException httpEx) {
            if (httpEx.getStatusCode().value() == 404) {
                return new RuntimeException("User not found: " + username);
            } else {
                return new RuntimeException("GitHub API error: " + httpEx.getStatusCode());
            }
        }
        return new RuntimeException("Unexpected error: " + e.getMessage());
    }
}
