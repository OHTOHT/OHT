package template.travel.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import template.travel.R;
import template.travel.data.Tools;
import template.travel.model.Airport;

import java.util.ArrayList;
import java.util.List;

public class AirportListAdapter extends RecyclerView.Adapter<AirportListAdapter.ViewHolder> implements Filterable {

    private Context ctx;
    private List<Airport> original_items = new ArrayList<>();
    private List<Airport> filtered_items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Airport obj, int position);
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

    public Filter getFilter() {
        return mFilter;
    }

    public AirportListAdapter(Context ctx, List<Airport> items) {
        this.ctx = ctx;
        original_items = items;
        filtered_items = items;
    }

    @Override
    public AirportListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_travel_item_airport, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Airport a = filtered_items.get(position);
        holder.title.setText(a.city);
        holder.subtitle.setText(Tools.getFormattedListAirport(a));

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
        return filtered_items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<Airport> list = original_items;
            final List<Airport> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                Airport a = list.get(i);
                String text = a.city + a.code + a.name;
                if (text.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered_items = (List<Airport>) results.values;
            notifyDataSetChanged();
        }

    }
}