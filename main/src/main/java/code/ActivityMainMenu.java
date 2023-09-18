package code;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.material.components.BuildConfig;
import com.material.components.R;

import java.util.ArrayList;
import java.util.List;

import code.adapter.AdapterListener;
import code.adapter.AdapterNews;
import code.adapter.ExpandableRecyclerAdapter;
import code.adapter.MnAdapter;
import code.adapter.MnAdapter.Item;
import code.adapter.MnSearchAdapter;
import code.connection.ParamList;
import code.connection.Request;
import code.connection.RequestListener;
import code.connection.response.RespNews;
import code.data.MenuGenerator;
import code.data.SharedPref;
import code.model.MnType;
import code.model.News;
import code.room.AppDatabase;
import code.room.DAO;
import code.system.Sell;
import code.utils.PermissionUtil;
import code.utils.Tools;
import retrofit2.Call;

public class ActivityMainMenu extends AppCompatActivity {

    private RecyclerView recycler, search_recycler;
    private MnAdapter adapter;
    private MnSearchAdapter searchAdapter;
    private SharedPref sharedPref;
    private ActionBar actionBar;
    private AppBarLayout appbar_layout;
    private Toolbar toolbar;
    private BottomNavigationView main_bottom_navigation;
    private View bottom_bar;
    private View content_screens, content_apps, content_updates, content_setting;

    private Integer news_page = 20;
    private View notif_badge;
    private int notification_count = -1;
    private DAO dao;
    List<Item> search_items = new ArrayList<>();

    private AdapterNews adapterNews;
    private boolean allLoaded = false;
    private Call<RespNews> callback = null;
    private RequestListener<RespNews> requestListener;

    private RecyclerView recycler_view_news;
    private SwipeRefreshLayout swipeRefresh;
    private View lytShimmer, lytFailed;
    private ShimmerFrameLayout shimmer;
    private Sell sell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        sharedPref = new SharedPref(this);

        dao = AppDatabase.getDb(this).getDAO();

        initToolbar();
        initComponentUpdates();
        initComponentMenu();
        initDrawerMenu();
        onNavigationMenuSelected(R.id.menu_screen);

        requestAction(1);

