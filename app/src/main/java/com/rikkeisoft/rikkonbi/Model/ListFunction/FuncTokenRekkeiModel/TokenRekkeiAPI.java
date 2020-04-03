package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncTokenRekkeiModel;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.TokenModel.TokenRekkei;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenRekkeiAPI implements MainActivityPresenter.Model.PostFCM {
    private APIInterface mApiInterface;
    private final String TAG = "TokenRekkeiAPI";
    @Override
    public void postNotificationFCM(OnSuccessPostNotificationFCM onSuccessPostNotificationFCM,  String token,String fcmToken) {
        mApiInterface = ApiUtils.getTokenAccess();
        TokenRekkei tokenRekkei = new TokenRekkei();
        tokenRekkei.setToken(fcmToken);
        tokenRekkei.setDevicesType("Android");

        new RegisterTokenID(token,tokenRekkei,onSuccessPostNotificationFCM).execute();
    }

    public class RegisterTokenID extends AsyncTask<String,String ,String > {
        private String tokenID;
        private TokenRekkei tokenRekkei;
        private OnSuccessPostNotificationFCM onSuccessPostNotificationFCM;
        public RegisterTokenID(String tokenID, TokenRekkei tokenRekkei,OnSuccessPostNotificationFCM onSuccessPostNotificationFCM){
            this.tokenID = tokenID;
            this.tokenRekkei = tokenRekkei;
            this.onSuccessPostNotificationFCM = onSuccessPostNotificationFCM;

        }
        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG,tokenRekkei.toString());

            Call<Void> call = mApiInterface.postTokenRekkei("application/json",tokenID,tokenRekkei);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    onSuccessPostNotificationFCM.OnSuccessFCM("Success");

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });
            //mPresenterFCM.NotificationFCM(mTokenUser,token);


            return null;
        }
    }


    @Override
    public void delNotificationFCM(OnSuccessPostNotificationFCM onSuccessPostNotificationFCM, String token, String fcmToken) {
        mApiInterface = ApiUtils.getTokenAccess();

        new DeleteTokenID(token,fcmToken,onSuccessPostNotificationFCM).execute();
    }

    public class DeleteTokenID extends AsyncTask<String,String ,String > {
        private String tokenID;
        private String tokenRekkei;
        private OnSuccessPostNotificationFCM onSuccessPostNotificationFCM;
        public DeleteTokenID(String tokenID, String tokenRekkei,OnSuccessPostNotificationFCM onSuccessPostNotificationFCM){
            this.tokenID = tokenID;
            this.tokenRekkei = tokenRekkei;
            this.onSuccessPostNotificationFCM = onSuccessPostNotificationFCM;

        }
        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG,tokenRekkei.toString());

            Call<Void> call = mApiInterface.delTokenRekkei("application/json", tokenID,tokenRekkei);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    onSuccessPostNotificationFCM.OnSuccessFCM("Del");

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });


            return null;
        }
    }
}
