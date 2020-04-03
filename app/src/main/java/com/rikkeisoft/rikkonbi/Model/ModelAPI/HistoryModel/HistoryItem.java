package com.rikkeisoft.rikkonbi.Model.ModelAPI.HistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryItem {
    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("userId")
    @Expose
    private String mUserId;

    @SerializedName("fullName")
    @Expose
    private String mFullName;

    @SerializedName("email")
    @Expose
    private String mEmail;

    @SerializedName("regionId")
    @Expose
    private int mRegionId;

    @SerializedName("totalAmount")
    @Expose
    private int mTotalAmount;

    @SerializedName("orderDate")
    @Expose
    private String mOrderDate;

    @SerializedName("paymentStatusId")
    @Expose
    private int mPaymentStatusId;

    @SerializedName("paymentStatusName")
    @Expose
    private String mPaymentStatusName;

    @SerializedName("items")
    @Expose
    private List<HistoryItemDetail> mItems;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public int getRegionId() {
        return mRegionId;
    }

    public void setRegionId(int regionId) {
        this.mRegionId = regionId;
    }

    public int getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.mTotalAmount = totalAmount;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        this.mOrderDate = orderDate;
    }

    public int getPaymentStatusId() {
        return mPaymentStatusId;
    }

    public void setPaymentStatusId(int paymentStatusId) {
        this.mPaymentStatusId = paymentStatusId;
    }

    public String getPaymentStatusName() {
        return mPaymentStatusName;
    }

    public void setPaymentStatusName(String paymentStatusName) {
        this.mPaymentStatusName = paymentStatusName;
    }

    public HistoryItem(int id, String userId, String fullName, String email, int regionId, int totalAmount, String orderDate, int paymentStatusId, String paymentStatusName, List<HistoryItemDetail> items) {
        this.mId = id;
        this.mUserId = userId;
        this.mFullName = fullName;
        this.mEmail = email;
        this.mRegionId = regionId;
        this.mTotalAmount = totalAmount;
        this.mOrderDate = orderDate;
        this.mPaymentStatusId = paymentStatusId;
        this.mPaymentStatusName = paymentStatusName;
        this.mItems = items;
    }

    public HistoryItem(){}

    public List<HistoryItemDetail> getItems() {
        return mItems;
    }

    public void setItems(List<HistoryItemDetail> items) {
        this.mItems = items;
    }
}
