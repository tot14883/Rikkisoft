package com.rikkeisoft.rikkonbi.Presenter.PresenterCart;

import android.content.Context;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncCategoryModel.CategoryDetailModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncPreferenceModel.PreferenceModel;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PresenterCart implements MainActivityPresenter.Presenter.CartBackground,
        MainActivityPresenter.Model.CartPre.OnSuccessDataCart,
        MainActivityPresenter.Model.ProductDetail.OnSuccessDataProduct{
    private final String TAG = "PresenterUser";
    private MainActivityPresenter.View.CartView mView;
    private MainActivityPresenter.Model.CartPre mCart;
    private MainActivityPresenter.Model.ProductDetail mProductModel;
    //Cart
    public PresenterCart(MainActivityPresenter.View.CartView view){
           this.mView = view;
           this.mCart = new PreferenceModel();
           mProductModel = new CategoryDetailModelAPI();

    }

    @Override
    public void ProductDetailBackground(String tokenUser, int Id) {
        mProductModel.getInfoProductDetail(this,tokenUser,Id);

    }


    @Override
    public void onSuccessProductDetail(List<String> productDetailList) {
        Gson gson = new Gson();
        Product product = gson.fromJson(productDetailList.get(0),Product.class);
       // mView.OrderQuantity("1",product.getMaxOrderQuantity());
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void PreferenceCart(String myCart, Context context) {
        mCart.PreferenceCartDuplicateModel(this,myCart,context);
    }

    @Override
    public void OnSuccessPreferenceCart(List<String> myCart, Context context) {
        PreferencesProvider.getIntanceCart(context).saveMyCart(myCart.get(0),"Add_Cart",context);
    }

    @Override
    public void OnSuccessPreferenceCartDuplicate(String myCart, Context context) {
        PreferencesProvider.getInstanceDup(context).addDuplicate(myCart,context);
    }


}
