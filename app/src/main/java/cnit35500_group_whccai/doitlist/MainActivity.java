package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import Functional.CoursesControl;
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
        }
    }
}