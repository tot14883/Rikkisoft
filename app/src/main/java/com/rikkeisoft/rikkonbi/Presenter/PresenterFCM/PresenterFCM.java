package com.rikkeisoft.rikkonbi.Presenter.PresenterFCM;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncTokenRekkeiModel.TokenRekkeiAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

public class PresenterFCM implements MainActivityPresenter.Model.PostFCM.OnSuccessPostNotificationFCM,
 MainActivityPresenter.Presenter.NotificationFCM
 {

    private final static String TAG = "PresenterFCM";
    private MainActivityPresenter.Model.PostFCM mPostFCM;

    public PresenterFCM(){
        this.mPostFCM = new TokenRekkeiAPI();
    }
    @Override
    public void OnSuccessFCM(String status) {
    }

    @Override
    public void OnFailFCM(String status) {

    }

     @Override
     public void NotificationFCM(String tokenUser, String fcmToken) {
         mPostFCM.postNotificationFCM(this,tokenUser,fcmToken);

     }

     @Override
     public void DelNotificationFCM(String tokenUser, String fcmToken) {
         mPostFCM.delNotificationFCM(this,tokenUser,fcmToken);
     }
 }
