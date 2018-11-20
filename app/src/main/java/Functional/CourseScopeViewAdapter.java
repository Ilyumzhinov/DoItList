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
public class CourseScopeViewAdapter extends RecyclerView.Adapter<CourseScopeViewAdapter.ViewHolder>
{
    private List<Integer> ViewColors;
    private List<String> ViewLabels;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String cDivider;
    private int mSelected_position = 0;

    public CourseScopeViewAdapter(Context context, List<String> xLabels, List<Integer> xColors, String xDivider)
    {
        this.mInflater = LayoutInflater.from(context);
        this.ViewLabels = xLabels;
        this.ViewColors = xColors;
        this.cDivider = xDivider;
    }

    // Inflate the row layout from xml
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.bubble_item, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to each item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.itemView.setSelected(mSelected_position == position);

        int color = ViewColors.get(position);
        String label = ViewLabels.get(position);
        holder.txtItemLabel.setText(label);
        Drawable dr = holder.txtItemLabel.getBackground();

        holder.lblDivider.setText(cDivider);
    }

    // Receive number of items
    @Override
    public int getItemCount()
    {
        return ViewLabels.size();
    }

    // Get item at click position
    public String getItem(int id)
    {
        return ViewLabels.get(id);
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
        TextView txtItemLabel;
        TextView lblDivider;

        ViewHolder(View itemView)
        {
            super(itemView);
            txtItemLabel = itemView.findViewById(R.id.txtLabel);
            lblDivider = itemView.findViewById(R.id.lblDivider);

            if (1 == 1)
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
        }
    }
}