package template.mailbox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.mailbox.ActivityComposeMail;
import template.mailbox.ActivityMailDetails;
import template.mailbox.ActivityMain;
import template.mailbox.R;
import template.mailbox.adapter.MailListAdapter;
import template.mailbox.data.GlobalVariable;
import template.mailbox.model.Mail;
import template.mailbox.widget.DividerItemDecoration;
import com.google.android.material.snackbar.Snackbar;

public class OutboxFragment extends Fragment {

    public RecyclerView recyclerView;
    public MailListAdapter mAdapter;
    private ProgressBar progressBar;
    private View view;
    private SearchView searchView;
    private LinearLayout lyt_not_found;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_mailbox_fragment_outbox, null);

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lyt_not_found = (LinearLayout) view.findViewById(R.id.lyt_not_found);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        mAdapter = new MailListAdapter(MailListAdapter.OUTBOX_MODE, getActivity(), GlobalVariable.getOutbox());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MailListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Mail obj, int position) {
                ActivityMailDetails.navigate((ActivityMain) getActivity(), view.findViewById(R.id.lyt_parent), obj, MailListAdapter.OUTBOX_MODE, position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        if (mAdapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
        return view;
    }

    // handle swipe to delete, and dragable
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final Mail mail = mAdapter.getItem(position);
            mAdapter.remove(position);
            Snackbar.make(view, "Moved to Trash", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapter.add(position, mail);
                }
            }).show();
        }
    });

    @Override
    public void onResume() {
        mAdapter.setItems(GlobalVariable.getOutbox());
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_mailbox_menu_fragment_inbox, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search mail...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    mAdapter.getFilter().filter(s);
                } catch (Exception e) {
                }
                return true;
            }
        });
        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTouchHelper.attachToRecyclerView(null);
                setItemsVisibility(menu, searchItem, false);
            }
        });

        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                itemTouchHelper.attachToRecyclerView(recyclerView);
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
        searchView.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_mail) {
            Intent i = new Intent(getActivity().getApplicationContext(), ActivityComposeMail.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
