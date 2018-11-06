package Functional;

import java.util.Date;

public class Task
{
    private String taskName;
    private Course taskCourse;
    private Date taskDeadline;
    private Long taskTimeEst;
    private Long taskTimeSpent;
    private String taskDetail;
    private Boolean taskHighlight;

    // All get methods
    public String getName()
    {
        return taskName;
    }

    public Course getCourse()
    {
        return taskCourse;
    }

    public Date getDeadline()
    {
        return taskDeadline;
    }

    public Long getTimeEst()
    {
        return taskTimeEst;
    }

    public Long getTimeSpent()
    {
        return taskTimeSpent;
    }

    public String getDetail()
    {
        return taskDetail;
    }

    public Boolean getHighlight()
    {
        return taskHighlight;
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

    public void setDeadline(Date taskDeadline)
    {
        this.taskDeadline = taskDeadline;
    }

    public void setTimeEst(Long timeEst)
    {
        this.taskTimeEst = timeEst;
    }

    public void setTimeSpent(Long timeSpent)
    {
        this.taskTimeSpent = timeSpent;
    }

    public void setDetail(String taskDetail)
    {
        this.taskDetail = taskDetail;
    }

    public void setHighlight(Boolean taskHighlight)
    {
        this.taskHighlight = taskHighlight;
    }

    @Override
    public String toString()
    {
        return this.taskName;
    }
}
