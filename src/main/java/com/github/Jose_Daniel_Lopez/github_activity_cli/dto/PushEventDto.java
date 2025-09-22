package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a git push event from GitHub.
 * <p>
 * Derived from GitHub's {@code PushEvent}, triggered when a user pushes commits to a repository branch.
 * This DTO captures the repository context and the number of commits included in the push.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushEventDto {

    /**
     * Name of the repository where the push occurred.
     * Example: "backend-service"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "dev-team"
     */
    private String repoOwner;

    /**
     * Number of commits included in this push.
     * Corresponds to the size of the commits array in GitHub's PushEvent payload.
     * Example: 3 (if three commits were pushed together)
     */
    private int commitCount;

    /**
     * ISO 8601 formatted timestamp indicating when the push occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String pushedAt;
}