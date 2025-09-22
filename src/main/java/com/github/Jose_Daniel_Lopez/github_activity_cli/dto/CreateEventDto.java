package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a repository or reference creation event from GitHub.
 * <p>
 * Derived from GitHub's {@code CreateEvent}, which is triggered when a user creates a repository,
 * branch, or tag. This DTO captures key metadata about the creation event.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDto {

    /**
     * Name of the repository where the creation occurred.
     * Example: "my-new-project"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "johndoe"
     */
    private String repoOwner;

    /**
     * Type of reference that was created. Possible values:
     * <ul>
     *   <li>{@code "repository"} — entire repo was created</li>
     *   <li>{@code "branch"} — a new branch was created</li>
     *   <li>{@code "tag"} — a new tag was created</li>
     * </ul>
     */
    private String refType;

    /**
     * Name of the reference that was created (branch name, tag name, or null if repo creation).
     * Example: "feature/login" (for branch), "v1.0.0" (for tag)
     */
    private String ref;

    /**
     * ISO 8601 formatted timestamp indicating when the creation event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;
}