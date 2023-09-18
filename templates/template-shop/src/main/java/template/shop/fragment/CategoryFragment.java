package template.shop.fragment;

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

import template.shop.ActivityItemDetails;
import template.shop.ActivityMain;
import template.shop.R;
import template.shop.adapter.ItemGridAdapter;
import template.shop.data.Constant;
import template.shop.data.Tools;
import template.shop.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    public static String TAG_CATEGORY = "template.shop.tagCategory";

    private View view;
    private RecyclerView recyclerView;
    private ItemGridAdapter mAdapter;
    private LinearLayout lyt_notfound;
    private String category = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_shop_fragment_category, null);
        category = getArguments().getString(TAG_CATEGORY);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lyt_notfound = (LinearLayout) view.findViewById(R.id.lyt_notfound);

        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), Tools.getGridSpanCount(getActivity()));
        recyclerView.setLayoutManager(mLayoutManager);

        //set data and list adapter
        List<ItemModel> items = new ArrayList<>();
        if(category.equals(getString(R.string.app_shop_menu_clothing))){
            items = Constant.getItemClothes(getActivity());
        }else if(category.equals(getString(R.string.app_shop_menu_shoes))){
            items = Constant.getItemShoes(getActivity());
        }else if(category.equals(getString(R.string.app_shop_menu_watches))){
            items = Constant.getItemWatches(getActivity());
        }else if(category.equals(getString(R.string.app_shop_menu_accessories))){
            items = Constant.getItemAccessories(getActivity());
        }else if(category.equals(getString(R.string.app_shop_menu_bags))){
            items = Constant.getItemBags(getActivity());
        }else if(category.equals(getString(R.string.app_shop_menu_new))){
            items = Constant.getItemNew(getActivity());
        }
        mAdapter = new ItemGridAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ItemGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, ItemModel obj, int position) {
                ActivityItemDetails.navigate((ActivityMain)getActivity(), v.findViewById(R.id.lyt_parent), obj);
            }
        });

        if(mAdapter.getItemCount()==0){
            lyt_notfound.setVisibility(View.VISIBLE);
        }else{
            lyt_notfound.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
