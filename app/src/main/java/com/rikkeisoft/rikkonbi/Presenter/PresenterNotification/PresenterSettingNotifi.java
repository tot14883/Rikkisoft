package com.rikkeisoft.rikkonbi.Presenter.PresenterNotification;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncNotificationModel.SettingNotiModelAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

public class PresenterSettingNotifi implements
        MainActivityPresenter.Presenter.SettingNotifiBackground,
        MainActivityPresenter.Model.settingNotification.OnSuccessPostNotification{

    private static final String TAG = "PresenterSettingNotifi";
    private MainActivityPresenter.View.NotificationSettingView mView;
    private MainActivityPresenter.Model.settingNotification mSetModel;

    public PresenterSettingNotifi(MainActivityPresenter.View.NotificationSettingView mView){
        this.mView = mView;
        mSetModel = new SettingNotiModelAPI();
    }



    @Override
    public void OnDelSuccessNotifi(String status) {
        mView.Progress("del_success");


    }

    @Override
    public void OnReadSuccessNotifi(String status) {
        mView.Progress("read_success");
    }

    @Override
    public void OnFailPostNotification(String status) {

    }

    @Override
    public void NotificationDel(String tokenUser, int id) {
        mSetModel.postDelNotification(this,tokenUser,id);
    }

    @Override
    public void NotificationRead(String tokenUser, int id) {
        mSetModel.postReadNotification(this,tokenUser,id);
    }
}
