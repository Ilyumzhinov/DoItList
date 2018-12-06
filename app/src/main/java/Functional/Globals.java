package Functional;

import android.app.Activity;
import android.text.format.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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
    public static String formatTimeTotal(Long xMinutes)
    {
        String returnMinutes;
        if (xMinutes >= 1440) {
            // If time is longer than a day
            returnMinutes = xMinutes/24/60 + " D, " + xMinutes/60%24 + " H, " + xMinutes%60 + " min";
        } else if (xMinutes < 60) {
            // If time is shorter than a day
           returnMinutes = xMinutes%60 + " min";
        } else {
            returnMinutes = xMinutes/60%24 + " H, " + xMinutes%60 + " min";
        }
        if (returnMinutes.contains("-")) {
            returnMinutes = returnMinutes.replaceAll("-", "");
            returnMinutes += " ago";
        }
        return returnMinutes;
    }

    // Receive a date and parse it into a string of that date or one of special cases, like “Today”, “Tomorrow”, “Yesterday”.
    // E.g. (input) -> “Tomorrow, 23:59”.
    // E.g. (input) -> “12-11-2018, 23:59”.
    public static String formatDate(LocalDateTime xLocalDate)
    {
        // Reference: https://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string
        // Grab local time
        Date currentTime = Calendar.getInstance().getTime();
        // Convert it to localdatetime so everything is the same
        LocalDateTime localCurrentTime = currentTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter;
        String formattedString;
        // compare dates to see if its today, tomorrow, or yesterday
        if (xLocalDate.getDayOfYear()-1 == localCurrentTime.getDayOfYear()) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            formattedString = "Tomorrow at " + xLocalDate.format(formatter);
        } else if (xLocalDate.getDayOfYear() == localCurrentTime.getDayOfYear()) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            formattedString = "Today at " + xLocalDate.format(formatter);
        } else if (xLocalDate.getDayOfYear()+1 == localCurrentTime.getDayOfYear()) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            formattedString = "Yesterday at " + xLocalDate.format(formatter);
        } else {
            formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy, HH:mm");
            formattedString = xLocalDate.format(formatter);
        }
        return formattedString;
    }
}