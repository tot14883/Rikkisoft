package com.rikkeisoft.rikkonbi.UI.CheckInternet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.Account.AccountFragment;
import com.rikkeisoft.rikkonbi.UI.Cart.CartScreen;
import com.rikkeisoft.rikkonbi.UI.Category.CategoryFragment;
import com.rikkeisoft.rikkonbi.UI.Home.HomeFragment;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.UI.Notification.NotificationFragment;

public class CheckInternetFragment extends Fragment implements View.OnClickListener, MainActivityPresenter.View.ConnectedWifi {

     private ImageButton mCheckInternet;
     private String mReceived = null;
     private PresenterCheckWiFi mCheckWiFi;
    private final String PAGE_ID = "Page_Key";


      public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       View root = inflater.inflate(R.layout.fragment_check_internet, container, false);


         Bundle bundle = this.getArguments();
         if(bundle != null){
             mReceived = bundle.getString("TAG");
         }

          Log.d("Yo",mReceived);

         mCheckInternet = (ImageButton) root.findViewById(R.id.btn_refresh);
         mCheckInternet.setOnClickListener(this);

         mCheckWiFi = new PresenterCheckWiFi(this);
         mCheckWiFi.CheckWiFiBackground();

       return root;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_refresh:
                checkConnect();break;
        }
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        String category = "CategoryFragment";
        String cart = "CartScreen";
        String notification = "NotificationFragment";
        String home = "HomeFragment";
        String account = "Account";
        if (isConnected) {
            if (mReceived == category) {

                CategoryFragment categoryFragment = new CategoryFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, categoryFragment)
                        .addToBackStack(null)
                        .commit();
                mReceived = null;

            } else if (mReceived == cart) {
                Intent intent = new Intent(getActivity(), CartScreen.class);
                startActivity(intent);
                mReceived = null;


            } else if (mReceived == notification) {

                NotificationFragment notificationFragment = new NotificationFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, notificationFragment)
                        .addToBackStack(null)
                        .commit();
                mReceived = null;


            }else if(mReceived == home){

                HomeFragment homeFragment = new HomeFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, homeFragment)
                        .addToBackStack(null)
                        .commit();
                mReceived = null;


            }
            else if(mReceived == account){
                AccountFragment accountFragment = new AccountFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, accountFragment)
                        .addToBackStack(null)
                        .commit();
                mReceived = null;

            }
        }
    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);
    }



}
