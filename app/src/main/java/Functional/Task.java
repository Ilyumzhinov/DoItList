package Functional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.Duration;

public class Task {

    private String taskName;
    private Course taskCourse;
    private Date taskDeadline;
    private Duration timeEst;
    private Duration timeSpent;
    private String taskDetail;

    // All get methods
    public String getTaskName() {
        return taskName;
    }

    public Course getTaskCourse() {
        return taskCourse;
    }

    public Date getTaskDeadline() {
        return taskDeadline;
    }

    public Duration getTimeEst() {
        return timeEst;
    }

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    // All set methods

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskCourse(Course taskCourse) {
        this.taskCourse = taskCourse;
    }

    public void setTaskDeadline(Date taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTimeEst(Duration timeEst) {
        this.timeEst = timeEst;
    }

    public void setTimeSpent(Duration timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    @Override
    public String toString()
    {
        return this.taskName;
    }
}
