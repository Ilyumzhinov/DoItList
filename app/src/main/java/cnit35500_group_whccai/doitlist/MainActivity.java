package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.SaveTasks;
import Functional.Task;
import Functional.TasksControl;
import Functional.ViewAdapterTasks;

public class MainActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private TasksControl mTasks;
    private SaveTasks saveTasks;


//    Thread thUpdateViews = new Thread()
//    {
//        @Override
//        public void run()
//        {
//            int spentTime = 0;
//            int totalTime = 9999;
//
//            while (spentTime < totalTime)
//            {
//                handler.sendMessage(new Message());
//
//                try
//                {
//                    Thread.sleep(1000); // Just to display the progress
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//                spentTime += 1;
//            }
//        }
//    };
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            Task xtask;
//
//            try
//            {
//                xtask = mTasks.getTaskAt(0);
//            }
//            catch (Exception c)
//            {
//                return;
//            }
//
//            TextView txt;
//
//            // Set status label
//            txt = findViewById(R.id.txtTaskStatus);
//
//            String status;
//
//            if (xtask.getStatusFinished())
//                status = "Finished";
//            else
//            {
//                if (xtask.getSessions().getSessions().length == 0)
//                    status = "Not started";
//                else if (xtask.getSessions().checkOpenSession())
//                    status = "Active";
//                else
//                    status = "Not finished";
//            }
//            txt.setText(status);
//            //
//
//            //
//            Integer remain = xtask.getTimeGoal() - xtask.getTimeSpent();
//
//            txt = findViewById(R.id.txtTimePrg);
//            txt.setText(String.valueOf(remain) + " min");
//
//            //
//            long timeBefore = ChronoUnit.MINUTES.between(LocalDateTime.now(), xtask.getDeadline());
//
//            txt = findViewById(R.id.txtTaskBeforeDeadline);
//            txt.setText(String.valueOf(timeBefore) + " min");
//
//
//            //
//            ProgressBar timeSpent = findViewById(R.id.prgSpent);
//            timeSpent.setProgress(xtask.getTimeSpent());
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise data
        saveTasks = new SaveTasks();
        saveTasks.createSaveTasks(this);

        // Try to load saved courses
        try
        {
            mCourses = saveTasks.openCoursesFile();
            if (mCourses == null)
                throw new Exception();
        } catch (Exception e)
        {
            // This should only get hit if this is the first run and nothing has been saved
            Toast.makeText(this, "Error loading Courses", Toast.LENGTH_SHORT).show();
            mCourses = new CoursesControl();
        }

        // Try to load saved tasks
        try
        {
            mTasks = saveTasks.openTasksFile();
            if (mTasks == null)
                throw new Exception();
        } catch (Exception e)
        {
            // This should only get hit if this is the first run and nothing has been saved
            Toast.makeText(this, "Error loading Tasks", Toast.LENGTH_SHORT).show();
            mTasks = new TasksControl();
        }

//        thUpdateViews.start();

        // Set up tasks RecyclerView
        List<Task> tasksList = Arrays.asList(mTasks.getTasks());

        RecyclerView recyclerView = findViewById(R.id.listTasks);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ViewAdapterTasks adapter = new ViewAdapterTasks(this, tasksList, this);
        recyclerView.setAdapter(adapter);
        //

    }

    public void NewTask(View view)
    {
        Intent i = new Intent(this, ManageTaskActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra(Globals.ExtraKey_ViewMode,Globals.ViewMode_New);
        i.putExtra(Globals.ExtraKey_Tasks, mTasks);
        i.putExtra(Globals.ExtraKey_Courses, mCourses);

        startActivityForResult(i, Globals.ManageTaskRequestCode);
    }

//    public void OpenTask(View view)
//    {
//        Intent i = new Intent(this, TaskActivity.class);
//
//        // Pass an object to another activity
//        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
//        i.putExtra(Globals.ExtraKey_Tasks, mTasks);
//        i.putExtra(Globals.ExtraKey_Courses, mCourses);
//        // Todo: change hardcoded value
//        i.putExtra(Globals.ExtraKey_Index, 0);
//
//        startActivityForResult(i, Globals.OpenTaskRequestCode);
//    }

    public void openTaskList(View view)
    {
        Intent i = new Intent(this, NavigationActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra(Globals.ExtraKey_Tasks, mTasks);
        i.putExtra(Globals.ExtraKey_Courses, mCourses);

        startActivityForResult(i, Globals.OpenTaskRequestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            // Receive Intent info from ManageTaskActivity
            case (Globals.ManageTaskRequestCode):
                if (resultCode == Globals.RESULT_SAVE)
                {
                    // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
                    // Receive object through Intent
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        // Obtain data
                        mTasks = (TasksControl) data.getSerializableExtra(Globals.ExtraKey_Tasks);
                        mCourses = (CoursesControl) data.getSerializableExtra(Globals.ExtraKey_Courses);
                    }
                }
                break;

            case (Globals.OpenTaskRequestCode):
                if (resultCode == Globals.RESULT_SAVE)
                {
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        // Obtain data
                        mTasks = (TasksControl) data.getSerializableExtra(Globals.ExtraKey_Tasks);
                        mCourses = (CoursesControl) data.getSerializableExtra(Globals.ExtraKey_Courses);
                    }
                }
        }
    }

//    public void RecordTask(View view)
//    {
//        // Todo: change hardcoded value
//        Task taskReceived = mTasks.getTaskAt(0);
//
//        taskReceived.updateRecordingTime();
//    }

//    public void CompleteTask(View view)
//    {
//        Boolean taskStatusFinished = mTasks.getTaskAt(0).getStatusFinished();
//
//        mTasks.getTaskAt(0).setStatusFinished(!taskStatusFinished);
//    }

    // Save mTasks and mCourses prior to stopping
    @Override
    protected void onStop() {
        saveTasks.saveFile(mTasks);
        saveTasks.saveFile(mCourses);
        super.onStop();
    }
}