package template.music.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import template.music.ActivityAlbumDetail;
import template.music.ActivityMain;
import template.music.R;
import template.music.adapter.AdapterGridAlbum;
import template.music.data.Constant;
import template.music.data.Tools;
import template.music.model.MusicAlbum;
import template.music.widget.SpacingItemDecoration;

import java.util.List;


public class FragmentTabAlbum extends Fragment {

    private View root;

    private AdapterGridAlbum mAdapter;

    public FragmentTabAlbum() {
    }

    public static FragmentTabAlbum newInstance() {
        FragmentTabAlbum fragment = new FragmentTabAlbum();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.app_music_fragment_tab_album, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 4), true));

        List<MusicAlbum> items = Constant.getMusicAlbum(getActivity());
        //set data and list adapter
        mAdapter = new AdapterGridAlbum(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridAlbum.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MusicAlbum obj, int position) {
                ActivityAlbumDetail.navigate(((ActivityMain) getActivity()), view, obj);
            }
        });


        return root;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_music_menu_fragment_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Toast.makeText(getActivity(), item.getTitle() + " Album Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}