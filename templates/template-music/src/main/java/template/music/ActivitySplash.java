package template.music;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import template.music.data.Tools;

import java.util.Timer;
import java.util.TimerTask;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_music_activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // go to the main activity
                Intent i = new Intent(getApplicationContext(), ActivityMain.class);
                startActivity(i);
                // kill current activity
                finish();
            }
        };
        // Show splash screen for 3 seconds
        new Timer().schedule(task, 2000);

        // for system bar in lollipop
        Tools.systemBarLollipop(this);
    }
}
