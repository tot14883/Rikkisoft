package com.rikkeisoft.rikkonbi.Model.ModelAPI.ContactModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("generalInfo")
    @Expose
    private String generalInfo;

    @SerializedName("webSite")
    @Expose
    private String webSite;

    @SerializedName("hotline")
    @Expose
    private String hotline;

    public Contact(){

    }

    public Contact(String generalInfo, String webSite,String hotline) {
        this.generalInfo = generalInfo;
        this.webSite = webSite;
        this.hotline = hotline;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(String generalInfo) {
        this.generalInfo = generalInfo;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}
