package Functional;

import java.time.LocalDateTime;

public class Session
{
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public void openSession(LocalDateTime localDateTime)
    {
        this.startDate = localDateTime;
    }

    public void endSession(LocalDateTime localDateTime)
    {
        this.endDate = localDateTime;
    }

    public LocalDateTime getStartDate()
    {
        return startDate;
    }

    public LocalDateTime getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate)
    {
        this.endDate = endDate;
    }

    public void setStartDate(LocalDateTime startDate)
    {

        this.startDate = startDate;
    }
}
