package com.DevBox.Homenagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SnippetYT {

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("thumbnails")
    @Expose
    private ThumbnailYT thumbnails;

    public SnippetYT() {
    }

    public SnippetYT(String publishedAt, String title, String description, ThumbnailYT thumbnails) {
        this.publishedAt = publishedAt;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
    }

    public String getPublishedAt() {
        return parsingUpdate(publishedAt);
    }
    public String parsingUpdate(String update)
    {
        String ab = publishedAt.substring(0, 11);
        StringBuilder str = new StringBuilder(ab);
        str.setCharAt(4, '년');
        str.setCharAt(7, '월');
        str.setCharAt(10, '일');
        str.insert(5, ' ');
        str.insert(9, ' ');



        return str.toString();
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ThumbnailYT getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ThumbnailYT thumbnails) {
        this.thumbnails = thumbnails;
    }
}
