package com.rikkeisoft.rikkonbi.UI.CheckInternet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.Cart.CartScreen;
import com.rikkeisoft.rikkonbi.UI.History.History;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

public class CheckInternetForCart extends AppCompatActivity implements  MainActivityPresenter.View.ConnectedWifi {

    private Button mCheckaaa;
    private ImageButton close_page;
    private PresenterCheckWiFi mCheckWiFi;
    private static final String PAGE_ID = "Page_Key";
    private static final String PAGE1_ID = "Page_Key1";

    private String mKeyPage;
    private String mKeyPage1;

    private int mCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_internet_for_cart);
        getSupportActionBar().hide();

        mKeyPage = getIntent().getExtras().getString(PAGE_ID);
        mKeyPage1 = getIntent().getExtras().getString(PAGE1_ID);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();

        mCheckaaa = (Button) findViewById(R.id.btn_refresh_1);
        mCheckaaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  checkConnect();
            }
        });

        close_page = (ImageButton) findViewById(R.id.close_page);
        close_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKeyPage1.equals("History")){
                    Intent intent = new Intent(CheckInternetForCart.this, History.class);
                    intent.putExtra(PAGE_ID, mKeyPage);
                    startActivity(intent);
                    finish();

                }
                else{
                    Intent intent = new Intent(CheckInternetForCart.this, MainPage.class);
                    intent.putExtra(PAGE_ID, "Category");
                    startActivity(intent);
                    finish();
                }

            }
        });
    }


    @Override
    public void showConnectedWifi(boolean isConnected) {
        String cart = "CartScreen";
        if (isConnected) {
            if(mKeyPage1.equals("History")){
                Intent intent = new Intent(CheckInternetForCart.this, History.class);
                intent.putExtra(PAGE_ID, mKeyPage);
                startActivity(intent);
                finish();

            }
            else {
                Intent intent = new Intent(CheckInternetForCart.this, CartScreen.class);
                intent.putExtra(PAGE_ID, mKeyPage);
                startActivity(intent);
                finish();
            }
        }
        else{
           /* if(mCheck == 1) {
                mCheck = 0;*/
                /*Intent intent = new Intent(CheckInternetForCart.this, MainPage.class);
                intent.putExtra(PAGE_ID, mKeyPage);
                startActivity(intent);
                finish();*/
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.no_internet),
                        Toast.LENGTH_SHORT).show();
          //  }

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        checkConnect();
        //mCheck = 1;
    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }

}
