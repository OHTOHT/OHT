package template.messenger;

import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import template.messenger.data.Tools;
import template.messenger.model.Friend;
import template.messenger.widget.CircleTransform;
import com.squareup.picasso.Picasso;

public class ActivityCalling extends AppCompatActivity {

    public static String KEY_FRIEND     = "template.messenger.FRIEND";

    private Friend friend;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_messenger_activity_calling);
        parent_view = findViewById(android.R.id.content);

        // initialize conversation data
        Intent intent = getIntent();
        friend = (Friend) intent.getExtras().getSerializable(KEY_FRIEND);

        initComponent();
        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        ((TextView) findViewById(R.id.name)).setText(friend.getName());
        ImageView image = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(friend.getPhoto()).resize(200, 200).transform(new CircleTransform()).into(image);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actionClick(View v){
        // Handle item selection
        int id = v.getId();
        if (id == R.id.bt_end) {
            onBackPressed();
        } else if (id == R.id.bt_speaker) {
            Snackbar.make(parent_view, "Loud Speaker Clicked ", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.bt_record) {
            Snackbar.make(parent_view, "Record Clicked ", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.bt_chat) {
            Snackbar.make(parent_view, "Chat Clicked ", Snackbar.LENGTH_SHORT).show();
        }
    }
}
