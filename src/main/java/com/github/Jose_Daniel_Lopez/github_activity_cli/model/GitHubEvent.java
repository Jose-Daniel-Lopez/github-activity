package com.github.Jose_Daniel_Lopez.github_activity_cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubEvent {
    private String type;

    @JsonProperty("repo")
    private Repo repo;

    @JsonProperty("payload")
    private Object payload;

    public Object getPayload() {
        return payload;
    }

    public String getType() {
        return type;
    }

    public Repo getRepo() {
        return repo;
    }
}