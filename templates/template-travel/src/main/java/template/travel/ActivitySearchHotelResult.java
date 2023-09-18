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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import template.travel.adapter.HotelResultListAdapter;
import template.travel.data.Constant;
import template.travel.data.Tools;
import template.travel.model.Hotel;
import template.travel.model.HotelResult;

public class ActivitySearchHotelResult extends AppCompatActivity {

    public static final String EXTRA_OBJECT = "extra.data.HOTEL";

    // give preparation animation activity transition
    public static void navigate(Activity activity, Hotel obj) {
        Intent intent = new Intent(activity, ActivitySearchHotelResult.class);
        intent.putExtra(EXTRA_OBJECT, obj);
        activity.startActivity(intent);
    }

    private Hotel hotel;
    private ProgressBar progress_bar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_search_hotel_result);

        // get extra object
        hotel = (Hotel) getIntent().getSerializableExtra(EXTRA_OBJECT);

        initComponent();
        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setProgress(0);

        //((TextView) findViewById(R.id.tv_city)).setText(hotel.city);
        ((TextView) findViewById(R.id.tv_details)).setText(Tools.getHotelDetails(hotel));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        HotelResultListAdapter mAdapter = new HotelResultListAdapter(this, Constant.getHotelResultData(this));
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new HotelResultListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, HotelResult obj, int position) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_travel_menu_activity_search_hotel_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
