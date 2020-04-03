package com.rikkeisoft.rikkonbi.Model.ModelAPI.HistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryItemDetail {

    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("productId")
    @Expose
    private int mProductId;

    @SerializedName("productName")
    @Expose
    private String mProductName;

    @SerializedName("price")
    @Expose
    private int mPrice;

    @SerializedName("imageUrl")
    @Expose
    private String mImageUrl;

    @SerializedName("quantity")
    @Expose
    private int mQuantity;

    @SerializedName("isDeleted")
    @Expose
    private boolean mIsDeleted;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        this.mProductId = productId;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        this.mProductName = productName;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

    public boolean isDeleted() {
        return mIsDeleted;
    }

    public void setDeleted(boolean deleted) {
        mIsDeleted = deleted;
    }

    public HistoryItemDetail(){}
    public HistoryItemDetail(int id, int productId, String productName, int price, String imageUrl, int quantity, boolean isDeleted) {
        this.mId = id;
        this.mProductId = productId;
        this.mProductName = productName;
        this.mPrice = price;
        this.mImageUrl = imageUrl;
        this.mQuantity = quantity;
        this.mIsDeleted = isDeleted;
    }
}
