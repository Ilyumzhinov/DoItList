package cnit35500_group_whccai.doitlist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

import Functional.Course;
import Functional.CoursesControl;
import Functional.Globals;
import Functional.RecyclerViewAdapterCourseSelection;
import Functional.Task;
import Functional.TasksControl;

public class ManageTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    private TasksControl mTasks;
    private CoursesControl mCourses;
    private Integer cTaskIndex;

    private int day, month, year, hour, minute;
    private LocalDateTime dueDatePicked;
    private Task currentTask;
    private Menu menu;
    private String viewMode;

    private RecyclerViewAdapterCourseSelection mAdapterCourses, mAdapterCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);

        // Set up toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.ios_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout col_toolbar = findViewById(R.id.ios_col_toolbar);

        // Receive data through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        viewMode = getIntent().getStringExtra(Globals.ExtraKey_ViewMode);
        if (extras != null && viewMode != null)
        {
            switch (viewMode)
            {
                case (Globals.ViewMode_New):
                    // Obtain data
                    mTasks = (TasksControl) getIntent().getSerializableExtra(Globals.ExtraKey_Tasks);
                    mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);

                    col_toolbar.setTitle("New Task");

                    break;

                case (Globals.ViewMode_Edit):
                    // Obtain data
                    mTasks = (TasksControl) getIntent().getSerializableExtra(Globals.ExtraKey_Tasks);
                    mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);
                    cTaskIndex = getIntent().getIntExtra(Globals.ExtraKey_Index, 0);

                    // Set global values
                    currentTask = mTasks.getTaskAt(cTaskIndex);
                    dueDatePicked = currentTask.getDeadline();

                    col_toolbar.setTitle("Edit Task");

                    // Populate views with data
                    ((TextView) findViewById(R.id.edtTaskName)).setText(currentTask.getName());
                    ((TextView) findViewById(R.id.txtDueDate)).setText(Globals.formatDateAndTime(dueDatePicked));
                    ((EditText) findViewById(R.id.edtTimeEstH)).setText(String.valueOf(Math.round(currentTask.getTimeGoal() / 60)));
                    ((EditText) findViewById(R.id.edtTimeEstM)).setText(String.valueOf(currentTask.getTimeGoal() % 60));
                    ((TextView) findViewById(R.id.edtNotes)).setText(currentTask.getDetail());

                    break;
            }
        } else
        {
            Toast.makeText(this, "Failed to receive data", Toast.LENGTH_LONG).show();

            finish();
            return;
        }
        //

        // Populate course selection
        RecyclerView rvCourses = findViewById(R.id.rvCourses);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCourses.setLayoutManager(horizontalLayoutManager);

        mAdapterCourses = new RecyclerViewAdapterCourseSelection(this, mCourses.getCoursesAtScope(mCourses.getEmptyCourseScope()));

        // Catch RecyclerView clicks
        // Reference: https://stackoverflow.com/questions/27703779/detect-click-on-recyclerview-outside-of-items
        mAdapterCourses.setClickListener(new RecyclerViewAdapterCourseSelection.ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                mAdapterCategories.updateData(mCourses.getCoursesAtScope(mAdapterCourses.getItem(position).getScopeStrArrayOf(false)));

                updateRecyclerViewsVisibles(mAdapterCourses, mAdapterCategories);
            }
        });

        rvCourses.setAdapter(mAdapterCourses);
        //

        // Set up course selection
        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        LinearLayoutManager horizontalLayoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(horizontalLayoutManager2);

        mAdapterCategories = new RecyclerViewAdapterCourseSelection(this, mCourses.getCoursesAtScope(mAdapterCourses.getCoursesScope()));

        rvCategories.setAdapter(mAdapterCategories);
        //

        updateRecyclerViewsVisibles(mAdapterCourses, mAdapterCategories);
    }

    // Open a NewCourse activity with scope
    public void createNewCourse(View view)
    {
        Intent i = new Intent(this, NewCourseActivity.class);

        // Pass objects to another activity
        i.putExtra(Globals.ExtraKey_Courses, mCourses);
        i.putExtra(Globals.ExtraKey_Parent, mCourses.getEmptyCourse());

        startActivityForResult(i, Globals.NewCourseRequestCode);
    }

    // Open a NewCourse activity with scope
    public void createNewCategory(View view)
    {
        Intent i = new Intent(this, NewCourseActivity.class);

        // Pass objects to another activity
        i.putExtra(Globals.ExtraKey_Courses, mCourses);
        i.putExtra(Globals.ExtraKey_Parent, mAdapterCourses.getSelectedCourse());

        startActivityForResult(i, Globals.NewCourseRequestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            // Receive Intent info from NewCourseActivity
            case (Globals.NewCourseRequestCode):
                if (resultCode == Globals.RESULT_SAVE)
                {
                    // Receive object through Intent
                    // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        // Obtain data
                        mCourses = (CoursesControl) data.getSerializableExtra(Globals.ExtraKey_Courses);

                        //
                        mAdapterCourses.updateData(mCourses.getCoursesAtScope(mCourses.getEmptyCourseScope()));
                        mAdapterCategories.updateData(mCourses.getCoursesAtScope(mAdapterCourses.getCoursesScope()));

                        updateRecyclerViewsVisibles(mAdapterCourses, mAdapterCategories);
                    }
                }
                break;
        }
    }

    // Update RecyclerViews visibility state
    private void updateRecyclerViewsVisibles(RecyclerViewAdapterCourseSelection xCoursesAdapter, RecyclerViewAdapterCourseSelection xCategoriesAdapter)
    {
        final RecyclerView rvCourses = findViewById(R.id.rvCourses),
                rvCategories = findViewById(R.id.rvCategories);

        final TextView txtCoursesNoItems = findViewById(R.id.lblCoursesNoItems),
                txtCategoriesNoItems = findViewById(R.id.lblCategoriesNoItems);

        final LinearLayout lytCourseLevelDivider = findViewById(R.id.lytCourseLevelDivider),
                lytCategories = findViewById(R.id.lytCourseCategory);

        // Update categories visible
        if (xCoursesAdapter.getItemCount() < 1)
        {
            txtCoursesNoItems.setVisibility(View.VISIBLE);
            rvCourses.setVisibility(View.GONE);

            lytCourseLevelDivider.setVisibility(View.GONE);
            lytCategories.setVisibility(View.GONE);
        } else
        {
            txtCoursesNoItems.setVisibility(View.GONE);
            rvCourses.setVisibility(View.VISIBLE);

            lytCourseLevelDivider.setVisibility(View.VISIBLE);
            lytCategories.setVisibility(View.VISIBLE);
        }

        // Update categories visible
        if (xCategoriesAdapter.getItemCount() < 1)
        {
            txtCategoriesNoItems.setVisibility(View.VISIBLE);
            rvCategories.setVisibility(View.GONE);
        } else
        {
            txtCategoriesNoItems.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);
        }
    }

    private void updateMenuVisibles(String mode)
    {
        MenuItem itemSave = menu.findItem(R.id.btnToolBarSave);
        MenuItem itemDone = menu.findItem(R.id.btnToolBarDone);
        MenuItem itemEdit = menu.findItem(R.id.btnToolBarEdit);
        MenuItem itemRemove = menu.findItem(R.id.btnToolBarRemove);

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

    public void PickDate(View view)
    {
        Calendar calc = Calendar.getInstance();
        this.year = calc.get(Calendar.YEAR);
        this.month = calc.get(Calendar.MONTH);
        this.day = calc.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, LocalDateTime.now().getYear(), (LocalDateTime.now().getMonthValue() - 1), LocalDateTime.now().getDayOfMonth());
        datePickerDialog.show();
    }

    // Add ToolBar button
    // Reference: https://stackoverflow.com/questions/38158953/how-to-create-button-in-action-bar-in-android
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_buttons, menu);

        this.menu = menu;

        // Show and hide menu buttons
        switch (viewMode)
        {
            case (Globals.ViewMode_New):
                updateMenuVisibles("sd");
                break;

            case(Globals.ViewMode_Edit):
                updateMenuVisibles("rsd");
                break;
        }

        return super.onCreateOptionsMenu(menu);
    }

    // Handle toolbar button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case (R.id.btnToolBarRemove):
                mTasks.removeTask(currentTask);

                Toast.makeText(this, "Task removed!", Toast.LENGTH_LONG).show();

                Intent i = new Intent();
                i.putExtra(Globals.ExtraKey_Tasks, mTasks);
                i.putExtra(Globals.ExtraKey_Courses, mCourses);

                setResult(Globals.RESULT_SAVE, i);

                finish();

                break;

            case (R.id.btnToolBarSave):
                String name,
                        notes;
                Course course = null;
                LocalDateTime dueDate;
                Long timeEst;

                // Try to get input values
                try
                {
                    name = ((TextView) findViewById(R.id.edtTaskName)).getText().toString();
                    notes = ((TextView) findViewById(R.id.edtNotes)).getText().toString();

                    if (null != ((RecyclerViewAdapterCourseSelection) ((RecyclerView) findViewById(R.id.rvCategories)).getAdapter()).getSelectedCourse())
                        course = ((RecyclerViewAdapterCourseSelection) ((RecyclerView) findViewById(R.id.rvCategories)).getAdapter()).getSelectedCourse();
                    else if (null != ((RecyclerViewAdapterCourseSelection) ((RecyclerView) findViewById(R.id.rvCourses)).getAdapter()).getSelectedCourse())
                        course = ((RecyclerViewAdapterCourseSelection) ((RecyclerView) findViewById(R.id.rvCourses)).getAdapter()).getSelectedCourse();

                    dueDate = dueDatePicked;

                    // Check time input in minutes
                    Long timefromInput = calculateMinutesFromTimeInput(((TextView) findViewById(R.id.edtTimeEstH)).getText().toString(), ((TextView) findViewById(R.id.edtTimeEstM)).getText().toString());
                    if (timefromInput >= 0)
                        timeEst = timefromInput;
                    else
                        throw new Exception("Check time input");

                    // Check that there is a name
                    if (name.trim().length() == 0)
                        throw new Exception("Task must have a name");
                    // Check that we have a due date
                    if (dueDate == null)
                        throw new Exception("Task must have a due date");
                    // Check that there is a course selected
                    if (null == course || course.equals(mCourses.getEmptyCourse()))
                        throw new Exception("Task must have a Course");
                } catch (Exception e)
                {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return false;
                }
                //

                try
                {
                    i = new Intent(this, TaskActivity.class);

                    switch (viewMode)
                    {
                        // Try to save a task
                        case (Globals.ViewMode_New):
                            mTasks.addTask(name, notes, course, dueDate, timeEst);
                            Toast.makeText(this, "Task added!", Toast.LENGTH_LONG).show();

                            cTaskIndex = mTasks.getTasks().size() - 1;

                            break;

                        // Try to update a task
                        case (Globals.ViewMode_Edit):
                            mTasks.updateTask(currentTask, name, notes, course, dueDate, timeEst);
                            Toast.makeText(this, "Task updated!", Toast.LENGTH_LONG).show();

                            break;
                    }

                    // Show up the Task Activity
                    i.putExtra(Globals.ExtraKey_Tasks, mTasks);
                    i.putExtra(Globals.ExtraKey_Courses, mCourses);
                    i.putExtra(Globals.ExtraKey_Index, cTaskIndex);

                    // Set result code from Third activity to first activity
                    // Reference: https://stackoverflow.com/questions/28944137/android-get-result-from-third-activity
                    i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(i);

                    finish();

                } catch (Exception e)
                {
                    Toast.makeText(this, "Failed to add!", Toast.LENGTH_LONG).show();
                    return false;
                }
                //

                break;

            case (R.id.btnToolBarDone):
                switch (viewMode)
                {
                    case (Globals.ViewMode_New):
                        Intent i2 = new Intent();
                        i2.putExtra(Globals.ExtraKey_Courses, mCourses);
                        i2.putExtra(Globals.ExtraKey_Tasks, mTasks);

                        setResult(Globals.RESULT_SAVE, i2);

                        finish();

                        break;

                    case (Globals.ViewMode_Edit):
                        Intent i3 = new Intent(this, TaskActivity.class);
                        i3.putExtra(Globals.ExtraKey_Tasks, mTasks);
                        i3.putExtra(Globals.ExtraKey_Courses, mCourses);
                        i3.putExtra(Globals.ExtraKey_Index, cTaskIndex);

                        // Set result code from Third activity to first activity
                        // Reference: https://stackoverflow.com/questions/28944137/android-get-result-from-third-activity
                        i3.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(i3);

                        finish();

                        break;
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Long calculateMinutesFromTimeInput(String xHours, String xMinutes)
    {
        Long hours, minutes;

        try
        {
            if (xHours.isEmpty())
                hours = 0L;
            else
                hours = Long.valueOf(xHours);

            if (xMinutes.isEmpty())
                minutes = 30L;
            else
                minutes = Long.valueOf(xMinutes);

            return hours * 60 + minutes;
        } catch (Exception ignored)
        {
            return -1L;
        }
    }

    // Implement method for setting date in a dialog window
    // Reference: https://www.youtube.com/watch?v=a_Ap6T4RlYU
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        this.year = year;
        this.month = month + 1;
        this.day = dayOfMonth;

        LocalTime timeNow = LocalTime.now();

        this.hour = timeNow.getHour();
        this.minute = timeNow.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), true);
        timePickerDialog.show();
    }

    // Implement method for setting time in a dialog window
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        this.hour = hourOfDay;
        this.minute = minute;

        dueDatePicked =  LocalDateTime.of(this.year, this.month, this.day, this.hour, this.minute);

        String date = Globals.formatDateAndTime(dueDatePicked);
        TextView txtView = findViewById(R.id.txtDueDate);

        txtView.setText(date);
    }
}