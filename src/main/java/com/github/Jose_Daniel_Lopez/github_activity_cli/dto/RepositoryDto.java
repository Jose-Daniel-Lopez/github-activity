package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a GitHub repository.
 * <p>
 * Typically populated from GitHub's {@code /users/{username}/repos} API endpoint.
 * Captures key metadata such as name, description, language, stars, forks, and timestamps.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDto {

    /**
     * Short name of the repository (without owner prefix).
     * Example: "spring-boot-app"
     */
    private String name;

    /**
     * Full name of the repository in "owner/repo" format.
     * Example: "johndoe/spring-boot-app"
     */
    private String fullName;

    /**
     * Optional description of the repository provided by the owner.
     * May be {@code null} or empty if not set.
     * Example: "A demo Spring Boot application with REST API"
     */
    private String description;

    /**
     * Primary programming language of the repository, as detected by GitHub.
     * May be {@code null} if undetected or not applicable.
     * Example: "Java", "Python", "JavaScript"
     */
    private String language;

    /**
     * Number of stars (favorites) this repository has received.
     * Example: 142
     */
    private int stargazersCount;

    /**
     * Number of forks (copies) of this repository.
     * Example: 25
     */
    private int forksCount;

    /**
     * ISO 8601 formatted timestamp indicating when the repository was created.
     * Example: "2023-01-15T08:30:00Z"
     * <p>Populated from GitHub's {@code created_at} field.</p>
     */
    private String createdAt;

    /**
     * ISO 8601 formatted timestamp indicating when the repository was last updated.
     * Reflects the latest push or metadata change.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from GitHub's {@code updated_at} field.</p>
     */
    private String updatedAt;
}