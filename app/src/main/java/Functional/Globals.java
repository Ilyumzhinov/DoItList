package Functional;

import android.app.Activity;

public class Globals
{
    // Single instance with global constant values and funcitons
    // Reference: https://androidresearch.wordpress.com/2012/03/22/defining-global-variables-in-android/

    private static final Globals ourInstance = new Globals();

    public static Globals getInstance()
    {
        return ourInstance;
    }

    private Globals()
    {
    }

    // GLOBAL VALUES
    // Intent request codes
    public static final int NewCourseRequestCode = 1, ManageTaskRequestCode = 2, OpenTaskRequestCode = 3, SessionsHistoryRequestCode = 4;

    // Intent result codes
    // Return when no data was changed
    public static final int RESULT_OK = Activity.RESULT_OK;
    // ???
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    // Return when data was changed, save it
    public static final int RESULT_SAVE = 9;

    // Intent extra keys
    public static final String ExtraKey_ViewMode = "viewMode";
    public static final String ExtraKey_Parent = "parent";
    public static final String ExtraKey_Courses = "courses";
    public static final String ExtraKey_Tasks = "tasks";
    public static final String ExtraKey_Index = "index";
    public static final String ExtraKey_Sessions = "sessions";

    // Intent extra values for viewMode key
    public static final String ViewMode_New = "new";
    public static final String ViewMode_Edit = "edit";
    //

    // Receive minutes as integer and parse them into a string of # days, # hours, # minutes.
    // E.g.: 110 -> “1 h, 50 min”
    public static String formatTimeTotal(Integer xMinutes)
    {
        // PLACEHOLDER LOGIC
        return String.valueOf(xMinutes) + " min";
    }

    // Receive minutes as long and parse them into a string of a date or one of special cases, like “Today”, “Tomorrow”, “Yesterday”.
    // E.g. 1440 -> “Tomorrow at 23:59”.
    // E.g. 2: 10080 -> “12-11-2018, 23:59”.
    public static String formatDate(Long xMinutes)
    {
        // PLACEHOLDER LOGIC
        return String.valueOf(xMinutes) + " min";
    }
}