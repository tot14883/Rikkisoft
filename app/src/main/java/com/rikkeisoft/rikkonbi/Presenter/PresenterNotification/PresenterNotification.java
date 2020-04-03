package com.rikkeisoft.rikkonbi.Presenter.PresenterNotification;

import android.util.Log;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncNotificationModel.NotificationModelAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

public class PresenterNotification implements
                      MainActivityPresenter.Presenter.NotificationBackground,
                      MainActivityPresenter.Model.getNotification.OnSuccessNotification{

    private final String TAG = "PresenterNotification";
    private MainActivityPresenter.View.MessageNotificationView mView;
    private MainActivityPresenter.Model.getNotification mNotiModel;

    public PresenterNotification(MainActivityPresenter.View.MessageNotificationView mView){
        this.mView = mView;
        mNotiModel = new NotificationModelAPI();
    }

    @Override
    public void OnShowNotification(List<String> message,int pageIndex) {
         if(message.size() >= 1){
             mView.setDataNotification(message);
         }
    }
    @Override
    public void OnFailNotification(String status) {

    }

    //Not use this method
    @Override
    public void OnRefreshData(int pageIndex) {
        mView.RefreshRecycle(pageIndex);
    }


    @Override
    public void NotificationBackground(String tokenUser,int pageNum) {
        mNotiModel.getNotificationInfo(this,tokenUser,pageNum);
    }
}
