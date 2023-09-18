package template.chat;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import template.chat.adapter.FriendsListAdapter;
import template.chat.data.Constant;
import template.chat.data.Tools;
import template.chat.model.Friend;
import template.chat.widget.DividerItemDecoration;

public class ActivitySelectFriend extends AppCompatActivity {

    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private FriendsListAdapter mAdapter;
    private SearchView search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_chat_activity_new_chat);
        initToolbar();
        initComponent();
        // specify an adapter (see also next example)
        mAdapter = new FriendsListAdapter(this, Constant.getFriendsData(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int position) {
                ActivityChatDetails.navigate(ActivitySelectFriend.this, v.findViewById(R.id.lyt_parent), obj, null);
            }
        });

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
	}

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setSubtitle(Constant.getFriendsData(this).size()+" friends");
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_chat_menu_new_chat, menu);
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return true;
            }
        });
        search.onActionViewCollapsed();
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_search) {// this do magic
            supportInvalidateOptionsMenu();
            return true;
        }
        return false;
    }
}
