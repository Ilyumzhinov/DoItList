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
import android.widget.TextView;
import android.widget.Toast;

import Functional.CoursesControl;
import Functional.Globals;
import Functional.RecyclerViewAdapterTasks;
import Functional.TasksControl;


public class TasksAllFragment extends Fragment implements NotifiableFragment
{
    private TasksControl mTaskControl;
    private CoursesControl mCoursesControl;
    private RecyclerViewAdapterTasks mAdapter;
    private TextView txtTasksRemainTotal;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tasks_all, null);
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
        txtTasksRemainTotal = view.findViewById(R.id.txtTasksRemainTotal);

        // Set up tasks RecyclerView
        recyclerView = view.findViewById(R.id.listTasks);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set empty adapter
        mAdapter = new RecyclerViewAdapterTasks(view.getContext(), null, getActivity());
        recyclerView.setAdapter(mAdapter);

        // Update adapter with data
        updateAdapter(mAdapter);
        //
    }

    // Update values in RecyclerView on calendar date pick
    private void updateAdapter(RecyclerViewAdapterTasks xAdapter)
    {
        // Set visual elements
        txtTasksRemainTotal.setText(Globals.formatTimeTotal(mTaskControl.getTotalTimeRemainEst(mTaskControl.getTasks())));

        // Update adapter data
        xAdapter.updateData(mTaskControl.getTasks());
    }

    public void notifyFragment(TasksControl newTasks, CoursesControl newCourses)
    {
        txtTasksRemainTotal.setText(Globals.formatTimeTotal(mTaskControl.getTotalTimeRemainEst(mTaskControl.getTasks())));

        // Obtain data
        mTaskControl = newTasks;
        mCoursesControl = newCourses;
        updateAdapter(mAdapter);
    }
}
