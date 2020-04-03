package com.rikkeisoft.rikkonbi.Presenter.PresenterCategory;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncCategoryModel.CategoryDetailModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncPreferenceModel.PreferenceModel;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PresenterCategoryDetail implements MainActivityPresenter.Presenter.DetailCategoryBackground ,
MainActivityPresenter.Model.ProductDetail.OnSuccessDataProduct
,MainActivityPresenter.Model.CartPre.OnSuccessDataCart{

    private final String TAG = "PresenterCategoryItem";
    private MainActivityPresenter.View.CategoryDetailView mView;
    private MainActivityPresenter.Model.ProductDetail mProductModel;
    private MainActivityPresenter.Model.CartPre mCart;
    public PresenterCategoryDetail (MainActivityPresenter.View.CategoryDetailView view){
        this.mView = view;
        mProductModel = new CategoryDetailModelAPI();
        mCart = new PreferenceModel();
    }
    @Override
    public void onSuccessProductDetail(List<String> productDetailList) {
        mView.showMessage(productDetailList.get(0));
        Gson gson = new Gson();
        Product product = gson.fromJson(productDetailList.get(0),Product.class);
        //mView.OrderQuantity("1",product.getMaxOrderQuantity());
    }


    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void PreferenceCart(List<String> myCart, Context context) {
        mCart.PreferenceCartModel(this,myCart,context);
    }


    @Override
    public void ProductDetailBackground(String tokenUser, int Id) {
        mProductModel.getInfoProductDetail(this,tokenUser,Id);

    }

    @Override
    public void OnSuccessPreferenceCart(List<String> myCart, Context context) {
        PreferencesProvider.getIntanceCart(context).saveMyCart(myCart.get(0),"Add_Cart",context);

    }

    @Override
    public void OnSuccessPreferenceCartDuplicate(String myCart, Context context) {

    }

}
