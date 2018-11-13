package Functional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SessionsControl implements Serializable
{
    private List<Session> Sessions = new ArrayList<>();
    private Session sessionOpen = null;

    public void updateSession()
    {
        if (sessionOpen == null)
        {
            Session ss = new Session();
            ss.setStartDate(LocalDateTime.now());

            Sessions.add(ss);
            sessionOpen = ss;
        } else
        {
            sessionOpen.setEndDate(LocalDateTime.now());

            sessionOpen = null;
        }
    }

    public int getGrandTotal()
    {
        int iTotal = 0;

        if (sessionOpen == null)
            for (Session iSession : Sessions)
                iTotal += (int) ChronoUnit.MINUTES.between(iSession.getStartDate(), iSession.getEndDate());
        else
            for (Session iSession : Sessions)
            {
                if (iSession.equals(sessionOpen))
                {
                    LocalDateTime ldt = LocalDateTime.now();
                    iTotal += (int) ChronoUnit.MINUTES.between(iSession.getStartDate(), ldt);
                } else
                    iTotal += (int) ChronoUnit.MINUTES.between(iSession.getStartDate(), iSession.getEndDate());
            }

        return iTotal;
    }

    public Session[] getSessions()
    {
        return Sessions.toArray(new Session[0]);
    }

    public void addSession(Session session)
    {
        Sessions.add(session);
    }

    public Boolean checkOpenSession()
    {
        return sessionOpen != null;
    }
}