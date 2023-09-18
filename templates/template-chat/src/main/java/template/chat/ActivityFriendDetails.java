package template.chat;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import template.chat.model.Friend;

public class ActivityFriendDetails extends AppCompatActivity {

    public static final String EXTRA_OBJCT = "template.chat";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Friend obj) {
        Intent intent = new Intent(activity, ActivityFriendDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Friend friend;
    private View parent_view;

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_chat_activity_friend_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        friend = (Friend) getIntent().getSerializableExtra(EXTRA_OBJCT);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(friend.getName());

        ((ImageView) findViewById(R.id.image)).setImageResource(friend.getPhoto());
        ((Button) findViewById(R.id.bt_view_photos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "View Photos Clicked ", Snackbar.LENGTH_SHORT).show();
            }
        });
        ((Button) findViewById(R.id.bt_view_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "View Gallery Clicked ", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.action_send_message){
            Intent i = new Intent(getApplicationContext(), ActivityChatDetails.class);
            i.putExtra(ActivityChatDetails.KEY_FRIEND, friend);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_chat_menu_friend_details, menu);
        return true;
    }

}
