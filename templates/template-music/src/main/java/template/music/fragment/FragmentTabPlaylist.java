package template.music.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import template.music.R;
import template.music.adapter.AdapterPlaylist;
import template.music.data.GlobalVariable;
import template.music.data.PlayerState;
import template.music.data.Tools;
import template.music.model.Playlist;

import java.util.List;


public class FragmentTabPlaylist extends Fragment {

    private View root;
    private GlobalVariable global;
    private AdapterPlaylist mAdapter;

    public FragmentTabPlaylist() {
    }

    public static FragmentTabPlaylist newInstance() {
        FragmentTabPlaylist fragment = new FragmentTabPlaylist();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.app_music_fragment_tab_playlist, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        global = GlobalVariable.getInstance(getContext().getApplicationContext());

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Playlist> items = global.getPlaylist();

        //set data and list adapter
        mAdapter = new AdapterPlaylist(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterPlaylist.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Playlist obj, int position) {
                Toast.makeText(getActivity(), obj.title + " Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnMoreButtonClickListener(new AdapterPlaylist.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, Playlist obj, MenuItem item, int pos) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_play_all) {
                    global.setMusicSong(global.getMusicSong());
                    global.setPlayerState(PlayerState.RESTART);
                } else if (itemId == R.id.action_rename) {
                    dialogManagePlaylist(obj, pos);
                } else if (itemId == R.id.action_delete) {
                    dialogDeletePlaylist(obj, pos);
                } else {
                    Toast.makeText(getActivity(), obj.title + " " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_music_menu_fragment_playlist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_playlist) {
            dialogManagePlaylist(null, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogManagePlaylist(final Playlist p, final int position) {
        final boolean isNew = (p == null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(isNew ? "New playlist" : "Rename playlist");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(0);
        input.setSingleLine(true);
        input.setEms(100);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        input.setHint("Playlist name");
        input.setText(isNew ? "" : p.title);

        LinearLayout lyt = new LinearLayout(getActivity());
        int padding = Tools.dpToPx(getActivity(), 20);
        lyt.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lyt.setLayoutParams(params);
        lyt.addView(input);
        builder.setView(lyt);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Playlist name cannot empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (global.isPlaylistExist(name)) {
                    Toast.makeText(getActivity(), "Name already exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isNew) {
                    global.addPlaylist(name);
                    mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
                } else {
                    p.title = name;
                    global.updatePlaylist(p);
                    mAdapter.notifyItemChanged(position);
                }
                Toast.makeText(getActivity(), "Playlist " + (isNew ? "added" : "updated"), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void dialogDeletePlaylist(final Playlist p, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete playlist");
        builder.setMessage(Html.fromHtml("Delete the playlist <b>" + p.title + "</b> ?"));

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                global.removePlaylist(p);
                mAdapter.notifyItemRemoved(position);
                Toast.makeText(getActivity(), "Playlist deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}