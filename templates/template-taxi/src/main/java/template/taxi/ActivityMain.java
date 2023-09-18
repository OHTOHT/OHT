package template.taxi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import template.taxi.adapter.RideClassListAdapter;
import template.taxi.data.Constant;
import template.taxi.data.Tools;
import template.taxi.fragment.FragmentDialogLocation;
import template.taxi.fragment.FragmentDialogPayment;
import template.taxi.fragment.FragmentDialogPromo;
import template.taxi.model.Payment;
import template.taxi.model.Promo;
import template.taxi.model.RideClass;

public class ActivityMain extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private TextView tv_note, tv_promo, tv_payment;
    private EditText et_pickup, et_destination;

    private GoogleMap mMap;
    private Polyline polyline;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private Promo promo;
    private Payment payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_taxi_activity_main);

        initToolbar();
        initDrawerMenu();
        initMapFragment();
        initComponent();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.app_taxi_ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.app_taxi_grey_very_hard), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Tools.setCompleteSystemBarLight(this);
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_taxi_navigation_drawer_open, R.string.app_taxi_navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                onNavigationItemClick(menuItem);
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void onNavigationItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_booking) {
            startActivity(new Intent(this, ActivityBooking.class));
        } else if (id == R.id.nav_notification) {
            startActivity(new Intent(this, ActivityNotification.class));
        } else if (id == R.id.nav_payment) {
            startActivity(new Intent(this, ActivityPayment.class));
        } else if (id == R.id.nav_coupon) {
            showDialogPromo();
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(this, ActivityHelpCenter.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, ActivitySettings.class));
        } else if (id == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage(getString(R.string.app_taxi_about_text));
            builder.setNeutralButton("OK", null);
            builder.show();
        }
    }

    private void initMapFragment() {
        Tools.checkInternetConnection(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                configureMap(googleMap);
            }
        });
    }

    public void configureMap(GoogleMap googleMap) {
        mMap = Tools.configBasicGoogleMap(googleMap);
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(new com.google.android.gms.maps.model.LatLng(48.84486746, 2.31412714), 12);

        mMap.moveCamera(center);

        LatLng origin = new LatLng(48.842948, 2.318795);
        LatLng destination = new LatLng(48.874050, 2.294372);

        displayMarker(origin, true);
        displayMarker(destination, false);

        drawPolyLine(origin, destination);

        Tools.displayCarAroundMarkers(this, mMap);
    }

    private void drawPolyLine(LatLng origin, LatLng destination) {
        GeoApiContext context = new GeoApiContext().setApiKey(Tools.getGoogleMapApiKey(this));
        context.setConnectTimeout(10, TimeUnit.SECONDS);
        DirectionsApiRequest d = DirectionsApi.newRequest(context);
        d.origin(origin).destination(destination).mode(TravelMode.DRIVING).alternatives(false);
        d.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                final PolylineOptions polylineOptions = new PolylineOptions().width(8).color(getResources().getColor(R.color.app_taxi_colorAccent)).geodesic(true);
                for (DirectionsRoute d : result.routes) {
                    for (LatLng l : d.overviewPolyline.decodePath()) {
                        polylineOptions.add(new com.google.android.gms.maps.model.LatLng(l.lat, l.lng));
                    }
                }
                // draw polyline
                runOnUiThread(new Runnable() {
                    public void run() {
                        polyline = mMap.addPolyline(polylineOptions);
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    private void displayMarker(LatLng location, boolean isOrigin) {
        // make current location marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new com.google.android.gms.maps.model.LatLng(location.lat, location.lng));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marker_view = inflater.inflate(R.layout.app_taxi_maps_marker, null);
        ImageView marker = (ImageView) marker_view.findViewById(R.id.marker);
        marker.setBackgroundResource(isOrigin ? R.drawable.app_taxi_marker_origin : R.drawable.app_taxi_marker_destination);
        if (isOrigin) {
            marker.setImageResource(R.drawable.app_taxi_ic_origin);
        }

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(this, marker_view)));
        mMap.addMarker(markerOptions);
    }

    private void initComponent() {
        tv_note = (TextView) findViewById(R.id.tv_note);
        tv_promo = (TextView) findViewById(R.id.tv_promo);
        tv_payment = (TextView) findViewById(R.id.tv_payment);

        et_pickup = (EditText) findViewById(R.id.et_pickup);
        et_destination = (EditText) findViewById(R.id.et_destination);

        ((View) findViewById(R.id.lyt_ride)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRideClass();
            }
        });

        ((View) findViewById(R.id.lyt_note)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogNote();
            }
        });

        ((View) findViewById(R.id.lyt_promo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPromo();
            }
        });

        ((View) findViewById(R.id.lyt_payment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPayment();
            }
        });

        ((View) findViewById(R.id.lyt_pickup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLocation(true);
            }
        });

        ((View) findViewById(R.id.lyt_destination)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLocation(false);
            }
        });
        ((View) findViewById(R.id.lyt_request_ride)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityRequestRide.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_taxi_menu_activity_main, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.app_taxi_grey_very_hard));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            Tools.showToastMiddle(getApplicationContext(), item.getTitle() + " clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogRideClass() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.app_taxi_dialog_ride_class);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        RideClassListAdapter mAdapter = new RideClassListAdapter(this, Constant.getRideClassData(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RideClassListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RideClass obj, int position) {
                changeRideClass(obj);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void changeRideClass(RideClass obj) {
        Picasso.with(this).load(obj.image).into(((ImageView) findViewById(R.id.image)));
        ((TextView) findViewById(R.id.class_name)).setText(obj.class_name);
        ((TextView) findViewById(R.id.price)).setText(obj.price);
        ((TextView) findViewById(R.id.pax)).setText(obj.pax);
        ((TextView) findViewById(R.id.duration)).setText(obj.duration);
    }

    private void showDialogNote() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.app_taxi_dialog_note);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextInputEditText et_note = (TextInputEditText) dialog.findViewById(R.id.et_note);
        String home_note = tv_note.getText().toString();
        if (!home_note.equals("None")) et_note.setText(home_note);

        ((View) dialog.findViewById(R.id.lyt_clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_note.setText("");
            }
        });

        ((Button) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = et_note.getText().toString().trim();
                if (note.equals("")) {
                    tv_note.setText("None");
                } else {
                    tv_note.setText(note);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showDialogLocation(boolean isPickUp) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment previous = getSupportFragmentManager().findFragmentByTag(FragmentDialogLocation.class.getName());
        if (previous != null) transaction.remove(previous);
        transaction.addToBackStack(null);

        final FragmentDialogLocation fragment = new FragmentDialogLocation();
        fragment.setHint(isPickUp ? "Enter pickup location" : "Enter destination location");
        fragment.setRequestCode(isPickUp ? 5000 : 6000);

        fragment.setOnCallbackResult(new FragmentDialogLocation.CallbackResult() {
            @Override
            public void sendResult(int requestCode, String loc) {
                if (requestCode == 5000) {
                    et_pickup.setText(loc);
                } else if (requestCode == 6000) {
                    et_destination.setText(loc);
                }
            }
        });

        fragment.show(transaction, FragmentDialogLocation.class.getName());
    }

    private void showDialogPromo() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment previous = getSupportFragmentManager().findFragmentByTag(FragmentDialogPromo.class.getName());
        if (previous != null) transaction.remove(previous);
        transaction.addToBackStack(null);

        fragmentManager = getSupportFragmentManager();
        final FragmentDialogPromo fragment = new FragmentDialogPromo();
        fragment.setRequestCode(1000);
        fragment.setPromo(promo);

        fragment.setOnCallbackResult(new FragmentDialogPromo.CallbackResult() {
            @Override
            public void sendResult(int requestCode, Promo p) {
                if (requestCode != 1000) return;
                Tools.showToastMiddle(getApplicationContext(), "Promo Applied");
                tv_promo.setText(p.code);
                tv_promo.setTextColor(getResources().getColor(R.color.app_taxi_price_color));
                promo = p;

            }

            @Override
            public void removePromo(int requestCode) {
                if (requestCode != 1000) return;
                Tools.showToastMiddle(getApplicationContext(), "Promo Removed");
                tv_promo.setText("None");
                tv_promo.setTextColor(getResources().getColor(R.color.app_taxi_grey_text));
                promo = null;
            }
        });


        fragment.show(transaction, FragmentDialogPromo.class.getName());
    }

    private void showDialogPayment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment previous = getSupportFragmentManager().findFragmentByTag(FragmentDialogPayment.class.getName());
        if (previous != null) transaction.remove(previous);
        transaction.addToBackStack(null);

        final FragmentDialogPayment fragment = new FragmentDialogPayment();
        fragment.setRequestCode(2000);
        fragment.setPayment(payment);

        fragment.setOnCallbackResult(new FragmentDialogPayment.CallbackResult() {
            @Override
            public void sendResult(int requestCode, Payment p) {
                if (requestCode != 2000) return;
                payment = p;
                tv_payment.setText(p.type);
            }

        });

        fragment.show(transaction, FragmentDialogPayment.class.getName());
    }

}
