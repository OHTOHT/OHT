package template.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import template.taxi.R;

import java.util.ArrayList;
import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    private Context ctx;
    private List<String> items = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView subtitle;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            subtitle = (TextView) v.findViewById(R.id.subtitle);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public LocationListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_taxi_item_location, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String a = items.get(position);
        holder.title.setText(a);
        holder.subtitle.setText(a);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, a, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}