package template.insta.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import template.insta.ActivityHelp;
import template.insta.ActivityLogin;
import template.insta.R;
import template.insta.adapter.ExploreGridAdapter;
import template.insta.adapter.FeedListAdapter;
import template.insta.data.Constant;
import template.insta.data.Tools;
import template.insta.model.Feed;

import java.util.ArrayList;
import java.util.List;

public class PageProfileFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ExploreGridAdapter mGridAdapter;
    private FeedListAdapter mListAdapter;
    private List<Feed> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_insta_page_fragment_profile, container, false);
        // activate fragment menu
        setHasOptionsMenu(true);
        items = Constant.getListMyFeed(getActivity(), 15);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        switchGridMode();
        return view;
    }

    private void switchGridMode() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Tools.getGridExplorerCount(getActivity()));
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPadding(2, 2, 2, 2);

        //set data and list adapter
        mGridAdapter = new ExploreGridAdapter(getActivity(), items);
        recyclerView.setAdapter(mGridAdapter);
        mGridAdapter.setOnItemClickListener(new ExploreGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Feed obj, int position) {
                switchListMode();
            }
        });
    }

    private void switchListMode() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerView.setNestedScrollingEnabled(false);

        mListAdapter = new FeedListAdapter(getActivity(), items);
        recyclerView.setAdapter(mListAdapter);
    }

    public void actionClick(View v) {
        if (v.getId() == R.id.bt_grid) {
            switchGridMode();
        } else if (v.getId() == R.id.bt_list) {
            switchListMode();
        } else if (v.getId() == R.id.bt_places) {
            Snackbar.make(view, "Places Clicked", Snackbar.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.bt_tags) {
            Snackbar.make(view, "Tags Clicked", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_insta_menu_fragment_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("About");
            builder.setMessage(getString(R.string.app_insta_about_text));
            builder.setNeutralButton("OK", null);
            builder.show();
            return true;
        } else if (id == R.id.action_login) {
            Intent i = new Intent(getActivity(), ActivityLogin.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_settings) {
            Snackbar.make(view, "Setting Clicked", Snackbar.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_help) {
            Intent i = new Intent(getActivity(), ActivityHelp.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
