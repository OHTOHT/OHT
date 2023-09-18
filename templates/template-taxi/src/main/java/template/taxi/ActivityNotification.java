package template.taxi;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import template.taxi.adapter.NotificationListAdapter;
import template.taxi.data.Constant;
import template.taxi.data.Tools;
import template.taxi.model.Notification;

public class ActivityNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_taxi_activity_notification);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setCompleteSystemBarLight(this);
    }

    private void initComponent() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        List<Notification> items = Constant.getNotificationData(this);
        NotificationListAdapter mAdapter = new NotificationListAdapter(this, items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new NotificationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Notification obj, int position) {
                Toast.makeText(getApplicationContext(), obj.title + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
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
