package template.chat.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import template.chat.ActivityGroupDetails;
import template.chat.ActivityMain;
import template.chat.R;
import template.chat.adapter.GroupsGridAdapter;
import template.chat.data.Constant;
import template.chat.data.Tools;
import template.chat.model.Group;

public class GroupsFragment extends Fragment {

    RecyclerView recyclerView;
    public GroupsGridAdapter mAdapter;
    private ProgressBar progressBar;
    private View view;
    private LinearLayout lyt_not_found;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.app_chat_fragment_groups, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar  = (ProgressBar) view.findViewById(R.id.progressBar);
        lyt_not_found   = (LinearLayout) view.findViewById(R.id.lyt_not_found);

        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (Constant.getGroupData(getActivity()).size() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        }else{
            lyt_not_found.setVisibility(View.GONE);
        }

        // specify an adapter (see also next example)
        mAdapter = new GroupsGridAdapter( getActivity(), Constant.getGroupData(getActivity()));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new GroupsGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Group obj, int position) {
                ActivityGroupDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.lyt_parent), obj);
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
