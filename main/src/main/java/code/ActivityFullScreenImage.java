package code;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.material.components.R;

public class ActivityFullScreenImage extends AppCompatActivity {

    public static final String EXTRA_URL = "key.EXTRA_URL";

    public static void navigate(AppCompatActivity activity, String url) {
        Intent intent = new Intent(activity, ActivityFullScreenImage.class);
        intent.putExtra(EXTRA_URL, url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        String url = getIntent().getStringExtra(EXTRA_URL);
        if (!URLUtil.isValidUrl(url)) {
            Toast.makeText(this, "invalid image url", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Failed when open image", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                        return false;
                    }
                })
                .into((ImageView) findViewById(R.id.image));

        (findViewById(R.id.btnClose)).setOnClickListener(view -> finish());
        setMarginStatusBarView(this, findViewById(R.id.btnClose));

        darkNavigation(this);
    }

    public static void setMarginStatusBarView(Context context, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.setMargins(lp.leftMargin, lp.topMargin + getStatusBarHeight(context), lp.rightMargin, lp.bottomMargin);
        view.setLayoutParams(lp);
    }

    public static void darkNavigation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getWindow().setNavigationBarColor(Color.BLACK);
            activity.getWindow().setStatusBarColor(Color.BLACK);
            activity.getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int height;
        Resources myResources = context.getResources();
        int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = context.getResources().getDimensionPixelSize(idStatusBarHeight);
        } else {
            height = 0;
        }
        return height;
    }

}
