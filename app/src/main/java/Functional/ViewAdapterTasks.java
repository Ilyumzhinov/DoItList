package Functional;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import cnit35500_group_whccai.doitlist.NavigationActivity;
import cnit35500_group_whccai.doitlist.R;


public class ViewAdapterTasks extends RecyclerView.Adapter<ViewAdapterTasks.ViewHolder>
{
    private List<Task> mTasks;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity mParent;

    // 1 - A common logic for both constructors to call
    public ViewAdapterTasks(Context context, List<Task> Tasks, Activity parent)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mTasks = Tasks;
        this.mParent = parent;
    }

    // 2 - Do the recycle view stuff
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtTaskStatus,
                txtTaskName,
                txtTimePrg,
                txtTaskCourse,
                txtTaskBeforeDeadline;
        ImageView btnTaskComplete,
                btnTaskOpenDetails;
        ProgressBar prgSpent;

        // Try to assign views from inflated layout
        ViewHolder(View itemView)
        {
            super(itemView);
            txtTaskStatus = itemView.findViewById(R.id.txtTaskStatus);
            txtTaskName = itemView.findViewById(R.id.txtTaskName);
            txtTaskCourse = itemView.findViewById(R.id.txtTaskCourse);
            txtTimePrg = itemView.findViewById(R.id.txtTimePrg);
            txtTaskBeforeDeadline = itemView.findViewById(R.id.txtTaskBeforeDeadline);
            btnTaskComplete = itemView.findViewById(R.id.btnTaskComplete);
            btnTaskOpenDetails = itemView.findViewById(R.id.btnTaskOpenDetails);
            prgSpent = itemView.findViewById(R.id.prgSpent);
        }

        // Todo: remove comment
        @Override
        public void onClick(View view)
        {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());

            Task taskReceived = mTasks.get(getAdapterPosition());
            taskReceived.updateRecordingTime();
        }
    }

    // 3 - Inflate the row layout from xml
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.task_item_overview, parent, false);
        return new ViewHolder(view);
    }

    // 4 - Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        setupTaskViewHolder(holder, position);
    }

    private void setupTestTaskViewHolder(ViewAdapterTasks.ViewHolder xVH, int position)
    {
        String name = mTasks.get(position).getName();

        xVH.txtTaskName.setText(name);
    }

    private void setupTaskViewHolder(final ViewAdapterTasks.ViewHolder xVH, final int position)
    {
        final Task iTask = mTasks.get(position);

        // Get values
        // Get status
        String status = iTask.getStatusStr(),
                taskName = iTask.getName(),
                courseName = iTask.getCourse().getScopeStrOf(false);

        Integer remain = (iTask.getTimeEst() - iTask.getTimeSpent()),
                timeEst = iTask.getTimeEst(),
                timeSpent = iTask.getTimeSpent();

        Long timeBefore = ChronoUnit.MINUTES.between(LocalDateTime.now(), iTask.getDeadline());

        // Set values
        xVH.txtTaskStatus.setText(status);
        xVH.txtTaskName.setText(taskName);
        xVH.txtTaskCourse.setText(courseName);
        xVH.txtTimePrg.setText(Globals.formatTimeTotal(remain));
        xVH.txtTaskBeforeDeadline.setText(Globals.formatDate(timeBefore));

        xVH.prgSpent.setIndeterminate(false);
        xVH.prgSpent.setMax(timeEst);
        xVH.prgSpent.setProgress(timeSpent);

        // Set button actions
        xVH.btnTaskOpenDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    ((NavigationActivity) mParent).openTask(null, mTasks.get(position));
                } catch (Exception ignored)
                {
                    Toast.makeText(v.getContext(), "Failed to open a task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        xVH.btnTaskComplete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean taskStatusFinished = iTask.getStatusFinished();

                String status = iTask.getStatusStr();

                xVH.txtTaskStatus.setText(status);
                iTask.setStatusFinished(!taskStatusFinished);
            }
        });
    }

    // Receive number of items
    @Override
    public int getItemCount()
    {
        return mTasks.size();
    }

    // Get item at click position
    public Task getItem(int id)
    {
        return mTasks.get(id);
    }

    // Get click events
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener
    {
        void onItemClick(View view, int position);
    }
}
