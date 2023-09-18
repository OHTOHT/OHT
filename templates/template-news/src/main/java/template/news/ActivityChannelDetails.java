package template.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.news.adapter.AdapterNewsListWithHeader;
import template.news.data.Constant;
import template.news.data.GlobalVariable;
import template.news.data.Tools;
import template.news.model.Channel;
import template.news.model.News;

import java.util.ArrayList;
import java.util.List;

public class ActivityChannelDetails extends AppCompatActivity {
    public static final String EXTRA_OBJCT = "template.news.CHANNEL";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Channel obj) {
        Intent intent = new Intent(activity, ActivityChannelDetails.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Toolbar toolbar;
    private ActionBar actionBar;
    private AdapterNewsListWithHeader mAdapter;
    private Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_news_activity_channel_details);

        // animation transition
        ViewCompat.setTransitionName(findViewById(android.R.id.content), EXTRA_OBJCT);

        // get extra object
        channel = (Channel) getIntent().getSerializableExtra(EXTRA_OBJCT);

        initToolbar();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String chnnl = channel.getName();
        List<Channel> channels = Constant.getChannelData(this);
        List<News>  list_news  = new ArrayList<>();

        if(chnnl.equalsIgnoreCase(channels.get(0).getName())){       // politics
            list_news =  GlobalVariable.getNewsPolitics();

        }else if(chnnl.equalsIgnoreCase(channels.get(1).getName())){ // Entertainment
            list_news =  GlobalVariable.getNewsEntertainment();

        }else if(chnnl.equalsIgnoreCase(channels.get(2).getName())){ // Science
            list_news =  GlobalVariable.getNewsScience();

        }else if(chnnl.equalsIgnoreCase(channels.get(3).getName())){ // Sport
            list_news =  GlobalVariable.getNewsSport();

        }else if(chnnl.equalsIgnoreCase(channels.get(4).getName())){ // Business
            list_news =  GlobalVariable.getNewsBusiness();

        }else if(chnnl.equalsIgnoreCase(channels.get(5).getName())){ // Technology
            list_news =  GlobalVariable.getNewsTechnology();
        }

        //set data and list adapter
        mAdapter = new AdapterNewsListWithHeader(this, list_news.get(list_news.size()-1), list_news);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdapterNewsListWithHeader.OnItemClickListener() {
            @Override
            public void onItemClick(View v, News obj, int position) {
                template.news.ActivityNewsDetails.navigate(ActivityChannelDetails.this, v.findViewById(R.id.image), obj);
            }
        });

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(channel.getName());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
