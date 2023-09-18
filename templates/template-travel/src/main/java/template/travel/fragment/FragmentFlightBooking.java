package template.travel.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import template.travel.ActivityBookingFlightDetails;
import template.travel.R;
import template.travel.adapter.FlightBookingListAdapter;
import template.travel.data.GlobalVariable;
import template.travel.model.FlightBooking;

import java.util.List;

public class FragmentFlightBooking extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_travel_fragment_flight_booking, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        List<FlightBooking> flightBookings = GlobalVariable.getInstance(getContext().getApplicationContext()).getFlightBookings();
        FlightBookingListAdapter mAdapter = new FlightBookingListAdapter(getActivity(), flightBookings);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new FlightBookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, FlightBooking obj, int position) {
                ActivityBookingFlightDetails.navigate(getActivity(), obj);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
