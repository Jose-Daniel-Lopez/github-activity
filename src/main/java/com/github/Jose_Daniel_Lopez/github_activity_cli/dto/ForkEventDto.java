package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForkEventDto {
    private String repoName;
    private String repoOwner;
    private String forkedRepoName;
    private String occurredAt;
}

