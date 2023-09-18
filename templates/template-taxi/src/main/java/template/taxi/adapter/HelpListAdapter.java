package template.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import template.taxi.R;

public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.ViewHolder> {

    private Context ctx;
    private String[] items;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.text);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public HelpListAdapter(Context ctx, String[] items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_taxi_item_help, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String a = items[position];
        holder.text.setText(a);

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
        return items.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}