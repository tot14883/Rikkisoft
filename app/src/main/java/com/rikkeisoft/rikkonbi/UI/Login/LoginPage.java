package com.rikkeisoft.rikkonbi.UI.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.kinda.alert.KAlertDialog;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterFCM.PresenterFCM;
import com.rikkeisoft.rikkonbi.Presenter.PresenterLogin.PresenterLogin;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.LoginModel.LoginToken;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity implements MainActivityPresenter.View.LoginView,View.OnClickListener,
        MainActivityPresenter.View.ConnectedWifi{
    private GoogleSignInClient mGoogleSignInClient;
    private Button mBtnLogin;
    private final String TAG = "LoginPage";
    private static int RC_SIGN_IN = 0;
    private APIInterface mAPIInterface;
    private PresenterLogin mUserPresenterCompl;
    private final String PAGE_ID = "Page_Key";
    private PresenterCheckWiFi mCheckWiFi;
    private PresenterFCM mPresenterFCM;
    private String mTokenUser;
    private String mKeyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();

        mKeyPage = getIntent().getExtras().getString(PAGE_ID);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id_1))
                .build();

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);

        mUserPresenterCompl = new PresenterLogin(this);
        mPresenterFCM = new PresenterFCM();

        SignOut("Yes");
    }



    @Override
    protected void onStart() {
        super.onStart();
             PreferencesProvider.getInstance(getApplication()).setCommitLogin("");
             GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
             if(account != null){
                 SignOut("Yes");
             }
             else{
                 //mUserPresenterCompl.LoginBackground(account,true);
             }
    }

    private void signIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            SignOut("Yes");
        }
        else{
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            String token = null;
            if(completedTask.isSuccessful()) {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                GoogleSignInAccount act = GoogleSignIn.getLastSignedInAccount(this);
                if(act != null) {
                    token = act.getIdToken();
                    String email = act.getEmail();
                    String[] split = email.split("@");
                    String domain = split[1];
                    if (domain.equals("rikkeisoft.com")) {
                        new LoginTokenResponse(token).execute();
                    } else {
                        showMessage(getResources().getString(R.string.Login_fail));
                        SignOut("Yes");
                    }
                }
            }else{
                SignOut("Yes");
            }
            Log.d(TAG, "tokenId: " + token);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:signIn();break;
        }
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if(!isConnected){
            Toast.makeText(LoginPage.this,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
    }


    public class LoginTokenResponse extends AsyncTask<String,String ,String >{
        private String tokenID;
        public LoginTokenResponse(String tokenID){
            this.tokenID = tokenID;
        }
        @Override
        protected String doInBackground(String... strings) {
            LoginToken loginToken = new LoginToken();
            loginToken.setToken(tokenID);
            mAPIInterface = ApiUtils.getTokenAccess();

            Call<User> call = mAPIInterface.getToken2Server(getResources().getString(R.string.application_json),loginToken);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        User user = response.body();
                        Gson gson = new Gson();
                        List<String> reponseData = new ArrayList<String>();
                        reponseData.add(gson.toJson(user));

                        PreferencesProvider.getInstanceID(getApplication()).setUserId(user.getId());

                        PreferencesProvider.getsInstanceToken(getApplication()).setTokenBearer(reponseData.get(0));

                        showProgress("Success");
                        //Send Once time to api token notification
                        mTokenUser = getResources().getString(R.string.bearer)+response.body().getToken();
                        newTokenId();
                    }else{
                        showMessage(getResources().getString(R.string.Login_fail));
                        SignOut("Yes");

                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                    SignOut("Yes");
                }
            });
            return null;
        }
    }


    @Override
    public void showProgress(String massage) {
        if(!massage.equals("")) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            PreferencesProvider.getInstance(getApplication()).setCommitLogin(account.getIdToken());
            StartActivity();
            // Log.d(TAG,massage);
        }
    }

    @Override
    public void SignOut(String signout) {
        if (signout == "Yes") {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if(account != null) {
                mGoogleSignInClient.revokeAccess()
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String tokenFCM = PreferencesProvider.getInstanceFB(getApplicationContext()).getFirebase(getApplicationContext());
                                String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(LoginPage.this).getTokenBearer(LoginPage.this));
                                final JSONObject jobject;
                                try {
                                    jobject = new JSONObject(user_info);
                                    String token = jobject.getString("token");
                                    mTokenUser = getResources().getString(R.string.bearer)+token;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mPresenterFCM.DelNotificationFCM(mTokenUser, tokenFCM);

                                PreferencesProvider.getInstance(getApplication()).setCommitLogin("");


                            }
                        });
            }
        }

    }


    @Override
    public void showMessage(String message) {
        new KAlertDialog(this, KAlertDialog.ERROR_TYPE)
                .setTitleText(getResources().getString(R.string.Oops))
                .setContentText(message)
                .show();
    }

    @Override
    public void UserAccount(List<String> user) {

    }

    @Override
    public void ChangePage(boolean val) {
        if (val == true) {
            StartActivity();
        }

    }

    public void StartActivity(){
        Intent intent = new Intent(LoginPage.this,MainPage.class);
        intent.putExtra(PAGE_ID,mKeyPage);
        startActivity(intent);
        finish();
    }

    public void newTokenId(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);


                        mPresenterFCM.NotificationFCM(mTokenUser,token);

                        PreferencesProvider.getInstanceFB(getApplicationContext()).setPrefKey(token);
                        Log.d(TAG, msg);
                    }
                });
    }
}
