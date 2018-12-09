package Functional;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cnit35500_group_whccai.doitlist.R;

// Reference: https://stackoverflow.com/questions/28460300/how-to-build-a-horizontal-listview-with-recyclerview
// Reference: 4 - Advanced User Interface I
public class RecyclerViewAdapterCourseScope extends RecyclerView.Adapter<RecyclerViewAdapterCourseScope.ViewHolder>
{
    private List<Course> mCoursesAtScope;
    private LayoutInflater mInflater;
    private RecyclerViewAdapterCourseScope.ItemClickListener mClickListener;
    private int mSelected_position = 0;

    public RecyclerViewAdapterCourseScope(Context context, List<Course> xCourses)
    {
        uglyFunc(context, xCourses);
    }

    public RecyclerViewAdapterCourseScope(Context context, Course xCourse)
    {
        List<Course> xCourses = xCourse.getScopeArrayOf(false);

        uglyFunc(context, xCourses);
    }

    // A common logic for both constructors to call
    private void uglyFunc(Context context, List<Course> xCourses)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mCoursesAtScope = xCourses;
    }

    public List<Course> getmCoursesAtScope()
    {
        return mCoursesAtScope;
    }

    // Inflate the row layout from xml
    @Override
    @NonNull
    public RecyclerViewAdapterCourseScope.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.course_item, parent, false);
        return new RecyclerViewAdapterCourseScope.ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterCourseScope.ViewHolder holder, int position)
    {
        holder.itemView.setSelected(mSelected_position == position);

        int color = mCoursesAtScope.get(position).getAssociatedColor();
        String label = mCoursesAtScope.get(position).getName();

        holder.txtItemLabel.setText(label);
        Drawable dr = holder.txtItemLabel.getBackground();
        dr.setTint(color);
    }

    // Receive number of items
    @Override
    public int getItemCount()
    {
        return mCoursesAtScope.size();
    }

    // Get item at click position
    public Course getItem(int id)
    {
        return mCoursesAtScope.get(id);
    }

    // Get click events
    public void setClickListener(RecyclerViewAdapterCourseScope.ItemClickListener itemClickListener)
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
        TextView txtItemLabel;

        ViewHolder(View itemView)
        {
            super(itemView);
            txtItemLabel = itemView.findViewById(R.id.txtLabel);

//            if (1 == 1)
//                txtItemLabel.setOnClickListener(this);
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