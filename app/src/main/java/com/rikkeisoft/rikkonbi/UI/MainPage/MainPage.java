package com.rikkeisoft.rikkonbi.UI.MainPage;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.UnreadNotification;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterFCM.PresenterFCM;
import com.rikkeisoft.rikkonbi.Presenter.PresenterMain.PresenterMain;
import com.rikkeisoft.rikkonbi.Presenter.PresenterNotification.PresenterSettingNotifi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.myDBHelper;
import com.rikkeisoft.rikkonbi.Service.NotificationService;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.Login.LoginPage;
import com.rikkeisoft.rikkonbi.UI.Notification.NotificationFragment;
import com.rikkeisoft.rikkonbi.UI.Notification.SettingNotification.SettingNotification;
import com.rikkeisoft.rikkonbi.UI.QRcode.CameraQRCode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import q.rorbin.badgeview.QBadgeView;


public class MainPage extends AppCompatActivity implements MainActivityPresenter.View.MainView
        , NotificationFragment.BottomSheetListener
        , MainActivityPresenter.View.NotificationSettingView,
        MainActivityPresenter.View.ConnectedWifi

{
    private GoogleSignInClient mGoogleSignInClient;
    private final String TAG = "MainPage";
    private PresenterMain mUserPresenterCompl;
    private BottomNavigationView mNavView;
    private final String PAGE_ID = "Page_Key";
    private String mKeyPage;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private BottomNavigationMenuView mBottomNavigationMenuView;
    private APIInterface mApiInterface;
    private String mTokenUser;
    public int countUnread = 0;
    private View mView;
    private int mX;
    private int mY;
    private int mEnd;
    private int mTop;
    private PresenterSettingNotifi mSettingNotifi;
    public ListenerMethod mListenerMethod;
    private String mAccount;
    private QBadgeView mQbadge;
    private myDBHelper helper;
    private String cPage = "Home";
    private  FloatingActionButton floatingActionButton;

    private PresenterFCM mPresenterFCM;
    private PresenterCheckWiFi mCheckWiFi;

    public interface ListenerMethod{
       void RefreshNotification(String token);
    }

    public void RefreshUI(ListenerMethod listenerMethod){
        this.mListenerMethod = listenerMethod;
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().hide();


        mKeyPage = getIntent().getExtras().getString(PAGE_ID);

        mAccount = PreferencesProvider.getInstance(getApplication()).getCommitLogin(getApplication());

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mX = 60;
                mY = 0;
                mEnd = Gravity.END;
                mTop = Gravity.TOP;
        } else {
                mX = 20;
                mY = 0;
                mEnd = Gravity.END;
                mTop = Gravity.TOP;
        }

        mNavView = findViewById(R.id.nav_view);
        mBottomNavigationMenuView = (BottomNavigationMenuView) mNavView.getChildAt(0);
        mView  = mBottomNavigationMenuView.getChildAt(3);


        mQbadge =  new QBadgeView(MainPage.this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_category,
                R.id.navigation_message,R.id.navigation_account)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mNavView, navController);



        if(mKeyPage.equals("Category")){
            mNavView.setSelectedItemId(R.id.navigation_category);
            getIntent().putExtra(PAGE_ID,"");
        }

        if(mKeyPage.equals("Account")){
            mNavView.setSelectedItemId(R.id.navigation_account);
            getIntent().putExtra(PAGE_ID,"");
        }

        if(mKeyPage.equals("Notification")){
            mNavView.setSelectedItemId(R.id.navigation_message);
            getIntent().putExtra(PAGE_ID,"");
        }

        mCheckWiFi = new PresenterCheckWiFi(this);




        floatingActionButton = findViewById(R.id.fab_qecode_nav);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainPage.this
                        , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainPage.this,CameraQRCode.class);
                    intent.putExtra(PAGE_ID,cPage);
                    startActivity(intent);
                    finish();
                    //finish();
                } else {
                    ActivityCompat.requestPermissions(MainPage.this, new
                            String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mUserPresenterCompl = new PresenterMain(this);
        updateUI();

        mPresenterFCM = new PresenterFCM();


        mSettingNotifi = new PresenterSettingNotifi(this);



        helper = new myDBHelper(this);
        helper.deleteAll();
        Log.d(TAG,helper.getData().toString());



    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setCountNotification(mView, mTokenUser,mX,mY,mEnd,mTop);
            }
    };



    public void setTabBottom(String check){
        if(check.equals("true")){
            floatingActionButton.show();
            mBottomNavigationMenuView.setVisibility(View.VISIBLE);
        }
        else{
            floatingActionButton.hide();
            mBottomNavigationMenuView.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public void SignOut(String signout) {
        if(signout.equals("Yes")) {

            PreferencesProvider.getInstance(getApplication()).setCommitLogin("");
            startActivity(new Intent(MainPage.this, LoginPage.class));

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          if(requestCode == REQUEST_CAMERA_PERMISSION){
              if(permissions[0].equals(Manifest.permission.CAMERA)
                 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                  Intent intent = new Intent(MainPage.this,CameraQRCode.class);
                  intent.putExtra(PAGE_ID,cPage);
                  startActivity(intent);
                  finish();
              }
          }
    }



    @Override
    public void Progress(String status) {
        String account = PreferencesProvider.getInstance(getApplication()).getCommitLogin(getApplication());
        mUserPresenterCompl.UserBackground(account);

        updateUI();
    }


    public void updateUI() {
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(this).getTokenBearer(this));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");
            this.mTokenUser = getResources().getString(R.string.bearer)+token;

            mCheckWiFi.CheckWiFiBackground();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setCountNotification(mView, mTokenUser,mX,mY,mEnd,mTop);

    }

   @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void cPage(String cPage){
        this.cPage = cPage;
    }



    public void setCountNotification(View view,String tokenUser,int x, int y,int end , int top){
        mApiInterface = ApiUtils.getTokenAccess();

        Observable<UnreadNotification> observableNotifi =
                mApiInterface.getTotalNotification("application/json", mTokenUser);

        observableNotifi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UnreadNotification>(){
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UnreadNotification unreadNotification) {
                        countUnread = unreadNotification.getTotal();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,String.valueOf(countUnread));
                        BadgeNumber(countUnread, view, tokenUser, x, y, end, top);
                    }

                });

    }

    public void BadgeNumber(int count,View view,String tokenUser,int x, int y,int end , int top){
        if(count == 0) {
           mQbadge.bindTarget(view).hide(true);
        }
        if(count >= 1){
           mQbadge.bindTarget(view)
                    .setBadgeText(String.valueOf(count))
                    .setBadgeBackgroundColor(getResources().getColor(R.color.bg_orange_badge))
                    .setBadgeTextSize(10, true)
                    .setBadgeGravity(end | top)
                    .setGravityOffset(x,y,true)
                    .setShowShadow(false);
        }
        if(count >= 99){
           mQbadge.bindTarget(view)
                    .setBadgeText("99+")
                    .setBadgeBackgroundColor(getResources().getColor(R.color.bg_orange_badge))
                    .setBadgeTextSize(10, true)
                    .setBadgeGravity(end | top)
                    .setGravityOffset(x,y,true)
                    .setShowShadow(false);
        }

    }


    @Override
    protected void onResume() {
            super.onResume();
            try {
                MyApplication.getInstance().setConnectivityListener(mCheckWiFi);

                registerReceiver(broadcastReceiver, new IntentFilter("NEW_MASSAGE"));
            }catch (Exception e){
               // already registered
            }
            ComponentName component=new ComponentName(this, NotificationService.class);
            getPackageManager()
                    .setComponentEnabledSetting(component,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
        }

    @Override
    protected void onStop() {
            super.onStop();
            unregisterReceiver(broadcastReceiver);
            ComponentName component=new ComponentName(this, NotificationService.class);
            getPackageManager()
                    .setComponentEnabledSetting(component,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);



            Log.e(TAG,"app is closed");
        }


    @Override
    public void onButtonClicked(int id,String command) {
        if (command.equals("1")) {
            int count = helper.updateName(String.valueOf(id),String.valueOf(true));
            if(count == -100){
                Toast.makeText(MainPage.this,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            }
            else{
                mSettingNotifi.NotificationRead(mTokenUser, id);
                mListenerMethod.RefreshNotification(mTokenUser);
            }
        } else if (command.equals("2")) {
            int count = helper.delete(String.valueOf(id));
            if(count == -100){
                Toast.makeText(MainPage.this,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            }
            else{
                mSettingNotifi.NotificationDel(mTokenUser, id);
                mListenerMethod.RefreshNotification(mTokenUser);
            }
        }
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            setTabBottom("false");
        }else{
            setTabBottom("true");
        }
    }




}
