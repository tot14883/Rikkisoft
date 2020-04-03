package com.rikkeisoft.rikkonbi.Presenter.PresenterMain;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncContact.ContactModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PresenterMain  implements MainActivityPresenter.Presenter.MainBackground{

    private GoogleSignInAccount account;
    private final String TAG = "PresenterMain";
    private MainActivityPresenter.View.MainView mView;
    public PresenterMain (MainActivityPresenter.View.MainView view){
        this.mView = view;

    }

    @Override
    public void UserBackground(String account) {
       // Log.d(TAG,account);
    }



}
