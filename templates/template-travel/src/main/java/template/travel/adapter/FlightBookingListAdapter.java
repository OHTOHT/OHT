package template.travel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import template.travel.R;
import template.travel.data.Tools;
import template.travel.model.FlightBooking;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FlightBookingListAdapter extends RecyclerView.Adapter<FlightBookingListAdapter.ViewHolder> {

    private Context ctx;

    private List<FlightBooking> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, FlightBooking obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView tv_origin, tv_destination;
        public TextView tv_date;
        public TextView tv_airline_airport, tv_status;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            icon = (ImageView) v.findViewById(R.id.icon);
            tv_origin = (TextView) v.findViewById(R.id.tv_origin);
            tv_destination = (TextView) v.findViewById(R.id.tv_destination);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_airline_airport = (TextView) v.findViewById(R.id.tv_airline_airport);
            tv_status = (TextView) v.findViewById(R.id.tv_status);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public FlightBookingListAdapter(Context ctx, List<FlightBooking> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public FlightBookingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_travel_item_flight_booking, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FlightBooking fb = items.get(position);
        Picasso.with(ctx).load(fb.flightResult.icon).into(holder.icon);

        holder.tv_origin.setText(fb.flight.origin.city);
        holder.tv_destination.setText(fb.flight.destination.city);

        holder.tv_date.setText(Tools.getFormattedSimpleDateFlight(fb.flight.date) + " - " + fb.flightResult.time_origin);
        holder.tv_airline_airport.setText(fb.flightResult.airline + " - " + fb.flight.origin.name);
        holder.tv_status.setText(fb.status);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, fb, position);
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