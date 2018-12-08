package cnit35500_group_whccai.doitlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.Task;
import Functional.TasksControl;
import Functional.RecyclerViewAdapterTasks;


public class TasksForDateFragment extends Fragment
{
    private View mainView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TasksControl mTaskControl;
    private CoursesControl mCourses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.fragment_tasks_for_date, null);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.listTasks);
        mLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        // Receive object through Intent
        // Reference: https://stackoverflow.com/questions/14333449/passing-data-through-intent-using-serializable
        Bundle extras = getArguments();
        if (extras != null)
        {
            // Obtain data
            mTaskControl = (TasksControl) extras.getSerializable(Globals.ExtraKey_Tasks);
            mCourses = (CoursesControl) extras.getSerializable(Globals.ExtraKey_Courses);

        } else
        {
            Toast.makeText(getActivity(), "Failed to receive data", Toast.LENGTH_LONG).show();

            return;
        }

        // Set up tasks RecyclerView
        List<Task> tasksList = Arrays.asList(mTaskControl.getTasks());

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapterTasks adapter = new RecyclerViewAdapterTasks(view.getContext(), tasksList, getActivity());
        recyclerView.setAdapter(adapter);
        //

        //set Calendar onClickListener
        /*
        recyclerView.setAdapter(adapter);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        TasksControl tasksControl = new TasksControl();
        final Task[] tasks = tasksControl.getTasks();
        calendarView.getDate();
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewLayout(R.layout.task_item_overview);
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                TasksControl tasksControl = new TasksControl();
                tasksControl.getTasks();
                ManageTaskActivity manageTaskActivity = new ManageTaskActivity();
                for (Task task : manageTaskActivity.mTasks.getTasks()) {
                    if (task.getDeadline().getYear() == year && task.getDeadline().getMonthValue() == month && task.getDeadline().getDayOfMonth() == dayOfMonth ) {
                        tasksControl.addTask(task.getName(),task.getDetail(),task.getCourse(),task.getDeadline(),task.getTimeGoal());
                    }
                }
            }
        });
        */
    }

    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.addView(mainView);
    }
}
