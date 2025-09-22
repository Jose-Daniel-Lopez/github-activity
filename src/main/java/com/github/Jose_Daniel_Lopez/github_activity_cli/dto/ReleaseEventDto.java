package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class ReleaseEventDto {
    private String repoName;
    private String repoOwner;
    private String releaseName;
    private String action;
    private String occurredAt;

    public ReleaseEventDto() {}

    public ReleaseEventDto(String repoName, String repoOwner, String releaseName, String action, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.releaseName = releaseName;
        this.action = action;
        this.occurredAt = occurredAt;
    }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }
    public String getReleaseName() { return releaseName; }
    public void setReleaseName(String releaseName) { this.releaseName = releaseName; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}

