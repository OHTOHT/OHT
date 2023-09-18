package template.messenger.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import template.messenger.R;
import template.messenger.adapter.CallListAdapter;
import template.messenger.data.Constant;
import template.messenger.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class PageCallFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    private List<Friend> items = new ArrayList<>();
    private CallListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_messenger_page_fragment_call, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        items = Constant.getFriendsData(getActivity());

        // specify an adapter (see also next example)
        mAdapter = new CallListAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

}
