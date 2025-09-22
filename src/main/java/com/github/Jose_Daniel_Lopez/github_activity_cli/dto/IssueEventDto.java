package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an issue lifecycle event from GitHub.
 * <p>
 * Derived from GitHub's {@code IssuesEvent}, triggered when a user performs an action
 * on an issue (e.g., opened, closed, reopened, assigned, labeled, etc.).
 * This DTO captures the repository context, issue title, and action performed.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueEventDto {

    /**
     * Name of the repository where the issue event occurred.
     * Example: "bug-tracker-app"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "johndoe"
     */
    private String repoOwner;

    /**
     * Title of the issue that was acted upon.
     * Example: "Login page crashes on Safari"
     */
    private String issueTitle;

    /**
     * The action performed on the issue. Common values include:
     * <ul>
     *   <li>{@code "opened"} — issue was created</li>
     *   <li>{@code "closed"} — issue was closed</li>
     *   <li>{@code "reopened"} — issue was reopened</li>
     *   <li>{@code "assigned"}, {@code "unassigned"}, {@code "labeled"}, {@code "unlabeled"}, etc.</li>
     * </ul>
     * <p>Populated from the {@code action} field in GitHub's event payload.</p>
     */
    private String action;

    /**
     * ISO 8601 formatted timestamp indicating when the issue event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;

    public String getCreatedAt() {
        return occurredAt;
    }
}