package com.rikkeisoft.rikkonbi.Model.ModelAPI.TokenModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenRekkei {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("devicesType")
    @Expose
    private String devicesType;

    public TokenRekkei(){}

    public TokenRekkei(String token, String devicesType) {
        this.token = token;
        this.devicesType = devicesType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevicesType() {
        return devicesType;
    }

    public void setDevicesType(String devicesType) {
        this.devicesType = devicesType;
    }
}
