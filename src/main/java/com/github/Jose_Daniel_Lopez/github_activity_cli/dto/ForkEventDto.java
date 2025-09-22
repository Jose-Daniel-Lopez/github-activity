package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a repository fork event from GitHub.
 * <p>
 * Derived from GitHub's {@code ForkEvent}, which is triggered when a user forks a repository.
 * This DTO captures the original repository and the newly created fork.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForkEventDto {

    /**
     * Name of the original (source) repository that was forked.
     * Example: "spring-boot"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the original repository.
     * Example: "spring-projects"
     */
    private String repoOwner;

    /**
     * Name of the newly created forked repository (usually same as source, but under forker's namespace).
     * Example: "my-spring-boot-fork"
     */
    private String forkedRepoName;

    /**
     * ISO 8601 formatted timestamp indicating when the fork event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;
}