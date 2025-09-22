package com.github.Jose_Daniel_Lopez.github_activity_cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubEvent {
    private String type;

    @JsonProperty("repo")
    private Repo repo;

    @JsonProperty("payload")
    private Object payload;

    @JsonProperty("created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String created_at;
}