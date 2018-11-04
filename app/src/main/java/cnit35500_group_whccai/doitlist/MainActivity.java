package cnit35500_group_whccai.doitlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void NewTask(View view) {
        Intent i = new Intent(this,TaskActivity.class);
        startActivity(i);
    }

    public void NewCourse(View view) {
        Intent i = new Intent(this,NewCourseActivity.class);
        startActivity(i);
    }
}
