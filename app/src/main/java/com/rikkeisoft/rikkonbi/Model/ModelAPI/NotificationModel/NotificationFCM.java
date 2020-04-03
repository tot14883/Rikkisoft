package com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationFCM {
    @SerializedName("title")
    @Expose
    private String title;


    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("toUser")
    @Expose
    private List<String> toUser;

    public NotificationFCM(){

    }

    public NotificationFCM(String title, String content, String description, List<String> toUser) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.toUser = toUser;
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

    public List<String> getToUser() {
        return toUser;
    }

    public void setToUser(List<String> toUser) {
        this.toUser = toUser;
    }
}
