package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

import Functional.CoursesControl;

public class NewCourseActivity extends AppCompatActivity
{
    private CoursesControl mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Add back button
        // Reference: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_new_course);

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            mCourses = (CoursesControl) getIntent().getSerializableExtra("courses"); //Obtaining data

            if (mCourses == null)
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, mCourses.getCourses()[0].getName(), Toast.LENGTH_LONG).show();
        }
    }

    // Add Save button
    // Reference: https://stackoverflow.com/questions/38158953/how-to-create-button-in-action-bar-in-android
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            onSupportNavigateUp();
        }
        return super.onOptionsItemSelected(item);
    }
    // Reference: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
    @Override
    public boolean onSupportNavigateUp(){
        // Send data back
        Intent i = new Intent();
        i.putExtra("courses", mCourses);
        setResult(RESULT_OK, i);

        finish();
        return true;
    }
}