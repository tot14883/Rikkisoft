package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncCategoryModel;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailModelAPI implements MainActivityPresenter.Model.ProductDetail {
    private APIInterface mApiInterface;
    private final String TAG = "CategoryDetailListModel";

    @Override
    public void getInfoProductDetail(final OnSuccessDataProduct onSuccessData, String tokenUser, int id) {
        mApiInterface = ApiUtils.getTokenAccess();
        new PostInfoProduct(onSuccessData,tokenUser,id).execute();
    }

    public class PostInfoProduct extends AsyncTask<String,String ,String > {
        private int id;
        private String tokenUser;
        private OnSuccessDataProduct onSuccessData;
        public PostInfoProduct(OnSuccessDataProduct onSuccessData, String tokenUser
                , int id){
            this.onSuccessData = onSuccessData;
            this.tokenUser = tokenUser;
            this.id = id;

        }
        @Override
        protected String doInBackground(String... strings) {
            Call<Product> call = mApiInterface.getProductDetailServer("application/json",tokenUser,id);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if(response.isSuccessful()){
                        Product product = response.body();
                        Gson gson = new Gson();
                        List<String> reponseData = new ArrayList<String>();
                        reponseData.add(gson.toJson(product));
                        //Log.d(TAG,reponseData.toString());
                        onSuccessData.onSuccessProductDetail(reponseData);
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });
            return call.toString();
        }
    }

}
