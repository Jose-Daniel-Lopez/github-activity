package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.util.AnsiColor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventFormatter {

    public static String format(GitHubEvent event) {
        if (event == null || event.getRepo() == null) {
            return AnsiColor.RED + "Unknown event" + AnsiColor.RESET;
        }

        String repoName = event.getRepo().getName();

        return switch (event.getType()) {
            case "PushEvent" -> {
                int size = getPushSize(event);
                yield AnsiColor.BOLD_GREEN + AnsiColor.ICON_PUSH +
                        "Pushed " + size + " commits to " + repoName +
                        AnsiColor.RESET + " -> " + event.getCreatedAt();
            }
            case "WatchEvent" -> // Starred
                    AnsiColor.BOLD_YELLOW + AnsiColor.ICON_STAR +
                            "Starred " + repoName +
                            AnsiColor.RESET;
            case "IssuesEvent" -> AnsiColor.BOLD_PURPLE + AnsiColor.ICON_ISSUE +
                    "Opened an issue in " + repoName +
                    AnsiColor.RESET;
            case "ForkEvent" -> AnsiColor.BOLD_BLUE + AnsiColor.ICON_FORK +
                    "Forked " + repoName +
                    AnsiColor.RESET;
            case "PullRequestEvent" -> AnsiColor.CYAN + AnsiColor.ICON_PR +
                    "Created a pull request in " + repoName +
                    AnsiColor.RESET;
            default -> AnsiColor.WHITE + AnsiColor.ICON_DEFAULT +
                    event.getType() + " on " + repoName +
                    AnsiColor.RESET;
        };
    }

    // Helper method
    private static int getPushSize(GitHubEvent event) {
        try {
            Object payload = event.getPayload();
            if (payload instanceof Map<?, ?> map) {
                Object size = map.get("size");
                if (size instanceof Number) {
                    return ((Number) size).intValue();
                }
            }
        } catch (Exception e) {
            // Ignore and return 0
        }
        return 0;
    }
}