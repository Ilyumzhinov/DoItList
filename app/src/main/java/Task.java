import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Task {

    private String taskName;
    private String taskCourse;
    private SimpleDateFormat taskStart;
    private SimpleDateFormat taskDeadline;
    private SimpleDateFormat timeSpent;
    private String taskDetail;

    /* Constructor for a new task, sets all variables as well as time spent to 0 */
    Task(String name, String course, SimpleDateFormat start, SimpleDateFormat deadline, String detail) {
        taskName = name;
        taskCourse = course;
        taskStart = start;
        taskDeadline = deadline;
        taskDetail = detail;
        // Set total time spent to 0, catch parse error which shouldn't be an issue.
        try {
            timeSpent.parse("00:00");
        } catch (ParseException e) {
            return;
        }
    }

    // All get methods
    public String getTaskName() {
        return taskName;
    }

    public String getTaskCourse() {
        return taskCourse;
    }

    public SimpleDateFormat getTaskStart() {
        return taskStart;
    }

    public SimpleDateFormat getTaskDeadline() {
        return taskDeadline;
    }

    public SimpleDateFormat getTimeSpent() {
        return timeSpent;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    // All set methods

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskCourse(String taskCourse) {
        this.taskCourse = taskCourse;
    }

    public void setTaskStart(SimpleDateFormat taskStart) {
        this.taskStart = taskStart;
    }

    public void setTaskDeadline(SimpleDateFormat taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTimeSpent(SimpleDateFormat timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }
}
