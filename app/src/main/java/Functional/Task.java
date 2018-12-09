package Functional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Task implements Serializable
{
    private String taskName;
    private Course taskCourse;
    private LocalDateTime taskDateAdded;
    private LocalDateTime taskDeadline;
    private Long taskTimeGoal;
    private String taskDetail;
    private SessionsControl taskSessions;
    private Boolean taskStatusFinished;

    // Constructors
    Task()
    {
        // Initialise attributes
        taskSessions = new SessionsControl();
    }

    // All get methods
    public String getName()
    {
        return taskName;
    }

    public Course getCourse()
    {
        return taskCourse;
    }

    public LocalDateTime getDeadline()
    {
        return taskDeadline;
    }

    public LocalDateTime set()
    {
        return taskDateAdded;
    }

    public Long getTimeGoal()
    {
        return taskTimeGoal;
    }

    public Long getTimeSpent()
    {
        return taskSessions.getGrandTotal();
    }

    public Long getAvgSessionLength()
    {
        return taskSessions.getAvgLength();
    }

    public String getDetail()
    {
        return taskDetail;
    }

    public Boolean getStatusFinished()
    {
        return taskStatusFinished;
    }

    public String getStatusStr()
    {
        String status;

        if (getStatusFinished())
            status = "Finished";
        else
        {
            if (getSessions().getSessions().length == 0)
                status = "Not started";
            else if (getSessions().checkOpenSession())
                status = "Active";
            else
                status = "Not finished";
        }

        return status;
    }

    // Get total estimated time remaining which is based time goal minus spent time
    // If task is completed, returns 0
    public Long getTimeRemainEst()
    {
        if (taskStatusFinished)
            return 0L;

        Long timeSpent = getTimeSpent();

        return taskTimeGoal - timeSpent;
    }

    // Calculate time from now until deadline
    public Long getTimeBeforeDeadline()
    {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), taskDeadline);
    }

    public SessionsControl getSessions()
    {
        return taskSessions;
    }

    // All set methods
    public void setName(String taskName)
    {
        this.taskName = taskName;
    }

    public void setCourse(Course taskCourse)
    {
        this.taskCourse = taskCourse;
    }

    public void setDeadline(LocalDateTime taskDeadline)
    {
        this.taskDeadline = taskDeadline;
    }

    public void setDateAdded(LocalDateTime taskDateAdded)
    {
        this.taskDateAdded = taskDateAdded;
    }

    public void setTimeGoal(Long timeEst)
    {
        this.taskTimeGoal = timeEst;
    }

    public void updateRecordingTime()
    {
        if (this.taskStatusFinished)
            return;

        this.taskSessions.updateSession();
    }

    public void addSession(Session session)
    {
        this.taskSessions.addSession(session);
    }

    public void setDetail(String taskDetail)
    {
        this.taskDetail = taskDetail;
    }

    public void setStatusFinished(Boolean taskStatusFinished)
    {
        this.taskStatusFinished = taskStatusFinished;

        if (this.taskStatusFinished && this.taskSessions.checkOpenSession())
            this.taskSessions.updateSession();
    }

    @Override
    public String toString()
    {
        return this.taskName;
    }
}