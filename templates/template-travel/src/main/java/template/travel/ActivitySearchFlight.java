package template.travel;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import template.travel.data.GlobalVariable;
import template.travel.data.Tools;
import template.travel.fragment.FragmentDialogAirport;
import template.travel.fragment.FragmentDialogDate;
import template.travel.model.Airport;
import template.travel.model.Flight;
import template.travel.model.SeatClass;

import java.util.Date;
import java.util.List;

public class ActivitySearchFlight extends AppCompatActivity {

    private Flight flight;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private View bottom_sheet;
    private TextView tv_origin, tv_destination, tv_date, tv_passenger, tv_seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_search_flight);
        flight = GlobalVariable.getInstance(getApplicationContext()).getFlight();

        initComponent();
        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Search Flight");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        tv_origin = (TextView) findViewById(R.id.tv_origin);
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_passenger = (TextView) findViewById(R.id.tv_passenger);
        tv_seat = (TextView) findViewById(R.id.tv_seat);

        tv_origin.setText(Tools.getFormattedAirport(flight.origin));
        tv_destination.setText(Tools.getFormattedAirport(flight.destination));
        tv_date.setText(Tools.getFormattedDateFlight(flight.date));
        tv_seat.setText(flight.seatClass.displayName());

        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        ((Button) findViewById(R.id.bt_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySearchFlightResult.navigate(ActivitySearchFlight.this, flight);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!dismissFragment()) {
            finish();
        }
    }

    public void onFieldClicked(View view) {
        int id = view.getId();
        if (id == R.id.tv_origin) {
            showDialogAirport(1000);
        } else if (id == R.id.tv_destination) {
            showDialogAirport(2000);
        } else if (id == R.id.tv_date) {
            showDialogDepartureDate();
        } else if (id == R.id.tv_passenger) {
            showBottomSheetPassenger();
        } else if (id == R.id.tv_seat) {
            showBottomSheetSeatClass();
        }
    }

    private void showDialogAirport(int result_code) {
        fragmentManager = getSupportFragmentManager();
        final FragmentDialogAirport fragment = new FragmentDialogAirport();
        fragment.setRequestCode(result_code);

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.app_travel_anim_dialog_in, R.anim.app_travel_anim_dialog_out);
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();

        fragment.setOnCallbackResult(new FragmentDialogAirport.CallbackResult() {
            @Override
            public void sendResult(int requestCode, Airport airport) {
                if (requestCode == 1000) {
                    flight.origin = airport;
                    tv_origin.setText(Tools.getFormattedAirport(flight.origin));
                } else if (requestCode == 2000) {
                    flight.destination = airport;
                    tv_destination.setText(Tools.getFormattedAirport(flight.destination));
                }
            }
        });

        fragment.setOnCallbackDismiss(new FragmentDialogAirport.CallbackDismiss() {
            @Override
            public void dismiss() {
                dismissFragment();
            }
        });
    }

    private void showDialogDepartureDate() {
        fragmentManager = getSupportFragmentManager();
        final FragmentDialogDate fragment = new FragmentDialogDate();
        fragment.setTitle("Departure Date");
        fragment.setRequestCode(0);
        fragment.setDate(flight.date);

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.app_travel_anim_dialog_in, R.anim.app_travel_anim_dialog_out);
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();

        fragment.setOnCallbackResult(new FragmentDialogDate.CallbackResult() {
            @Override
            public void sendResult(int requestCode, Date date) {
                if (requestCode == 0) {
                    flight.date = date;
                    tv_date.setText(Tools.getFormattedDateFlight(flight.date));
                }
            }
        });

        fragment.setOnCallbackDismiss(new FragmentDialogDate.CallbackDismiss() {
            @Override
            public void dismiss() {
                dismissFragment();
            }
        });
    }

    private void showBottomSheetPassenger() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        final View view = getLayoutInflater().inflate(R.layout.app_travel_bottomsheet_passanger, null);
        final TextView tv_adult = (TextView) view.findViewById(R.id.tv_adult);
        final TextView tv_child = (TextView) view.findViewById(R.id.tv_child);
        final TextView tv_infant = (TextView) view.findViewById(R.id.tv_infant);

        tv_adult.setText(flight.adult + "");
        tv_child.setText(flight.child + "");
        tv_infant.setText(flight.infant + "");

        /* adult passenger */
        (view.findViewById(R.id.img_dec_adult)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.decrementPassenger(tv_adult, true);
            }
        });

        (view.findViewById(R.id.img_inc_adult)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.incrementPassenger(tv_adult);
            }
        });

        /* child passenger */
        (view.findViewById(R.id.img_dec_child)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.decrementPassenger(tv_child, false);
            }
        });

        (view.findViewById(R.id.img_inc_child)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.incrementPassenger(tv_child);
            }
        });

        /* infant passenger */
        (view.findViewById(R.id.img_dec_infant)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.decrementPassenger(tv_infant, false);
            }
        });
        (view.findViewById(R.id.img_inc_infant)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.incrementPassenger(tv_infant);
            }
        });

        /* tools button */
        (view.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        (view.findViewById(R.id.img_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flight.adult = Integer.parseInt(tv_adult.getText().toString());
                flight.child = Integer.parseInt(tv_child.getText().toString());
                flight.infant = Integer.parseInt(tv_infant.getText().toString());
                tv_passenger.setText(Tools.getFormattedPassenger(flight));
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    private void showBottomSheetSeatClass() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        final View view = getLayoutInflater().inflate(R.layout.app_travel_bottomsheet_seat_class, null);

        final AppCompatRadioButton radio_economy = (AppCompatRadioButton) view.findViewById(R.id.radio_economy);
        final AppCompatRadioButton radio_premium_economy = (AppCompatRadioButton) view.findViewById(R.id.radio_premium_economy);
        final AppCompatRadioButton radio_business = (AppCompatRadioButton) view.findViewById(R.id.radio_business);
        final AppCompatRadioButton radio_first_class = (AppCompatRadioButton) view.findViewById(R.id.radio_first_class);

        radio_economy.setChecked(flight.seatClass.equals(SeatClass.ECONOMY));
        radio_premium_economy.setChecked(flight.seatClass.equals(SeatClass.PREMIUM_ECONOMY));
        radio_business.setChecked(flight.seatClass.equals(SeatClass.BUSINESS));
        radio_first_class.setChecked(flight.seatClass.equals(SeatClass.FIRST_CLASS));

        /* tools button */
        (view.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        (view.findViewById(R.id.img_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_economy.isChecked()) {
                    flight.seatClass = SeatClass.ECONOMY;
                } else if (radio_premium_economy.isChecked()) {
                    flight.seatClass = SeatClass.PREMIUM_ECONOMY;
                } else if (radio_business.isChecked()) {
                    flight.seatClass = SeatClass.BUSINESS;
                } else if (radio_first_class.isChecked()) {
                    flight.seatClass = SeatClass.FIRST_CLASS;
                }
                tv_seat.setText(flight.seatClass.displayName());
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    private boolean dismissFragment() {
        if (fragmentManager == null) return false;
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.size() == 0) return false;

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.app_travel_anim_dialog_in, R.anim.app_travel_anim_dialog_out);
        transaction.detach(fragments.get(0)).commit();
        return true;
    }

}
