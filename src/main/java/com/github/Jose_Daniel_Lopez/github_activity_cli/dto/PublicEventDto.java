package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a repository publicization event from GitHub.
 * <p>
 * Derived from GitHub's {@code PublicEvent}, which is triggered when a user changes a repository's
 * visibility from private to public.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicEventDto {

    /**
     * Name of the repository that was made public.
     * Example: "my-private-project"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "johndoe"
     */
    private String repoOwner;

    /**
     * ISO 8601 formatted timestamp indicating when the repository was made public.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;
}