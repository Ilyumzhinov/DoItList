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
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    public static final int RESULT_REMOVE = 10;
    //
}