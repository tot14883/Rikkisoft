package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncNotificationModel;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.UI.Notification.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationModelAPI implements MainActivityPresenter.Model.getNotification {

    private APIInterface mApiInterface;
    private List<Notification> mNotification;
    //private List<String> mNotification;
    private final String TAG = "NotificationModelAPI";
    @Override
    public void getNotificationInfo(final OnSuccessNotification onSuccessNotification, String tokenUser, int pageNum) {

          mApiInterface = ApiUtils.getTokenAccess();
          new getNotification(onSuccessNotification,tokenUser,pageNum);

    }

    public class getNotification extends AsyncTask<String,String ,String > {
        private String tokenUser;
        private int pageNum;
        private OnSuccessNotification onSuccessNotification;
        public getNotification(OnSuccessNotification onSuccessNotification, String tokenUser, int pageNum){
            this.onSuccessNotification = onSuccessNotification;
            this.tokenUser = tokenUser;
            this.pageNum = pageNum;
        }
        @Override
        protected String doInBackground(String... strings) {
            mNotification = new ArrayList<>();
            mNotification.clear();

            NotificationFragment notificationFragment = new NotificationFragment();

            final int pageNumber = pageNum;

            Call<List<Notification>> call = mApiInterface.getNotification("application/json", tokenUser, pageNum);

            call.enqueue(new Callback<List<Notification>>() {
                @Override
                public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                    if(response.isSuccessful()){
                        mNotification = response.body();
                        if(mNotification.size() >= 1) {
                            Gson gson =new Gson();
                            List<String> responseData = new ArrayList<String>();
                            responseData.clear();
                            for(int i = 0;i < mNotification.size();i++){
                                responseData.add(gson.toJson(mNotification.get(i)));
                            }
                            Log.d(TAG,responseData.toString());
                            onSuccessNotification.OnShowNotification(responseData,pageNum);
                        }
                    }
                    else{
                        if(pageNumber != 1){
                            onSuccessNotification.OnRefreshData(pageNumber-1);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Notification>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
            return call.toString();
        }
    }
}
