package cnit35500_group_whccai.doitlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Functional.Course;
import Functional.RecyclerViewAdapterCourseScope;
import Functional.CoursesControl;
import Functional.Globals;

public class NewCourseActivity extends AppCompatActivity
{
    private CoursesControl mCourses;
    private List<Course> mCoursesAtScope;

    private Course parent;
    private Menu menu;
    RecyclerViewAdapterCourseScope viewAdapterCourseScope;
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
        MenuItem itemSave = menu.findItem(R.id.btnToolBarSave);
        MenuItem itemDone = menu.findItem(R.id.btnToolBarDone);

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

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // Obtain data
            mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);
            parent = (Course) getIntent().getSerializableExtra(Globals.ExtraKey_Parent);

            // Add an empty course if nothing was passed
            if (parent == null)
            {
                mCourses.addEmptyCourse();
                parent = mCourses.getEmptyCourse();
            }

            if (parent.isNull())
            {
                col_toolbar.setTitle("New Course");
            } else
            {
                col_toolbar.setTitle("New Category");
            }

            // Set up scope view
            RecyclerView recyclerView = findViewById(R.id.rvCourses);
            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            RecyclerViewAdapterCourseScope adapter = new RecyclerViewAdapterCourseScope(this, parent);
            //viewAdapterCourseScope.setClickListener(this);

            if (adapter.getmCoursesAtScope().isEmpty())
            {
                View vw = findViewById(R.id.lytCourseScope);
                vw.setVisibility(View.GONE);
            }
            recyclerView.setAdapter(adapter);
            //

        } else
        {
            Toast.makeText(this, "Failed to receive data", Toast.LENGTH_LONG).show();

            finish();
            return;
        }
        //

        // Set listener for the Input edit text
        // Reference: EditTextChangeTest
        EditText txtEditInput = findViewById(R.id.txtInpCourseTitle);
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

        // Populate Saved at scope RecyclerView
        // Get courses at scope
        mCoursesAtScope = mCourses.getCoursesAtScope(parent.getScopeStrArrayOf(false));

        if (mCoursesAtScope == null)
            mCoursesAtScope = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rvCoursesAtScope);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        viewAdapterCourseScope = new RecyclerViewAdapterCourseScope(this, mCoursesAtScope);

        recyclerView.setAdapter(viewAdapterCourseScope);
    }

    // Handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.btnToolBarSave)
        {
            TextView txt = findViewById(R.id.txtInpCourseTitle);

            // Get name value
            String name = txt.getText().toString();

            // Get color value
            int color = getSelectedColor();

            Course courseTemp;
            // Try to add a course
            try
            {
                if (parent.isNull())
                    courseTemp = mCourses.addCourse(name, color);
                else
                    courseTemp = mCourses.addCourse(name, color, parent);
            } catch (Exception c)
            {
                Toast.makeText(this, c.getMessage(), Toast.LENGTH_LONG).show();
                return false;
            }

            // Close keyboard programmatically
            // Reference: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
            View view = this.getCurrentFocus();
            if (view != null)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            Toast.makeText(this, "Added as " + courseTemp.getScopeStrOf(false), Toast.LENGTH_LONG).show();

            // Add to Saved at scope RecyclerView
            // Todo: This is ugly
            List<Course> cs = mCourses.getCoursesAtScope(parent.getScopeStrArrayOf(false));

            mCoursesAtScope.add(cs.get(cs.size() - 1));
            viewAdapterCourseScope.notifyItemInserted(mCoursesAtScope.size() - 1);

            txt.setText("");
            rgOne.clearCheck();
            rgTwo.clearCheck();
            //
        } else if (id == R.id.btnToolBarDone)
        {
            // Send data back
            Intent i = new Intent();
            i.putExtra(Globals.ExtraKey_Courses, mCourses);
            setResult(Globals.RESULT_SAVE, i);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // Do the cleanup
    @Override
    public void onPause()
    {
        mCourses.removeEmptyCourse();
        super.onPause();
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
