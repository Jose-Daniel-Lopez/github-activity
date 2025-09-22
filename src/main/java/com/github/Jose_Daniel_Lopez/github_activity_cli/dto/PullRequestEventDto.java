package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class PullRequestEventDto {
    private String repoName;
    private String repoOwner;
    private String prTitle;
    private String action;
    private String occurredAt;

    public PullRequestEventDto() {}

    public PullRequestEventDto(String repoName, String repoOwner, String prTitle, String action, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.prTitle = prTitle;
        this.action = action;
        this.occurredAt = occurredAt;
    }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }
    public String getPrTitle() { return prTitle; }
    public void setPrTitle(String prTitle) { this.prTitle = prTitle; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}

