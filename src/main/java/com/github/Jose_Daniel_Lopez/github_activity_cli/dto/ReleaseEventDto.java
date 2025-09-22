package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a release lifecycle event from GitHub.
 * <p>
 * Derived from GitHub's {@code ReleaseEvent}, triggered when a user publishes, edits, or deletes
 * a release in a repository.
 * This DTO captures the repository context, release name, and the action performed.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseEventDto {

    /**
     * Name of the repository where the release event occurred.
     * Example: "mobile-app"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "mobile-team"
     */
    private String repoOwner;

    /**
     * Name or tag of the release that was acted upon.
     * Often corresponds to a Git tag (e.g., "v2.1.0") or a custom release title.
     * Example: "v1.0.0-stable"
     */
    private String releaseName;

    /**
     * The action performed on the release. Common values include:
     * <ul>
     *   <li>{@code "published"} — release was created and published</li>
     *   <li>{@code "edited"} — release metadata was updated</li>
     *   <li>{@code "deleted"} — release was removed</li>
     *   <li>{@code "prereleased"}, {@code "released"} (less common)</li>
     * </ul>
     * <p>Populated from the {@code action} field in GitHub's event payload.</p>
     */
    private String action;

    /**
     * ISO 8601 formatted timestamp indicating when the release event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;
}