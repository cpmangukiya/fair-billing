import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.SECONDS;

public class FairBilling {

    private static final String START_ACTIVITY = "Start";
    private static final String END_ACTIVITY = "End";

    public static void main(String[] args) {
        try {
            if (args == null || args.length != 1) {
                System.out.println("Desired input parameters could not be found. Please refer readme.md for more details !");
                return;
            }

            // Define and initialize needed variables
            String filePath = args[0];
            Map<String, SessionInfo> userSessionsInfo;

            // Process line by line using Java Files and Streams API
            ActivityInfo firstExtremeActivityInfo = new ActivityInfo(null, null, null);
            ActivityInfo lastExtremeActivityInfo = new ActivityInfo(null, null, null);

            try(Stream<String> streamOfLines = Files.lines(Paths.get(filePath))) {
                userSessionsInfo = streamOfLines
                        .filter(FairBilling::validateActivity)
                        .map(FairBilling::parseAndConvertToActivityInfo)
                        .reduce(new HashMap<>(), (sessionMap, currentActivityInfo) ->
                                FairBilling.storeAndCalculateStatsForEachActivity(currentActivityInfo, sessionMap,
                                firstExtremeActivityInfo, lastExtremeActivityInfo), FairBilling::combineHashMap);

                FairBilling.storeAndCalculateStatsForOrphanedActivities(userSessionsInfo, firstExtremeActivityInfo, lastExtremeActivityInfo);
            } catch (IOException e) {
                System.out.println("Error! File could not be read. Requested file was : " + e.getMessage());
                return;
            }

            if(userSessionsInfo.isEmpty()) {
                System.out.println("Error! No valid activity details found");
                return;
            }

            // Print the result - Output
            userSessionsInfo.forEach((userName, sessionInfo) -> System.out.println(userName + " " +
                    sessionInfo.getSessionCount() + " " +
                    sessionInfo.getTotalActiveDuration()));

        } catch (Exception e) {
            System.out.println("Error! " + e.getMessage());
        }
    }

    private static boolean validateActivity(String line) {
        if(line != null && !line.trim().isEmpty()) {
            String[] itemsOfLine = line.split(" "); // Split line with blank space to find activity time, user name, activity(Start and Stop)
            if(itemsOfLine.length == 3){
                LocalTime activityTime;
                String userName;
                String activity;

                // Validate date as per HH:mm:ss format
                try {
                    DateTimeFormatter strictTimeFormatter = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss")
                            .toFormatter()
                            .withResolverStyle(ResolverStyle.STRICT);
                    activityTime = LocalTime.parse(itemsOfLine[0], strictTimeFormatter);
                } catch (DateTimeParseException | NullPointerException e) {
                    activityTime = null;
                }

                // Validate value is not empty
                userName = itemsOfLine[1].isEmpty() ? null : itemsOfLine[1] ;

                // Validate value is not empty and must be either 'Start' or 'Stop'
                activity = START_ACTIVITY.equals(itemsOfLine[2]) || END_ACTIVITY.equals(itemsOfLine[2]) ? itemsOfLine[2] : null;

                return activityTime != null && userName != null && activity != null;
            }
        }
        return false;
    }

    private static ActivityInfo parseAndConvertToActivityInfo(String line) {
        String[] itemsOfLine = line.split(" ");
        LocalTime activityTime = LocalTime.parse(itemsOfLine[0], new DateTimeFormatterBuilder().appendPattern("HH:mm:ss")
                                                                    .toFormatter()
                                                                    .withResolverStyle(ResolverStyle.STRICT));
        String userName = itemsOfLine[1];
        String activity = itemsOfLine[2];
        return new ActivityInfo(activityTime, userName, activity);
    }

