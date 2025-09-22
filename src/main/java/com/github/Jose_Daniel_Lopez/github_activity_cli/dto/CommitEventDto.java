package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class CommitEventDto {
    private String repoName;
    private String repoOwner;
    private int commitCount;
    private String pushedAt;

    public CommitEventDto() {}

    public CommitEventDto(String repoName, String repoOwner, int commitCount, String pushedAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.commitCount = commitCount;
        this.pushedAt = pushedAt;
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

    public int getCommitCount() {
        return commitCount;
    }

    public void setCommitCount(int commitCount) {
        this.commitCount = commitCount;
    }

    public String getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(String pushedAt) {
        this.pushedAt = pushedAt;
    }
}

