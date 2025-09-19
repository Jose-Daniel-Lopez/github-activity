package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import org.springframework.stereotype.Service;

@Service
public class EventFormatter {

    public static String format(GitHubEvent event) {
        if (event == null || event.getRepo() == null) {
            return "Unknown event";
        }

        String repoName = event.getRepo().getName();

        int commitCount = 0;
        if (event.getPayload() instanceof java.util.Map) {
            Object commits = ((java.util.Map<?, ?>) event.getPayload()).get("commits");
            if (commits instanceof java.util.List) {
                commitCount = ((java.util.List<?>) commits).size();
            }
        }

        return switch (event.getType()) {
            case "PushEvent" -> "Pushed " + commitCount + " commit(s) to " + repoName;
            case "WatchEvent" -> // "WatchEvent" = starring a repo
                    "Starred " + repoName;
            case "IssuesEvent" -> "Opened an issue in " + repoName;
            case "ForkEvent" -> "Forked " + repoName;
            case "PullRequestEvent" -> "Created a pull request in " + repoName;
            default -> event.getType() + " on " + repoName;
        };
    }
}
