package com.rikkeisoft.rikkonbi.PreferencesProvider;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;


public class PreferencesProvider {
    private static String PREF_LOGIN =  "LoginStats";
    private static String PREF_CART = "MYcart";
    private static String PREF_CART_DUP = "DUPLICATE";
    private static String PREF_FIREBASE = "FB";
    private static String PREF_USER_ID = "USER_ID";
    private static String PREF_PROFILE_USER = "PROFILE_USER";
    private static String PREF_CONTACT = "CONTACT";
    private static String PREF_TOKEN_USER = "USER_TOKEN";


    private static SharedPreferences sPrefer_UserID;
    private static SharedPreferences sPrefer_Cart_Dup;
    private static SharedPreferences sPreferences_Login;
    private static SharedPreferences sPreferences_Cart;
    private static SharedPreferences sPreferences_Product;
    private static SharedPreferences sPreferences_FB;
    private static SharedPreferences sPreferences_Profile;
    private static SharedPreferences sPreferences_Token;

    private static PreferencesProvider sInstanceToken;
    private static PreferencesProvider sInstanceID;
    private static PreferencesProvider sInstanceDup;
    private static PreferencesProvider sInstance;
    private static PreferencesProvider sIntanceCart;
    private static PreferencesProvider sIntanceProduct;
    private static PreferencesProvider sIntanceFB;
    private static PreferencesProvider sIntanceProfile;

    private List<String> mOldValue = new ArrayList<>();
    private List<String> mCartDup = new ArrayList<>();


    public static SharedPreferences providePreferencesToken(){ return sPreferences_Token; }

    public static SharedPreferences providePreferences() {
        return sPreferences_Login;
    }

    public static SharedPreferences providePreferencesCart() {
        return sPreferences_Cart;
    }

    public static SharedPreferences providePreferencesDup(){
        return sPrefer_Cart_Dup;
    }

    public static SharedPreferences providePreferencesProduct(){ return sPreferences_Product; }

    public static SharedPreferences providerPreferencesFB(){return  sPreferences_FB; }

    public static SharedPreferences providerPreferencesID(){return sPrefer_UserID;}

    public static SharedPreferences providerPreferencesProfile(){return  sPreferences_Profile;}

