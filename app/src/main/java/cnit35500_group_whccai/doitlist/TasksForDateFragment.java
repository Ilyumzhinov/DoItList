package cnit35500_group_whccai.doitlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.RecyclerViewAdapterTasks;
import Functional.Task;
import Functional.TasksControl;


public class TasksForDateFragment extends Fragment implements NotifiableFragment
{
    private TasksControl mTaskControl;
    private CoursesControl mCoursesControl;
    private List<Task> mTasksForDate;
    private CalendarView calView;
    private TextView txtViewDate, txtTasksRemainTotal;

    private RecyclerViewAdapterTasks mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tasks_for_date, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getArguments();
        if (extras != null)
        {
            // Obtain data
            mTaskControl = (TasksControl) extras.getSerializable(Globals.ExtraKey_Tasks);
            mCoursesControl = (CoursesControl) extras.getSerializable(Globals.ExtraKey_Courses);

        } else
        {
            Toast.makeText(getActivity(), "Failed to receive data", Toast.LENGTH_LONG).show();

            return;
        }

        // Save references
        txtViewDate = view.findViewById(R.id.txtDate);
        txtTasksRemainTotal = view.findViewById(R.id.txtTasksRemainTotal);

        // Set Calendar onClickListener
        calView = view.findViewById(R.id.calView);

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                updateAdapterForDate(mAdapter, year, month, dayOfMonth);
            }
        });
        //

        // Set up tasks RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.listTasks);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set empty adapter
        mAdapter = new RecyclerViewAdapterTasks(view.getContext(), null, getActivity());
        recyclerView.setAdapter(mAdapter);

        // Update adapter with data
        updateAdapterForDate(mAdapter, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue() - 1, LocalDateTime.now().getDayOfMonth());
        //
    }

    // Update values in RecyclerView on calendar date pick
    private void updateAdapterForDate(RecyclerViewAdapterTasks xAdapter, int year, int month, int dayOfMonth)
    {
        // Create date/time object
        LocalDateTime ldt = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);

        // Get a list of tasks to display
        mTasksForDate = mTaskControl.getTasks(ldt);

        // Set visual elements
        txtViewDate.setText(Globals.formatDate(ldt));

        txtTasksRemainTotal.setText(Globals.formatTimeTotal(mTaskControl.getTotalTimeRemainEst(mTasksForDate)));

        // Update adapter data
        xAdapter.updateData(mTasksForDate);
    }

    public void notifyFragment(TasksControl newTasks, CoursesControl newCourses) {
            // Obtain data
        mTaskControl = newTasks;
        mCoursesControl = newCourses;
        Date date = new Date(calView.getDate());
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        updateAdapterForDate(mAdapter, ldt.getYear(), ldt.getMonthValue() - 1, ldt.getDayOfMonth());
    }
}
