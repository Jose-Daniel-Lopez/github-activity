package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class IssueEventDto {
    private String repoName;
    private String repoOwner;
    private String issueTitle;
    private String action;
    private String occurredAt;

    public IssueEventDto() {}

    public IssueEventDto(String repoName, String repoOwner, String issueTitle, String action, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.issueTitle = issueTitle;
        this.action = action;
        this.occurredAt = occurredAt;
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

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(String occurredAt) {
        this.occurredAt = occurredAt;
    }
}

