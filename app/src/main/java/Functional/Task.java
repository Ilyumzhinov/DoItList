package Functional;

import java.time.LocalDateTime;

public class Task
{
    private String taskName;
    private Course taskCourse;
    private LocalDateTime taskDateAdded;
    private LocalDateTime taskDeadline;
    private Integer taskTimeEst;
    private String taskDetail;
    private Boolean taskHighlight;
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

    public Boolean getHighlight()
    {
        return taskHighlight;
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

    public void startRecordingTime()
    {
        this.taskSessions.openSession();
    }

    public void stopRecordingTime()
    {
        this.taskSessions.closeSession();
    }

    public void addSession(Session session)
    {
        this.taskSessions.addSession(session);
    }

    public void setDetail(String taskDetail)
    {
        this.taskDetail = taskDetail;
    }

    public void setHighlight(Boolean taskHighlight)
    {
        this.taskHighlight = taskHighlight;
    }

    public void setStatusFinished(Boolean taskStatusFinished)
    {
        this.taskStatusFinished = taskStatusFinished;
    }

    @Override
    public String toString()
    {
        return this.taskName;
    }
}