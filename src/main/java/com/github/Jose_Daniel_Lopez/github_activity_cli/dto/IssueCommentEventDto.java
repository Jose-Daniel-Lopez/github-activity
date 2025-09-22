package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class IssueCommentEventDto {
    private String repoName;
    private String repoOwner;
    private String commentBody;
    private String occurredAt;

    public IssueCommentEventDto() {}

    public IssueCommentEventDto(String repoName, String repoOwner, String commentBody, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.commentBody = commentBody;
        this.occurredAt = occurredAt;
    }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }
    public String getCommentBody() { return commentBody; }
    public void setCommentBody(String commentBody) { this.commentBody = commentBody; }
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}

