package com.rikkeisoft.rikkonbi.UI.Cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCart.PresenterCart;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetForCart;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.History.History;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartScreen extends AppCompatActivity implements MainActivityPresenter.View.CartView,View.OnClickListener,
        MainActivityPresenter.View.ConnectedWifi{
    private final String TAG = "CartScreen";
    private PresenterCart mUserPresenterCompl;
    Fragment mCartList;
    Fragment mCartEmpty;
    FragmentManager mFm;
    FragmentTransaction mFragmentTransaction;
    private static final String PAGE_ID = "Page_Key";
    TextView mScreenCart;
    private PresenterCheckWiFi mCheckWiFi;
    private String mKeyPage;
    private static final String PAGE1_ID = "Page_Key1";
    private CartSqlite helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);
        getSupportActionBar().hide();

        mKeyPage = getIntent().getExtras().getString(PAGE_ID);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        helper = new CartSqlite(this);
        ImageButton btn_close = (ImageButton) findViewById(R.id.close_page);
        btn_close.setOnClickListener(this);

        mScreenCart = (TextView) findViewById(R.id.text_title_cartscreen);

        mCartList =  new CartListItem();
        mCartEmpty = new CartEmpty();

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();


        String account = PreferencesProvider.getInstance(getApplicationContext()).getCommitLogin(getApplicationContext());

        mUserPresenterCompl = new PresenterCart(this);
        updateUI();
    }



    public void updateUI(){
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(this).getTokenBearer(this));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            setDataToRecycleOrder(helper.getData());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setDataToRecycleOrder(List<String> carts) {
        if (helper.getProfilesCount() > 0) {
            mCartList =  new CartListItem();
            mFm = getSupportFragmentManager();
            mFragmentTransaction = mFm.beginTransaction();
            mFragmentTransaction.replace(R.id.frameLayout_cart,mCartList);
            mFragmentTransaction.commitAllowingStateLoss();
            mScreenCart.setText(getResources().getString(R.string.title_cart_screen)+" ("+helper.getProfilesCount()+")");
        }
        else{
            mCartEmpty = new CartEmpty();
            mFm = getSupportFragmentManager();
            mFragmentTransaction = mFm.beginTransaction();
            mFragmentTransaction.replace(R.id.frameLayout_cart,mCartEmpty);
            mFragmentTransaction.commitAllowingStateLoss();
            mScreenCart.setText(getResources().getString(R.string.title_cart_empty));
        }

    }

    public void ChangeText(){
        mScreenCart.setText(getResources().getString(R.string.title_cart_empty));
    }


    @Override
    public void OnClickItem(int position) {

    }

    @Override
    public void showMessage(String message) {
        mScreenCart.setText(getResources().getString(R.string.title_cart_screen)+" ("+message+")");
    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void checkQuantity(String cartQuantity) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_page:
                StartActivity();break;
        }
    }

    public void StartActivity(){
        if(!mKeyPage.equals("History")) {
            Intent intent = new Intent(CartScreen.this, MainPage.class);
            intent.putExtra(PAGE_ID, mKeyPage);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(CartScreen.this, History.class);
            intent.putExtra(PAGE_ID, mKeyPage);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(mCheckWiFi);
    }

    @Override
    public void onBackPressed() {
        StartActivity();
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
          Intent intent = new Intent(CartScreen.this,CheckInternetForCart.class);
          intent.putExtra(PAGE_ID,mKeyPage);
          intent.putExtra(PAGE1_ID,mKeyPage);
          startActivity(intent);
          finish();
        }
        else{
        }

    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }

}

