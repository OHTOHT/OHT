package template.music.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import template.music.R;
import template.music.adapter.AdapterListArtist;
import template.music.data.Constant;
import template.music.model.Artist;

import java.util.List;


public class FragmentTabArtist extends Fragment {

    private View root;

    public FragmentTabArtist() {
    }

    public static FragmentTabArtist newInstance() {
        FragmentTabArtist fragment = new FragmentTabArtist();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.app_music_fragment_tab_artist, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Artist> items = Constant.getArtist(getActivity());

        //set data and list adapter
        AdapterListArtist mAdapter = new AdapterListArtist(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListArtist.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Artist obj, int pos) {
                Toast.makeText(getActivity(), obj.title + " Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_music_menu_fragment_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Toast.makeText(getActivity(), item.getTitle() + " Artist Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}