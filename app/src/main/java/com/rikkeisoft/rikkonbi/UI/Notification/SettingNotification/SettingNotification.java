package com.rikkeisoft.rikkonbi.UI.Notification.SettingNotification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterNotification.PresenterSettingNotifi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.myDBHelper;
import com.rikkeisoft.rikkonbi.UI.Category.DetailCategoryProduct;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.UI.Notification.NotificationFragment;

import java.util.List;


public class SettingNotification extends AppCompatActivity implements View.OnClickListener
        ,MainActivityPresenter.View.ConnectedWifi{
    private BottomSheetListener mListener;
    private Button mBtnWatch;
    private Button mBtnRemove;
    private Button mBtnCancel;
    private String mID;
    private final String PAGE_ID = "Notification_Key";
    private boolean mIsCheckInternet;
    private PresenterCheckWiFi mCheckWiFi;
    private static final String TAG = "NotificationFragment";
    private boolean aBoolean = true;
    private static BottomSheetBehavior bottomSheetBehavior;
    private static View bottomSheetInternal;
    private CoordinatorLayout coordinatorLayout;

   /* public SettingNotification(){
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notification);
        getWindow().setGravity(Gravity.BOTTOM);


        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AlertDialogCustom);
       // mID = getArguments().getString(PAGE_ID);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();

        mBtnWatch = (Button) findViewById(R.id.btn_watch);
        mBtnWatch.setOnClickListener(this);
        mBtnRemove = (Button) findViewById(R.id.btn_remove);
        mBtnRemove.setOnClickListener(this);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        lp.x = 10;
        lp.y = 10;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindowManager().updateViewLayout(view, lp);
    }
  /*  @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           View v = inflater.inflate(R.layout.activity_setting_notification,container,false);

           mBtnWatch = (Button) v.findViewById(R.id.btn_watch);
           mBtnWatch.setOnClickListener(this);
           mBtnRemove = (Button) v.findViewById(R.id.btn_remove);
           mBtnRemove.setOnClickListener(this);
           mBtnCancel = (Button) v.findViewById(R.id.btn_cancel);
           mBtnCancel.setOnClickListener(this);
           blurView = (BlurView) v.findViewById(R.id.blurView);
           blurBackground(true);


        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                BottomSheetDialog d = (BottomSheetDialog) dialog;
                coordinatorLayout = (CoordinatorLayout) d.findViewById(R.id.coordinator_setting);
                bottomSheetInternal = d.findViewById(R.id.blurView);
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetInternal);
                bottomSheetBehavior.setHideable(false);
                BottomSheetBehavior.from((View)coordinatorLayout.getParent()).setPeekHeight(bottomSheetInternal.getHeight());
                bottomSheetBehavior.setPeekHeight(bottomSheetInternal.getHeight());



                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View view, int i) {
                        if (i == BottomSheetBehavior.STATE_HIDDEN)
                        {
                            dismiss();
                            ((MainPage)  getActivity()).setTabBottom("true");
                        }


                    }

                    @Override
                    public void onSlide(@NonNull View view, float v) {

                    }
                });
                coordinatorLayout.getParent().requestLayout();
              }
          });


        return v;
    }*/



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_watch:WatchNotification();break;
            case R.id.btn_remove:RemoveNotification();break;
            case R.id.btn_cancel:close();
                ;break;
        }
    }



    public void close(){
        //dismiss();
       // ((MainPage)  getActivity()).setTabBottom("true");
    }

    public void WatchNotification(){
        checkConnect();
        if(mIsCheckInternet) {
            mListener.onButtonClicked(Integer.parseInt(mID), "1");
        }
        else{
            Toast.makeText(SettingNotification.this,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
       // dismiss();

        //((MainPage)  getActivity()).setTabBottom("true");
    }

    public void RemoveNotification(){
        checkConnect();
        if(mIsCheckInternet) {
            mListener.onButtonClicked(Integer.parseInt(mID),"2");
        }
        else{
            Toast.makeText(SettingNotification.this,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
       // dismiss();
        //((MainPage)).setTabBottom("true");
    }
    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }
    @Override
    public void showConnectedWifi(boolean isConnected) {
        if(isConnected){
            mIsCheckInternet = true;
        }
        else{
            mIsCheckInternet = false;
           // NoInternet();
            //dismiss();

            //((MainPage)  getActivity()).setTabBottom("true");
        }
    }

    public interface BottomSheetListener {
        void onButtonClicked(int id, String command);
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }*/
    /*public void NoInternet(){
        Bundle bundle = new Bundle();
        bundle.putString("TAG",TAG);
        CheckInternetFragment checkInternetFragment = new CheckInternetFragment();
        checkInternetFragment.setArguments(bundle);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, checkInternetFragment, null)
                .addToBackStack(null)
                .commit();

    }
*/

    /*@Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        ((MainPage)  getActivity()).setTabBottom("true");
    }*/
}
