package com.github.Jose_Daniel_Lopez.github_activity_cli.service;

import com.github.Jose_Daniel_Lopez.github_activity_cli.model.GitHubEvent;
import com.github.Jose_Daniel_Lopez.github_activity_cli.util.AnsiColor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Utility service for formatting GitHub events into human-readable, colorized strings for CLI output.
 * <p>
 * Uses ANSI escape codes (via {@link AnsiColor}) to provide visual distinction between event types.
 * Intended for terminal/console display — not for API responses or logs.
 * </p>
 * <p><strong>Thread Safety:</strong> This class is stateless. All methods are effectively static and thread-safe.</p>
 * <p><strong>Extensibility:</strong> To support new event types, add cases to the {@code switch} in {@link #format}.</p>
 *
 * @since 1.0
 */
@Service
public class EventFormatter {

    /**
     * Formats a GitHub event into a colorized, human-readable string for CLI display.
     * <p>
     * If event or repo is null, returns a red "Unknown event" message.
     * For unsupported event types, returns a default formatted message.
     * </p>
     * <p><strong>Example Output:</strong><br>
     * {@code 🟢 Pushed 3 commits to user/repo -> 2025-04-01T12:34:56Z}</p>
     *
     * @param event the GitHub event to format (may be null)
     * @return a formatted, colorized string ready for terminal output
     */
    public static String format(GitHubEvent event) {
        if (event == null || event.getRepo() == null) {
            return AnsiColor.RED + "Unknown event" + AnsiColor.RESET;
        }

        final String repoName = event.getRepo().getName();

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

    /**
     * Helper method to safely extract the number of commits from a PushEvent payload.
     * <p>Returns 0 if payload is malformed or missing {@code size} field.</p>
     * <p>Gracefully handles any exception — never throws.</p>
     *
     * @param event the PushEvent to extract size from
     * @return number of commits, or 0 if unavailable
     */
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
            // Graceful degradation: ignore and return 0
            // Consider logging.warn("Failed to extract push size", e) in future
        }
        return 0;
    }
}