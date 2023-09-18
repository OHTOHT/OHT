package template.insta.fragment;

import android.os.AsyncTask;
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
import android.widget.ProgressBar;

import template.insta.R;
import template.insta.adapter.FeedListAdapter;
import template.insta.data.Constant;
import template.insta.model.Feed;

import java.util.ArrayList;
import java.util.List;

public class PageHomeFragment extends Fragment {

    private View view;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private FeedListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_insta_page_fragment_home, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);

        if (!taskRunning) {
            new DummyFeedLoader().execute("");
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_insta_menu_fragment_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar.make(view, item.getTitle() + " Clicked", Snackbar.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private boolean taskRunning = false;

    private class DummyFeedLoader extends AsyncTask<String, String, String> {
        private String status = "";
        private List<Feed> items = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            taskRunning = true;
            items.clear();
            progressbar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(500);
                items = Constant.getRandomFeed(getActivity());
                status = "success";
            } catch (Exception e) {
                status = "failed";
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressbar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (status.equals("success")) {
                //set data and list adapter
                mAdapter = new FeedListAdapter(getActivity(), items);
                recyclerView.setAdapter(mAdapter);
            }
            taskRunning = false;
            super.onProgressUpdate(values);
        }
    }

}
