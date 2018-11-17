package cnit35500_group_whccai.doitlist;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import Functional.Globals;
import Functional.Session;
import Functional.SessionsControl;

public class SessionsHistoryActivity extends AppCompatActivity
{
    private SessionsControl mTaskSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_history);

        // Receive data through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // Obtain data
            mTaskSessions = (SessionsControl) getIntent().getSerializableExtra(Globals.ExtraKey_Sessions);

            // Set up toolbar
            android.support.v7.widget.Toolbar toolbar = findViewById(R.id.ios_toolbar);
            setSupportActionBar(toolbar);
            CollapsingToolbarLayout col_toolbar = findViewById(R.id.ios_col_toolbar);
            col_toolbar.setTitle("Saved sessions");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //
        } else
        {
            Toast.makeText(this, "Failed to load Saved sessions", Toast.LENGTH_SHORT).show();

            finish();
        }
        //

        LinearLayout lytCourses = findViewById(R.id.lytSessionsHistory);

        int i = 1;
        String sessionStr;
        LocalDateTime startDate, endDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy, HH:mm");

        for (Session iSession : mTaskSessions.getSessions())
        {
            TextView tv = new TextView(this);
            startDate = iSession.getStartDate();

            sessionStr = String.valueOf(i) + ": ";
            sessionStr += " from " + startDate.format(formatter);

            if (iSession.getEndDate() != null)
            {
                endDate = iSession.getEndDate();
                sessionStr += " to " + endDate.format(formatter);
                sessionStr += " (" + ChronoUnit.MINUTES.between(startDate, endDate) + " min)";
            }
            else
            {
                sessionStr += " to ??";
                sessionStr += " (??)";
            }

            tv.setText(sessionStr);
            lytCourses.addView(tv);

            i++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
