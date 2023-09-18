package template.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import template.news.data.GlobalVariable;
import template.news.model.News;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class ActivityNewsDetails extends AppCompatActivity {
    public static final String EXTRA_OBJC = "template.news.EXTRA_OBJC";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, News obj) {
        Intent intent = new Intent(activity, ActivityNewsDetails.class);
        intent.putExtra(EXTRA_OBJC, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJC);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Toolbar toolbar;
    private ActionBar actionBar;
    // extra obj
    private News news;
    private View parent_view;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_news_activity_news_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.image), EXTRA_OBJC);

        // get extra object
        news = (News) getIntent().getSerializableExtra(EXTRA_OBJC);
        initToolbar();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabToggle();

        ((TextView) findViewById(R.id.title)).setText(news.getTitle());
        ((TextView) findViewById(R.id.content)).setText(news.getContent());
        ((TextView) findViewById(R.id.date)).setText(news.getDate());
        TextView channel = (TextView) findViewById(R.id.channel);
        channel.setText(news.getChannel().getName());
        channel.setBackgroundColor(Color.parseColor(news.getChannel().getColor()));
        Picasso.with(this).load(news.getImage()).into(((ImageView) findViewById(R.id.image)));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalVariable.isSaved(news)) {
                    GlobalVariable.removeSaved(news);
                    Snackbar.make(parent_view, "News remove from favorites", Snackbar.LENGTH_SHORT).show();
                } else {
                    GlobalVariable.addSaved(news);
                    Snackbar.make(parent_view, "News added to favorites", Snackbar.LENGTH_SHORT).show();
                }
                fabToggle();
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }

    private void fabToggle() {
        if (GlobalVariable.isSaved(news)) {
            fab.setImageResource(R.drawable.app_news_ic_nav_saved);
        } else{
            fab.setImageResource(R.drawable.app_news_ic_nav_saved_outline);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }else{
            Snackbar.make(parent_view, item.getTitle() + " clicked", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.app_news_menu_activity_news_details, menu);
        return true;
    }

}
