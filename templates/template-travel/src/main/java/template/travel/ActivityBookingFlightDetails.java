package template.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import template.travel.data.Tools;
import template.travel.model.FlightBooking;
import com.squareup.picasso.Picasso;

public class ActivityBookingFlightDetails extends AppCompatActivity {

    public static final String EXTRA_OBJECT = "extra.data.FLIGHT_BOOKING";

    // give preparation animation activity transition
    public static void navigate(Activity activity, FlightBooking obj) {
        Intent intent = new Intent(activity, ActivityBookingFlightDetails.class);
        intent.putExtra(EXTRA_OBJECT, obj);
        activity.startActivity(intent);
    }

    private FlightBooking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_booking_flight_details);

        // get extra object
        booking = (FlightBooking) getIntent().getSerializableExtra(EXTRA_OBJECT);

        initComponent();
        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        ((TextView) findViewById(R.id.tv_origin_toolbar)).setText("E-Ticket: " + booking.flight.origin.city);
        ((TextView) findViewById(R.id.tv_destination_toolbar)).setText(booking.flight.destination.city);
        ((TextView) findViewById(R.id.tv_order_id)).setText("Booking ID " + booking.order_id);

        ((TextView) findViewById(R.id.tv_airline_code)).setText(booking.flightResult.airline + " " + booking.flightResult.flight_code);
        ((TextView) findViewById(R.id.tv_seat_class)).setText(booking.flight.seatClass.displayName() + " Class");

        ImageView icon = (ImageView) findViewById(R.id.icon);
        Picasso.with(this).load(booking.flightResult.icon).into(icon);

        ((TextView) findViewById(R.id.tv_time_origin)).setText(booking.flightResult.time_origin);
        ((TextView) findViewById(R.id.tv_date_origin)).setText(Tools.getShortFormattedDate(booking.flight.date));
        ((TextView) findViewById(R.id.tv_time_destination)).setText(booking.flightResult.time_destination);
        ((TextView) findViewById(R.id.tv_date_destination)).setText(Tools.getShortFormattedDate(booking.flight.date));

        ((TextView) findViewById(R.id.tv_origin_city)).setText(Tools.getFormattedAirport(booking.flight.origin));
        ((TextView) findViewById(R.id.tv_origin_airport)).setText(booking.flight.origin.name);
        ((TextView) findViewById(R.id.tv_destination_city)).setText(Tools.getFormattedAirport(booking.flight.destination));
        ((TextView) findViewById(R.id.tv_destination_airport)).setText(booking.flight.destination.name);
        ((TextView) findViewById(R.id.tv_duration)).setText(booking.flightResult.duration);

        ((TextView) findViewById(R.id.tv_booking_code)).setText(booking.booking_code);
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
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}