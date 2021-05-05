package com.example.healthmyself.CustomClass;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PostCalendar {

    public String ex;
    public String time;
    public String video;

    public PostCalendar(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public PostCalendar(String ex, String time, String video) {
        this.ex = ex;
        this.time = time;
        this.video = video;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ex", ex);
        result.put("time", time);
        result.put("video", video);
        return result;
    }
}