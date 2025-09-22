package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class StarEventDto {
    private String repoName;
    private String repoOwner;
    private String starredAt;

    public StarEventDto() {}

    public StarEventDto(String repoName, String repoOwner, String starredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.starredAt = starredAt;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoOwner() {
        return repoOwner;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public String getStarredAt() {
        return starredAt;
    }

    public void setStarredAt(String starredAt) {
        this.starredAt = starredAt;
    }
}

