package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a repository star event by a user on GitHub.
 * <p>
 * Typically populated from GitHub's {@code /users/{username}/starred} API endpoint,
 * which returns repositories starred by the user, along with the timestamp of when the star was given.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StarEventDto {

    /**
     * Name of the repository that was starred.
     * Example: "react"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "facebook"
     */
    private String repoOwner;

    /**
     * ISO 8601 formatted timestamp indicating when the repository was starred by the user.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code starred_at} field in GitHub's starred repositories API response.</p>
     */
    private String starredAt;
}