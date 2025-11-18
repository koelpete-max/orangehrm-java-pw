package com.example.pages.main;

public enum SidePanelItem {
    SEARCH {
        @Override public String toString() { return "Search"; }
    },
    ADMIN {
        @Override public String toString() { return "Admin"; }
    },
    PIM,
    LEAVE {
        @Override public String toString() { return "Leave"; }
    },
    TIME {
        @Override public String toString() { return "Time"; }
    },
    RECRUITMENT {
        @Override public String toString() { return "Recruitment"; }
    },
    MYINFO {
        @Override public String toString() { return "My Info"; }
    },
    PERFORMANCE {
        @Override public String toString() { return "Performance"; }
    },
    DASHBOARD {
        @Override public String toString() { return "Dashboard"; }
    },
    DIRECTORY {
        @Override public String toString() { return "Directory"; }
    },
    MAINTENANCE {
        @Override public String toString() { return "Maintenance"; }
    },
    CLAIM {
        @Override public String toString() { return "Claim"; }
    },
    BUZZ {
        @Override public String toString() { return "Buzz"; }
    }
}
