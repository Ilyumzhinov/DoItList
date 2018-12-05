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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.Task;
import Functional.TasksControl;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private CoursesControl mCourses;
    private TasksControl mTasks;
    private ListView listTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Receive data through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            // Obtain data
            mTasks = (TasksControl) getIntent().getSerializableExtra(Globals.ExtraKey_Tasks);
            mCourses = (CoursesControl) getIntent().getSerializableExtra(Globals.ExtraKey_Courses);
        } else
        {
            Toast.makeText(this, "Failed to receive data", Toast.LENGTH_LONG).show();

            finish();
        }
        //

        // Set default fragment
        fragment = new TasksForDateFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.navContent, fragment).commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NavigationActivity.this, ManageTaskActivity.class);

                // Pass an object to another activity
                // Reference: https://stackoverflow.com/questions/2736389/
                i.putExtra(Globals.ExtraKey_ViewMode,Globals.ViewMode_New);
                i.putExtra(Globals.ExtraKey_Tasks, mTasks);
                i.putExtra(Globals.ExtraKey_Courses, mCourses);

                startActivityForResult(i, Globals.ManageTaskRequestCode);

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
            case R.id.nav_list:
                fragment = new TasksForDateFragment();
                break;
            case R.id.nav_class:
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                break;
            case R.id.nav_main:
                finish();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.navContent,fragment).commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
}
