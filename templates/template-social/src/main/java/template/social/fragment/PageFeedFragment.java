package template.social.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.social.R;
import template.social.adapter.FeedListAdapter;
import template.social.data.Constant;
import template.social.model.Feed;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PageFeedFragment extends Fragment {

    private View view;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private FeedListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_social_page_fragment_feed, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);
        if(!taskRunning){
            new DummyFeedLoader().execute("");
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_social_menu_fragment_feed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new_feed) {
            Snackbar.make(view, item.getTitle() + " Clicked", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean taskRunning = false;
    private class DummyFeedLoader extends AsyncTask<String, String, String> {
        private String status="";
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
            try{
                Thread.sleep(500);
                items = Constant.getRandomFeed(getActivity());
                status = "success";
            }catch (Exception e){
                status = "failed";
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressbar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if(status.equals("success")) {
                //set data and list adapter
                mAdapter = new FeedListAdapter(getActivity(), items);
                recyclerView.setAdapter(mAdapter);
            }
            taskRunning = false;
            super.onProgressUpdate(values);
        }
    }

}
