package template.chat.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import template.chat.ActivityFriendDetails;
import template.chat.ActivityMain;
import template.chat.R;
import template.chat.adapter.FriendsListAdapter;
import template.chat.data.Constant;
import template.chat.model.Friend;
import template.chat.widget.DividerItemDecoration;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    public FriendsListAdapter mAdapter;
    private ProgressBar progressBar;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_chat_fragment_friends, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar  = (ProgressBar) view.findViewById(R.id.progressBar);
		
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		
        // specify an adapter (see also next example)
        mAdapter = new FriendsListAdapter(getActivity(), Constant.getFriendsData(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int position) {
                ActivityFriendDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.image), obj);
            }
        });

        return view;
    }

    public void onRefreshLoading(){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

}
