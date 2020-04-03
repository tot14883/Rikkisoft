package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncWalletsModel;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletsAPI implements MainActivityPresenter.Model.MyWallets {
    private APIInterface mApiInterface;
    private final String TAG = "WalletsAPI";
    @Override
    public void getInfoMyWallets(final OnSuccessWallets onSuccessWallets, String tokenUser) {
        mApiInterface = ApiUtils.getTokenAccess();
        new getWallets(onSuccessWallets,tokenUser).execute();

    }


    public class getWallets extends AsyncTask<String,String ,String > {
        private String tokenUser;
        private OnSuccessWallets onSuccessWallets;
        public getWallets(OnSuccessWallets onSuccessWallets, String tokenUser){
            this.onSuccessWallets = onSuccessWallets;
            this.tokenUser = tokenUser;
        }
        @Override
        protected String doInBackground(String... strings) {
            Call<String> call = mApiInterface.getMyOwnWallets("application/json",tokenUser);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        //Log.d(TAG,String.valueOf(response.body()));
                        onSuccessWallets.OnShowMyWallets(String.valueOf(response.body()));
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                    onSuccessWallets.OnFailShowMyWallets("Fail");

                }
            });
            return call.toString();
        }
    }
}
