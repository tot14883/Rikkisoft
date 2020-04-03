package com.rikkeisoft.rikkonbi.UI.Account;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterAccount.PresenterAccount;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterContact.PresenterContact;
import com.rikkeisoft.rikkonbi.Presenter.PresenterFCM.PresenterFCM;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.UI.Cart.CartScreen;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.History.History;
import com.rikkeisoft.rikkonbi.UI.Home.HomeFragment;
import com.rikkeisoft.rikkonbi.UI.Login.LoginPage;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.UI.RikkeiInfo.RikkeiInfo;
import com.rikkeisoft.rikkonbi.UI.SplashScreen.SplashScreen;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AccountFragment extends Fragment implements View.OnClickListener
        ,MainActivityPresenter.View.ContactView, MainActivityPresenter.View.ConnectedWifi {

    private final String TAG = "Account";
    private PresenterAccount mUserPresenterCompl;
    private TextView mPersonUsername,mPersonUseremail;
    private ImageView mPersonProfile;
    private Button mBtnSignout;
    private GoogleSignInClient mGoogleSignInClient;
    private Button mOrder_management;
    private Button mBtnHotLine;
    private Button mRikkeiInfo;
    private PresenterFCM mPresenterFCM;
    private String mTokenUser;
    private PresenterContact mPresenterContact;
    private  SpannableStringBuilder mBuilder;
    private final String PAGE_ID = "UserID";
    private final String PAGE_ID_LOGIN = "Page_Key";
    private boolean mCheck;
    private PresenterCheckWiFi mCheckWiFi;
    private TextView textCartItemCount;
    long mCartItemCount = 0;
    private ImageView mImageCart;
    private CartSqlite helper;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account, container, false);


        String account = PreferencesProvider.getInstance(getContext()).getCommitLogin(getContext());

        mPresenterFCM = new PresenterFCM();



        mPresenterContact = new PresenterContact(this);

        mOrder_management = (Button) root.findViewById(R.id.order_management);
        mOrder_management.setOnClickListener(this);

        mPersonUseremail = (TextView) root.findViewById(R.id.person_useremail);
        mPersonUsername = (TextView) root.findViewById(R.id.person_username);
        mPersonProfile = (ImageView) root.findViewById(R.id.person_profile);

        mBtnSignout = (Button) root.findViewById(R.id.signout_btn);
        mBtnSignout.setOnClickListener(this);

        mRikkeiInfo = (Button) root.findViewById(R.id.info_rikkei_soft);
        mRikkeiInfo.setOnClickListener(this);

        mCheckWiFi = new PresenterCheckWiFi(this);


        ((MainPage) getActivity()).cPage("Account");

        mBtnHotLine = (Button) root.findViewById(R.id.hotline_rikkei_soft);

        textCartItemCount = (TextView) root.findViewById(R.id.cart_badge);
        textCartItemCount.setOnClickListener(this);
        mImageCart = (ImageView) root.findViewById(R.id.action_btn_cart);
        mImageCart.setOnClickListener(this);
        helper = new CartSqlite(getContext());

        updateUI();



        return root;
    }

    private void setupBadge() {
        mCartItemCount =  helper.getProfilesCount();
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else if(mCartItemCount > 99){
                textCartItemCount.setText("99+");
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void updateUI(){

        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(getContext()).getTokenBearer(getContext()));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");
            String avatar = jobject.getString("avatar");
            String email = jobject.getString("email");
            String fullname = jobject.getString("fullName");


            this.mTokenUser = getResources().getString(R.string.bearer)+token;

            mPresenterContact.ContactBackground(mTokenUser);

            mPersonUsername.setText(fullname);
            mPersonUseremail.setText(email);
            Picasso.get()
                    .load(avatar)
                    .into(mPersonProfile);

            setupBadge();

            mCheckWiFi.CheckWiFiBackground();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signout_btn:
                SignOut();
                break;
            case R.id.order_management:
                StartActivity();
                break;
            case R.id.info_rikkei_soft:
                startRikkei();
                break;
            case R.id.hotline_rikkei_soft:
                showAlert();
                break;
            case R.id.cart_badge:
                startCart();
                break;
            case R.id.action_btn_cart:
                startCart();
                break;
        }
    }

    public void startCart(){
        Intent intent = new Intent(getActivity(),CartScreen.class);
        intent.putExtra(PAGE_ID_LOGIN,"Account");
        startActivity(intent);
    }
    public void SignOut(){
        PreferencesProvider.getInstance(getContext()).setCommitLogin("");
        Intent intent = new Intent(getActivity(),LoginPage.class);
        intent.putExtra(PAGE_ID_LOGIN,"LoginPage");
        startActivity(intent);
        getActivity().finish();
    }


    public void startRikkei(){
        checkConnect();
        if(mCheck) {
            Intent intent = new Intent(getActivity(), RikkeiInfo.class);
            intent.putExtra(PAGE_ID, mTokenUser);
            startActivity(intent);
        }else{
            NoInternet();
        }
        //getActivity().finish();
    }

    public void StartActivity(){
        checkConnect();
        if(mCheck) {
          Intent intent = new Intent(getActivity(), History.class);
          intent.putExtra(PAGE_ID_LOGIN,"Account");
          startActivity(intent);
          getActivity().finish();
        }else{
            NoInternet();
        }
        //getActivity().finish();
    }

    public void showAlert() {
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_message_hot_line);
        mDialog.setCanceledOnTouchOutside(false);

        Button mBtnOk = (Button) mDialog.findViewById(R.id.btn_massage_yes);
        Button mBtnCancel = (Button) mDialog.findViewById(R.id.btn_massage_close);

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    @Override
    public void ShowMessage(String contant, String hotline) {
        mBuilder = new SpannableStringBuilder();
        String phonLine = getResources().getString(R.string.phone_line)+hotline;
        SpannableString redSpannable= new SpannableString(phonLine);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 8, phonLine.length(), 0);
        mBuilder.append(redSpannable);
        mBtnHotLine.setText(mBuilder, TextView.BufferType.SPANNABLE);
        mBtnHotLine.setOnClickListener(this);
    }


    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            Bundle bundle = new Bundle();
            bundle.putString("TAG",TAG);
            CheckInternetFragment checkInternetFragment = new CheckInternetFragment();
            checkInternetFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, checkInternetFragment, null)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();

            ((MainPage) getActivity()).setTabBottom("false");

            mCheck = false;
        }
        else{

            ((MainPage) getActivity()).setTabBottom("true");
            mCheck = true;
        }
    }

    public void NoInternet(){
        Bundle bundle = new Bundle();
        bundle.putString("TAG",TAG);
        CheckInternetFragment checkInternetFragment = new CheckInternetFragment();
        checkInternetFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, checkInternetFragment, null)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(mCheckWiFi);
    }



}
