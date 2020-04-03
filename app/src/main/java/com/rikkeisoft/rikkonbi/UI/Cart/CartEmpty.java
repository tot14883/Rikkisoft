package com.rikkeisoft.rikkonbi.UI.Cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCart.PresenterCart;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetForCart;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartEmpty extends Fragment implements MainActivityPresenter.View.CartView,View.OnClickListener
        ,MainActivityPresenter.View.ConnectedWifi {
    private final String TAG = "CartEmpty";
    private PresenterCart mUserPresenterCompl;
    private String mUserId;
    private String mTokenId;
    private static final String PAGE_ID = "Page_Key";
    private PresenterCheckWiFi mCheckWiFi;
    private boolean mIsCheckInternet;
    private CartSqlite cartSqlite;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_empty, container, false);

        String account = PreferencesProvider.getInstance(getContext()).getCommitLogin(getContext());

        ((CartScreen) getActivity()).ChangeText();


        mUserPresenterCompl = new PresenterCart(this);

        cartSqlite = new CartSqlite(getContext());

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();


        Button btn_c_b = (Button) view.findViewById(R.id.continue_buy);
        btn_c_b.setOnClickListener(this);

        updateUI();


        return view;
    }

    @Override
    public void setDataToRecycleOrder(List<String> carts) {
        if (cartSqlite.getProfilesCount() > 0) {
           StartActivity();
        }
    }
    public void updateUI(){
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(getContext()).getTokenBearer(getContext()));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");
            String id = jobject.getString("id");
            this.mUserId = id;
            this.mTokenId = token;



            setDataToRecycleOrder(cartSqlite.getData());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnClickItem(int position) {

    }


    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void checkQuantity(String cartQuantity) {

    }


    public void StartActivity(){
        Intent intent = new Intent(getActivity(), MainPage.class);
        intent.putExtra(PAGE_ID,"Category");
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_buy:
                StartActivity();
                break;
        }
    }

    public void StartCart(){
        CartListItem cartListItem=new CartListItem();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_cart, cartListItem, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if(isConnected){
            mIsCheckInternet = true;
        }
        else{
            mIsCheckInternet = false;
            Intent intent = new Intent(getActivity(), CheckInternetForCart.class);
            intent.putExtra(PAGE_ID,"Category");
            startActivity(intent);
            getActivity().finish();
        }
    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }
}
