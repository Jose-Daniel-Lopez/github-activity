package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a repository collaborator membership event from GitHub.
 * <p>
 * Derived from GitHub's {@code MemberEvent}, triggered when a user is added, removed, or edited
 * as a collaborator on a repository.
 * </p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, this DTO is intended
 * to be effectively immutable after instantiation. Avoid modifying instances in multi-threaded contexts.</p>
 *
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberEventDto {

    /**
     * Name of the repository where the membership change occurred.
     * Example: "team-project"
     */
    private String repoName;

    /**
     * Owner (user or organization) of the repository.
     * Example: "acme-org"
     */
    private String repoOwner;

    /**
     * GitHub login/username of the collaborator who was added, removed, or edited.
     * Example: "janedoe"
     */
    private String memberLogin;

    /**
     * The action performed on the collaborator. Common values include:
     * <ul>
     *   <li>{@code "added"} — collaborator was added to the repo</li>
     *   <li>{@code "removed"} — collaborator was removed</li>
     *   <li>{@code "edited"} — collaborator's permissions were modified</li>
     * </ul>
     * <p>Populated from the {@code action} field in GitHub's event payload.</p>
     */
    private String action;

    /**
     * ISO 8601 formatted timestamp indicating when the membership event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>Populated from the {@code created_at} field of the GitHub event.</p>
     */
    private String occurredAt;
}