package template.travel;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import template.travel.data.Tools;
import template.travel.fragment.FragmentFlightBooking;
import template.travel.fragment.FragmentHotelBooking;

import java.util.ArrayList;
import java.util.List;

public class ActivityBooking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_travel_activity_booking);

        initComponent();
        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        ViewPager view_pager = (ViewPager) findViewById(R.id.view_pager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        FragmentFlightBooking fragmentBookingFlight = new FragmentFlightBooking();
        FragmentHotelBooking fragmentBookingHotel = new FragmentHotelBooking();

        adapter.addFragment(fragmentBookingFlight, "Flight");
        adapter.addFragment(fragmentBookingHotel, "Hotel");

        view_pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(view_pager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bookings");
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

    public class FragmentAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }


        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}