        // Permission Notification
        PermissionUtil.checkAndRequestNotification(this);
    }


    private void initComponentUpdates() {
        lytFailed = findViewById(R.id.lyt_failed);
        lytShimmer = findViewById(R.id.lyt_shimmer);
        shimmer = findViewById(R.id.shimmer);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setProgressViewOffset(false, Tools.dpToPx(this, 50), Tools.dpToPx(this, 70));
        recycler_view_news = findViewById(R.id.recycler_view_news);

        recycler_view_news.setLayoutManager(new GridLayoutManager(this, 1));
        recycler_view_news.setHasFixedSize(true);

        //set data and list adapter
        adapterNews = new AdapterNews(this, recycler_view_news, news_page);
        recycler_view_news.setAdapter(adapterNews);

        // detect when scroll reach bottom
        adapterNews.setListener(new AdapterListener<News>() {
            @Override
            public void onClick(View view, String type, News obj, int position) {
                super.onClick(view, type, obj, position);
                ActivityNewsDetail.navigate(ActivityMainMenu.this, obj);
            }

            @Override
            public void onLoadMore(int page) {
                super.onLoadMore(page);
                if (allLoaded) {
                    adapterNews.setLoaded();
                } else {
                    int next_page = page + 1;
                    requestAction(next_page);
                }
            }
        });

        swipeRefresh.setOnRefreshListener(() -> {
            stopRequest();
            allLoaded = false;
            adapterNews.resetListData();
            requestAction(1);
        });

        requestListener = new RequestListener<RespNews>() {
            @Override
            public void onSuccess(RespNews resp) {
                allLoaded = resp.list.size() < news_page;
                displayApiResult(resp.list);
            }

            @Override
            public void onFailed(String messages) {
                onFailRequest();
            }
        };
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        Tools.setSystemBarColorInt(this, getResources().getColor(R.color.darkHomeDark));
    }

    private void initComponentMenu() {
        bottom_bar = findViewById(R.id.bottom_bar);
        appbar_layout = findViewById(R.id.appbar_layout);
        main_bottom_navigation = findViewById(R.id.main_bottom_navigation);
        content_screens = findViewById(R.id.content_screens);
        content_apps = findViewById(R.id.content_apps);
        content_updates = findViewById(R.id.content_updates);
        content_setting = findViewById(R.id.content_setting);

        List<Item> items = MenuGenerator.getItems();
        search_items.clear();
        for (Item i : items) {
            if (i.ItemType == MnType.SUB.getValue()) search_items.add(i);
        }
        recycler = findViewById(R.id.main_recycler);
        adapter = new MnAdapter(this, items, new MnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Item item) {
                onMenuItemSelected(item);
            }
        });

        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setNestedScrollingEnabled(false);
        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //up
                    animateSearchBar(true);
                } else { // down
                    animateSearchBar(false);
                }
            }
        });

        search_recycler = findViewById(R.id.search_recycler);
        search_recycler.setLayoutManager(new LinearLayoutManager(this));
        search_recycler.setNestedScrollingEnabled(false);
        searchAdapter = new MnSearchAdapter(this, search_items);
        search_recycler.setAdapter(searchAdapter);
        searchAdapter.setOnItemClickListener(new MnSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Item obj, int position) {
                onMenuItemSelected(obj);
            }
        });

        search_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //up
                    animateSearchBar(true);
                } else { // down
                    animateSearchBar(false);
                }
            }
        });

        recycler_view_news.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { //up
                    animateSearchBar(true);
                } else { // down
                    animateSearchBar(false);
                }
            }
        });

        if (sharedPref.isFirstLaunch()) {
            showDialogAbout();
        }

        main_bottom_navigation.setOnItemSelectedListener(item -> {
            onNavigationMenuSelected(item.getItemId());
            return true;
        });

        findViewById(R.id.lyt_app_taxi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.taxi.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_mailbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.mailbox.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.news.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_social).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.social.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.shop.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_messenger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.messenger.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_recipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.recipe.ActivityMain.class));
            }
        });

        findViewById(R.id.lyt_app_music).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.music.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.chat.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_travel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.travel.ActivitySplash.class));
            }
        });

        findViewById(R.id.lyt_app_insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityMainMenu.this, template.insta.ActivityMain.class));
            }
        });
    }

    private void onNavigationMenuSelected(int id) {
        content_screens.setVisibility(id == R.id.menu_screen ? View.VISIBLE : View.GONE);
        content_apps.setVisibility(id == R.id.menu_apps ? View.VISIBLE : View.GONE);
        content_updates.setVisibility(id == R.id.menu_updates ? View.VISIBLE : View.GONE);
        content_setting.setVisibility(id == R.id.menu_setting ? View.VISIBLE : View.GONE);
        onMenuItemSelected(null);
    }

    boolean isSearchBarHide = false;

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * appbar_layout.getHeight()) : 0;
        appbar_layout.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();

        int moveNavBar = hide ? main_bottom_navigation.getHeight() : 0;
        bottom_bar.animate().translationY(moveNavBar).setStartDelay(100).setDuration(300).start();
    }

    private void onMenuItemSelected(Item item) {
        try {
            sell.showInterstitial();
        } catch (Exception e) {
        }

        if (Sell.SELL && item != null && item.Act != null) {
            startActivity(new Intent(this, item.Act));
            return;
        }

        if (sharedPref.getClickSwitch()) {
            if (sharedPref.actionClickOffer()) {
                showDialogOffer();
                sharedPref.setClickSwitch(false);
                return;
            }
        } else {
            if (sharedPref.actionClickInters()) {
                sharedPref.setClickSwitch(true);
            }
        }

        if (item != null && item.Act != null) {
            startActivity(new Intent(this, item.Act));
            return;
        }

        if (item != null && item.Id == 1) {
            showDialogAbout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        Tools.changeMenuIconColor(menu, Color.WHITE);
        Tools.changeOverflowMenuIconColor(toolbar, Color.WHITE);

        final MenuItem menu_notif = menu.findItem(R.id.action_notifications);
        View actionView = MenuItemCompat.getActionView(menu_notif);
        notif_badge = actionView.findViewById(R.id.notif_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menu_notif);
            }
        });

        MenuItem action_search = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(action_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(action_search, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recycler.setVisibility(View.VISIBLE);
                main_bottom_navigation.setVisibility(View.VISIBLE);
                search_recycler.setVisibility(View.GONE);
                menu_notif.setVisible(true);
                initToolbar();
                initDrawerMenu();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                onNavigationMenuSelected(R.id.menu_screen);
                recycler.setVisibility(View.GONE);
                main_bottom_navigation.setVisibility(View.GONE);
                search_recycler.setVisibility(View.VISIBLE);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
                menu_notif.setVisible(false);
                return true;
            }
        });

        return true;
    }

    private void setupBadge() {
        if (notif_badge == null) return;
        if (notification_count == 0) {
            notif_badge.setVisibility(View.GONE);
            (findViewById(R.id.notif_badge)).setVisibility(View.GONE);
        } else {
            notif_badge.setVisibility(View.VISIBLE);
            (findViewById(R.id.notif_badge)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.action_notifications) {
            ActivityNotifications.navigate(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDrawerMenu() {

        (findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openInAppBrowser(ActivityMainMenu.this, "https://codecanyon.net/item/materialx-android-material-design-ui-components-10/20482674", false);
            }
        });

        (findViewById(R.id.action_portfolio)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openInAppBrowser(ActivityMainMenu.this, "https://portfolio.dream-space.web.id/", false);
            }
        });

        (findViewById(R.id.action_notifications)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityNotifications.navigate(ActivityMainMenu.this);
            }
        });

        (findViewById(R.id.action_favorite)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityFavorites.navigate(ActivityMainMenu.this);
            }
        });

        (findViewById(R.id.action_rate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.rateAction(ActivityMainMenu.this);
            }
        });

        (findViewById(R.id.action_about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAbout();
            }
        });

        (findViewById(R.id.action_contact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openInAppBrowser(ActivityMainMenu.this, "https://dream-space.web.id/contact-us", false);
            }
        });

        (findViewById(R.id.action_hire)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openInAppBrowser(ActivityMainMenu.this, "https://dream-space.web.id/hire-us", false);
            }
        });

        (findViewById(R.id.action_website)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openInAppBrowser(ActivityMainMenu.this, "https://dream-space.web.id/", false);
            }
        });

        (findViewById(R.id.action_request)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"dev.dream.space@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "MATERIALX_SUGGEST");
                i.putExtra(Intent.EXTRA_TEXT, "Hi, I have suggestion UI for material x, the design attached");
                try {
                    startActivity(Intent.createChooser(i, "Choose email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    // handle edge case where no email client is installed
                }
            }
        });

    }


    private boolean ads_initiate = false;

    @Override
    protected void onResume() {
        super.onResume();

        sell = new Sell(this);
        if (Sell.isSynced() && !ads_initiate) {
            ads_initiate = true;
            sell.showBanner(findViewById(R.id.ad_container));
            sell.prepareInterstitial();
        }

        int new_notif_count = dao.getNotificationUnreadCount();
        if (new_notif_count != notification_count) {
            notification_count = new_notif_count;
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void showDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.main_dialog_about);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText("Version " + BuildConfig.VERSION_NAME);

        ((View) dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.openInAppBrowser(ActivityMainMenu.this, "https://codecanyon.net/item/materialx-android-material-design-ui-components-10/20482674", false);
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sharedPref.setFirstLaunch(false);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showDialogOffer() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_offer);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((View) dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.openInAppBrowser(ActivityMainMenu.this, "https://codecanyon.net/item/materialx-android-material-design-ui-components-10/20482674", false);
            }
        });

        sharedPref.setFirstLaunch(false);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Tools.checkGooglePlayUpdate(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Tools.checkGooglePlayUpdateStopListener();
    }

    private void requestAction(final int page_no) {
        if (page_no == 1) {
            showNoItemView(false);
            swipeProgress(true);
        } else {
            adapterNews.setLoadingOrFailed(null);
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> requestPost(page_no), 200);
    }

    private void requestPost(int pageNo) {
        ParamList paramList = new ParamList();
        paramList.count = news_page.toString();
        paramList.page = String.valueOf(pageNo);
        callback = new Request(this).getNews(paramList, requestListener);
    }

    private void onFailRequest() {
        try {
            swipeProgress(false);
            if (Tools.isConnect(this)) {
                adapterNews.setLoadingOrFailed(getString(R.string.failed_text));
            } else {
                adapterNews.setLoadingOrFailed(getString(R.string.no_internet_text));
            }
        } catch (Exception e) {

        }
    }

    private void swipeProgress(final boolean show) {
        swipeRefresh.post(() -> swipeRefresh.setRefreshing(show));
        if (!show) {
            recycler_view_news.setVisibility(View.VISIBLE);
            lytShimmer.setVisibility(View.GONE);
            shimmer.stopShimmer();
            return;
        }
        recycler_view_news.setVisibility(View.GONE);
        lytShimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmer();
    }

    private void displayApiResult(final List<News> items) {
        try {
            adapterNews.insertData(items);
            swipeProgress(false);
            showNoItemView(adapter.getItemCount() == 0);
        } catch (Exception e) {

        }
    }

    private void showNoItemView(boolean show) {
        if (show) {
            lytFailed.setVisibility(View.VISIBLE);
        } else {
            lytFailed.setVisibility(View.GONE);
        }
    }

    private void stopRequest() {
        if (callback != null && callback.isExecuted()) {
            callback.cancel();
        }
    }


}
