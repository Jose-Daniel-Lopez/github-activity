package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a reference deletion event from GitHub.
 * <p>
 * Derived from GitHub's {@code DeleteEvent}, which is triggered when a user deletes a branch or tag.
 * Note: Repository deletions do not trigger this event.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteEventDto {

    /**
     * Name of the repository where the deletion occurred.
     * Example: "my-project"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "johndoe"
     */
    private String repoOwner;

    /**
     * Type of reference that was deleted. Possible values:
     * <ul>
     *   <li>{@code "branch"} — a branch was deleted</li>
     *   <li>{@code "tag"} — a tag was deleted</li>
     * </ul>
     * <p><strong>Note:</strong> Repository deletions do not generate {@code DeleteEvent}.</p>
     */
    private String refType;

    /**
     * Name of the reference that was deleted (branch or tag name).
     * Example: "feature/old-branch", "v0.9-beta"
     */
    private String ref;

    /**
     * ISO 8601 formatted timestamp indicating when the deletion event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;
}