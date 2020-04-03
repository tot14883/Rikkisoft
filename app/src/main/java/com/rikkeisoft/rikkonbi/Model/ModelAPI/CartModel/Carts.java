package com.rikkeisoft.rikkonbi.Model.ModelAPI.CartModel;

import com.google.gson.annotations.SerializedName;

public class Carts {
   @SerializedName("productId")
   private int mProductId;

   @SerializedName("productName")
   private String mProductName;

   @SerializedName("price")
   private int mPrice;

   @SerializedName("imageUrl")
   private String mImageUrl;

   @SerializedName("quantity")
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

    public Carts(int productId, String productName, int price, String imageUrl, int quantity) {
        this.mProductId = productId;
        this.mProductName = productName;
        this.mPrice = price;
        this.mImageUrl = imageUrl;
        this.mQuantity = quantity;
    }
}
