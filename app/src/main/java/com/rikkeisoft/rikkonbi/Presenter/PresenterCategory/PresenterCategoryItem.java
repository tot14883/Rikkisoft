package com.rikkeisoft.rikkonbi.Presenter.PresenterCategory;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncCategoryModel.CategoryModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PresenterCategoryItem implements MainActivityPresenter.Presenter.CategoryBackground,
        MainActivityPresenter.Model.ProductItem.OnSuccessDataItemProduct
{
    private final String TAG = "PresenterCategoryItem";
    private MainActivityPresenter.View.CategoryView mView;
    private MainActivityPresenter.Model.ProductItem mModel;

    public PresenterCategoryItem (MainActivityPresenter.View.CategoryView view){
        this.mView = view;
        mModel = new CategoryModelAPI();
    }

    @Override
    public void onSuccessProduct(List<Product> productArrayList) {
        mView.setDataToRecycleView(productArrayList);
    }



    @Override
    public void onFailure(Throwable t) {

    }
    @Override
    public void ProductBackground(String tokenUser) {
        mModel.getInfoProduct(this,tokenUser);

    }
}
