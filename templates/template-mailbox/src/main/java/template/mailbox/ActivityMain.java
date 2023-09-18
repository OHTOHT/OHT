package template.mailbox;

import android.content.Context;
import android.os.Bundle;
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

import template.mailbox.data.Constant;
import template.mailbox.data.GlobalVariable;
import template.mailbox.data.Tools;
import template.mailbox.fragment.HelpFragment;
import template.mailbox.fragment.InboxFragment;
import template.mailbox.fragment.OutboxFragment;
import template.mailbox.fragment.PeoplesFragment;
import template.mailbox.fragment.SettingFragment;
import template.mailbox.fragment.TrashFragment;
import com.google.android.material.navigation.NavigationView;

public class ActivityMain extends AppCompatActivity{

    private Toolbar toolbar;
    public ActionBar actionBar;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_mailbox_activity_main);

        initData();

        initToolbar();
        initDrawerMenu();

        // set home view
        actionBar.setTitle(getString(R.string.app_mailbox_str_nav_inbox));
        displayContentView(R.id.nav_inbox);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
    private void initDrawerMenu(){
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_mailbox_navigation_drawer_open, R.string.app_mailbox_navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                updateDrawerCounter();
                hideKeyboard();
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
                drawer.closeDrawers();
                actionBar.setTitle(menuItem.getTitle());
                displayContentView(menuItem.getItemId());
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        updateDrawerCounter();
        super.onResume();
    }

    private void updateDrawerCounter(){
        setMenuAdvCounter(R.id.nav_inbox, GlobalVariable.getInbox().size());
        setMenuStdCounter(R.id.nav_trash, GlobalVariable.getTrash().size());
    }

    //set counter in drawer
    private void setMenuAdvCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView().findViewById(R.id.counter);
        view.setText(count > 0 ? String.valueOf(count) : null);
    }
    //set counter in drawer
    private void setMenuStdCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void displayContentView(int id) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        if (id == R.id.nav_inbox) {
            fragment = new InboxFragment();
        } else if (id == R.id.nav_outbox) {
            fragment = new OutboxFragment();
        } else if (id == R.id.nav_people) {
            fragment = new PeoplesFragment();
        } else if (id == R.id.nav_trash) {
            fragment = new TrashFragment();
        } else if (id == R.id.nav_setting) {
            fragment = new SettingFragment();
        } else if (id == R.id.nav_help) {
            fragment = new HelpFragment();
        }
        fragment.setArguments(bundle);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initData(){
        GlobalVariable.setPeoples(Constant.getPeopleData(this));
        GlobalVariable.setInbox(Constant.getInboxData(this));
        GlobalVariable.setOutbox(Constant.getOutboxData(this));
    }


}
