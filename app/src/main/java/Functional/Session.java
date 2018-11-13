package Functional;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Session implements Serializable
{
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public void setStartDate(LocalDateTime localDateTime)
    {
        this.startDate = localDateTime;
    }

    public void setEndDate(LocalDateTime localDateTime)
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
}
