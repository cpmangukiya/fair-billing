import java.time.LocalTime;

/**
 * DTO/POJO - Carries session activity details
 */
public class ActivityInfo {

    /**
     * Activity time in HH:mm:ss format
     */
    private LocalTime activityTime;

    /**
     * User by which the activity is performed
     */
    private String userName;

    /**
     * Activity can be Start, End
     */
    private String activity;

    public LocalTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(LocalTime activityTime) {
        this.activityTime = activityTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public ActivityInfo(LocalTime activityTime, String userName, String activity) {
        this.activityTime = activityTime;
        this.userName = userName;
        this.activity = activity;
    }
}