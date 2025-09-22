package com.github.Jose_Daniel_Lopez.github_activity_cli.dto;

public class RepositoryDto {
    private String name;
    private String fullName;
    private String description;
    private String language;
    private int stargazersCount;
    private int forksCount;
    private String createdAt;
    private String updatedAt;

    public RepositoryDto() {}

    public RepositoryDto(String name, String fullName, String description, String language,
                        int stargazersCount, int forksCount, String createdAt, String updatedAt) {
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.language = language;
        this.stargazersCount = stargazersCount;
        this.forksCount = forksCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public int getStargazersCount() { return stargazersCount; }
    public void setStargazersCount(int stargazersCount) { this.stargazersCount = stargazersCount; }
    public int getForksCount() { return forksCount; }
    public void setForksCount(int forksCount) { this.forksCount = forksCount; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
