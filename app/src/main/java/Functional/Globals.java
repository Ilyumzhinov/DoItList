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
    public static final String ViewMode_NewWithParent = "newWithParent";
    //
}