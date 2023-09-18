package template.messenger.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import template.messenger.ActivityChatDetails;
import template.messenger.ActivityMain;
import template.messenger.R;
import template.messenger.adapter.ChatsListAdapter;
import template.messenger.data.Constant;
import template.messenger.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class PageRecentFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    private List<Chat> items = new ArrayList<>();
    private ChatsListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_messenger_page_fragment_recent, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        items = Constant.getChatsData(getActivity());

        // specify an adapter (see also next example)
        mAdapter = new ChatsListAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ChatsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Chat obj, int position) {
                ActivityChatDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.lyt_parent), obj.getFriend(), obj.getSnippet());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
