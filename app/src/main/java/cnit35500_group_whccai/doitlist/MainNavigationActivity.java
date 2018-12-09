package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.SaveTasks;
import Functional.Task;
import Functional.TasksControl;

public class MainNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private Fragment fragment;
    private CoursesControl mCourses;
    private TasksControl mTasks;
    private SaveTasks saveTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        // Initialise data
        saveTasks = new SaveTasks();
        saveTasks.createSaveTasks(this);

        // Try to load saved courses
        try
        {
            mCourses = saveTasks.openCoursesFile();
            if (mCourses == null)
                throw new Exception();
        } catch (Exception e)
        {
            // This should only get hit if this is the first run and nothing has been saved
            Toast.makeText(this, "Error loading Courses", Toast.LENGTH_SHORT).show();
            mCourses = new CoursesControl();
        }

        // Try to load saved tasks
        try
        {
            mTasks = saveTasks.openTasksFile();
            if (mTasks == null)
                throw new Exception();
        } catch (Exception e)
        {
            // This should only get hit if this is the first run and nothing has been saved
            Toast.makeText(this, "Error loading Tasks", Toast.LENGTH_SHORT).show();
            mTasks = new TasksControl();
        }

        // Set default fragment
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Globals.ExtraKey_Tasks, mTasks);
//        bundle.putSerializable(Globals.ExtraKey_Courses, mCourses);
//
//        fragment = new TasksForDateFragment();
//        fragment.setArguments(bundle);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.navContent, fragment).commit();

        createTasksForDate();

        // Add a new task action
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                openManageTask_New(view);
            }
        });

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.ios_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_date:
//                fragment = new TasksForDateFragment();

                createTasksForDate();
                break;
            case R.id.nav_class:
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.navContent,fragment).commit();
                break;
        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.navContent,fragment).commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createTasksForDate()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Globals.ExtraKey_Tasks, mTasks);
        bundle.putSerializable(Globals.ExtraKey_Courses, mCourses);

        fragment = new TasksForDateFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.navContent, fragment).commit();
    }

    // Open Task activity
    public void openTask(View view, Task task)
    {
        Intent i = new Intent(this, TaskActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra(Globals.ExtraKey_Tasks, mTasks);
        i.putExtra(Globals.ExtraKey_Courses, mCourses);

        i.putExtra(Globals.ExtraKey_Index, mTasks.getTaskIndexFor(task));

        startActivityForResult(i, Globals.OpenTaskRequestCode);
    }

    // Open Manage Task activity in New mode
    public void openManageTask_New(View view)
    {
        Intent i = new Intent(this, ManageTaskActivity.class);

        // Pass an object to another activity
        // Reference: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        i.putExtra(Globals.ExtraKey_ViewMode, Globals.ViewMode_New);
        i.putExtra(Globals.ExtraKey_Tasks, mTasks);
        i.putExtra(Globals.ExtraKey_Courses, mCourses);

        startActivityForResult(i, Globals.ManageTaskRequestCode);
    }


    // Show or hide calendar view
    public void displayCalendar(View v)
    {
        try
        {
            CalendarView calView = findViewById(R.id.calView);
            TextView tv = (TextView) v;

            if (calView.getVisibility() == View.VISIBLE)
            {
                calView.setVisibility(View.GONE);
                tv.setText(R.string.lbl_reveal_calendar);
            } else
            {
                calView.setVisibility(View.VISIBLE);
                tv.setText(R.string.lbl_hide_calendar);
            }
        } catch (Exception ignored)
        {
            Toast.makeText(this, "Failed to load calendar", Toast.LENGTH_SHORT).show();
        }
    }

    // Receive data from other activities
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            // Receive Intent info from ManageTaskActivity
            case (Globals.ManageTaskRequestCode):
                if (resultCode == Globals.RESULT_SAVE)
                {
                    // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
                    // Receive object through Intent
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        // Obtain data
                        mTasks = (TasksControl) data.getSerializableExtra(Globals.ExtraKey_Tasks);
                        mCourses = (CoursesControl) data.getSerializableExtra(Globals.ExtraKey_Courses);
                        ((TasksForDateFragment) fragment).notifyFragment(mTasks, mCourses);
                    }
                }
                break;

            case (Globals.OpenTaskRequestCode):
                if (resultCode == Globals.RESULT_SAVE)
                {
                    Bundle extras = data.getExtras();
                    if (extras != null)
                    {
                        // Obtain data
                        mTasks = (TasksControl) data.getSerializableExtra(Globals.ExtraKey_Tasks);
                        mCourses = (CoursesControl) data.getSerializableExtra(Globals.ExtraKey_Courses);
                        ((TasksForDateFragment) fragment).notifyFragment(mTasks, mCourses);
                    }
                }
        }
    }

    // Save mTasks and mCourses prior to stopping
    @Override
    protected void onStop()
    {
        saveTasks.saveFile(mTasks);
        saveTasks.saveFile(mCourses);
        super.onStop();
    }

    // Todo: thread code
    //    Thread thUpdateViews = new Thread()
//    {
//        @Override
//        public void run()
//        {
//            int spentTime = 0;
//            int totalTime = 9999;
//
//            while (spentTime < totalTime)
//            {
//                handler.sendMessage(new Message());
//
//                try
//                {
//                    Thread.sleep(1000); // Just to display the progress
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//                spentTime += 1;
//            }
//        }
//    };
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            Task xtask;
//
//            try
//            {
//                xtask = mTasks.getTaskAt(0);
//            }
//            catch (Exception c)
//            {
//                return;
//            }
//
//            TextView txt;
//
//            // Set status label
//            txt = findViewById(R.id.txtTaskStatus);
//
//            String status;
//
//            if (xtask.getStatusFinished())
//                status = "Finished";
//            else
//            {
//                if (xtask.getSessions().getSessions().length == 0)
//                    status = "Not started";
//                else if (xtask.getSessions().checkOpenSession())
//                    status = "Active";
//                else
//                    status = "Not finished";
//            }
//            txt.setText(status);
//            //
//
//            //
//            Integer remain = xtask.getTimeGoal() - xtask.getTimeSpent();
//
//            txt = findViewById(R.id.txtTimePrg);
//            txt.setText(String.valueOf(remain) + " min");
//
//            //
//            long timeBefore = ChronoUnit.MINUTES.between(LocalDateTime.now(), xtask.getDeadline());
//
//            txt = findViewById(R.id.txtTaskBeforeDeadline);
//            txt.setText(String.valueOf(timeBefore) + " min");
//
//
//            //
//            ProgressBar timeSpent = findViewById(R.id.prgSpent);
//            timeSpent.setProgress(xtask.getTimeSpent());
//        }
//    };
}
