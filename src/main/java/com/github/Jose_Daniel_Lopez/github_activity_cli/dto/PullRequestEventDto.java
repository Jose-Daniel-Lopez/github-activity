package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a pull request lifecycle event from GitHub.
 * <p>
 * Derived from GitHub's {@code PullRequestEvent}, triggered when a user performs an action
 * on a pull request — such as opening, closing, merging, or reopening.
 * This DTO captures the repository context, PR title, and the action performed.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestEventDto {

    /**
     * Name of the repository where the pull request event occurred.
     * Example: "web-app"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "frontend-team"
     */
    private String repoOwner;

    /**
     * Title of the pull request that was acted upon.
     * Example: "Fix login button alignment on mobile"
     */
    private String prTitle;

    /**
     * The action performed on the pull request. Common values include:
     * <ul>
     *   <li>{@code "opened"} — PR was created</li>
     *   <li>{@code "closed"} — PR was closed without merging</li>
     *   <li>{@code "reopened"} — PR was reopened</li>
     *   <li>{@code "merged"} — PR was merged into base branch</li>
     *   <li>{@code "assigned"}, {@code "unassigned"}, {@code "review_requested"}, etc.</li>
     * </ul>
     * <p>Populated from the {@code action} field in GitHub's event payload.</p>
     */
    private String action;

    /**
     * ISO 8601 formatted timestamp indicating when the pull request event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;
}