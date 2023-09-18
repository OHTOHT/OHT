package template.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.news.ActivityMain;
import template.news.ActivityNewsDetails;
import template.news.R;
import template.news.adapter.AdapterNewsList;
import template.news.data.GlobalVariable;
import template.news.model.News;

public class FragmentSaved extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;
    private AdapterNewsList mAdapter;
    private LinearLayout lyt_notfound;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_news_fragment_saved, null);

        lyt_notfound = (LinearLayout) root_view.findViewById(R.id.lyt_notfound);
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mAdapter =  new AdapterNewsList(getActivity(), GlobalVariable.getSaved());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterNewsList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, News obj, int position) {
                ActivityNewsDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.image), obj);
            }
        });
        checkItems();
        return root_view;
    }

    private void checkItems(){
        if(mAdapter.getItemCount()<=0){
            lyt_notfound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            lyt_notfound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        mAdapter.setItems(GlobalVariable.getSaved());
        checkItems();
        super.onResume();
    }
}
