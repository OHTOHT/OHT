package template.taxi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.taxi.ActivityBookingHistoryDetails;
import template.taxi.R;
import template.taxi.adapter.BookingListAdapter;
import template.taxi.data.Constant;
import template.taxi.model.Booking;

import java.util.List;

public class FragmentBookingHistory extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_taxi_fragment_booking_history, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        List<Booking> bookingList = Constant.getBookingHistory(getActivity());
        BookingListAdapter mAdapter = new BookingListAdapter(getActivity(), bookingList);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new BookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Booking obj, int position) {
                ActivityBookingHistoryDetails.navigate(getActivity(), obj);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
