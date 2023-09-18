package template.social;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import template.social.data.Tools;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_social_activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                startActivity(i);
                finish();
            }
        };
        new Timer().schedule(task, 600);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }
}