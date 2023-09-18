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
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import template.travel.data.GlobalVariable;
import template.travel.data.Tools;
import template.travel.fragment.FragmentDialogCity;
import template.travel.fragment.FragmentDialogDate;
import template.travel.model.Hotel;

import java.util.Date;
import java.util.List;

public class ActivitySearchHotel extends AppCompatActivity {

    private Hotel hotel;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private View bottom_sheet;
    private TextView tv_destination, tv_check_in, tv_check_out, tv_night, tv_guest_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_search_hotel);
        hotel = GlobalVariable.getInstance(getApplicationContext()).getHotel();

        initComponent();
        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Search Hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tv_check_in = (TextView) findViewById(R.id.tv_check_in);
        tv_check_out = (TextView) findViewById(R.id.tv_check_out);
        tv_night = (TextView) findViewById(R.id.tv_night);
        tv_guest_room = (TextView) findViewById(R.id.tv_guest_room);

        tv_destination.setText(hotel.city);
        tv_check_in.setText(Tools.getFormattedCheckInHotel(hotel.check_in));
        tv_check_out.setText(Tools.getFormattedCheckOutHotel(hotel));
        tv_night.setText(Tools.getFormattedNight(hotel.night));
        tv_guest_room.setText(Tools.getFormattedGuestRoom(hotel));

        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        ((Button) findViewById(R.id.bt_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySearchHotelResult.navigate(ActivitySearchHotel.this, hotel);
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
        if (id == R.id.tv_destination) {
            showDialogCity();
        } else if (id == R.id.tv_check_in) {
            showDialogDepartureDate();
        } else if (id == R.id.tv_night) {
            showBottomSheetNight();
        } else if (id == R.id.tv_guest_room) {
            showBottomSheetGuestRoom();
        }
    }

    private void showDialogCity() {
        fragmentManager = getSupportFragmentManager();
        final FragmentDialogCity fragment = new FragmentDialogCity();
        fragment.setRequestCode(100);

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.app_travel_anim_dialog_in, R.anim.app_travel_anim_dialog_out);
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();

        fragment.setOnCallbackResult(new FragmentDialogCity.CallbackResult() {
            @Override
            public void sendResult(int requestCode, String city) {
                if (requestCode == 100) {
                    hotel.city = city;
                    tv_destination.setText(city);
                }
            }
        });

        fragment.setOnCallbackDismiss(new FragmentDialogCity.CallbackDismiss() {
            @Override
            public void dismiss() {
                dismissFragment();
            }
        });
    }

    private void showDialogDepartureDate() {
        fragmentManager = getSupportFragmentManager();
        final FragmentDialogDate fragment = new FragmentDialogDate();
        fragment.setTitle("Check-in Date");
        fragment.setRequestCode(0);
        fragment.setDate(hotel.check_in);

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.app_travel_anim_dialog_in, R.anim.app_travel_anim_dialog_out);
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();

        fragment.setOnCallbackResult(new FragmentDialogDate.CallbackResult() {
            @Override
            public void sendResult(int requestCode, Date date) {
                if (requestCode == 0) {
                    hotel.check_in = date;
                    tv_check_in.setText(Tools.getFormattedCheckInHotel(hotel.check_in));
                    tv_check_out.setText(Tools.getFormattedCheckOutHotel(hotel));
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

    private void showBottomSheetNight() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        final View view = getLayoutInflater().inflate(R.layout.app_travel_bottomsheet_night, null);
        final TextView tv_night_ = (TextView) view.findViewById(R.id.tv_night);
        final TextView tv_check_out_ = (TextView) view.findViewById(R.id.tv_check_out);

        tv_night_.setText(hotel.night + "");
        tv_check_out_.setText(Tools.getFormattedCheckOutHotel(hotel));

        /* button action */
        (view.findViewById(R.id.img_dec)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.decrementNight(tv_night_);
                tv_check_out_.setText(Tools.getFormattedCheckOut(hotel.check_in, Integer.parseInt(tv_night_.getText().toString())));
            }
        });

        (view.findViewById(R.id.img_inc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.incrementNight(tv_night_);
                tv_check_out_.setText(Tools.getFormattedCheckOut(hotel.check_in, Integer.parseInt(tv_night_.getText().toString())));
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
                hotel.night = Integer.parseInt(tv_night_.getText().toString());
                tv_night.setText(Tools.getFormattedNight(hotel.night));
                tv_check_out.setText(Tools.getFormattedCheckOutHotel(hotel));
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

    private void showBottomSheetGuestRoom() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        final View view = getLayoutInflater().inflate(R.layout.app_travel_bottomsheet_guest_room, null);
        final TextView tv_guest = (TextView) view.findViewById(R.id.tv_guest);
        final TextView tv_room = (TextView) view.findViewById(R.id.tv_room);

        tv_guest.setText(hotel.guest + "");
        tv_room.setText(hotel.room + "");

        /* guest */
        (view.findViewById(R.id.img_dec_guest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.decrementGuest(tv_guest);
            }
        });

        (view.findViewById(R.id.img_inc_guest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.incrementGuest(tv_guest);
            }
        });

        /* room */
        (view.findViewById(R.id.img_dec_room)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.decrementRoom(tv_room);
            }
        });

        (view.findViewById(R.id.img_inc_room)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.incrementRoom(tv_room);
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
                hotel.guest = Integer.parseInt(tv_guest.getText().toString());
                hotel.room = Integer.parseInt(tv_room.getText().toString());
                tv_guest_room.setText(Tools.getFormattedGuestRoom(hotel));
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
