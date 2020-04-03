package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncNotificationModel;

import android.util.Log;

import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.isReadNotification;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingNotiModelAPI implements MainActivityPresenter.Model.settingNotification {
    private APIInterface mApiInterface;
    private final String TAG = "NotificationModelAPI";
    @Override
    public void postDelNotification(final OnSuccessPostNotification onSuccessPostNotification, String token, int id) {
          mApiInterface = ApiUtils.getTokenAccess();
          Call<Void> call = mApiInterface.postDeleteNotification("application/json",token,id);

          call.enqueue(new Callback<Void>() {
              @Override
              public void onResponse(Call<Void> call, Response<Void> response) {
                  if(response.isSuccessful()){
                      onSuccessPostNotification.OnDelSuccessNotifi("del_success");
                  }
              }

              @Override
              public void onFailure(Call<Void> call, Throwable t) {
                  Log.e(TAG,t.getMessage());
                  onSuccessPostNotification.OnFailPostNotification("del_fail");
              }
          });
    }

    @Override
    public void postReadNotification(final OnSuccessPostNotification onSuccessPostNotification, String token, int id) {
        mApiInterface = ApiUtils.getTokenAccess();
        isReadNotification isReadNotification = new isReadNotification();
        isReadNotification.setNotificationId(id);
        Call<Void> call = mApiInterface.postReadNotification("application/json",token,isReadNotification);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    onSuccessPostNotification.OnReadSuccessNotifi("read_success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG,t.getMessage());
                onSuccessPostNotification.OnFailPostNotification("read_fail");
            }
        });
    }
}
