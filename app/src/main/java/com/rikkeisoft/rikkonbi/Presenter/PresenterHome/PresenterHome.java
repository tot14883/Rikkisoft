package com.rikkeisoft.rikkonbi.Presenter.PresenterHome;

import android.util.Log;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncHomeModel.HomeModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncWalletsModel.WalletsAPI;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.AssetImageModel.PiSignage;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PresenterHome implements MainActivityPresenter.Presenter.HomeBackground
        ,MainActivityPresenter.Model.InfoAsset.OnSuccessDataAsset,
        MainActivityPresenter.Model.MyWallets.OnSuccessWallets {


    private final String TAG = "PresenterHome";
    private MainActivityPresenter.View.HomeView mView;

    private MainActivityPresenter.Model.InfoAsset mAssetModel;
    private MainActivityPresenter.Model.MyWallets mWallets;

    public PresenterHome (MainActivityPresenter.View.HomeView view){
        this.mView = view;
        mAssetModel = new HomeModelAPI();
        mWallets = new WalletsAPI();
    }

    @Override
    public void onSuccessAsset(List<PiSignage> assetArrayList) {
        mView.setDataToImageSlide(assetArrayList);
    }


    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void HomeBackground(String tokenUser) {
        mAssetModel.getInfoAsset(this,tokenUser);
        mWallets.getInfoMyWallets(this,tokenUser);
    }



    @Override
    public void OnShowMyWallets(String money) {
        mView.ShowView(money);
    }

    @Override
    public void OnFailShowMyWallets(String status) {
        mView.ShowView(status);
    }
}
