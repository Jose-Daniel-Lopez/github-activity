package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDto {
    private String name;
    private String fullName;
    private String description;
    private String language;
    private int stargazersCount;
    private int forksCount;
    private String createdAt;
    private String updatedAt;
}
