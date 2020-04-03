package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncHomeModel;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.AssetImageModel.PiSignage;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeModelAPI implements MainActivityPresenter.Model.InfoAsset {
    private APIInterface mApiInterface;
    private final String TAG = "HomeModelAPI";
    private List<PiSignage> mPiSignagesArrayList;

    @Override
    public void getInfoAsset(final OnSuccessDataAsset onSuccessData, String tokenUser) {

        mApiInterface = ApiUtils.getTokenAccess();
        new getAsset(onSuccessData,tokenUser).execute();
    }

    public class getAsset extends AsyncTask<String,String ,String > {
        private String tokenUser;

        private OnSuccessDataAsset onSuccessData;
        public getAsset(OnSuccessDataAsset onSuccessData, String tokenUser){
            this.onSuccessData = onSuccessData;
            this.tokenUser = tokenUser;
        }
        @Override
        protected String doInBackground(String... strings) {
            mPiSignagesArrayList = new ArrayList<>();
            Call<List<PiSignage>> call = mApiInterface.getAssetPiSignage("application/json", tokenUser);
            call.enqueue(new Callback<List<PiSignage>>() {
                @Override
                public void onResponse(Call<List<PiSignage>> call, Response<List<PiSignage>> response) {
                    if(response.isSuccessful()){
                        mPiSignagesArrayList = response.body();
                        onSuccessData.onSuccessAsset(mPiSignagesArrayList);
                    }
                }

                @Override
                public void onFailure(Call<List<PiSignage>> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });
            return call.toString();
        }
    }
}
