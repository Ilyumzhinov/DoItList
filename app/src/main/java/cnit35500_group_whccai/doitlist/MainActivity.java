package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import Functional.Course;
import Functional.CoursesControl;
import Functional.Task;

public class MainActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private Task mTasks;

    private final static int newCourseRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCourses = new CoursesControl();
    }

    public void NewTask(View view)
    {
        Intent i = new Intent(this, TaskActivity.class);
        startActivity(i);
    }

    public void NewCourse(View view)
    {
        Intent i = new Intent(this, NewCourseActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra("courses", mCourses);

        startActivityForResult(i, newCourseRequestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == newCourseRequestCode) && (resultCode == RESULT_OK))
        {
            // Receive object through Intent
            // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
//            Bundle extras = data.getExtras();
//            if (extras != null)
//            {
//                // Obtain data
//                mCourses = (CoursesControl) getIntent().getSerializableExtra("courses");
//            }
//
//            if (mCourses.getCourses().length > 0)
//                Toast.makeText(this, mCourses.getCourses()[0].getName(), Toast.LENGTH_LONG).show();
        }
    }
}