package template.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.news.ActivityChannelDetails;
import template.news.ActivityMain;
import template.news.R;
import template.news.adapter.AdapterChannelList;
import template.news.data.Constant;
import template.news.model.Channel;

public class FragmentChannel extends Fragment{

    private View root_view;
    private RecyclerView recyclerView;
    private AdapterChannelList mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_news_fragment_channel, null);

        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new AdapterChannelList(getActivity(), Constant.getChannelData(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterChannelList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Channel obj, int position) {
                ActivityChannelDetails.navigate((ActivityMain)getActivity(), v, obj);
            }
        });
        return root_view;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
