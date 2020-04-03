package com.rikkeisoft.rikkonbi.UI.RikkeiInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterContact.PresenterContact;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;



public class RikkeiInfo extends AppCompatActivity implements View.OnClickListener,
        MainActivityPresenter.View.ContactView,MainActivityPresenter.View.ConnectedWifi{

    private ImageButton mCloseBtn;
    private final String PAGE_ID = "Page_Key";
    private PresenterContact mPresenterContact;
    private String mKeyPage;
    private PresenterCheckWiFi mCheckWiFi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rikkei_info);
        getSupportActionBar().hide();

        mCloseBtn = (ImageButton) findViewById(R.id.close_page);
        mCloseBtn.setOnClickListener(this);

        mKeyPage = getIntent().getExtras().getString(PAGE_ID);


        mPresenterContact = new PresenterContact(this);
        mPresenterContact.ContactBackground(mKeyPage);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_page:startActivity();break;
        }
    }

    public void startActivity(){
        Intent intent = new Intent(RikkeiInfo.this,MainPage.class);
        intent.putExtra(PAGE_ID,"Account");
        startActivity(intent);
        finish();
    }

    @Override
    public void ShowMessage(String contant, String hotline) {
        TextView rikki_1 = (TextView) findViewById(R.id.rikkei_info_1);
        TextView rikki_2 = (TextView) findViewById(R.id.rikkei_info_9);

        String tel_hotline = getResources().getString(R.string.rikkei_info_tel)+" "+hotline;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            rikki_1.setText(Html.fromHtml(contant, Html.FROM_HTML_MODE_COMPACT));
        } else {
            rikki_1.setText(Html.fromHtml(contant));
        }
        rikki_2.setText(tel_hotline);


    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(mCheckWiFi);

    }

    @Override
    public void onBackPressed() {
        checkConnect();
    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
          if(isConnected){

          }
          else{
              Toast.makeText(getApplicationContext(),
                      getResources().getString(R.string.no_internet),
                      Toast.LENGTH_SHORT).show();
          }
    }
}
