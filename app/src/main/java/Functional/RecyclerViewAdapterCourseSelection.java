package Functional;

import android.content.Context;
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
public class RecyclerViewAdapterCourseSelection extends RecyclerView.Adapter<RecyclerViewAdapterCourseSelection.ViewHolder>
{
    private List<Course> mCoursesAtScope;
    private LayoutInflater mInflater;
    private RecyclerViewAdapterCourseSelection.ItemClickListener mClickListener;
    private int mSelected_position = 0;
//    private View mSelected_view;

    public Course getSelectedCourse()
    {
        try {
            return mCoursesAtScope.get(mSelected_position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public RecyclerViewAdapterCourseSelection(Context context, List<Course> xCourses)
    {
        mInflater = LayoutInflater.from(context);

        if (xCourses != null)
            mCoursesAtScope = xCourses;
        else
            mCoursesAtScope = new ArrayList<>();
    }

    public List<String> getCoursesScope()
    {
        try
        {
            Course cr = mCoursesAtScope.get(mSelected_position);
            return cr.getScopeStrArrayOf(false);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
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

        if (mCoursesAtScope != null && xCourses.size() > 0)
            mCoursesAtScope.clear();

        mCoursesAtScope.addAll(xCourses);
        notifyDataSetChanged();
    }

    // Inflate the row layout from xml
    @Override
    @NonNull
    public RecyclerViewAdapterCourseSelection.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.recommendation_item, parent, false);
        return new RecyclerViewAdapterCourseSelection.ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterCourseSelection.ViewHolder holder, int position)
    {
        holder.itemView.setSelected(mSelected_position == position);

        int color = mCoursesAtScope.get(position).getAssociatedColor();
        String label = mCoursesAtScope.get(position).getName();

        holder.txtItemLabel.setText(label);
//        Drawable dr = holder.txtItemLabel.getBackground();
//        dr.setTint(color);
    }

    // Receive number of items
    @Override
    public int getItemCount()
    {
        try {
            return mCoursesAtScope.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Get item at click position
    public Course getItem(int index)
    {
        return mCoursesAtScope.get(index);
    }

    // Get click events
    public void setClickListener(RecyclerViewAdapterCourseSelection.ItemClickListener itemClickListener)
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

            txtItemLabel.setOnClickListener(this);
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

//            if (mSelected_view != null)
//                ViewCompat.setElevation(mSelected_view, 3);
//            mSelected_view = view;
//            ViewCompat.setElevation(mSelected_view, 6);
        }
    }
}