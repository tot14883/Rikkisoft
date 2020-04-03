package com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private String mId;

    @SerializedName("userName")
    @Expose
    private String mUserName;

    @SerializedName("fullName")
    @Expose
    private String mFullName;

    @SerializedName("email")
    @Expose
    private String mEmail;

    @SerializedName("token")
    @Expose
    private String mToken;

    @SerializedName("avatar")
    @Expose
    private String mAvatar;

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    public User(String id, String userName, String fullName, String email, String token,String avatar) {
        this.mId = id;
        this.mUserName = userName;
        this.mFullName = fullName;
        this.mEmail = email;
        this.mToken = token;
        this.mAvatar = avatar;
    }

    public User(){

  }

}
