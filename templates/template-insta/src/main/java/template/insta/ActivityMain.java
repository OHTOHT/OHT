package template.insta;

import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import template.insta.adapter.PageFragmentAdapter;
import template.insta.data.Tools;
import template.insta.fragment.PageHomeFragment;
import template.insta.fragment.PageExploreFragment;
import template.insta.fragment.PageGalleryFragment;
import template.insta.fragment.PageFriendFragment;
import template.insta.fragment.PageProfileFragment;

public class ActivityMain extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private View parent_view;

    private PageFragmentAdapter adapter;

    private PageHomeFragment f_home;
    private PageExploreFragment f_explore;
    private PageGalleryFragment f_gallery;
    private PageFriendFragment f_friend;
    private PageProfileFragment f_profile;
    private static int[] imageResId = {
            R.drawable.app_insta_tab_home,
            R.drawable.app_insta_tab_explore,
            R.drawable.app_insta_tab_gallery,
            R.drawable.app_insta_tab_friend,
            R.drawable.app_insta_tab_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_insta_activity_main);
        parent_view = findViewById(android.R.id.content);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setTitle("");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setupTabClick();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_home == null) {
            f_home = new PageHomeFragment();
        }
        if (f_explore== null) {
            f_explore = new PageExploreFragment();
        }
        if (f_gallery == null) {
            f_gallery = new PageGalleryFragment();
        }
        if (f_friend == null) {
            f_friend = new PageFriendFragment();
        }
        if (f_profile == null) {
            f_profile = new PageProfileFragment();
        }
        adapter.addFragment(f_home, null);
        adapter.addFragment(f_explore, null);
        adapter.addFragment(f_gallery, null);
        adapter.addFragment(f_friend, null);
        adapter.addFragment(f_profile, null);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
        tabLayout.getTabAt(2).setIcon(imageResId[2]);
        tabLayout.getTabAt(3).setIcon(imageResId[3]);
        tabLayout.getTabAt(4).setIcon(imageResId[4]);
    }

    private void setupTabClick() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.app_insta_colorPrimary);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.app_insta_grey_medium);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void actionClick(View v) {
        f_profile.actionClick(v);
    }

}
