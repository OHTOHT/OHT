package template.mailbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import template.mailbox.adapter.MailListAdapter;
import template.mailbox.data.GlobalVariable;
import template.mailbox.data.Tools;
import template.mailbox.model.Mail;
import template.mailbox.widget.CircleTransform;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class ActivityMailDetails extends AppCompatActivity {
    public static final String EXTRA_OBJCT = "template.mailbox.ITEM";
    public static final String EXTRA_MODE = "template.mailbox.MODE";
    public static final String EXTRA_POSITION = "template.mailbox.POSITION";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Mail obj, int mode, int position) {
        Intent intent = new Intent(activity, ActivityMailDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        intent.putExtra(EXTRA_MODE, mode);
        intent.putExtra(EXTRA_POSITION, position);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Toolbar toolbar;
    private ActionBar actionBar;
    private View parent_view;
    // extra obj
    private Mail mail;
    private int mode = 0;
    private int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_mailbox_activity_mail_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(parent_view, EXTRA_OBJCT);

        // get extra object
        mail        = (Mail) getIntent().getSerializableExtra(EXTRA_OBJCT);
        mode        = getIntent().getIntExtra(EXTRA_MODE, 0);
        position    = getIntent().getIntExtra(EXTRA_POSITION, 0);

        initToolbar();
        ((TextView) findViewById(R.id.subject)).setText(mail.getSubject());
        ((TextView) findViewById(R.id.name)).setText(mail.getSender().getName());
        ((TextView) findViewById(R.id.content)).setText(mail.getContent());
        Picasso.with(this).load(mail.getSender().getPhoto())
                .resize(100, 100)
                .transform(new CircleTransform())
                .into(((ImageView) findViewById(R.id.image)));
        TextView tv_from = (TextView) findViewById(R.id.address_from);
        TextView tv_to = (TextView) findViewById(R.id.address_to);
        tv_from.setText(mail.getSender().getAddress());
        tv_to.setText(mail.getReceiver().getAddress());

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void actionClick(View view){
        Button bt = (Button) findViewById(view.getId());
        Snackbar.make(parent_view, bt.getText()+" Clicked", Snackbar.LENGTH_SHORT).show();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_mailbox_menu_activity_mail_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.action_trash) {
            dialogDeleteMessageConfirm(mail, position);
        }
        if (item.getItemId() == R.id.action_mail) {
            Intent i = new Intent(getApplicationContext(), ActivityComposeMail.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogDeleteMessageConfirm(final Mail c, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Move to Trash Confirmation");
        builder.setMessage("Mail from : " + c.getSender().getName()+ " will be move to trash?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mode == MailListAdapter.INBOX_MODE) {
                    GlobalVariable.trashInbox(position);
                } else if (mode == MailListAdapter.OUTBOX_MODE) {
                    GlobalVariable.trashOutbox(position);
                } else if (mode == MailListAdapter.TRASH_MODE) {
                }
                Snackbar.make(parent_view, "Moved to Trash", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mode == MailListAdapter.INBOX_MODE) {
                            GlobalVariable.undoTrashInbox(c, position);
                        } else if (mode == MailListAdapter.OUTBOX_MODE) {
                            GlobalVariable.undoTrashOutbox(c, position);
                        } else if (mode == MailListAdapter.TRASH_MODE) {
                        }
                    }
                }).show();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}
