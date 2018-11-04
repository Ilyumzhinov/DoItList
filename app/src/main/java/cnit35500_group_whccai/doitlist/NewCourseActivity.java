package cnit35500_group_whccai.doitlist;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

public class NewCourseActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Add back button
        // Reference: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_new_course);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            onSupportNavigateUp();
        }
        return super.onOptionsItemSelected(item);
    }

    // Reference: https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}