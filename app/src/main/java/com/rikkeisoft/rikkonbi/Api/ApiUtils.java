package com.rikkeisoft.rikkonbi.Api;

public class ApiUtils {
    private ApiUtils(){}

    public static String baseUri = "http://test.rikkonbi.com/";
    public static final String BASE_URL = baseUri +  "api/";


    public static APIInterface getTokenAccess(){
        return ApiClass.getClient(BASE_URL).create(APIInterface.class);
    }
}
