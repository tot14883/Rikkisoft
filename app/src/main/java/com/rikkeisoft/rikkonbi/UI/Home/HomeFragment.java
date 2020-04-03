package com.rikkeisoft.rikkonbi.UI.Home;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterHome.PresenterHome;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.Adapter.AssetImageAdapter.AssetImageAdapter;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.AssetImageModel.PiSignage;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.UI.Account.AccountFragment;
import com.rikkeisoft.rikkonbi.UI.Category.CategoryFragment;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.UI.QRcode.CameraQRCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MainActivityPresenter.View.HomeView,
        View.OnClickListener,
        MainActivityPresenter.View.ConnectedWifi{

    private static final String TAG = "HomeFragment";
    private EditText mEdtWallet;
    private final String PAGE_ID = "Page_Key";
    private PresenterHome mPresenterCompl;
    private TextView mTextXinChao;
    private List<PiSignage> mAssetArrayList;
    private AssetImageAdapter mAssetImageAdapter;
    private AdapterViewFlipper mAdapterViewFlipper;
    private ImageButton mImgScan;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private PresenterCheckWiFi mCheckWiFi;
    private boolean mCheck;
    private static final String PAGE_CHECK_INTERNET = "Chenk_Internet";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        String account = PreferencesProvider.getInstance(getContext()).getCommitLogin(getContext());


        mCheckWiFi = new PresenterCheckWiFi(this);

        mAdapterViewFlipper = (AdapterViewFlipper) root.findViewById(R.id.adapterViewFlipper);

        mTextXinChao = (TextView) root.findViewById(R.id.hello_name);

        mEdtWallet = (EditText) root.findViewById(R.id.edit_balance);
        mPresenterCompl = new PresenterHome(this);

        mImgScan = (ImageButton) root.findViewById(R.id.img_btn_scan_n);
        mImgScan.setOnClickListener(this);

        mAssetArrayList = new ArrayList<>();
        mAssetImageAdapter = new AssetImageAdapter(this,mAssetArrayList);

        mAdapterViewFlipper.setAdapter(mAssetImageAdapter);
        mAdapterViewFlipper.setFlipInterval(4000);
        mAdapterViewFlipper.startFlipping();

        updateUI();

        ((MainPage) getActivity()).cPage("Home");


        return root;
    }

    public void updateUI(){
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(getContext()).getTokenBearer(getContext()));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");
            String name = jobject.getString("userName");

            mTextXinChao.setText(getResources().getString(R.string.xin_chao)+", "+name);
            mPresenterCompl.HomeBackground(getResources().getString(R.string.bearer)+token);
            mCheckWiFi.CheckWiFiBackground();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setDataToImageSlide(List<PiSignage> assetArrayList) {
          this.mAssetArrayList.addAll(assetArrayList);
          mAssetImageAdapter.notifyDataSetChanged();
    }


    @Override
    public void ShowView(String view) {
        if(view.equals("Fail")){
           // Log.e(TAG,getResources().getString(R.string.fail));
        }
        else{
            mEdtWallet.setText(view + getResources().getString(R.string.currency));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_btn_scan_n:
                startQRCODE();break;
        }
    }

    public void startQRCODE(){
        mCheckWiFi.CheckWiFiBackground();
        if(mCheck) {
            if (ActivityCompat.checkSelfPermission(getActivity()
                    , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(getActivity(),CameraQRCode.class);
                intent.putExtra(PAGE_ID,"");
                startActivity(intent);
                getActivity().finish();
                //getActivity().finish();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(permissions[0].equals(Manifest.permission.CAMERA)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(getActivity(), CameraQRCode.class));
                //getActivity().finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(mCheckWiFi);
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            CheckInternetFragment checkInternetFragment = new CheckInternetFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TAG",TAG);
            checkInternetFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, checkInternetFragment, null)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();

            ((MainPage) getActivity()).setTabBottom("false");

            mCheck = false;
        }
        else{
            /*getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, this, null)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();*/
            ((MainPage) getActivity()).setTabBottom("true");
            mCheck = true;
        }
    }


}