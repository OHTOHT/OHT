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
import template.music.adapter.AdapterListSong;
import template.music.data.Constant;
import template.music.data.GlobalVariable;
import template.music.data.PlayerState;
import template.music.model.MusicSong;

import java.util.List;


public class FragmentTabSong extends Fragment {

    private View root;
    private GlobalVariable global;

    public FragmentTabSong() {
    }

    public static FragmentTabSong newInstance() {
        FragmentTabSong fragment = new FragmentTabSong();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.app_music_fragment_tab_song, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        global = GlobalVariable.getInstance(getContext().getApplicationContext());

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<MusicSong> items = Constant.getMusicSong(getActivity());

        //set data and list adapter
        AdapterListSong mAdapter = new AdapterListSong(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        global.setMusicSong(mAdapter.getItem(0));

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSong.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MusicSong obj, int position) {
                global.setMusicSong(obj);
                global.setPlayerState(PlayerState.RESTART);
            }
        });

        mAdapter.setOnMoreButtonClickListener(new AdapterListSong.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, MusicSong obj, MenuItem item) {
                Toast.makeText(getActivity(), obj.title + " (" + item.getTitle() + ") clicked", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), item.getTitle() + " Song Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}