package template.travel.adapter;

import android.content.Context;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import template.travel.R;
import template.travel.model.HotelResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelResultListAdapter extends RecyclerView.Adapter<HotelResultListAdapter.ViewHolder> {

    private Context ctx;

    private List<HotelResult> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, HotelResult obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public AppCompatRatingBar rating;
        public TextView location;
        public TextView price;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            image = (ImageView) v.findViewById(R.id.image);
            rating = (AppCompatRatingBar) v.findViewById(R.id.rating);
            location = (TextView) v.findViewById(R.id.location);
            price = (TextView) v.findViewById(R.id.price);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public HotelResultListAdapter(Context ctx, List<HotelResult> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public HotelResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_travel_item_hotel_result, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final HotelResult h = items.get(position);
        holder.name.setText(h.name);
        Picasso.with(ctx).load(h.image).into(holder.image);
        holder.location.setText(h.location);
        holder.rating.setNumStars(h.rating);
        holder.rating.setRating(h.rating);
        holder.price.setText(h.price);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, h, position);
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