package com.screens.activity.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.screens.R;

import com.screens.utils.Tools;

public class TimelineExplore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_explore);

        initToolbar();

        // display image
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_1), R.drawable.photo_female_1);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_2), R.drawable.photo_female_4);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_3), R.drawable.photo_male_5);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_4), R.drawable.photo_male_8);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_5), R.drawable.photo_female_6);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_6), R.drawable.photo_male_7);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_7), R.drawable.photo_female_5);


        // display images
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.images_1), R.drawable.image_12);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.images_2), R.drawable.image_13);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.images_3), R.drawable.image_14);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.images_4), R.drawable.image_15);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.images_5), R.drawable.image_26);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.images_6), R.drawable.image_30);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}