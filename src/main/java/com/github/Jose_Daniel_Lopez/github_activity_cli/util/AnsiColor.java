package com.github.Jose_Daniel_Lopez.github_activity_cli.util;

public class AnsiColor {

    // Reset
    public static final String RESET = "\u001B[0m";

    // Regular Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bold + Colors
    public static final String BOLD_GREEN = "\u001B[1;32m";
    public static final String BOLD_YELLOW = "\u001B[1;33m";
    public static final String BOLD_BLUE = "\u001B[1;34m";
    public static final String BOLD_RED = "\u001B[1;31m";
    public static final String BOLD_PURPLE = "\u001B[1;35m";

    // Icons (optional - some consoles may not support emojis)
    public static final String ICON_PUSH = "🟢 ";
    public static final String ICON_STAR = "⭐ ";
    public static final String ICON_ISSUE = "🐛 ";
    public static final String ICON_PR = "📬 ";
    public static final String ICON_FORK = "🍴 ";
    public static final String ICON_DEFAULT = "📌 ";
}
