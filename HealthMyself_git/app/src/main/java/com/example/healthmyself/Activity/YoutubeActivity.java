package com.example.healthmyself.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmyself.Adapter.AdapterHome;
import com.example.healthmyself.R;
import com.example.healthmyself.models.ModelHome;
import com.example.healthmyself.models.VideoYT;
import com.example.healthmyself.network.YouTubeAPI;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class YoutubeActivity extends AppCompatActivity {

    private String keyword;
    private AdapterHome adapter;
    private LinearLayoutManager manager;
    private List<VideoYT> videoList = new ArrayList<>();
    private ShimmerFrameLayout loading1, loading2;
    private boolean isScroll = false;
    private int currentItem, totalItem, scrollOutItem;
    private String nextPageToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        loading1 = findViewById(R.id.shimmer1);
        loading2 = findViewById(R.id.shimmer2);
        RecyclerView rv = findViewById(R.id.recyclerView);
        adapter = new AdapterHome(getApplicationContext(), videoList);
        manager = new LinearLayoutManager(getApplicationContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = manager.getChildCount();
                totalItem = manager.getItemCount();
                scrollOutItem = manager.findFirstVisibleItemPosition();
                if( isScroll && (currentItem + scrollOutItem == totalItem)){
                    isScroll = false;
                    getJson(keyword);
                }
            }
        });

        if(videoList.size() == 0) {
            getJson(keyword);
        }

    }

    private void getJson(String keyword) {
        //loading1.setVisibility(View.VISIBLE);
        String url = YouTubeAPI.BASE_URL + YouTubeAPI.sch + YouTubeAPI.part + YouTubeAPI.query + keyword + YouTubeAPI.type
                + YouTubeAPI.KEY;
    //https://www.googleapis.com/youtube/v3/search?part=snippet&q=홈트&type=video&key=AIzaSyCSqIevjMQZerInWLAO6j70At4u-2NlEZ0
        if( nextPageToken != "" ){
            url = url + YouTubeAPI.NPT + nextPageToken;
            loading1.setVisibility(View.GONE);
            loading2.setVisibility(View.VISIBLE);
        }
        if (nextPageToken == null){
            return;
        }
        Call<ModelHome> data = YouTubeAPI.getHomeVideo().getYT(url);
        data.enqueue(new Callback<ModelHome>() {
            @Override
            public void onResponse(Call<ModelHome> call, Response<ModelHome> response) {
                if (response.errorBody() != null){
                    try {
                        Log.w(TAG, "onResponse: " + response.errorBody().string());
                        loading1.setVisibility(View.GONE);
                        loading2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    ModelHome mh = response.body();
                    videoList.addAll(mh.getItems());
                    adapter.notifyDataSetChanged();
                    loading1.setVisibility(View.GONE);
                    loading2.setVisibility(View.GONE);
                    if (mh.getNextPageToken() != null) {
                        nextPageToken = mh.getNextPageToken();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelHome> call, Throwable t) {
                Log.e(TAG, "onFailure : " + t);
                loading1.setVisibility(View.GONE);
                loading2.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}