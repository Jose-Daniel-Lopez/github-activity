package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class MemberEventDto {
    private String repoName;
    private String repoOwner;
    private String memberLogin;
    private String action;
    private String occurredAt;

    public MemberEventDto() {}

    public MemberEventDto(String repoName, String repoOwner, String memberLogin, String action, String occurredAt) {
        this.repoName = repoName;
        this.repoOwner = repoOwner;
        this.memberLogin = memberLogin;
        this.action = action;
        this.occurredAt = occurredAt;
    }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }
    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }
    public String getMemberLogin() { return memberLogin; }
    public void setMemberLogin(String memberLogin) { this.memberLogin = memberLogin; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }
}

