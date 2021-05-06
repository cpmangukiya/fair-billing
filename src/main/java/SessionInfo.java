import java.util.Queue;

/**
 * DTO/POJO - Carries session count and duration details for a user
 */
public class SessionInfo {

    /**
     * Use for which session details recorded
     */
    private String userName;

    /**
     * Total session count of user's activities
     */
    private int sessionCount;

    /**
     * Total Active Duration in seconds
     */
    private long totalActiveDuration;

    /**
     * Activity details are stored in this for processing. For intermittent use.
     */
    private Queue<ActivityInfo> ongoingActivities;

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

    public Queue<ActivityInfo> getOngoingActivities() {
        return ongoingActivities;
    }

    public void setOngoingActivities(Queue<ActivityInfo> ongoingActivities) {
        this.ongoingActivities = ongoingActivities;
    }

    public SessionInfo(String userName, int sessionCount, long totalActiveDuration, Queue<ActivityInfo> ongoingActivities) {
        this.userName = userName;
        this.sessionCount = sessionCount;
        this.totalActiveDuration = totalActiveDuration;
        this.ongoingActivities = ongoingActivities;
    }
}