package com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemProduct {

    @SerializedName("total")
    @Expose
    private int mTotal;

    @SerializedName("items")
    @Expose
    private List<Product> mProduct;

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        this.mTotal = total;
    }


    public ItemProduct(){

    }

    public ItemProduct(int total, List<Product> product) {
        this.mTotal = total;
        this.mProduct = product;
    }

    public List<Product> getProduct() {
        return mProduct;
    }

    public void setProduct(List<Product> product) {
        this.mProduct = product;
    }
}
