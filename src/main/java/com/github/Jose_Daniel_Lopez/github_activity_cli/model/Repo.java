package com.github.Jose_Daniel_Lopez.github_activity_cli.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
public class Repo {

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }
}
