package code;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.material.components.R;

import code.model.News;
import code.utils.Tools;

public class ActivityNewsDetail extends AppCompatActivity {

    private static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    public static void navigate(Activity activity, News object) {
        Intent i = new Intent(activity, ActivityNewsDetail.class);
        i.putExtra(EXTRA_OBJC, object);
        activity.startActivity(i);
    }

    private News listing;
    private int curTextSize;
    private Toolbar toolbar;
    private ImageView image;
    private TextView title, date;
    private WebView content;
    private View bt_open_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        listing = (News) getIntent().getSerializableExtra(EXTRA_OBJC);
        initToolbarComponent();
        displayData();

    }

    private void initToolbarComponent() {
        bt_open_link = findViewById(R.id.bt_open_link);
        date = findViewById(R.id.date);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        image.setOnClickListener(view -> ActivityFullScreenImage.navigate(this, listing.image));
        Tools.setSystemBarColorInt(this, getResources().getColor(R.color.darkHomeDark));
        bt_open_link.setVisibility(TextUtils.isEmpty(listing.link) ? View.GONE : View.VISIBLE);
        bt_open_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.directLinkCustomTab(ActivityNewsDetail.this, listing.link, false);
            }
        });

    }

    private void displayData() {
        Tools.displayImage(this, image, listing.image);
        title.setText(Html.fromHtml(listing.title));
        date.setText(Tools.getDateTime(listing.created_at, false));
        Tools.displayContentHtml(this, content, listing.description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}