    private static Map<String, SessionInfo> storeAndCalculateStatsForEachActivity(ActivityInfo currentActivityInfo, Map<String, SessionInfo> sessionInfoMap,
                                                              ActivityInfo firstExtremeActivityInfo, ActivityInfo lastExtremeActivityInfo) {
        // firstExtremeActivityInfo will be filled for first valid activity only. This shall be used for deciding session time on orphaned activities.
        if(firstExtremeActivityInfo.getActivity() == null) {
            firstExtremeActivityInfo.setActivity(currentActivityInfo.getActivity());
            firstExtremeActivityInfo.setActivityTime(currentActivityInfo.getActivityTime());
            firstExtremeActivityInfo.setUserName(currentActivityInfo.getUserName());
        }

        // This will be executed for each time valid activity is found and shall point to last valid entry at the end of execution. This shall be used for decideing session time on orphaned attributes.
        lastExtremeActivityInfo.setActivity(currentActivityInfo.getActivity());
        lastExtremeActivityInfo.setActivityTime(currentActivityInfo.getActivityTime());
        lastExtremeActivityInfo.setUserName(currentActivityInfo.getUserName());

        if(sessionInfoMap.containsKey(currentActivityInfo.getUserName())){
            SessionInfo sessionInfo = sessionInfoMap.get(currentActivityInfo.getUserName());
            ArrayDeque<ActivityInfo> activityInfoQueue = sessionInfo.getOngoingActivities();

            if(activityInfoQueue.isEmpty() || activityInfoQueue.peekLast().getActivity().equals(currentActivityInfo.getActivity())) {
                // If same activity as last then add up in the queue
                activityInfoQueue.addLast(currentActivityInfo);
            } else if(END_ACTIVITY.equals(currentActivityInfo.getActivity())) {
                // If End is followed by Start, then poll first entry from the ongoing operation queue
                // and process the duration and increase session count by 1
                ActivityInfo foundActivityInfo = activityInfoQueue.pollFirst();
                assert foundActivityInfo != null;
                long duration = SECONDS.between(foundActivityInfo.getActivityTime(), currentActivityInfo.getActivityTime());
                sessionInfo.setTotalActiveDuration(sessionInfo.getTotalActiveDuration() + duration);
                sessionInfo.setSessionCount(sessionInfo.getSessionCount() + 1);
            } else if(START_ACTIVITY.equals(currentActivityInfo.getActivity())) {
                // If Start is followed by End, then poll all End operations from the ongoing operation queue
                // and process the duration and increased session count by 1 for each
                activityInfoQueue.forEach(activityInfo -> {
                    assert activityInfo != null;
                    long duration = SECONDS.between(firstExtremeActivityInfo.getActivityTime(), activityInfo.getActivityTime());
                    sessionInfo.setTotalActiveDuration(sessionInfo.getTotalActiveDuration() + duration);
                    sessionInfo.setSessionCount(sessionInfo.getSessionCount() + 1);
                });
                activityInfoQueue.clear();
                activityInfoQueue.addLast(currentActivityInfo);
            }

            sessionInfoMap.put(currentActivityInfo.getUserName(), sessionInfo);
        } else {
            // Save user name as key and first activity details. Session Count is initialized with 0 and duration as 0(seconds).
            ArrayDeque<ActivityInfo> activityInfoQueue = new ArrayDeque<>();
            activityInfoQueue.addLast(currentActivityInfo);
            sessionInfoMap.put(currentActivityInfo.getUserName(), new SessionInfo(currentActivityInfo.getUserName(),
                    0, 0L,  activityInfoQueue));
        }

        return sessionInfoMap;
    }

    private static void storeAndCalculateStatsForOrphanedActivities(Map<String, SessionInfo> sessionInfoMap,
                                                                    ActivityInfo firstExtremeActivityInfo, ActivityInfo lastExtremeActivityInfo) {
        sessionInfoMap.forEach((userName, sessionInfo) -> {
            if(!sessionInfo.getOngoingActivities().isEmpty()) {

                // For each remaining Start and End activities for which there is no clear End or Start activity logged.
                // Compare each with extreme end activity (firstExtreme[for End activity] or lastExtreme[for Start activity])
                // and calculate session and duration count and add them up.
                sessionInfo.getOngoingActivities().forEach(activityInfo -> {
                    assert activityInfo != null;
                    if(END_ACTIVITY.equals(activityInfo.getActivity())) {
                        long duration = SECONDS.between(firstExtremeActivityInfo.getActivityTime(), activityInfo.getActivityTime());
                        sessionInfo.setTotalActiveDuration(sessionInfo.getTotalActiveDuration() + duration);
                    } else if(START_ACTIVITY.equals(activityInfo.getActivity()))  {
                        long duration = SECONDS.between(activityInfo.getActivityTime(), lastExtremeActivityInfo.getActivityTime());
                        sessionInfo.setTotalActiveDuration(sessionInfo.getTotalActiveDuration() + duration);
                    }
                    sessionInfo.setSessionCount(sessionInfo.getSessionCount() + 1);
                });

                sessionInfo.getOngoingActivities().clear();
            }
        });
    }

    private static Map<String, SessionInfo> combineHashMap(Map<String, SessionInfo> sessionMapOne, Map<String, SessionInfo> sessionMapTwo) {
        // This method has no significance since this is used by Streams APIs to infer different object type other then items being streamed.
        // Hence types of signature and return types matters here. Actual operation is carried by storeAndCalculateStatsForEachActivity method.
        return null;
    }
}