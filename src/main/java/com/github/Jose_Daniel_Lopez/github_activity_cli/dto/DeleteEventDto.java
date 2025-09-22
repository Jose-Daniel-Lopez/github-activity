package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class DeleteEventDto {
    private String repoName;
    private String repoOwner;
    private String refType;
    private String ref;
    private String occurredAt;

    public DeleteEventDto() {}

    public DeleteEventDto(String repoName, String repoOwner, String refType, String ref, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.refType = refType;
        this.ref = ref;
        this.occurredAt = occurredAt;
    }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }
    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }
    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}

