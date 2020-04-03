package com.rikkeisoft.rikkonbi.UI.History;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Adapter.HistoryAdapter.HistoryAdapter;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterHistory.PresenterHistory;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.UI.Cart.CartScreen;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetForCart;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements MainActivityPresenter.View.HistoryView,
        View.OnClickListener,
        MainActivityPresenter.View.ConnectedWifi{
    private PresenterHistory mPresenterHistory;
    private static final String TAG = "History";
    private RecyclerView mRecyclerView;
    private List<String> mHistoryList;
    private HistoryAdapter mHistoryAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mPosition;
    private String mTokenId;
    private String mUserid;
    private int mPositionLocal;
    private ImageButton mCloseBtn;
    private boolean mCheck;
    private static final String PAGE_ID = "Page_Key";
    private PresenterCheckWiFi mCheckWiFi;
    private String mKeyPage;
    private static final String PAGE1_ID = "Page_Key1";
    private TextView textCartItemCount;
    long mCartItemCount = 0;
    private ImageView mImageCart;
    private CartSqlite helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        mKeyPage = getIntent().getExtras().getString(PAGE_ID);


        helper = new CartSqlite(this);



        mPresenterHistory = new PresenterHistory(this);

        mCloseBtn = (ImageButton) findViewById(R.id.close_page);
        mCloseBtn.setOnClickListener(this);
        mRecyclerView =(RecyclerView) findViewById(R.id.recycle_history);

        textCartItemCount = (TextView) findViewById(R.id.cart_badge);
        textCartItemCount.setOnClickListener(this);
        mImageCart = (ImageView) findViewById(R.id.action_btn_cart);
        mImageCart.setOnClickListener(this);

        mHistoryList = new ArrayList<>();
        mHistoryAdapter = new HistoryAdapter(this,mHistoryList);

        mLinearLayoutManager =new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mHistoryAdapter);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();

        updateUI();

    }


    public void startCart(){
        checkConnect();
        if(mCheck) {
            Intent intent = new Intent(History.this, CartScreen.class);
            intent.putExtra(PAGE_ID, "History");
            startActivity(intent);
            finish();
        }

    }

    public void setDataToHistory(List<String> history) {
        // Log.d(TAG,String.valueOf(history.size()));
        if (!history.get(0).equals("null")) {
            if(mPosition == -1) {
                Log.d(TAG,String.valueOf(mPositionLocal));
                try {
                    mHistoryList.clear();
                    mRecyclerView.removeViewAt(mPositionLocal);
                    mHistoryAdapter.notifyItemRemoved(mPositionLocal);
                    mHistoryAdapter.notifyItemRangeChanged(mPositionLocal, mHistoryList.size());
                } catch (Exception e) {
                    //Check error when mCartAdapter uses notifyItemRemoved function
                    //Message When it happen error
                    Log.e(TAG, e.getMessage());
                }
            }
            mHistoryList.addAll(history);
            mHistoryAdapter.notifyDataSetChanged();
        }


    }


    public void showProgress(String message) {

    }


    public void showMessage(String message) {
        checkConnect();
        if(mCheck) {
            if (message.equals("Delete")) {
                mPosition = -1;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Delete_Success), Toast.LENGTH_SHORT).show();
                updateUI();
            } else if (message.equals("Delete_Fail")) {
                mPosition = 0;
            }
        }
    }
    public void updateUI(){
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(this).getTokenBearer(this));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String id = jobject.getString("id");
            String token = jobject.getString("token");

            this.mUserid = id;
            this.mTokenId = getResources().getString(R.string.bearer)+token;
            mPresenterHistory.HistoryBackground(mTokenId,mUserid);

            setupBadge();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void OnClickItem(String position) {
        String[] slpitarr = position.split("_");
        checkConnect();
        if(mCheck) {
            if (slpitarr[0].equals("0")) {
                mPresenterHistory.HistoryDelete(mTokenId, Integer.parseInt(slpitarr[1]), mUserid, Integer.parseInt(slpitarr[2]));
                mPositionLocal = Integer.parseInt(slpitarr[2]);
                Log.d(TAG, slpitarr[0] + " " + slpitarr[1] + " " + slpitarr[2]);
            }
        }else{
            Toast.makeText(History.this,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_page:backActivity();break;
            case R.id.action_btn_cart:startCart();break;
            case R.id.cart_badge:startCart();break;
        }
    }

    public void backActivity(){
        checkConnect();
        if (mCheck) {
            if (mKeyPage.equals("Account")) {
                Intent intent = new Intent(History.this, MainPage.class);
                intent.putExtra(PAGE_ID, "Account");
                startActivity(intent);
                finish();
            } else if (mKeyPage.equals("Home")) {
                Intent intent = new Intent(History.this, MainPage.class);
                intent.putExtra(PAGE_ID, "Home");
                startActivity(intent);
                finish();
            }else if(mKeyPage.equals("History")){
                Intent intent = new Intent(History.this, MainPage.class);
                intent.putExtra(PAGE_ID, "Account");
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        checkConnect();
        if(mCheck) {
            if (mKeyPage.equals("Account")) {
                Intent intent = new Intent(History.this, MainPage.class);
                intent.putExtra(PAGE_ID, "Account");
                startActivity(intent);
                finish();
            } else if (mKeyPage.equals("Home")) {
                Intent intent = new Intent(History.this, MainPage.class);
                intent.putExtra(PAGE_ID, "Home");
                startActivity(intent);
                finish();
            }else if(mKeyPage.equals("History")){
                Intent intent = new Intent(History.this, MainPage.class);
                intent.putExtra(PAGE_ID, "Account");
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(mCheckWiFi);
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            mCheck = false;
            Intent intent = new Intent(History.this, CheckInternetForCart.class);
            intent.putExtra(PAGE_ID,mKeyPage);
            intent.putExtra(PAGE1_ID,"History");
            startActivity(intent);
            finish();
        }
        else{

            mCheck = true;
        }
    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }
}
