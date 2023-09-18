package template.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import template.news.data.GlobalVariable;
import template.news.data.Tools;
import template.news.fragment.FragmentChannel;
import template.news.fragment.FragmentHome;
import template.news.fragment.FragmentSaved;
import template.news.fragment.FragmentSetting;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ActivityMain extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private View parent_view;
    private GlobalVariable global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_news_activity_main);
        parent_view = findViewById(android.R.id.content);
        GlobalVariable.init(this);

        initToolbar();
        initDrawerMenu();

        displayFragment(R.id.nav_home, getString(R.string.app_news_title_nav_home));

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
        Intent i = new Intent(getApplicationContext(), ActivityRegister.class);
        startActivity(i);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_news_navigation_drawer_open, R.string.app_news_navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
                updateSavedCounter(navigationView, R.id.nav_saved, global.getSaved().size());
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                displayFragment(menuItem.getItemId(), menuItem.getTitle().toString());
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void displayFragment(int id, String title) {
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new FragmentHome();
        } else if (id == R.id.nav_channel) {
            fragment = new FragmentChannel();
        } else if (id == R.id.nav_saved) {
            fragment = new FragmentSaved();
        } else if (id == R.id.nav_setting) {
            fragment = new FragmentSetting();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar.make(parent_view, item.getTitle() + " clicked", Snackbar.LENGTH_SHORT).show();
        if(item.getItemId() == R.id.action_register){
            Intent i = new Intent(getApplicationContext(), ActivityRegister.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.app_news_menu_activity_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        updateSavedCounter(navigationView, R.id.nav_saved, global.getSaved().size());
        super.onResume();
    }


    private void updateSavedCounter(NavigationView nav, @IdRes int itemId, int count) {
        TextView view = (TextView) nav.getMenu().findItem(itemId).getActionView().findViewById(R.id.counter);
        view.setText(String.valueOf(count));
    }

}
