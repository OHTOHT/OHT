package template.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.news.ActivityMain;
import template.news.ActivityNewsDetails;
import template.news.R;
import template.news.adapter.AdapterNewsListWithHeader;
import template.news.data.Constant;
import template.news.data.GlobalVariable;
import template.news.model.News;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private View root_view;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private AdapterNewsListWithHeader mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_news_fragment_home, null);

        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // prepare tab layout
        initTabLayout();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                displayListNews(getNewsByCategory(tab.getText().toString()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return root_view;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void displayListNews(List<News> items){
        mAdapter = new AdapterNewsListWithHeader(getActivity(), items.get(items.size()-1), items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterNewsListWithHeader.OnItemClickListener() {
            @Override
            public void onItemClick(View v, News obj, int position) {
                ActivityNewsDetails.navigate((ActivityMain)getActivity(), v.findViewById(R.id.image), obj);
            }
        });
    }

    private void initTabLayout(){
        tabLayout = (TabLayout) root_view.findViewById(R.id.tabs);
        List<String> items_tab = Constant.getHomeCatgeory(getActivity());
        tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(0)), true);

        // display first news
        displayListNews(getNewsByCategory(items_tab.get(0)));

        for (int i=1; i< items_tab.size(); i++){
            tabLayout.addTab(tabLayout.newTab().setText(items_tab.get(i)));
        }
    }

    private List<News> getNewsByCategory(String category) {
        if(category.equalsIgnoreCase("LATEST")){
            return GlobalVariable.getNewsLatest();
        }else if(category.equalsIgnoreCase("TRENDING")){
            return GlobalVariable.getNewsTrending();
        }else if(category.equalsIgnoreCase("HIGHLIGHT")){
            return GlobalVariable.getNewsHighlight();
        }else if(category.equalsIgnoreCase("POPULAR")){
            return GlobalVariable.getNewsPopular();
        }else if(category.equalsIgnoreCase("MOST VIEW")){
            return GlobalVariable.getNewsMostview();
        }
        return new ArrayList<>();
    }


}
