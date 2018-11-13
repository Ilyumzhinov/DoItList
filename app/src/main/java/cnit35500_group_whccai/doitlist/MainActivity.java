package cnit35500_group_whccai.doitlist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.Session;
import Functional.Task;
import Functional.TasksControl;

public class MainActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private TasksControl mTasks;

    Thread thUpdateViews = new Thread()
    {
        @Override
        public void run()
        {
            int spentTime = 0;
            int totalTime = 9999;

            while (spentTime < totalTime)
            {
                handler.sendMessage(new Message());

                try
                {
                    Thread.sleep(1000); // Just to display the progress
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                spentTime += 1;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Task xtask;

            try
            {
                xtask = mTasks.getTaskAt(0);
            }
            catch (Exception c)
            {
                return;
            }

            TextView txt;

            // Set status label
            txt = findViewById(R.id.txtTaskStatus);

            String status;

            if (xtask.getStatusFinished())
                status = "Finished";
            else
            {
                if (xtask.getSessions().getSessions().length == 0)
                    status = "Not started";
                else if (xtask.getSessions().checkOpenSession())
                    status = "Active";
                else
                    status = "Not finished";
            }
            txt.setText(status);
            //

            //
            Integer remain = xtask.getTimeEst() - xtask.getTimeSpent();

            txt = findViewById(R.id.txtTimePrg);
            txt.setText(String.valueOf(remain) + " min");

            //
            long timeBefore = ChronoUnit.MINUTES.between(LocalDateTime.now(), xtask.getDeadline());

            txt = findViewById(R.id.txtTaskBeforeDeadline);
            txt.setText(String.valueOf(timeBefore) + " min");


            //
            ProgressBar timeSpent = findViewById(R.id.prgSpent);
            timeSpent.setProgress(xtask.getTimeSpent());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise data
        mCourses = new CoursesControl();
        mTasks = new TasksControl();

        // Set default value
        mCourses.addCourse("CNIT 35500");

        Session ssTemp = new Session();
        ssTemp.setStartDate(LocalDateTime.of(2018, 11, 12, 5, 45));
        ssTemp.setEndDate(LocalDateTime.of(2018, 11, 12, 5, 50));

        Session ssTemp2 = new Session();
        ssTemp2.setStartDate(LocalDateTime.of(2018, 11, 13, 13, 30));
        ssTemp2.setEndDate(LocalDateTime.of(2018, 11, 13, 13, 35));

        // Test
        mTasks.addTask("Sprint 3", "You need to hurry up!", mCourses.getCourseWithName("CNIT 35500"), LocalDateTime.of(2018, 11, 18, 23, 59), 60, true);
        mTasks.getTaskAt(0).addSession(ssTemp);
        mTasks.getTaskAt(0).addSession(ssTemp2);
        mTasks.getTaskAt(0).setDateAdded(LocalDateTime.of(2018, 11, 12, 6, 0));
        populateTaskItemView(mTasks.getTaskAt(0));

        thUpdateViews.start();
    }

    public void NewTask(View view)
    {
        Intent i = new Intent(this, ManageTaskActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra("viewMode","new");
        i.putExtra("tasks", mTasks);
        i.putExtra("courses", mCourses);

        startActivityForResult(i, Globals.ManageTaskRequestCode);
    }

    public void OpenTask(View view)
    {
        Intent i = new Intent(this, TaskActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra("tasks", mTasks);
        i.putExtra("courses", mCourses);
        i.putExtra("index", 0);

        startActivityForResult(i, Globals.OpenTaskRequestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            // Receive Intent info from ManageTaskActivity
            case (Globals.ManageTaskRequestCode):
                if (resultCode == Globals.RESULT_OK)
                {
                    // Receive object through Intent
                    // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        // Obtain data
                        mTasks = (TasksControl) data.getSerializableExtra("tasks");
                        mCourses = (CoursesControl) data.getSerializableExtra("courses");
                    }
                }
                break;
        }
    }

    public void populateTaskItemView(Task xtask)
    {
        //
        TextView txt = findViewById(R.id.txtTaskName);
        txt.setText(xtask.getName());

        // Set status label
        txt = findViewById(R.id.txtTaskStatus);

        String status;

        if (xtask.getStatusFinished())
            status = "Finished";
        else
        {
            if (xtask.getSessions().getSessions().length == 0)
                status = "Not started";
            else if (xtask.getSessions().checkOpenSession())
                status = "Active";
            else
                status = "Not finished";
        }
        txt.setText(status);
        //

        //
        txt = findViewById(R.id.txtTaskCourse);
        txt.setText(xtask.getCourse().getName());

        //
        Integer remain = xtask.getTimeEst() - xtask.getTimeSpent();

        txt = findViewById(R.id.txtTimePrg);
        txt.setText(String.valueOf(remain) + " min");

        //
        long timeBefore = ChronoUnit.MINUTES.between(LocalDateTime.now(), xtask.getDeadline());

        txt = findViewById(R.id.txtTaskBeforeDeadline);
        txt.setText(String.valueOf(timeBefore) + " min");

        //
        ProgressBar timeSpent = findViewById(R.id.prgSpent);
        timeSpent.setIndeterminate(false);
        timeSpent.setMax(xtask.getTimeEst());
        timeSpent.setProgress(xtask.getTimeSpent());
    }

    public void RecordTask(View view)
    {
        Task taskReceived = mTasks.getTaskAt(0);

        taskReceived.updateRecordingTime();
    }

    public void CompleteTask(View view)
    {
        Boolean taskStatusFinished = mTasks.getTaskAt(0).getStatusFinished();

        mTasks.getTaskAt(0).setStatusFinished(!taskStatusFinished);
    }

    public void openTaskList(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }
}