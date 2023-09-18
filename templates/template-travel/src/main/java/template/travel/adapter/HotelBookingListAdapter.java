package template.travel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import template.travel.R;
import template.travel.data.Tools;
import template.travel.model.HotelBooking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelBookingListAdapter extends RecyclerView.Adapter<HotelBookingListAdapter.ViewHolder> {

    private Context ctx;

    private List<HotelBooking> items = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, HotelBooking obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_city;
        public TextView tv_date;
        public TextView tv_hotel_name, tv_status;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            tv_city = (TextView) v.findViewById(R.id.tv_city);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_hotel_name = (TextView) v.findViewById(R.id.tv_hotel_name);
            tv_status = (TextView) v.findViewById(R.id.tv_status);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    public HotelBookingListAdapter(Context ctx, List<HotelBooking> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @Override
    public HotelBookingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_travel_item_hotel_booking, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final HotelBooking hb = items.get(position);
        Date checkout_date = Tools.getAddedDate(hb.hotel.check_in, hb.hotel.night);

        holder.tv_city.setText(hb.hotel.city);
        holder.tv_date.setText(Tools.getSimpleFormattedDate(hb.hotel.check_in) + " - " + Tools.getSimpleFormattedDate(checkout_date));
        holder.tv_hotel_name.setText(hb.hotelResult.name);
        holder.tv_status.setText(hb.status);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, hb, position);
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