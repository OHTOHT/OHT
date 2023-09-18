package template.travel.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import template.travel.ActivityBookingHotelDetails;
import template.travel.R;
import template.travel.adapter.HotelBookingListAdapter;
import template.travel.data.GlobalVariable;
import template.travel.model.HotelBooking;

import java.util.List;

public class FragmentHotelBooking extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_travel_fragment_hotel_booking, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        List<HotelBooking> hotelBookings = GlobalVariable.getInstance(getContext().getApplicationContext()).getHotelBookings();
        HotelBookingListAdapter mAdapter = new HotelBookingListAdapter(getActivity(), hotelBookings);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new HotelBookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, HotelBooking obj, int position) {
                ActivityBookingHotelDetails.navigate(getActivity(), obj);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
