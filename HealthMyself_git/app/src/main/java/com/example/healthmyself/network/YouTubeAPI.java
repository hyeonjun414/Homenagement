package com.example.healthmyself.network;

import com.example.healthmyself.models.ModelHome;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class YouTubeAPI {
    //https://www.googleapis.com/youtube/v3/
    // search?
    // part=snippet
    // &q=홈트
    // &type=video
    // &key=AIzaSyCSqIevjMQZerInWLAO6j70At4u-2NlEZ0s

    public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    public static final String sch = "search?";
    public static final String part = "part=snippet";
    public static final String query = "&q=";
    public static final String type = "&type=video";
    public static final String KEY = "&key=AIzaSyCSqIevjMQZerInWLAO6j70At4u-2NlEZ0";
    public static final String NPT = "&pageToken=";

    public interface HomeVideo {
        @GET
        Call<ModelHome> getYT(@Url String url);
    }

    public static HomeVideo homeVideo = null;

    public  static HomeVideo getHomeVideo() {
        if(homeVideo == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            homeVideo = retrofit.create(HomeVideo.class);
        }
        return homeVideo;
    }

}
