package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import Functional.CoursesControl;
import Functional.Task;
import Functional.TasksControl;

public class MainActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private TasksControl mTasks;

    private final static int cNewCourseRequestCode = 1;
    private final static int cNewTaskRequestCode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise data
        mCourses = new CoursesControl();
        mTasks = new TasksControl();

        // Set default value
        mCourses.addCourse("default");

        // Test
        mTasks.addTask("Task Test","Test task",mCourses.getCourseWithName("default"),LocalDateTime.of(2018,11,15,23,59),5,true);
        mTasks.getTaskAt(0).setTimeSpent(3);
        mTasks.getTaskAt(0).setDateAdded(LocalDateTime.of(2018,11,1,23,59));
        populateTaskItemView(mTasks.getTaskAt(0));
    }

    public void NewTask(View view)
    {
        Intent i = new Intent(this, TaskActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra("tasks", mTasks);
        i.putExtra("courses", mCourses);

        startActivityForResult(i, cNewTaskRequestCode);
    }

    public void NewCourse(View view)
    {
        Intent i = new Intent(this, NewCourseActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra("courses", mCourses);

        startActivityForResult(i, cNewCourseRequestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            // Receive Intent info from NewCourseActivity
            case cNewCourseRequestCode:
                if (resultCode == RESULT_OK)
                {
                    // Receive object through Intent
                    // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        // Obtain data
                        mCourses = (CoursesControl) data.getSerializableExtra("courses");
                    }
                }
                break;
            // Receive Intent info from TaskActivity
            case cNewTaskRequestCode:
                if (resultCode == RESULT_OK)
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
        TextView txt = findViewById(R.id.txtTaskName);
        txt.setText(xtask.getName());

        //
        txt = findViewById(R.id.txtTaskCourse);
        txt.setText(xtask.getCourse().getName());

        //
        txt = findViewById(R.id.txtTaskCourse);
        txt.setText(xtask.getCourse().getName());

        //
        Integer remain = xtask.getTimeEst() - xtask.getTimeSpent();

        txt = findViewById(R.id.txtTaskTimeRemaining);
        txt.setText(String.valueOf(remain) + " min");

        //
        txt = findViewById(R.id.txtTaskTimeSpent);
        txt.setText(String.valueOf(xtask.getTimeSpent()) + " min");

        //
//        long timeTotal = ChronoUnit.MINUTES.between(xtask.getDateAdded(), xtask.getDeadline());
        long timeBefore = ChronoUnit.MINUTES.between(LocalDateTime.now(), xtask.getDeadline());
//        long timeElapsed = ChronoUnit.MINUTES.between(xtask.getDateAdded(), LocalDateTime.now());

        txt = findViewById(R.id.txtTaskBeforeDeadline);
        txt.setText(String.valueOf(timeBefore) + " min");

        //
//        ProgressBar timeEstimated = findViewById(R.id.prgEstimated);
//        timeEstimated.setMax((int) timeTotal);
//        timeEstimated.setProgress((int) timeElapsed);

        //
        ProgressBar timeSpent = findViewById(R.id.prgSpent);
        timeSpent.setMax(xtask.getTimeEst());
        timeSpent.setProgress(xtask.getTimeSpent());
    }
}