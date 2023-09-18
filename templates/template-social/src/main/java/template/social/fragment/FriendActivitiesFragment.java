package template.social.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.social.ActivityFriendDetails;
import template.social.R;
import template.social.adapter.FeedListAdapter;
import template.social.model.Feed;
import template.social.model.Friend;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class FriendActivitiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FeedListAdapter mAdapter;
    private List<Feed> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_social_fragment_friend_activities, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);

        TypedArray feed_photo = getResources().obtainTypedArray(R.array.app_social_feed_photos);
        // define feed wrapper
        Friend friend = ActivityFriendDetails.friend;
        items.add(new Feed(0, "14:56", friend, getString(R.string.app_social_middle_lorem_ipsum)));
        items.add(new Feed(1, "11:30", friend, feed_photo.getResourceId(0, -1)));
        items.add(new Feed(2, "09:10", friend, getString(R.string.app_social_lorem_ipsum)));
        items.add(new Feed(3, "Yesterday", friend, getString(R.string.app_social_short_lorem_ipsum), feed_photo.getResourceId(2, -1)));
        items.add(new Feed(4, "05 Nov 2015", friend, getString(R.string.app_social_long_lorem_ipsum)));

        mAdapter = new FeedListAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}
