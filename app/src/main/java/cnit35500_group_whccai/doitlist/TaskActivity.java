package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import Functional.Course;
import Functional.CoursesControl;
import Functional.TasksControl;

public class TaskActivity extends AppCompatActivity
{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TasksControl mTasks;
    private CoursesControl mCourses;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        drawerLayout = findViewById(R.id.taskLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("New Task");

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // Obtain data
            mTasks = (TasksControl) getIntent().getSerializableExtra("tasks");
            mCourses = (CoursesControl) getIntent().getSerializableExtra("courses");
        }
        else return;
        //

        // Populate spinner with currency values
        // Reference: SpinnerTest1
        Spinner spinner = findViewById(R.id.spnClass);

        // Create an ArrayAdapter
        ArrayAdapter<Course> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, mCourses.getCourses());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // To-Do
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        //
    }

    private void updateMenuVisibles(String mode)
    {
        MenuItem itemSave = menu.findItem(R.id.btnSaveToolBar);
        MenuItem itemDone = menu.findItem(R.id.btnDoneToolBar);
        MenuItem itemEdit = menu.findItem(R.id.btnEditToolBar);
        MenuItem itemRemove = menu.findItem(R.id.btnRemoveToolBar);

        itemSave.setVisible(false);
        itemDone.setVisible(false);
        itemEdit.setVisible(false);
        itemRemove.setVisible(false);

        if (mode.contains("r"))
            itemRemove.setVisible(true);

        if (mode.contains("e"))
            itemEdit.setVisible(true);

        if (mode.contains("d"))
            itemDone.setVisible(true);

        if (mode.contains("s"))
            itemSave.setVisible(true);
    }

    public void saveTask(View view)
    {
    }

    public void highlight(View view)
    {
    }

    // Add ToolBar button
    // Reference: https://stackoverflow.com/questions/38158953/how-to-create-button-in-action-bar-in-android
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_buttons, menu);

        this.menu = menu;

        // Show and hide menu buttons
        updateMenuVisibles("d");

        return super.onCreateOptionsMenu(menu);
    }

    // Handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.btnRemoveToolBar)
        {
           // To-Do
        } else if (id == R.id.btnEditToolBar)
        {
            // To-Do
        } else if (id == R.id.btnSaveToolBar)
        {
            // Try to save a course
            TextView txt = findViewById(R.id.txtInpCourseTitle);

            if (null == mCourses.addCourse(txt.getText().toString()))
            {
                Toast.makeText(this, "Failed to added: may already exist", Toast.LENGTH_SHORT).show();
                return false;
            } else
            {
                LinearLayout lytCourses = findViewById(R.id.lytCourses);
                TextView tv = new TextView(this);
                tv.setText(txt.getText().toString());
                lytCourses.addView(tv);

                txt.setText("");
            }
            //
        } else if (id == R.id.btnDoneToolBar)
        {
            // Send data back
            Intent i = new Intent();
            i.putExtra("tasks", mTasks);
            i.putExtra("courses", mCourses);
            setResult(RESULT_OK, i);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}