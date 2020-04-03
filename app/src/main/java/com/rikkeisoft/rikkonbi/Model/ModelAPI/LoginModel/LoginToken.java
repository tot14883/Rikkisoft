package com.rikkeisoft.rikkonbi.Model.ModelAPI.LoginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginToken {
    @SerializedName("token")
    @Expose
    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }
}
