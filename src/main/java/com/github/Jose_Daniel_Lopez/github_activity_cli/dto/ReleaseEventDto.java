package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseEventDto {
    private String repoName;
    private String repoOwner;
    private String releaseName;
    private String action;
    private String occurredAt;
}

