package template.travel.data;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import template.travel.R;
import template.travel.model.Airport;
import template.travel.model.Flight;
import template.travel.model.Hotel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Tools {

    public static void systemBarLolipop(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.app_travel_colorPrimaryDark));
        }
    }

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.app_travel_recycler_item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static String getFormattedDateFlight(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return newFormat.format(date);
    }

    public static String getFormattedSimpleDateFlight(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        return newFormat.format(date);
    }

    public static String getFormattedCheckInHotel(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        return newFormat.format(date);
    }

    public static String getFormattedCheckOutHotel(Hotel hotel) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(hotel.check_in);
        cal.add(Calendar.DATE, hotel.night);
        Date check_out = cal.getTime();
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        return "Check-out: " + newFormat.format(check_out);
    }

    public static Date getAddedDate(int increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, increment);
        return cal.getTime();
    }

    public static Date getAddedDate(Date date, int increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, increment);
        return cal.getTime();
    }

    public static String getFormattedCheckOut(Date date, int night) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, night);
        Date check_out = cal.getTime();
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        return "Check-out: " + newFormat.format(check_out);
    }

    public static String getFormattedNight(int night) {
        return night + " night(s)";
    }

    public static String getSimpleFormattedDate(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM");
        return newFormat.format(date);
    }

    public static String getSimpleDate(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yy");
        return newFormat.format(date);
    }

    public static String getShortFormattedDate(Date date) {
        SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM");
        return newFormat.format(date);
    }

    public static String getFormattedAirport(Airport airport) {
        return airport.city + " (" + airport.code + ")";
    }

    public static String getFormattedListAirport(Airport airport) {
        return airport.code + " - " + airport.name;
    }

    public static String getFormattedPassenger(Flight flight) {
        String result = flight.adult + " Adult";
        if (flight.child != 0) {
            result = result + ", " + flight.child + " Child";
        }
        if (flight.infant != 0) {
            result = result + ", " + flight.infant + " Infant";
        }
        return result;
    }

    public static String getFormattedGuestRoom(Hotel hotel) {
        String result = hotel.guest + " guest(s), " + hotel.room + " room(s)";
        return result;
    }

    public static String getFlightDetails(Flight flight) {
        String result = getSimpleFormattedDate(flight.date) + " - ";
        result = result + (flight.adult + flight.child + flight.infant) + " pax - ";
        result = result + flight.seatClass.displayName();
        return result;
    }

    public static String getHotelDetails(Hotel hotel) {
        String result = getFormattedCheckInHotel(hotel.check_in) + ", ";
        result = result + hotel.night + " night(s)";
        return result;
    }

    public static void decrementPassenger(TextView tv, boolean is_adult) {
        int count = Integer.parseInt(tv.getText().toString());
        int limit = is_adult ? 1 : 0;
        if (count > limit) {
            count--;
            tv.setText(count + "");
        }
    }

    public static void incrementPassenger(TextView tv) {
        int count = Integer.parseInt(tv.getText().toString());
        if (count < 5) {
            count++;
            tv.setText(count + "");
        }
    }

    public static void decrementNight(TextView tv) {
        int count = Integer.parseInt(tv.getText().toString());
        if (count > 1) {
            count--;
            tv.setText(count + "");
        }
    }

    public static void incrementNight(TextView tv) {
        int count = Integer.parseInt(tv.getText().toString());
        if (count < 15) {
            count++;
            tv.setText(count + "");
        }
    }

    public static void decrementGuest(TextView tv) {
        int count = Integer.parseInt(tv.getText().toString());
        if (count > 1) {
            count--;
            tv.setText(count + "");
        }
    }

    public static void incrementGuest(TextView tv) {
        int count = Integer.parseInt(tv.getText().toString());
        if (count < 32) {
            count++;
            tv.setText(count + "");
        }
    }

    public static void decrementRoom(TextView tv) {
        int count = Integer.parseInt(tv.getText().toString());
        if (count > 1) {
            count--;
            tv.setText(count + "");
        }
    }

    public static void incrementRoom(TextView tv) {
        int count = Integer.parseInt(tv.getText().toString());
        if (count < 8) {
            count++;
            tv.setText(count + "");
        }
    }
}