    public PreferencesProvider(Context context) {
        sPreferences_Login = context.getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);
        sPreferences_Cart = context.getSharedPreferences(PREF_CART , Context.MODE_PRIVATE);
        sPrefer_Cart_Dup = context.getSharedPreferences(PREF_CART_DUP,Context.MODE_PRIVATE);
        sPreferences_FB = context.getSharedPreferences(PREF_FIREBASE,Context.MODE_PRIVATE);
        sPrefer_UserID = context.getSharedPreferences(PREF_USER_ID,Context.MODE_PRIVATE);
        sPreferences_Profile = context.getSharedPreferences(PREF_PROFILE_USER,Context.MODE_PRIVATE);
        sPreferences_Token = context.getSharedPreferences(PREF_TOKEN_USER,Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesProvider getsInstanceToken(Context context){
        if(sInstanceToken == null){
            sInstanceToken = new PreferencesProvider(context);
        }
        return sInstanceToken;
    }

    public static synchronized PreferencesProvider getsIntanceProfile(Context context){
        if(sIntanceProfile == null){
            sIntanceProfile = new PreferencesProvider(context);
        }
        return sIntanceProfile;
    }

    public static synchronized PreferencesProvider getInstanceFB(Context context){
        if(sIntanceFB == null){
            sIntanceFB = new PreferencesProvider(context);
        }
        return sIntanceFB;
    }
    public static synchronized PreferencesProvider getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesProvider(context);
        }
        return sInstance;
    }

    public static synchronized PreferencesProvider getInstanceID(Context context){
        if(sInstanceID == null){
            sInstanceID = new PreferencesProvider(context);
        }
        return sInstanceID;
    }

    public static synchronized PreferencesProvider getInstanceProduct(Context context,String KeyPref){
        sPreferences_Product = context.getSharedPreferences(KeyPref,Context.MODE_PRIVATE);
        if(sIntanceProduct == null){
            sIntanceProduct = new PreferencesProvider(context);
        }
        return sIntanceProduct;
    }

    public static synchronized PreferencesProvider getIntanceCart(Context context) {
        if (sIntanceCart == null) {
            sIntanceCart = new PreferencesProvider(context);
        }
        return sIntanceCart;
    }

    public static synchronized PreferencesProvider getInstanceDup(Context context){
        if(sInstanceDup == null){
            sInstanceDup = new PreferencesProvider(context);
        }
        return sInstanceDup;
    }

    public void setUserId(String key){
        SharedPreferences.Editor editor = sPrefer_UserID.edit();
        editor.putString(PREF_USER_ID, key);
        editor.apply();
    }
    public void setPrefKey(String key){
        SharedPreferences.Editor editor = sPreferences_FB.edit();
        editor.putString(PREF_FIREBASE, key);
        editor.apply();
    }
    public void setCommitLogin(String value) {
        SharedPreferences.Editor editor = sPreferences_Login.edit();
        editor.putString(PREF_LOGIN, value);
        editor.apply();
    }

    public void setTokenBearer(String value){
        SharedPreferences.Editor editor = sPreferences_Token.edit();
        editor.putString(PREF_TOKEN_USER,value);
        editor.apply();
    }
    public void setProfile(String value){
        SharedPreferences.Editor editor = sPreferences_Profile.edit();
        editor.putString(PREF_PROFILE_USER, value);
        editor.apply();
    }

    public String getCommitLogin(Context context) {
        return sPreferences_Login.getString(PREF_LOGIN, "");
    }

    public boolean saveMyCart(String array, String arrayName, Context mContex) {
        SharedPreferences.Editor editor = sPreferences_Cart.edit();
        String checkExist = sPreferences_Cart.getString(PREF_CART , null);
        if (checkExist != null) {
            mOldValue.add(String.valueOf(checkExist) + "@@" + array);
            editor.putString(PREF_CART , mOldValue.get(0));
            mOldValue = new ArrayList<>();
            mOldValue.clear();
            editor.apply();
        } else {
            editor.putString(PREF_CART , array);
            editor.apply();
        }

        return true;
    }

    public boolean addDuplicate(String array, Context mContex) {
        SharedPreferences.Editor editor = sPrefer_Cart_Dup.edit();
        String checkExist = sPrefer_Cart_Dup.getString(PREF_CART_DUP, null);
        if (checkExist != null) {
            mCartDup.add(String.valueOf(checkExist) + "@@" + array);
            editor.putString(PREF_CART_DUP, mCartDup.get(0));
            mCartDup = new ArrayList<>();
            mCartDup.clear();
            editor.apply();
        } else {
            editor.putString(PREF_CART_DUP, array);
            editor.apply();
        }
        return true;
    }

    //อย่าลืมแก้กลับ
    public void DeleteMyCart(String array, int arrayName, int len) {
        mCartDup = new ArrayList<>();
        mCartDup.clear();
        SharedPreferences.Editor editor = sPrefer_Cart_Dup.edit();
        String checkExist = sPrefer_Cart_Dup.getString(PREF_CART_DUP, null);
        if (arrayName > 1) {
            mCartDup.add(checkExist + "@@" + array);
            editor.putString(PREF_CART_DUP, mCartDup.get(0));
            editor.apply();
        } else if (array.equals("delete") && arrayName == 0 && len == 1) {
            sPrefer_Cart_Dup.edit().clear().apply();
        } else {
            sPrefer_Cart_Dup.edit().clear().apply();
            editor.putString(PREF_CART_DUP, array);
            editor.apply();
        }
    }

    public void DeleteOldMyCart(String array, int arrayName, int len) {
        mOldValue = new ArrayList<>();
        mOldValue.clear();
        SharedPreferences.Editor editor = sPreferences_Cart.edit();
        String checkExist = sPreferences_Cart.getString(PREF_CART , null);
        if (arrayName > 1) {
            mOldValue.add(checkExist + "@@" + array);
            editor.putString(PREF_CART , mOldValue.get(0));
            editor.apply();
        } else if (array.equals("delete") && arrayName == 0 && len == 1) {
            sPreferences_Cart.edit().clear().apply();
        } else {
            sPreferences_Cart.edit().clear().apply();
            editor.putString(PREF_CART , array);
            editor.apply();
        }
    }

    public String getMyCart(Context context) {
        return String.valueOf(sPreferences_Cart.getString(PREF_CART , null));
    }

    public String getCartDup(Context context) {
        return String.valueOf(sPrefer_Cart_Dup.getString(PREF_CART_DUP, null));
    }

    public void addProductNum(Context context,String num,String KeyPref){
        SharedPreferences.Editor editor = sPreferences_Product.edit();
        editor.putString(KeyPref , num);
        editor.apply();

    }



    public String getProductNum(Context context,String KeyPref){
        return String.valueOf(sPreferences_Product.getString(KeyPref,null));
    }

    public String getFirebase(Context context){
        return String.valueOf(sPreferences_FB.getString(PREF_FIREBASE,null));
    }

    public String getUserID(Context context){
        return String.valueOf(sPrefer_UserID.getString(PREF_USER_ID,null));
    }

    public String getProfile(Context context){
        return String.valueOf(sPreferences_Profile.getString(PREF_PROFILE_USER,null));
    }

    public String getTokenBearer(Context context){
        return String.valueOf(sPreferences_Token.getString(PREF_TOKEN_USER,null));
    }

}
