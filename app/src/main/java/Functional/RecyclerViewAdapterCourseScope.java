package Functional;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cnit35500_group_whccai.doitlist.R;

// Reference: https://stackoverflow.com/questions/28460300/how-to-build-a-horizontal-listview-with-recyclerview
// Reference: 4 - Advanced User Interface I
public class RecyclerViewAdapterCourseScope extends RecyclerView.Adapter<RecyclerViewAdapterCourseScope.ViewHolder>
{
    private List<Course> mCoursesAtScope;
    private LayoutInflater mInflater;
    private int mSelected_position = 0;

    public RecyclerViewAdapterCourseScope(Context context)
    {
        uglyFunc(context, null);
    }

    public RecyclerViewAdapterCourseScope(Context context, Course xCourse)
    {
        uglyFunc(context, xCourse);
    }

    // A common logic for both constructors to call
    private void uglyFunc(Context context, Course xCourse)
    {
        mInflater = LayoutInflater.from(context);

        mCoursesAtScope = new ArrayList<>();

        if (null != xCourse)
            mCoursesAtScope.add(xCourse);
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

    // Do the recycle view stuff
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtItemLabel;

        ViewHolder(View itemView)
        {
            super(itemView);
            txtItemLabel = itemView.findViewById(R.id.txtLabel);
        }

    }

    // Update adapter
    // Reference: https://stackoverflow.com/questions/30053610/best-way-to-update-data-with-a-recyclerview-adapter?lq=1
    public void updateData(List<Course> xCourses)
    {
        if (xCourses == null)
        {
            mCoursesAtScope.clear();
            notifyDataSetChanged();

            return;
        }

        if (mCoursesAtScope != null && !xCourses.isEmpty())
            mCoursesAtScope.clear();

        mCoursesAtScope.addAll(xCourses);
        notifyDataSetChanged();
    }
}