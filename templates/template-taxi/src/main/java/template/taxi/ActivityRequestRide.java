package template.taxi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import template.taxi.data.Tools;

public class ActivityRequestRide extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_taxi_activity_request_ride);

        initComponent();
        scheduleRequest();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        Animation pulse1 = AnimationUtils.loadAnimation(this, R.anim.pulse);
        ((ImageView) findViewById(R.id.image1)).startAnimation(pulse1);

        Animation pulse2 = AnimationUtils.loadAnimation(this, R.anim.pulse2);
        pulse2.setStartOffset(500);
        ((ImageView) findViewById(R.id.image2)).startAnimation(pulse2);

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });

    }

    private void showConfirmDialog() {
        handler.removeCallbacks(runnable);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure want to cancel this Request?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Tools.showToastMiddle(getApplicationContext(), "Request Canceled");
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scheduleRequest();
            }
        });
        builder.show();
    }

    public void showFoundRide() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_taxi_dialog_found_driver);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((Button) dialog.findViewById(R.id.bt_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void scheduleRequest() {
        runnable = new Runnable() {
            public void run() {
                showFoundRide();
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}
