package com.github.Jose_Daniel_Lopez.github_activity_cli.model;

public class IssuePayload {

    private String action;
    private Issue issue;
    private String size;

    public String getAction() {
        return action;
    }

    public Issue getIssue() {
        return issue;
    }

    public String getSize() {
        return size;
    }

    public static class Issue {
        private int number;
        private String title;

        public int getNumber() {
            return number;
        }

        public String getTitle() {
            return title;
        }
    }
}
