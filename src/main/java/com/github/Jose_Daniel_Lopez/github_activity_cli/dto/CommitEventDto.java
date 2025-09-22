package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a commit event derived from a GitHub PushEvent.
 * <p>
 * This DTO captures the essential information about a push (commit) event, including
 * repository details, number of commits, and timestamp.
 * </p>
 * <p><strong>Note:</strong> Although mutable (due to Lombok-generated setters), this class is intended
 * to be effectively immutable after construction in typical usage (e.g., REST responses).
 * Avoid modifying instances after creation in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommitEventDto {

    /**
     * Name of the repository where the push occurred.
     * Example: "spring-boot-project"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "spring-projects"
     */
    private String repoOwner;

    /**
     * Number of commits included in this push event.
     * Corresponds to the size of the commits array in GitHub's PushEvent payload.
     */
    private int commitCount;

    /**
     * ISO 8601 formatted timestamp indicating when the push occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String pushedAt;

    public String getCreatedAt() {
        return pushedAt;
    }
}