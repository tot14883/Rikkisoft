package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel;

import android.util.Log;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.LoginModel.LoginToken;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserModelAPI implements MainActivityPresenter.Model.InfoMember {
    private APIInterface mApiInterface;
    private final String TAG = "UserModelAPI";
    @Override
    public void getInfoMember(final OnSuccessDataMember onSuccessData, String tokenID) {
        LoginToken loginToken = new LoginToken();
        loginToken.setToken(tokenID);
        mApiInterface = ApiUtils.getTokenAccess();

        Call<User> call = mApiInterface.getToken2Server("application/json",loginToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    Gson gson = new Gson();
                    List<String> reponseData = new ArrayList<String>();
                    reponseData.add(gson.toJson(user));
                    //Log.d(TAG,reponseData.toString());
                    onSuccessData.onSuccessMember(reponseData);
                }
                else{
                    switch (response.code()) {
                        case 400:
                            onSuccessData.OnFailureMember("Error");
                            break;
                        default:
                            onSuccessData.OnFailureMember("Error");
                            break;
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG,t.getMessage());
                onSuccessData.OnFailureMember("Error");
            }
        });
    }
}
