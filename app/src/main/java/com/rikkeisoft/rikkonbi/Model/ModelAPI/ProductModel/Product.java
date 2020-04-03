package com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    @Expose
    private int mId;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("imageUrl")
    @Expose
    private String mImageUrl;

    @SerializedName("price")
    @Expose
    private int mPrice;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("categoryId")
    @Expose
    private int mCategoryId;

    @SerializedName("qrCodeImageUrl")
    @Expose
    private String mQrCodeImageUrl;

    @SerializedName("maxOrderQuantity")
    @Expose
    private int mMaxOrderQuantity;

    private int quantity;

    public Product(int id, String name, String imageUrl, int price, String description, int categoryId, String qrCodeImageUrl, int maxOrderQuantity,int quantity) {
        this.mId = id;
        this.mName = name;
        this.mImageUrl = imageUrl;
        this.mPrice = price;
        this.quantity = quantity;
        this.mDescription = description;
        this.mCategoryId = categoryId;
        this.mQrCodeImageUrl = qrCodeImageUrl;
        this.mMaxOrderQuantity = maxOrderQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.mCategoryId = categoryId;
    }

    public String getQrCodeImageUrl() {
        return mQrCodeImageUrl;
    }

    public void setQrCodeImageUrl(String qrCodeImageUrl) {
        this.mQrCodeImageUrl = qrCodeImageUrl;
    }

    public int getMaxOrderQuantity() {
        return mMaxOrderQuantity;
    }

    public void setMaxOrderQuantity(int maxOrderQuantity) {
        this.mMaxOrderQuantity = maxOrderQuantity;
    }
}
