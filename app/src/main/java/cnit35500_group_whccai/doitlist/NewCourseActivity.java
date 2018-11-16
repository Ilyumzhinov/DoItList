package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import Functional.Course;
import Functional.CoursesControl;
import Functional.Globals;

public class NewCourseActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_course);

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            mCourses = (CoursesControl) getIntent().getSerializableExtra("courses"); //Obtaining data
        }

        // Set listener for the Input edit text
        // Reference: EditTextChangeTest
        final EditText txtEditInput = findViewById(R.id.txtInpCourseTitle);
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

        // Set up toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.ios_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout col_toolbar = findViewById(R.id.ios_col_toolbar);
        col_toolbar.setTitle("New Course");
        //
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
            // Try to save a course
            TextView txt = findViewById(R.id.txtInpCourseTitle);

            int xColor = ContextCompat.getColor(this, R.color.courseBlue);

            Course courseTemp = mCourses.addCourse(txt.getText().toString(), xColor);

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
            i.putExtra("courses", mCourses);
            setResult(Globals.RESULT_SAVE, i);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}