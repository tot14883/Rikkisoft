package com.rikkeisoft.rikkonbi.UI.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.Login.LoginPage;

public class SplashScreen extends AppCompatActivity {
    private final String PAGE_ID = "Page_Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashScreen.this,LoginPage.class);
                intent.putExtra(PAGE_ID,"Login");
                startActivity(intent);
                finish();

            }
        }, secondsDelayed * 1000);
    }
}
