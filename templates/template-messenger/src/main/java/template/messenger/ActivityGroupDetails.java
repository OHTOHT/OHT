package template.messenger;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import template.messenger.adapter.GroupDetailsListAdapter;
import template.messenger.data.Constant;
import template.messenger.data.Tools;
import template.messenger.model.Group;
import template.messenger.model.GroupDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ActivityGroupDetails extends AppCompatActivity {
    public static String KEY_GROUP = "com.app.sample.chatting.GROUP";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Group obj) {
        Intent intent = new Intent(activity, ActivityGroupDetails.class);
        intent.putExtra(KEY_GROUP, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, KEY_GROUP);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Button btn_send;
    private EditText et_content;
    public static GroupDetailsListAdapter adapter;

    private ListView listview;
    private ActionBar actionBar;
    private Group group;
    private List<GroupDetails> items = new ArrayList<>();
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_messenger_activity_group_details);
        parent_view = findViewById(android.R.id.content);

        // animation transition
        ViewCompat.setTransitionName(parent_view, KEY_GROUP);

        // initialize conversation data
        Intent intent = getIntent();
        group = (Group) intent.getExtras().getSerializable(KEY_GROUP);

        initToolbar();

        iniComponen();
        items = Constant.getGroupDetailsData(this);

        adapter = new GroupDetailsListAdapter(this, items);
        listview.setAdapter(adapter);
        listview.setSelectionFromTop(adapter.getCount(), 0);
        listview.requestFocus();
        registerForContextMenu(listview);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(group.getName());
        actionBar.setSubtitle(group.getMember());
    }

    public void bindView() {
        try {
            adapter.notifyDataSetChanged();
            listview.setSelectionFromTop(adapter.getCount(), 0);
        } catch (Exception e) {

        }
    }

    public void iniComponen() {
        final Random r = new Random();
        listview = (ListView) findViewById(R.id.listview);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = r.nextInt(group.getFriends().size()-1);

                items.add(items.size(), new GroupDetails(  0, Constant.formatTime(System.currentTimeMillis()), group.getFriends().get(0), et_content.getText().toString(), true));
                items.add(items.size(), new GroupDetails( 0, Constant.formatTime(System.currentTimeMillis()), group.getFriends().get(index), et_content.getText().toString(), false));

                et_content.setText("");
                bindView();
                hideKeyboard();
            }
        });
        et_content.addTextChangedListener(contentWatcher);
        if (et_content.length() == 0) {
            btn_send.setEnabled(false);
        }
        hideKeyboard();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setEnabled(false);
            } else {
                btn_send.setEnabled(true);
            }
            //draft.setContent(etd.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_messenger_menu_group_details, menu);
        return true;
    }

    /**
     * Handle click on action bar
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_sample) {
            Snackbar.make(parent_view, item.getTitle() + " Clicked ", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

