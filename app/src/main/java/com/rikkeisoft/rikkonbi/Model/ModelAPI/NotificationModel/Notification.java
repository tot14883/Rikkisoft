package com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Notification {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    @SerializedName("isRead")
    @Expose
    private boolean isRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Notification(){

    }
    public Notification(int id, String title, String content, String description, String createdOn, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.createdOn = createdOn;
        this.isRead = isRead;
    }
}
