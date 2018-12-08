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

import java.util.List;

import cnit35500_group_whccai.doitlist.MainNavigationActivity;
import cnit35500_group_whccai.doitlist.R;


public class RecyclerViewAdapterTasks extends RecyclerView.Adapter<RecyclerViewAdapterTasks.ViewHolder>
{
    private List<Task> mTasks;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity mParent;

    // 1 - A common logic for both constructors to call
    public RecyclerViewAdapterTasks(Context context, List<Task> Tasks, Activity parent)
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

    private void setupTaskViewHolder(final RecyclerViewAdapterTasks.ViewHolder xVH, final int position)
    {
        final Task iTask = mTasks.get(position);

        // Get values
        // Get status
        String status = iTask.getStatusStr(),
                taskName = iTask.getName(),
                courseName = iTask.getCourse().getScopeStrOf(false);

        Long timeEst = iTask.getTimeGoal(),
                timeSpent = iTask.getTimeSpent(),
                timeRemain = iTask.getTimeRemainEst(),
                timeBeforeDeadline = iTask.getTimeBeforeDeadline();

        // Set values
        xVH.txtTaskStatus.setText(status);
        xVH.txtTaskName.setText(taskName);
        xVH.txtTaskCourse.setText(courseName);
        xVH.txtTimePrg.setText(Globals.formatTimeTotal(timeRemain));
        xVH.txtTaskBeforeDeadline.setText(Globals.formatTimeTotal(timeBeforeDeadline));

        xVH.prgSpent.setIndeterminate(false);
        xVH.prgSpent.setMax(Math.toIntExact(timeEst));
        xVH.prgSpent.setProgress(Math.toIntExact(timeSpent));

        // Set events
        // Start/stop recording time
        xVH.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Task taskReceived = mTasks.get(position);
                taskReceived.updateRecordingTime();

                xVH.txtTaskStatus.setText(iTask.getStatusStr());
            }
        });

        // Open task details
        xVH.btnTaskOpenDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    ((MainNavigationActivity) mParent).openTask(null, mTasks.get(position));
                } catch (Exception ignored)
                {
                    Toast.makeText(v.getContext(), "Failed to open a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Complete task
        xVH.btnTaskComplete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean taskStatusFinished = iTask.getStatusFinished();

                iTask.setStatusFinished(!taskStatusFinished);

                String status = iTask.getStatusStr();

                xVH.txtTaskStatus.setText(status);
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
