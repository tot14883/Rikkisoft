package com.rikkeisoft.rikkonbi.Presenter.PresenterLogin;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PresenterLogin implements MainActivityPresenter.Presenter.LoginBackGround
        ,MainActivityPresenter.Model.InfoMember.OnSuccessDataMember {

    private GoogleSignInAccount account;
    private final String TAG = "PresenterLogin";
    private MainActivityPresenter.View.LoginView mView;
    private MainActivityPresenter.Model.InfoMember mInfoModel;

    public PresenterLogin (MainActivityPresenter.View.LoginView view){
        this.mView = view;
        mInfoModel = new UserModelAPI();
    }
    @Override
    public void LoginBackground(GoogleSignInAccount account, Boolean check) {
        if(check.equals(true)) {
            account = GoogleSignIn.getLastSignedInAccount((Context) this.mView);
            this.account = account;
            mView.showProgress(account.getIdToken());
            // doingBackground(account.getIdToken());
        }
    }

    @Override
    public void UserBackground(String account) {
        mInfoModel.getInfoMember(this,account);
    }

    @Override
    public void onSuccessMember(List<String> userArrayList) {
        mView.UserAccount(userArrayList);
    }

    @Override
    public void OnFailureMember(String check) {

    }

    @Override
    public void onFailure(Throwable t) {

    }
}
