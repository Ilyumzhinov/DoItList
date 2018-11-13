package cnit35500_group_whccai.doitlist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Objects;

import Functional.Course;
import Functional.CoursesControl;
import Functional.TasksControl;

public class NewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    private final static int cNewCourseRequestCode = 1, cNewTaskRequestCode = 2, cOpenTaskRequestCode = 3;

    private TasksControl mTasks;
    private CoursesControl mCourses;
    private Menu menu;

    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        // Receive data through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // Obtain data
            mTasks = (TasksControl) getIntent().getSerializableExtra("tasks");
            mCourses = (CoursesControl) getIntent().getSerializableExtra("courses");
        } else {
            Toast.makeText(this, "Failed to receive data", Toast.LENGTH_SHORT).show();

            finish();
        }
        //

        // Set up toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.ios_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout col_toolbar = findViewById(R.id.ios_col_toolbar);
        col_toolbar.setTitle("New Task");
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

    public void highlight(View view)
    {
    }


    public void PickDate(View view)
    {
        Calendar calc = Calendar.getInstance();
        this.year = calc.get(Calendar.YEAR);
        this.month = calc.get(Calendar.MONTH);
        this.day = calc.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, this.year, this.month, this.day);
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
        updateMenuVisibles("sd");

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

        } else if (id == R.id.btnSaveToolBar)
        {
            // Try to get input values
            String name, notes;
            Course course;
            LocalDateTime dueDate;
            Integer timeEst;
            Boolean highlight;
            try
            {
                name = ((TextView) findViewById(R.id.edtTaskName)).getText().toString();
                notes = ((TextView) findViewById(R.id.edtNotes)).getText().toString();
                course = (Course) ((Spinner) findViewById(R.id.spnCourse)).getSelectedItem();
                dueDate = LocalDateTime.of(yearFinal, monthFinal, dayFinal, hourFinal, minuteFinal);
                int timefromInput = calculateMinutesFromTimeInput(((EditText) findViewById(R.id.edtTimeEst)).getText().toString());
                if (timefromInput >= 0)
                    timeEst = calculateMinutesFromTimeInput(((EditText) findViewById(R.id.edtTimeEst)).getText().toString());
                else
                    throw new Exception();
                highlight = ((CheckBox) findViewById(R.id.chkHighlight)).isChecked();
            } catch (Exception e)
            {
                Toast.makeText(this, "Check input values!", Toast.LENGTH_SHORT).show();
                return false;
            }
            //

            // Try to save a task
            try
            {
                mTasks.addTask(name, notes, course, dueDate, timeEst, highlight);
                Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent();
                i.putExtra("tasks", mTasks);
                i.putExtra("courses", mCourses);
                setResult(RESULT_OK, i);

                View emptyView = new View(this);
                emptyView.setId(mTasks.getTasks().length - 1);
                OpenTask(emptyView);

                finish();
            } catch (Exception e)
            {
                Toast.makeText(this, "Failed to add!", Toast.LENGTH_SHORT).show();
                return false;
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

    public void OpenTask(View view)
    {
        Intent i = new Intent(this, TaskActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra("tasks", mTasks);
        i.putExtra("courses", mCourses);
        i.putExtra("index", view.getId());

        startActivityForResult(i, cOpenTaskRequestCode);
    }

    public Integer calculateMinutesFromTimeInput(String xTime)
    {
        Integer hours = 0, minutes = 0;

        try
        {
            String[] parsedTime = xTime.split(":");
            hours = Integer.valueOf(parsedTime[0]);
            minutes = Integer.valueOf(parsedTime[1]);

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
        this.yearFinal = year;
        this.monthFinal = month + 1;
        this.dayFinal = dayOfMonth;

        LocalTime timeNow = LocalTime.now();

        this.hour = timeNow.getHour();
        this.minute = timeNow.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, this.hour, this.minute, true);
        timePickerDialog.show();
    }

    // Implement method for setting time in a dialog window
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        this.hourFinal = hourOfDay;
        this.minuteFinal = minute;

        TextView txtView = findViewById(R.id.txtDueDate);

        String date = String.valueOf(this.monthFinal) + "-" + String.valueOf(this.dayFinal) + "-" + String.valueOf(this.yearFinal) + " " + String.valueOf(this.hourFinal) + ":" + String.valueOf(this.minuteFinal);

        txtView.setText(date);

        // Set new defaults
        this.year = this.yearFinal;
        this.month = this.monthFinal;
        this.day = this.dayFinal;
        this.hour = this.hourFinal;
        this.minute = this.minuteFinal;
    }
}