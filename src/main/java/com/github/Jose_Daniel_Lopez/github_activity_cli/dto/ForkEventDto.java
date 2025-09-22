package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class ForkEventDto {
    private String repoName;
    private String repoOwner;
    private String forkedRepoName;
    private String occurredAt;

    public ForkEventDto() {}

    public ForkEventDto(String repoName, String repoOwner, String forkedRepoName, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.forkedRepoName = forkedRepoName;
        this.occurredAt = occurredAt;
    }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }
    public String getForkedRepoName() { return forkedRepoName; }
    public void setForkedRepoName(String forkedRepoName) { this.forkedRepoName = forkedRepoName; }
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}

