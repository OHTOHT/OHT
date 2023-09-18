package template.music;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import template.music.data.GlobalVariable;
import template.music.data.OnMusicSongChange;
import template.music.data.OnPlayerStateChange;
import template.music.data.PlayerState;
import template.music.data.Tools;
import template.music.fragment.FragmentTabAlbum;
import template.music.fragment.FragmentTabArtist;
import template.music.fragment.FragmentTabPlaylist;
import template.music.fragment.FragmentTabSong;
import template.music.model.MusicSong;
import template.music.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    public View parent_view;

    private ViewPager view_pager;
    private TabLayout tab_layout;
    private SectionsPagerAdapter viewPagerAdapter;
    private ImageView image_album;
    private ImageView bt_expand;
    private View lyt_disc;

    private ImageView bt_play;

    private GlobalVariable global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_music_activity_main);
        parent_view = findViewById(R.id.parent_view);
        global = GlobalVariable.getInstance(getApplicationContext());

        initToolbar();
        initComponent();
        actionHandle();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Songs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Tools.systemBarLollipop(this);
    }

    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        image_album = (ImageView) findViewById(R.id.image_album);
        bt_expand = (ImageView) findViewById(R.id.bt_expand);
        lyt_disc = (View) findViewById(R.id.lyt_disc);
        bt_play = (ImageView) findViewById(R.id.bt_play);
        Picasso.with(this).load(R.drawable.app_music_photo_album_1).transform(new CircleTransform()).into(image_album);

        setupViewPager(view_pager);
        tab_layout.setupWithViewPager(view_pager);

        tab_layout.getTabAt(0).setIcon(R.drawable.app_music_ic_song);
        tab_layout.getTabAt(1).setIcon(R.drawable.app_music_ic_album);
        tab_layout.getTabAt(2).setIcon(R.drawable.app_music_ic_artist);
        tab_layout.getTabAt(3).setIcon(R.drawable.app_music_ic_playlist);

        // set icon color pre-selected
        tab_layout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.app_music_colorAccent), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.app_music_whiteOverlay), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.app_music_whiteOverlay), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.app_music_whiteOverlay), PorterDuff.Mode.SRC_IN);
    }

    private void actionHandle() {
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportActionBar().setTitle(viewPagerAdapter.getTitle(tab.getPosition()));
                tab.getIcon().setColorFilter(getResources().getColor(R.color.app_music_colorAccent), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.app_music_whiteOverlay), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // setup music component
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (global.isPlaying()) {
                    global.setPlayerState(PlayerState.PAUSE);
                } else {
                    global.setPlayerState(PlayerState.START);
                }
            }
        });

        bt_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityPlayerDetail.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        global.setOnPlayerStateChange(new OnPlayerStateChange() {
            @Override
            public void onStart() {
                bt_play.setImageResource(R.drawable.app_music_ic_pause);
                rotateImageAlbum();
            }

            @Override
            public void onPause() {
                bt_play.setImageResource(R.drawable.app_music_ic_play);
                rotateImageAlbum();
            }

            @Override
            public void onRestart() {

            }

            @Override
            public void onComplete() {
                rotateImageAlbum();
                bt_play.setImageResource(R.drawable.app_music_ic_play);
            }
        });

        global.setOnMusicSongChange(new OnMusicSongChange() {
            @Override
            public void onChange(MusicSong musicSong) {
                changeMusicInfo(musicSong);
            }
        });

        changeMusicInfo(global.getMusicSong());
        rotateImageAlbum();
        if (global.isPlaying()) {
            bt_play.setImageResource(R.drawable.app_music_ic_pause);
        } else {
            bt_play.setImageResource(R.drawable.app_music_ic_play);
        }
        super.onResume();
    }

    private void changeMusicInfo(MusicSong musicSong) {
        if (musicSong == null) return;
        Picasso.with(this).load(musicSong.image).transform(new CircleTransform()).into(image_album);
        ((TextView) findViewById(R.id.music_title)).setText(musicSong.title);
        ((TextView) findViewById(R.id.music_album)).setText(musicSong.album);
    }

    private void rotateImageAlbum() {
        if (!global.isPlaying()) return;
        lyt_disc.animate().setDuration(100)
                .setInterpolator(new LinearInterpolator())
                .rotation(lyt_disc.getRotation() + 5f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateImageAlbum();
                super.onAnimationEnd(animation);
            }
        });
    }

    // stop player when destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        global.releasePlayer();
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(FragmentTabSong.newInstance(), "Songs");
        viewPagerAdapter.addFragment(FragmentTabAlbum.newInstance(), "Albums");
        viewPagerAdapter.addFragment(FragmentTabArtist.newInstance(), "Artists");
        viewPagerAdapter.addFragment(FragmentTabPlaylist.newInstance(), "Playlist");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_music_menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_settings || id == R.id.action_about) {
            Toast.makeText(this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public String getTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

}

