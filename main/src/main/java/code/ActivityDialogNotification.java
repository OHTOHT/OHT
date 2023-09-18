package code;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.material.components.R;

import code.model.News;
import code.room.AppDatabase;
import code.room.table.NotificationEntity;
import code.utils.Tools;

public class ActivityDialogNotification extends AppCompatActivity {

    private static final String EXTRA_OBJECT = "key.EXTRA_OBJECT";
    private static final String EXTRA_FROM_NOTIF = "key.EXTRA_FROM_NOTIF";
    private static final String EXTRA_POSITION = "key.EXTRA_FROM_POSITION";

    // activity transition
    public static void navigate(Activity activity, NotificationEntity obj, Boolean from_notif, int position) {
        Intent i = navigateBase(activity, obj, from_notif);
        i.putExtra(EXTRA_POSITION, position);
        activity.startActivity(i);
    }

    public static Intent navigateBase(Context context, NotificationEntity obj, Boolean from_notif) {
        Intent i = new Intent(context, ActivityDialogNotification.class);
        i.putExtra(EXTRA_OBJECT, obj);
        i.putExtra(EXTRA_FROM_NOTIF, from_notif);
        return i;
    }

    private Boolean fromNotif;
    private NotificationEntity notification;
    private Intent intent;
    private int position;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_notification);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notification = (NotificationEntity) getIntent().getSerializableExtra(EXTRA_OBJECT);
        fromNotif = getIntent().getBooleanExtra(EXTRA_FROM_NOTIF, false);
        position = getIntent().getIntExtra(EXTRA_POSITION, -1);

        if (notification == null) {
            finish();
            intent = new Intent(this, ActivityMainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

        // set notification as read
        notification.read = true;
        AppDatabase.getDb(this).getDAO().insertNotification(notification);

        initComponent();
    }

    private void initComponent() {
        ((TextView) findViewById(R.id.title)).setText(notification.title);
        ((TextView) findViewById(R.id.content)).setText(notification.content);
        ((TextView) findViewById(R.id.date)).setText(Tools.getFormattedDateSimple(notification.created_at));

        String image_url = notification.image;
        String link = notification.link;
        String type = notification.type;
        intent = new Intent(this, ActivityMainMenu.class);

        if (fromNotif) {
            (findViewById(R.id.bt_delete)).setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(link)) {
                (findViewById(R.id.bt_open)).setVisibility(View.GONE);
            }
            (findViewById(R.id.logo)).setVisibility(View.GONE);
            (findViewById(R.id.view_space)).setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(image_url)) {
            (findViewById(R.id.lyt_image)).setVisibility(View.VISIBLE);
            Tools.displayImage(this, findViewById(R.id.image), image_url);
        } else {
            findViewById(R.id.lyt_image).setVisibility(View.GONE);
        }

        (findViewById(R.id.img_close)).setOnClickListener(view -> finish());
        if (type.equals("NEWS")) {
            news = new Gson().fromJson(notification.object, News.class);
        }

        (findViewById(R.id.bt_open)).setOnClickListener(v -> {
            finish();
            if (type.equals("NEWS") && news != null) {
                ActivityNewsDetail.navigate(this, news);
            } else if(!TextUtils.isEmpty(link)){
                Tools.directLinkCustomTab(this, link, false);
            } else {
                startActivity(intent);
            }
        });

        (findViewById(R.id.bt_delete)).setOnClickListener(view -> {
            finish();
            if (!fromNotif && position != -1) {
                AppDatabase.getDb(this).getDAO().deleteNotification(notification.id);
                ActivityNotifications.getInstance().adapter.removeItem(position);
            }
        });
    }
}