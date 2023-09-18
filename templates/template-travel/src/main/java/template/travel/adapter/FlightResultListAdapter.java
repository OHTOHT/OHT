package template.travel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import template.travel.R;
import template.travel.model.Flight;
import template.travel.model.FlightResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlightResultListAdapter extends RecyclerView.Adapter<FlightResultListAdapter.ViewHolder> {

    private Context ctx;
    private Flight flight;

    private List<FlightResult> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, FlightResult obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView airline;
        public ImageView icon;
        public TextView time_origin, time_destination;
        public TextView code_origin, duration, code_destination;
        public TextView price;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            airline = (TextView) v.findViewById(R.id.airline);
            icon = (ImageView) v.findViewById(R.id.icon);
            time_origin = (TextView) v.findViewById(R.id.time_origin);
            time_destination = (TextView) v.findViewById(R.id.time_destination);
            code_origin = (TextView) v.findViewById(R.id.code_origin);
            code_destination = (TextView) v.findViewById(R.id.code_destination);
            duration = (TextView) v.findViewById(R.id.duration);
            price = (TextView) v.findViewById(R.id.price);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public FlightResultListAdapter(Context ctx, Flight flight, List<FlightResult> items) {
        this.ctx = ctx;
        this.flight = flight;
        this.items = items;
    }

    @Override
    public FlightResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_travel_item_flight_result, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FlightResult f = items.get(position);
        holder.airline.setText(f.airline);
        Picasso.with(ctx).load(f.icon).into(holder.icon);

        holder.time_origin.setText(f.time_origin);
        holder.time_destination.setText(f.time_destination);

        holder.code_origin.setText("  " + flight.origin.code + "  ");
        holder.code_destination.setText("  " + flight.destination.code + "  ");
        holder.duration.setText(f.duration);
        holder.price.setText(f.price);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, f, position);
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