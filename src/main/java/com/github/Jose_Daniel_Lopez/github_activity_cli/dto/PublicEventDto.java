package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class PublicEventDto {
    private String repoName;
    private String repoOwner;
    private String occurredAt;

    public PublicEventDto() {}

    public PublicEventDto(String repoName, String repoOwner, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.occurredAt = occurredAt;
    }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}

