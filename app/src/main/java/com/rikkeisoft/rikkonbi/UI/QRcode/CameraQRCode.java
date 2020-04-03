package com.rikkeisoft.rikkonbi.UI.QRcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

import java.io.IOException;

public class CameraQRCode extends AppCompatActivity implements View.OnClickListener, MainActivityPresenter.View.ConnectedWifi {
    private SurfaceView mSurfaceView;
    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private final String PAGE_ID = "Page_Key";
    private ImageButton mClose_btn;
    private String mKeyPage;
    private PresenterCheckWiFi mCheckWiFi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_qrcode);
        getSupportActionBar().hide();

        mKeyPage = getIntent().getExtras().getString(PAGE_ID);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();


        initViews();

    }

    private void initViews() {
       // txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        mSurfaceView = findViewById(R.id.surfaceView);
        mClose_btn = (ImageButton) findViewById(R.id.close_page);
        mClose_btn.setOnClickListener(this);

    }
    private void initialiseDetectorsAndSources() {


        if (ActivityCompat.checkSelfPermission(CameraQRCode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            mBarcodeDetector = new BarcodeDetector.Builder(this)
                    .setBarcodeFormats(Barcode.ALL_FORMATS)
                    .build();

            mCameraSource = new CameraSource.Builder(this, mBarcodeDetector)
                    .setRequestedPreviewSize(1920, 1080)
                    .setAutoFocusEnabled(true) //you should add this feature
                    .build();

            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {


                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {
                        mCameraSource.start(mSurfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });


        } else {
            ActivityCompat.requestPermissions(CameraQRCode.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }


        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    /*txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {
                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);
                        }
                    });*/
                }
            }
        });
    }


    @Override
    protected void onPause() {
        mCameraSource.release();
        super.onPause();
    }

    @Override
    protected void onResume() {
        initialiseDetectorsAndSources();
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(mCheckWiFi);
    }

    @Override
    protected void onStart() {
        initialiseDetectorsAndSources();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_page:StartActivity();break;
        }
    }
    @Override
    public void onBackPressed() {
        checkConnect();
    }
    public void StartActivity(){
        Intent intent = new Intent(CameraQRCode.this, MainPage.class);
        intent.putExtra(PAGE_ID,mKeyPage);
        startActivity(intent);
        finish();
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
