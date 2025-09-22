package com.github.Jose_Daniel_Lopez.github_activity_cli.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

/**
 * Domain model representing a raw GitHub event as returned by the GitHub Events API.
 * <p>
 * This class is used for direct JSON deserialization of event data from GitHub.
 * It is not intended for direct display — downstream services should transform it into specific DTOs
 * (e.g., {@code CommitEventDto}, {@code IssueEventDto}) based on {@code type}.
 * </p>
 * <p><strong>Defensive Design:</strong> Ignores unknown JSON properties to remain compatible
 * with future GitHub API changes.</p>
 * <p><strong>Note:</strong> Although setters are generated via Lombok, instances of this class
 * are typically treated as immutable after deserialization. Avoid modifying instances in multi-threaded contexts.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Defensive: ignore new fields GitHub may add
public class GitHubEvent {

    /**
     * Type of GitHub event (e.g., "PushEvent", "IssuesEvent", "ForkEvent").
     * Used to determine how to interpret the {@code payload} field.
     * <p>Example: "PushEvent"</p>
     */
    private String type;

    /**
     * Repository context for the event.
     * Contains {@code name} (e.g., "spring-boot") and {@code url}.
     * <p>JSON field: {@code "repo"}</p>
     */
    @JsonProperty("repo")
    private Repo repo;

    /**
     * Event-specific payload data. Structure varies depending on {@code type}.
     * <p>Examples:
     * <ul>
     *   <li>For {@code PushEvent}: contains commits, ref, etc.</li>
     *   <li>For {@code IssuesEvent}: contains issue title, action, etc.</li>
     * </ul>
     * </p>
     * <p>JSON field: {@code "payload"}</p>
     * <p><strong>Note:</strong> Deserialized as generic {@code Object} — cast or convert downstream based on {@code type}.</p>
     */
    @JsonProperty("payload")
    private Object payload;

    /**
     * ISO 8601 timestamp indicating when the event occurred.
     * Example: "2025-04-01T12:34:56Z"
     * <p>JSON field: {@code "created_at"}</p>
     * <p><strong>Note:</strong> {@code @DateTimeFormat} is a Spring MVC annotation and has no effect during
     * Jackson deserialization. This field remains a {@code String} for compatibility.</p>
     */
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private String createdAt;
}