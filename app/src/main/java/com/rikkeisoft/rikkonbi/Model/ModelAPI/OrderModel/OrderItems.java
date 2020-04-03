package com.rikkeisoft.rikkonbi.Model.ModelAPI.OrderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItems {

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

}
