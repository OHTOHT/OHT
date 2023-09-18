package template.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import template.travel.adapter.FlightResultListAdapter;
import template.travel.data.Constant;
import template.travel.data.Tools;
import template.travel.model.Flight;
import template.travel.model.FlightResult;

public class ActivitySearchFlightResult extends AppCompatActivity {

    public static final String EXTRA_OBJECT = "extra.data.FLIGHT";

    // give preparation animation activity transition
    public static void navigate(Activity activity, Flight obj) {
        Intent intent = new Intent(activity, ActivitySearchFlightResult.class);
        intent.putExtra(EXTRA_OBJECT, obj);
        activity.startActivity(intent);
    }

    private Flight flight;
    private ProgressBar progress_bar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_search_flight_result);

        // get extra object
        flight = (Flight) getIntent().getSerializableExtra(EXTRA_OBJECT);

        initComponent();
        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setProgress(0);

        ((TextView) findViewById(R.id.tv_origin)).setText(flight.origin.city);
        ((TextView) findViewById(R.id.tv_destination)).setText(flight.destination.city);
        ((TextView) findViewById(R.id.tv_details)).setText(Tools.getFlightDetails(flight));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        FlightResultListAdapter mAdapter = new FlightResultListAdapter(this, flight, Constant.getFlightResultData(this));
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new FlightResultListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, FlightResult obj, int position) {

            }
        });

        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                int progress = progress_bar.getProgress() + 20;
                progress_bar.setProgress(progress);
                if (progress >= 100) {
                    progress_bar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    mHandler.postDelayed(this, 200);
                }
            }
        };
        mHandler.post(runnable);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
