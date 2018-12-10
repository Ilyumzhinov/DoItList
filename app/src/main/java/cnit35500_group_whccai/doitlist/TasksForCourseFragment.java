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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Functional.Course;
import Functional.CoursesControl;
import Functional.Globals;
import Functional.RecyclerViewAdapterCourseSelection;
import Functional.RecyclerViewAdapterTasks;
import Functional.Task;
import Functional.TasksControl;


public class TasksForCourseFragment extends Fragment implements NotifiableFragment
{
    private TasksControl mTaskControl;
    private CoursesControl mCoursesControl;
    private List<Task> mTasksForScope;

    private TextView txtViewDate, txtTasksRemainTotal;
    private RecyclerView rvCourses, rvCategories;

    private RecyclerViewAdapterTasks mAdapter;
    private RecyclerViewAdapterCourseSelection mAdapterCourses, mAdapterCategories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tasks_for_course, null);
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

        // Populate course selection
        rvCourses = view.findViewById(R.id.rvCourses);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCourses.setLayoutManager(horizontalLayoutManager);

        mAdapterCourses = new RecyclerViewAdapterCourseSelection(view.getContext(), mCoursesControl.getCoursesAtScope(mCoursesControl.getEmptyCourseScope()));

        // Catch RecyclerView clicks
        // Reference: https://stackoverflow.com/questions/27703779/detect-click-on-recyclerview-outside-of-items
        mAdapterCourses.setClickListener(new RecyclerViewAdapterCourseSelection.ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                performOnClickChanges(view, position);
            }
        });

        rvCourses.setAdapter(mAdapterCourses);
        //

        // Set up course selection
        rvCategories = view.findViewById(R.id.rvCategories);
        LinearLayoutManager horizontalLayoutManager2
                = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(horizontalLayoutManager2);

        mAdapterCategories = new RecyclerViewAdapterCourseSelection(view.getContext(), mCoursesControl.getCoursesAtScope(mAdapterCourses.getCoursesScope()));
        // Catch RecyclerView clicks
        mAdapterCategories.setClickListener(new RecyclerViewAdapterCourseSelection.ItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                performOnClickChanges(view, position);
            }
        });

        rvCategories.setAdapter(mAdapterCategories);

        // Set up tasks RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.listTasks);
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set empty adapter
        mAdapter = new RecyclerViewAdapterTasks(view.getContext(), null, getActivity());
        recyclerView.setAdapter(mAdapter);

        // Update adapter data
        updateRecyclerViewsVisibles(mAdapterCourses, mAdapterCategories);
        updateAdapterForScope(mAdapter, getCourseSelection());
        //
    }

    public Course getCourseSelection()
    {
        Course course = null;

        if (null != mAdapterCategories.getSelectedCourse() && null != mAdapterCourses.getSelectedCourse())
            course = mAdapterCategories.getSelectedCourse();
        else if (null != mAdapterCourses.getSelectedCourse())
            course = mAdapterCourses.getSelectedCourse();

        return course;
    }

    private void performOnClickChanges(View v, int position)
    {
        mAdapterCategories.updateData(mCoursesControl.getCoursesAtScope(mAdapterCourses.getItem(position).getScopeStrArrayOf(false)));

        updateRecyclerViewsVisibles(mAdapterCourses, mAdapterCategories);

        updateAdapterForScope(mAdapter, getCourseSelection());
    }

    // Update RecyclerViews visibility state
    private void updateRecyclerViewsVisibles(RecyclerViewAdapterCourseSelection xCoursesAdapter, RecyclerViewAdapterCourseSelection xCategoriesAdapter)
    {
        final RecyclerView rvCourses = getView().findViewById(R.id.rvCourses),
                rvCategories = getView().findViewById(R.id.rvCategories);

        final TextView txtCoursesNoItems = getView().findViewById(R.id.lblCoursesNoItems),
                txtCategoriesNoItems = getView().findViewById(R.id.lblCategoriesNoItems);

        final LinearLayout lytCourseLevelDivider = getView().findViewById(R.id.lytCourseLevelDivider),
                lytCategories = getView().findViewById(R.id.lytCourseCategory);

        // Update categories visible
        if (xCoursesAdapter.getItemCount() < 1)
        {
            txtCoursesNoItems.setVisibility(View.VISIBLE);
            rvCourses.setVisibility(View.GONE);
        } else
        {
            txtCoursesNoItems.setVisibility(View.GONE);
            rvCourses.setVisibility(View.VISIBLE);

        }

        // Update categories visible
        if (xCategoriesAdapter.getItemCount() < 1)
        {
            lytCourseLevelDivider.setVisibility(View.GONE);
            lytCategories.setVisibility(View.GONE);
        } else
        {
            txtCategoriesNoItems.setVisibility(View.GONE);
            rvCategories.setVisibility(View.VISIBLE);

            lytCourseLevelDivider.setVisibility(View.VISIBLE);
            lytCategories.setVisibility(View.VISIBLE);
        }
    }

    // Update values in RecyclerView on calendar date pick
    private void updateAdapterForScope(RecyclerViewAdapterTasks xAdapter, Course xCourse)
    {
        if (null == xCourse)
            return;

        // Get a list of tasks to display
        mTasksForScope = mTaskControl.getTasks(xCourse.getScopeStrArrayOf(false));

        // Set visual elements
        txtViewDate.setText(xCourse.getScopeStrOf(false));

        txtTasksRemainTotal.setText(Globals.formatTimeTotal(mTaskControl.getTotalTimeRemainEst(mTasksForScope)));

        // Update adapter data
        xAdapter.updateData(mTasksForScope);
    }

    public void notifyFragment(TasksControl newTasks, CoursesControl newCourses)
    {
        // Obtain data
        mTaskControl = newTasks;
        mCoursesControl = newCourses;

        updateAdapterForScope(mAdapter, getCourseSelection());
    }
}
