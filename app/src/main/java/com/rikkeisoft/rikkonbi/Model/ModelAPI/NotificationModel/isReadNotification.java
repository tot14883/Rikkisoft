package com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class isReadNotification {

    @SerializedName("notificationId")
    @Expose
    private int notificationId;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

}
