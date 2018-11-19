package cnit35500_group_whccai.doitlist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import Functional.Course;
import Functional.CoursesControl;
import Functional.Globals;
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

                    // Set global values
                    dueDatePicked = LocalDateTime.now();
                    dueDatePicked = dueDatePicked.plusDays(7);

                    col_toolbar.setTitle("New Task");

                    // Populate views with data
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy, HH:mm");
                    ((TextView) findViewById(R.id.txtDueDate)).setText(dueDatePicked.format(formatter));

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
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM-dd-yyyy, HH:mm");

                    // Todo: set spinner for saved course
                    ((TextView) findViewById(R.id.edtTaskName)).setText(currentTask.getName());
                    ((TextView) findViewById(R.id.txtDueDate)).setText(dueDatePicked.format(formatter2));
                    ((EditText) findViewById(R.id.edtTimeEstH)).setText(String.valueOf(Math.round(currentTask.getTimeEst() / 60)));
                    ((EditText) findViewById(R.id.edtTimeEstM)).setText(String.valueOf(currentTask.getTimeEst() % 60));
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

        // Populate spinner with courses values
        // Reference: SpinnerTest1
        Spinner spinner = findViewById(R.id.spnCourse);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        //
    }

    // Open a NewCourse activity
    public void NewCourse(View view)
    {
        Intent i = new Intent(this, NewCourseActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra(Globals.ExtraKey_Courses, mCourses);
        i.putExtra(Globals.ExtraKey_Parent, new Course());

        startActivityForResult(i, Globals.NewCourseRequestCode);
    }

    // Open a NewCourse activity with scope
    public void NewCourseWithScope(View view)
    {
        Intent i = new Intent(this, NewCourseActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra(Globals.ExtraKey_Courses, mCourses);

        // Todo: change the hardcoded value
        i.putExtra(Globals.ExtraKey_Parent, mCourses.getCourses()[0]);

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
                    }

                    // Populate spinner with courses values
                    // Reference: SpinnerTest1
                    Spinner spinner = findViewById(R.id.spnCourse);

                    // Create an ArrayAdapter
                    ArrayAdapter<Course> adapter = new ArrayAdapter<>
                            (this, android.R.layout.simple_spinner_item, mCourses.getCourses());

                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
                break;
        }
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

    public void PickDate(View view)
    {
        Calendar calc = Calendar.getInstance();
        this.year = calc.get(Calendar.YEAR);
        this.month = calc.get(Calendar.MONTH);
        this.day = calc.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, dueDatePicked.getYear(), (dueDatePicked.getMonthValue() -1), dueDatePicked.getDayOfMonth());
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
            case (R.id.btnRemoveToolBar):
                mTasks.removeTask(currentTask);

                Toast.makeText(this, "Task removed!", Toast.LENGTH_LONG).show();

                Intent i = new Intent();
                i.putExtra(Globals.ExtraKey_Tasks, mTasks);
                i.putExtra(Globals.ExtraKey_Courses, mCourses);

                setResult(Globals.RESULT_SAVE, i);

                finish();

                break;

            case (R.id.btnSaveToolBar):
                String name, notes;
                Course course;
                LocalDateTime dueDate;
                Integer timeEst;

                // Try to get input values
                try
                {
                    name = ((TextView) findViewById(R.id.edtTaskName)).getText().toString();
                    notes = ((TextView) findViewById(R.id.edtNotes)).getText().toString();
                    course = (Course) ((Spinner) findViewById(R.id.spnCourse)).getSelectedItem();
                    dueDate = dueDatePicked;

                    int timefromInput = calculateMinutesFromTimeInput(((TextView) findViewById(R.id.edtTimeEstH)).getText().toString(), ((TextView) findViewById(R.id.edtTimeEstM)).getText().toString());
                    if (timefromInput >= 0)
                        timeEst = timefromInput;
                    else
                        throw new Exception();
                } catch (Exception e)
                {
                    Toast.makeText(this, "Check input values!", Toast.LENGTH_LONG).show();
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

                            cTaskIndex = mTasks.getTasks().length - 1;

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

            case (R.id.btnDoneToolBar):
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

    public Integer calculateMinutesFromTimeInput(String xHours, String xMinutes)
    {
        Integer hours, minutes;

        try
        {
            if (xHours.isEmpty())
                hours = 1;
            else
                hours = Integer.valueOf(xHours);

            if (xMinutes.isEmpty())
                minutes = 0;
            else
                minutes = Integer.valueOf(xMinutes);

            return hours * 60 + minutes;
        } catch (Exception ignored)
        {
            return -1;
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, dueDatePicked.getHour(), dueDatePicked.getMinute(), true);
        timePickerDialog.show();
    }

    // Implement method for setting time in a dialog window
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        this.hour = hourOfDay;
        this.minute = minute;

        TextView txtView = findViewById(R.id.txtDueDate);

        String date = String.valueOf(this.month) + "-" + String.valueOf(this.day) + "-" + String.valueOf(this.year) + " " + String.valueOf(this.hour) + ":" + String.valueOf(this.minute);

        txtView.setText(date);

        dueDatePicked =  LocalDateTime.of(this.year, this.month, this.day, this.hour, this.minute);
    }
}