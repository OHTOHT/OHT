package template.travel;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import template.travel.data.Tools;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_main);

        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
    }

    public void onMainMenuClick(View v) {
        Intent i = null;
        int id = v.getId();
        if (id == R.id.lyt_flight) {
            i = new Intent(this, ActivitySearchFlight.class);
        } else if (id == R.id.lyt_hotel) {
            i = new Intent(this, ActivitySearchHotel.class);
        }
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_travel_menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_booking) {
            Intent i = new Intent(getApplicationContext(), ActivityBooking.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage(getString(R.string.app_travel_about_text));
            builder.setNeutralButton("OK", null);
            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}