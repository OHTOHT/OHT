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

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> implements Filterable {

    private Context ctx;
    private List<String> original_items = new ArrayList<>();
    private List<String> filtered_items = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public View lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }

    }

    public Filter getFilter() {
        return mFilter;
    }

    public CityListAdapter(Context ctx, List<String> items) {
        this.ctx = ctx;
        original_items = items;
        filtered_items = items;
    }

    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_travel_item_city, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String a = filtered_items.get(position);
        holder.title.setText(a);

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
            final List<String> list = original_items;
            final List<String> result_list = new ArrayList<>(list.size());

            for (int i = 0; i < list.size(); i++) {
                String a = list.get(i);
                if (a.toLowerCase().contains(query)) {
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
            filtered_items = (List<String>) results.values;
            notifyDataSetChanged();
        }

    }
}