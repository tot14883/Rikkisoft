package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncCategoryModel;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.ItemProduct;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryModelAPI implements MainActivityPresenter.Model.ProductItem {
    private APIInterface mApiInterface;
    private final String TAG = "CategoryListModel";

    @Override
    public void getInfoProduct(final OnSuccessDataItemProduct onSuccessData, String tokenUser) {
        mApiInterface = ApiUtils.getTokenAccess();
        new PostInfoProductModel(onSuccessData,tokenUser).execute();
    }

    public class PostInfoProductModel extends AsyncTask<String,String ,String > {
        private String tokenUser;
        private OnSuccessDataItemProduct onSuccessData;
        public PostInfoProductModel(OnSuccessDataItemProduct onSuccessData, String tokenUser){
            this.onSuccessData = onSuccessData;
            this.tokenUser = tokenUser;
        }
        @Override
        protected String doInBackground(String... strings) {
            Call<ItemProduct> call = mApiInterface.getProductServer("application/json", tokenUser );
            call.enqueue(new Callback<ItemProduct>() {
                @Override
                public void onResponse(Call<ItemProduct> call, Response<ItemProduct> response) {
                    if(response.isSuccessful()){
                        List<Product> products = response.body().getProduct();
                        onSuccessData.onSuccessProduct(products);
                    }
                }

                @Override
                public void onFailure(Call<ItemProduct> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });
            return call.toString();
        }
    }
}
