package template.chat.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import template.chat.ActivityChatDetails;
import template.chat.ActivityMain;
import template.chat.R;
import template.chat.adapter.ChatsListAdapter;
import template.chat.data.Constant;
import template.chat.model.Chat;
import template.chat.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    public RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;
    public ChatsListAdapter mAdapter;
    private ProgressBar progressBar;
    private ActionMode actionMode;
    private List<Chat> items = new ArrayList<>();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.app_chat_fragment_chat, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        items = Constant.getChatsData(getActivity());

        // specify an adapter (see also next example)
        mAdapter = new ChatsListAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ChatsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Chat obj, int position) {
                if (actionMode != null) {
                    myToggleSelection(position);
                    return;
                }
                ActivityChatDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.lyt_parent), obj.getFriend(), obj.getSnippet());
            }
        });

        mAdapter.setOnItemLongClickListener(new ChatsListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, Chat obj, int position) {
                actionMode = getActivity().startActionMode(modeCallBack);
                myToggleSelection(position);
            }
        });

        bindView();

        return view;
    }

    private void dialogDeleteMessageConfirm(final int count) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Confirmation");
        builder.setMessage("All chat from " + count + " selected item will be deleted?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAdapter.removeSelectedItem();
                mAdapter.notifyDataSetChanged();
                Snackbar.make(view, "Delete " + count + " items success", Snackbar.LENGTH_SHORT).show();
                modeCallBack.onDestroyActionMode(actionMode);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void bindView() {
        try {
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }

    }

    public void onRefreshLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }


    private void myToggleSelection(int idx) {
        mAdapter.toggleSelection(idx);
        String title = mAdapter.getSelectedItemCount() + " selected";
        actionMode.setTitle(title);
    }

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.app_chat_menu_multiple_select, menu);
            ((ActivityMain) getActivity()).setVisibilityAppBar(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.action_delete && mAdapter.getSelectedItemCount() > 0) {
                dialogDeleteMessageConfirm(mAdapter.getSelectedItemCount());
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode act) {
            actionMode.finish();
            actionMode = null;
            mAdapter.clearSelections();
            ((ActivityMain) getActivity()).setVisibilityAppBar(true);
        }
    };

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
