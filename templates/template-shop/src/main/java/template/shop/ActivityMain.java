package template.shop;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.IdRes;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import template.shop.data.GlobalVariable;
import template.shop.data.Tools;
import template.shop.fragment.CartFragment;
import template.shop.fragment.CategoryFragment;

public class ActivityMain extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar actionBar;
    private Menu menu;
    private View parent_view;
    private NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_shop_activity_main);
        parent_view = findViewById(R.id.main_content);

        initToolbar();

        setupDrawerLayout();

        // display first page
        displayView(R.id.nav_new, getString(R.string.app_shop_menu_new));
        actionBar.setTitle(R.string.app_shop_menu_new);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    @Override
    protected void onResume() {
        updateChartCounter(nav_view, R.id.nav_cart, GlobalVariable.getCartItem());
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.app_shop_drawer_open, R.string.app_shop_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                updateChartCounter(nav_view, R.id.nav_cart, GlobalVariable.getCartItem());
                super.onDrawerOpened(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(mDrawerToggle);
        updateChartCounter(nav_view, R.id.nav_cart, GlobalVariable.getCartItem());

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                actionBar.setTitle(menuItem.getTitle());
                displayView(menuItem.getItemId(), menuItem.getTitle().toString());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_shop_menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else if (itemId == R.id.action_cart) {
            displayView(R.id.nav_cart, getString(R.string.app_shop_menu_cart));
            actionBar.setTitle(R.string.app_shop_menu_cart);
        } else if (itemId == R.id.action_credit) {
            Snackbar.make(parent_view, "Credit Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (itemId == R.id.action_settings) {
            Snackbar.make(parent_view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (itemId == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage(getString(R.string.app_shop_about_text));
            builder.setNeutralButton("OK", null);
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayView(int id, String title) {
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (id == R.id.nav_cart) {
            fragment = new CartFragment();
        } else if (id == R.id.nav_new) {
            fragment = new CategoryFragment();
            bundle.putString(CategoryFragment.TAG_CATEGORY, title);
        } else if (id == R.id.nav_promo) {
            fragment = new CategoryFragment();
            bundle.putString(CategoryFragment.TAG_CATEGORY, title);


            //sub menu
        } else if (id == R.id.nav_clothing) {
            fragment = new CategoryFragment();
            bundle.putString(CategoryFragment.TAG_CATEGORY, title);
        } else if (id == R.id.nav_shoes) {
            fragment = new CategoryFragment();
            bundle.putString(CategoryFragment.TAG_CATEGORY, title);
        } else if (id == R.id.nav_watches) {
            fragment = new CategoryFragment();
            bundle.putString(CategoryFragment.TAG_CATEGORY, title);
        } else if (id == R.id.nav_accessories) {
            fragment = new CategoryFragment();
            bundle.putString(CategoryFragment.TAG_CATEGORY, title);
        } else if (id == R.id.nav_bags) {
            fragment = new CategoryFragment();
            bundle.putString(CategoryFragment.TAG_CATEGORY, title);
        }

        fragment.setArguments(bundle);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
            //initToolbar();
        }
    }

    private void updateChartCounter(NavigationView nav, @IdRes int itemId, int count) {
        TextView view = (TextView) nav.getMenu().findItem(itemId).getActionView().findViewById(R.id.counter);
        view.setText(String.valueOf(count));
    }

}


