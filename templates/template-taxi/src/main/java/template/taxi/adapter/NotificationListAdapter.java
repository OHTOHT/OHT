package template.taxi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import template.taxi.R;
import template.taxi.model.Notification;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private Context ctx;
    private List<Notification> items;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Notification obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView subtitle;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            icon = (ImageView) v.findViewById(R.id.icon);
            title = (TextView) v.findViewById(R.id.title);
            subtitle = (TextView) v.findViewById(R.id.subtitle);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public NotificationListAdapter(Context ctx, List<Notification> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_taxi_item_notification, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Notification n = items.get(position);
        Picasso.with(ctx).load(n.icon).into(holder.icon);
        holder.title.setText(n.title);
        holder.subtitle.setText(n.subtitle);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, n, position);
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
}