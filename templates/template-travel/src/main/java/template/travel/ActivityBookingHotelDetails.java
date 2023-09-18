package template.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import template.travel.data.Tools;
import template.travel.model.HotelBooking;

import java.util.Date;

public class ActivityBookingHotelDetails extends AppCompatActivity {

    public static final String EXTRA_OBJECT = "extra.data.HOTEL_BOOKING";

    // give preparation animation activity transition
    public static void navigate(Activity activity, HotelBooking obj) {
        Intent intent = new Intent(activity, ActivityBookingHotelDetails.class);
        intent.putExtra(EXTRA_OBJECT, obj);
        activity.startActivity(intent);
    }

    private HotelBooking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_booking_hotel_details);

        // get extra object
        booking = (HotelBooking) getIntent().getSerializableExtra(EXTRA_OBJECT);

        initComponent();
        initToolbar();
        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        ((TextView) findViewById(R.id.tv_city_toolbar)).setText("Hotel Voucher: " + booking.hotel.city);
        ((TextView) findViewById(R.id.tv_order_id)).setText("Booking ID " + booking.order_id);

        ((TextView) findViewById(R.id.tv_hotel_name)).setText(booking.hotelResult.name);
        ((TextView) findViewById(R.id.tv_location)).setText(booking.hotelResult.location);

        Date checkout_date = Tools.getAddedDate(booking.hotel.check_in, booking.hotel.night);
        ((TextView) findViewById(R.id.tv_checkin_date)).setText(Tools.getSimpleDate(booking.hotel.check_in));
        ((TextView) findViewById(R.id.tv_checkout_date)).setText(Tools.getSimpleDate(checkout_date));

        ((TextView) findViewById(R.id.tv_night)).setText(Tools.getFormattedNight(booking.hotel.night));
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
