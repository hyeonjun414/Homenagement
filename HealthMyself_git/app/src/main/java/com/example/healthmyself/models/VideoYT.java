package com.example.healthmyself.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoYT {

    @SerializedName("id")
    @Expose
    private com.example.healthmyself.models.VideoID id;

    @SerializedName("snippet")
    @Expose
    private SnippetYT snippet;

    public VideoYT() {
    }

    public VideoYT(com.example.healthmyself.models.VideoID id, SnippetYT snippet) {
        this.id = id;
        this.snippet = snippet;
    }

    public com.example.healthmyself.models.VideoID getId() {
        return id;
    }

    public void setId(com.example.healthmyself.models.VideoID id) {
        this.id = id;
    }

    public SnippetYT getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetYT snippet) {
        this.snippet = snippet;
    }
}
