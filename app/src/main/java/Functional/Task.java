package Functional;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable
{
    private String taskName;
    private Course taskCourse;
    private LocalDateTime taskDateAdded;
    private LocalDateTime taskDeadline;
    private Integer taskTimeEst;
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

    public LocalDateTime getDateAdded()
    {
        return taskDateAdded;
    }

    public Integer getTimeEst()
    {
        return taskTimeEst;
    }

    public Integer getTimeSpent()
    {
        return taskSessions.getGrandTotal();
    }

    public String getDetail()
    {
        return taskDetail;
    }

    public Boolean getStatusFinished()
    {
        return taskStatusFinished;
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

    public void setTimeEst(Integer timeEst)
    {
        this.taskTimeEst = timeEst;
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