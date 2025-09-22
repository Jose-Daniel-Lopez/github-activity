package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class GitHubApiService {

    private final WebClient webClient;

    @Autowired
    public GitHubApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Fetches user events from GitHub API
     */
    public GitHubEvent[] fetchUserEvents(String username) {
        String url = "https://api.github.com/users/" + username + "/events";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GitHubEvent[].class)
                .block(); // Using block() for synchronous behavior
    }

    /**
     * Fetches user starred repositories from GitHub API
     */
    public Object[] fetchUserStarredRepos(String username) {
        String url = "https://api.github.com/users/" + username + "/starred";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Object[].class)
                .block();
    }

    /**
     * Fetches user repositories from GitHub API
     */
    public Object[] fetchUserRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Object[].class)
                .block();
    }

    /**
     * Handles common GitHub API exceptions
     */
    public RuntimeException handleGitHubApiException(Exception e, String username) {
        if (e instanceof WebClientResponseException webEx) {
            if (webEx.getStatusCode().value() == 404) {
                return new RuntimeException("User not found: " + username);
            } else {
                return new RuntimeException("GitHub API error: " + webEx.getStatusCode());
            }
        }
        return new RuntimeException("Unexpected error: " + e.getMessage());
    }
}
