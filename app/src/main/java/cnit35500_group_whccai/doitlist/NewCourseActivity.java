package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Functional.Course;
import Functional.CoursesControl;
import Functional.Globals;

public class NewCourseActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private Course parent;
    private Menu menu;
    private String viewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_course);

        // Set up toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.ios_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout col_toolbar = findViewById(R.id.ios_col_toolbar);

        EditText txtEditInput = findViewById(R.id.txtInpCourseTitle);

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        viewMode = getIntent().getStringExtra(Globals.ExtraKey_ViewMode);
        if (extras != null)
        {
            switch (viewMode)
            {
                case (Globals.ViewMode_New):
                    // Obtain data
                    mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);

                    col_toolbar.setTitle("New Course");
                    txtEditInput.setHint("e.g. CNIT 35500");

                    break;

                case (Globals.ViewMode_NewWithParent):
                    // Obtain data
                    mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);
                    parent = (Course) getIntent().getSerializableExtra(Globals.ExtraKey_Parent);

                    col_toolbar.setTitle("New Type");
                    txtEditInput.setHint("e.g. Homework");

                    // Set up scope label
                    TextView txt = findViewById(R.id.txtCourseScope);
                    txt.setText(parent.getFullScope(parent));
                    txt.setBackgroundColor(parent.getAssociatedColor());

                    break;
            }
        } else
        {
            Toast.makeText(this, "Failed to receive data", Toast.LENGTH_SHORT).show();

            finish();
            return;
        }
        //

        // Set listener for the Input edit text
        // Reference: EditTextChangeTest
        txtEditInput.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s)
            {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after)
            {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
                // Show and hide menu buttons
                if (s.length() > 0)
                    updateMenuVisibles("s");
                else
                    updateMenuVisibles("d");
            }
        });
    }

    private void updateMenuVisibles(String mode)
    {
        MenuItem itemSave = menu.findItem(R.id.btnSaveToolBar);
        MenuItem itemDone = menu.findItem(R.id.btnDoneToolBar);

        itemSave.setVisible(false);
        itemDone.setVisible(false);

        if (mode.contains("d"))
            itemDone.setVisible(true);

        if (mode.contains("s"))
            itemSave.setVisible(true);
    }

    // Add ToolBar buttons
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

        if (id == R.id.btnSaveToolBar)
        {
            TextView txt = findViewById(R.id.txtInpCourseTitle);

            // Get name value
            String name = txt.getText().toString();

            // Get color value
            // Todo: change this hardcoded value
            int color = ContextCompat.getColor(this, R.color.courseBlue);

            Course courseTemp = null;

            // Try to add a course
            switch (viewMode)
            {
                case (Globals.ViewMode_New):
                    courseTemp = mCourses.addCourse(name, color);
                    break;
                case (Globals.ViewMode_NewWithParent):
                    courseTemp = mCourses.addCourse(name, color, parent);
                    break;
            }

            if (null == courseTemp)
            {
                Toast.makeText(this, "Error: wrong input OR may exist already", Toast.LENGTH_SHORT).show();
                return false;
            } else
            {
                Toast.makeText(this, "Course added as " + courseTemp.getFullScope(courseTemp), Toast.LENGTH_SHORT).show();

                txt.setText("");
            }
            //
        } else if (id == R.id.btnDoneToolBar)
        {
            // Send data back
            Intent i = new Intent();
            i.putExtra(Globals.ExtraKey_Courses, mCourses);
            setResult(Globals.RESULT_SAVE, i);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}