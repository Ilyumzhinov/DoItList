package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.Task;
import Functional.TasksControl;

public class TaskActivity extends AppCompatActivity
{
    private TasksControl mTasks;
    private CoursesControl mCourses;
    private Task currentTask;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Receive data through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // Obtain data
            mTasks = (TasksControl) getIntent().getSerializableExtra(Globals.ExtraKey_Tasks);
            mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);
            Integer cIndex = getIntent().getIntExtra(Globals.ExtraKey_Index, 0);

            // Set global values
            currentTask = mTasks.getTaskAt(cIndex);

            // Set up toolbar
            android.support.v7.widget.Toolbar toolbar = findViewById(R.id.ios_toolbar);
            setSupportActionBar(toolbar);
            //
        } else
        {
            Toast.makeText(this, "Failed to open a task", Toast.LENGTH_SHORT).show();

            finish();
        }
        //

        populateViews();
    }

    public void OpenSessionsHistory(View view)
    {
        Intent i = new Intent(this, SessionsHistoryActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra(Globals.ExtraKey_Sessions, currentTask.getSessions());

        startActivityForResult(i, Globals.SessionsHistoryRequestCode);
    }

    private void populateViews()
    {
        CollapsingToolbarLayout col_toolbar = findViewById(R.id.ios_col_toolbar);
        col_toolbar.setTitle(currentTask.getName());

        // Populate views with data
        // Set course
        TextView txt = findViewById(R.id.txtTaskCourse);
        txt.setText(currentTask.getCourse().getName());
        txt.setBackgroundColor(currentTask.getCourse().getAssociatedColor());

        // Set deadline label
        // Reference: https://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string
        LocalDateTime localDate = currentTask.getDeadline();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy, HH:mm");
        String formattedString = localDate.format(formatter);

        txt = findViewById(R.id.lblTaskDeadline);
        txt.setText(formattedString);

        // Set time before deadline label
        long timBeforeDeadline = ChronoUnit.MINUTES.between(LocalDateTime.now(), currentTask.getDeadline());

        txt = findViewById(R.id.lblTaskDeadlineTimeRemaining);
        txt.setText(String.valueOf(timBeforeDeadline) + " min");

        // Set time remaining
        long timeGoal = currentTask.getTimeEst();
        long timeSpentMin = currentTask.getTimeSpent();

        txt = findViewById(R.id.lblTaskProgressRemaining);
        txt.setText(String.valueOf(timeSpentMin) + " min / " + String.valueOf(timeGoal) + " min");

        // Set progress bar
        ProgressBar timeSpent = findViewById(R.id.prgTaskSpent);
        timeSpent.setIndeterminate(false);
        timeSpent.setMax(currentTask.getTimeEst());
        timeSpent.setProgress(currentTask.getTimeSpent());

        // Set time before deadline label
        long sessionsCount = currentTask.getSessions().getSessions().length;

        txt = findViewById(R.id.lblTaskSessionsCount);
        txt.setText(String.valueOf(sessionsCount));

        // Set notes
        txt = findViewById(R.id.lblTaskNotes);

        txt.setText(currentTask.getDetail());

        // Set record floating button
        FloatingActionButton fab = findViewById(R.id.btnFloatTaskRecord);

        if (currentTask.getStatusFinished())
            fab.hide();
        else
            fab.show();
    }

    // Add ToolBar button
    // Reference: https://stackoverflow.com/questions/38158953/how-to-create-button-in-action-bar-in-android
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_buttons, menu);

        this.menu = menu;

        // Show and hide menu buttons
        updateMenuVisibles("fed");

        return super.onCreateOptionsMenu(menu);
    }

    private void updateMenuVisibles(String mode)
    {
        MenuItem itemFinish = menu.findItem(R.id.btnToolBarFinish);
        MenuItem itemDone = menu.findItem(R.id.btnDoneToolBar);
        MenuItem itemEdit = menu.findItem(R.id.btnEditToolBar);
        MenuItem itemRemove = menu.findItem(R.id.btnRemoveToolBar);

        itemFinish.setVisible(false);
        itemEdit.setVisible(false);
        itemRemove.setVisible(false);
        itemDone.setVisible(false);

        if (mode.contains("f"))
            itemFinish.setVisible(true);

        if (mode.contains("e"))
            itemEdit.setVisible(true);

        if (mode.contains("d"))
            itemDone.setVisible(true);
    }

    // Handle toolbar button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case (R.id.btnToolBarFinish):
                currentTask.setStatusFinished(!currentTask.getStatusFinished());

                FloatingActionButton fab = findViewById(R.id.btnFloatTaskRecord);

                if (currentTask.getStatusFinished())
                    fab.hide();
                else
                    fab.show();

                break;

            case (R.id.btnEditToolBar):
                Intent i = new Intent(this, ManageTaskActivity.class);

                // Pass an object to another activity
                // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
                i.putExtra(Globals.ExtraKey_ViewMode, Globals.ViewMode_Edit);
                i.putExtra(Globals.ExtraKey_Tasks, mTasks);
                i.putExtra(Globals.ExtraKey_Courses, mCourses);
                i.putExtra(Globals.ExtraKey_Index, mTasks.getTaskIndexFor(currentTask));

                // Set result code from Third activity to first activity
                // Reference: https://stackoverflow.com/questions/28944137/android-get-result-from-third-activity
                i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(i);

                finish();
                break;

            case (R.id.btnDoneToolBar):
                // Send data back
                Intent i2 = new Intent();
                i2.putExtra(Globals.ExtraKey_Tasks, mTasks);
                i2.putExtra(Globals.ExtraKey_Courses, mCourses);
                setResult(Globals.RESULT_SAVE, i2);

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void RecordTask(View view)
    {
        Task taskReceived = currentTask;

        taskReceived.updateRecordingTime();
    }
}