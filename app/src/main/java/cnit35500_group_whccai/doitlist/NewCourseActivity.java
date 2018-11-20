package cnit35500_group_whccai.doitlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import Functional.Course;
import Functional.CourseScopeViewAdapter;
import Functional.CoursesControl;
import Functional.Globals;

public class NewCourseActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private Course parent;
    private Menu menu;
    private RadioGroup rgOne, rgTwo;
    // Handle radio group clicks
    // Reference: https://stackoverflow.com/questions/10425569/radiogroup-with-two-columns-which-have-ten-radiobuttons
    private RadioGroup.OnCheckedChangeListener rgOne_Listener = new RadioGroup.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if (checkedId != -1)
            {
                rgTwo.setOnCheckedChangeListener(null);
                rgTwo.clearCheck();
                rgTwo.setOnCheckedChangeListener(rgTwo_Listener);
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener rgTwo_Listener = new RadioGroup.OnCheckedChangeListener()
    {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            if (checkedId != -1)
            {
                rgOne.setOnCheckedChangeListener(null);
                rgOne.clearCheck();
                rgOne.setOnCheckedChangeListener(rgOne_Listener);
            }
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_course);

        // Set up toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.ios_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout col_toolbar = findViewById(R.id.ios_col_toolbar);
        //

        // Set up radio groups
        rgOne = findViewById(R.id.lytColorsOne);
        rgTwo = findViewById(R.id.lytColorsTwo);
        rgOne.setOnCheckedChangeListener(rgOne_Listener);
        rgTwo.setOnCheckedChangeListener(rgTwo_Listener);
        //

        EditText txtEditInput = findViewById(R.id.txtInpCourseTitle);

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // Obtain data
            mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);
            parent = (Course) getIntent().getSerializableExtra(Globals.ExtraKey_Parent);

            if (parent.isNull())
            {
                col_toolbar.setTitle("New Course");
                txtEditInput.setHint("e.g. CNIT 35500");
            } else
            {
                col_toolbar.setTitle("New Type");
                txtEditInput.setHint("e.g. Homework");

                // Set up scope view
                // RelativeLayout item = (RelativeLayout) view.findViewById(R.id.item);
                RecyclerView recyclerView = findViewById(R.id.rvCourses);
                LinearLayoutManager horizontalLayoutManager
                        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(horizontalLayoutManager);
                CourseScopeViewAdapter adapter = new CourseScopeViewAdapter(this, parent.getScopeArrayOf(false), parent.getColorAsArray(false), " < ");
                //adapter.setClickListener(this);
                recyclerView.setAdapter(adapter);
                //
            }
        } else
        {
            Toast.makeText(this, "Failed to receive data", Toast.LENGTH_LONG).show();

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

        // Populate history layout
        LinearLayout lytCourses = findViewById(R.id.lytCourses);

        Course[] coursesLocal;

        coursesLocal = mCourses.getCoursesAtScope(parent.getScopeArrayOf(false));

        if (coursesLocal == null)
            return;

        for (Course iCourse : coursesLocal)
        {
            TextView tv = new TextView(this);

            tv.setText(iCourse.getName());
            lytCourses.addView(tv);
        }
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
            int color = getSelectedColor();

            Course courseTemp;
            // Try to add a course
            if (parent.isNull())
                courseTemp = mCourses.addCourse(name, color);
            else
                courseTemp = mCourses.addCourse(name, color, parent);

            if (null == courseTemp)
            {
                Toast.makeText(this, "Error: wrong input OR may exist already", Toast.LENGTH_LONG).show();
                return false;
            } else
            {
                // Close keyboard programmatically
                // Reference: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Toast.makeText(this, "Added as " + courseTemp.getScopeStrOf(false), Toast.LENGTH_LONG).show();

                LinearLayout lytCourses = findViewById(R.id.lytCourses);
                TextView tv = new TextView(this);
                tv.setText(courseTemp.getName());
                lytCourses.addView(tv);

                txt.setText("");
                rgOne.clearCheck();
                rgTwo.clearCheck();
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

    private Integer getSelectedColor()
    {
        int valID;

        if ((valID = rgOne.getCheckedRadioButtonId()) == -1)
            if ((valID = rgTwo.getCheckedRadioButtonId()) == -1)
            {
                return -1;
            }

        try
        {
            RadioButton rb = findViewById(valID);

            return Color.parseColor((String) rb.getTag());
        } catch (Exception c)
        {
            return -1;
        }
    }
}