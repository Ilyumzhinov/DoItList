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
import android.widget.ListView;
import android.widget.Toast;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.TasksControl;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    private CoursesControl mCourses;
    private TasksControl mTasks;
    private SaveTasks saveTasks;
    ListView listTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialise data
        saveTasks = new SaveTasks();
        saveTasks.createSaveTasks(this);

        // Try to load saved courses
        try {
            mCourses = saveTasks.openCoursesFile();
            if (mCourses == null) {
                throw new Exception("bad");
            }
        } catch (Exception e) {
            // This should only get hit if this is the first run and nothing has been saved
            Toast.makeText(this, "Error loading Courses", Toast.LENGTH_SHORT).show();
            mCourses = new CoursesControl();
        }

        // Try to load saved tasks
        try {
            mTasks = saveTasks.openTasksFile();
            if (mTasks == null) {
                throw new Exception("bad");
            }
        } catch (Exception e) {
            // This should only get hit if this is the first run and nothing has been saved
            Toast.makeText(this, "Error loading Tasks", Toast.LENGTH_SHORT).show();
            mTasks = new TasksControl();
        }


        fragment = new TaskListFragment();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                fragment = new TaskListFragment();
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
}
