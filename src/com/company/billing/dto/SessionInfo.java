package com.company.billing.dto;

import java.util.ArrayDeque;

public class SessionInfo {
    private String userName;

    private int sessionCount;

    /**
     * Total Active Duration in seconds
     */
    private long totalActiveDuration;

    private ArrayDeque<ActivityInfo> ongoingActivities;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public long getTotalActiveDuration() {
        return totalActiveDuration;
    }

    public void setTotalActiveDuration(long totalActiveDuration) {
        this.totalActiveDuration = totalActiveDuration;
    }

    public ArrayDeque<ActivityInfo> getOngoingActivities() {
        return ongoingActivities;
    }

    public void setOngoingActivities(ArrayDeque<ActivityInfo> ongoingActivities) {
        this.ongoingActivities = ongoingActivities;
    }

    public SessionInfo(String userName, int sessionCount, long totalActiveDuration, ArrayDeque<ActivityInfo> ongoingActivities) {
        this.userName = userName;
        this.sessionCount = sessionCount;
        this.totalActiveDuration = totalActiveDuration;
        this.ongoingActivities = ongoingActivities;
    }
}