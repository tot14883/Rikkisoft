package com.rikkeisoft.rikkonbi.Presenter.PresenterAccount;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PresenterAccount implements MainActivityPresenter.Presenter.AccountBackground {
    private final String TAG = "PresenterAccount";
    private MainActivityPresenter.View.AccountView mView;
    public PresenterAccount (MainActivityPresenter.View.AccountView view){
        this.mView = view;
    }
}
