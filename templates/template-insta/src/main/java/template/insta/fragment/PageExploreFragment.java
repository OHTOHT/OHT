package template.insta.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import template.insta.R;
import template.insta.adapter.ExploreGridAdapter;
import template.insta.adapter.FeedListAdapter;
import template.insta.data.Constant;
import template.insta.data.GlobalVariable;
import template.insta.data.Tools;
import template.insta.model.Feed;

import java.util.ArrayList;
import java.util.List;

public class PageExploreFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ExploreGridAdapter mGridAdapter;
    private FeedListAdapter mListAdapter;
    private int selected_pos = 0;
    private List<Feed> items = new ArrayList<>();
    private MenuItem menuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_insta_page_fragment_explore, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);
        items = Constant.getRandomExploreFeed(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (GlobalVariable.isGrid_mode()) {
            switchGridMode();
        } else {
            switchListMode();
        }
        return view;
    }

    private void switchGridMode() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Tools.getGridExplorerCount(getActivity())));
        recyclerView.setHasFixedSize(true);
        recyclerView.setPadding(2, 2, 2, 2);

        //set data and list adapter
        mGridAdapter = new ExploreGridAdapter(getActivity(), items);
        recyclerView.setAdapter(mGridAdapter);
        mGridAdapter.setOnItemClickListener(new ExploreGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Feed obj, int position) {
                selected_pos = position;
                switchListMode();
                menuItem.setIcon(R.drawable.app_insta_ic_menu_grid);
            }
        });
        GlobalVariable.setGrid_mode(true);
    }

    private void switchListMode() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerView.scrollToPosition(selected_pos);

        mListAdapter = new FeedListAdapter(getActivity(), items);
        recyclerView.setAdapter(mListAdapter);
        GlobalVariable.setGrid_mode(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_insta_menu_fragment_explore, menu);
        this.menuItem = menu.getItem(1);
        if (GlobalVariable.isGrid_mode()) {
            menuItem.setIcon(R.drawable.app_insta_ic_menu_list);
        } else {
            menuItem.setIcon(R.drawable.app_insta_ic_menu_grid);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_mode) {
            if (GlobalVariable.isGrid_mode()) {
                switchListMode();
                item.setIcon(R.drawable.app_insta_ic_menu_grid);
            } else {
                switchGridMode();
                item.setIcon(R.drawable.app_insta_ic_menu_list);
            }
        } else {
            Snackbar.make(view, item.getTitle() + " Clicked", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
