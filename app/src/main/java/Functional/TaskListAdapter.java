package Functional;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.PortUnreachableException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import cnit35500_group_whccai.doitlist.R;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<Task> mTasks;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int mSelected_position = 0;

    public TaskListAdapter (Context context, List<Task> Tasks) {
        this.mInflater = LayoutInflater.from(context);
        this.mTasks = Tasks;
    }

    // Inflate the row layout from xml
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.itemView.setSelected(mSelected_position == position);
        String status;
        if (mTasks.get(position).getStatusFinished())
            status = "Finished";
        else
        {
            if (mTasks.get(position).getSessions().getSessions().length == 0)
                status = "Not started";
            else if (mTasks.get(position).getSessions().checkOpenSession())
                status = "Active";
            else
                status = "Not finished";
        }
        Integer remain = mTasks.get(position).getTimeEst() - mTasks.get(position).getTimeSpent();
        long timeBefore = ChronoUnit.MINUTES.between(LocalDateTime.now(), mTasks.get(position).getDeadline());
        holder.txtTaskName.setText(mTasks.get(position).getName());
        holder.txtTaskStatus.setText(status);
        holder.txtTaskCourse.setText(mTasks.get(position).getCourse().getName());
        holder.txtTimePrg.setText(String.valueOf(remain) + " min");
        holder.txtTaskBeforeDeadline.setText(String.valueOf(timeBefore) + " min");
        holder.prgSpent.setIndeterminate(false);
        holder.prgSpent.setMax(mTasks.get(position).getTimeEst());
        holder.prgSpent.setProgress(mTasks.get(position).getTimeSpent());
        holder.btnTaskOpenDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnTaskComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    // Do the recycle view stuff
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txtTaskStatus;
        TextView txtTaskName;
        TextView txtTimePrg;
        TextView txtTaskCourse;
        TextView txtTaskBeforeDeadline;
        Button btnTaskComplete;
        Button btnTaskOpenDetails;
        ProgressBar prgSpent;

        ViewHolder(View itemView)
        {
            super(itemView);
            txtTaskCourse = itemView.findViewById(R.id.txtTaskCourse);
            txtTaskStatus = itemView.findViewById(R.id.txtTaskStatus);
            txtTaskName = itemView.findViewById(R.id.txtTaskName);
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
            // Set activated item
            // Reference: https://stackoverflow.com/questions/27194044/how-to-properly-highlight-selected-item-on-recyclerview
            notifyItemChanged(mSelected_position);
            mSelected_position = getAdapterPosition();
            notifyItemChanged(mSelected_position);
        }
    }
}
