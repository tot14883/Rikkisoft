package com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi;

import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

public class PresenterCheckWiFi implements ConnectivityReceiver.ConnectivityReceiverListener,
    MainActivityPresenter.Presenter.CheckWiFiBackground{
    private MainActivityPresenter.View.ConnectedWifi view;
    public PresenterCheckWiFi(MainActivityPresenter.View.ConnectedWifi view){
        this.view = view;
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
          view.showConnectedWifi(isConnected);
    }

    @Override
    public void CheckWiFiBackground() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        view.showConnectedWifi(isConnected);

    }
}
