package com.rikkeisoft.rikkonbi.UI.OrderSuccess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.History.History;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

public class OrderSuccessScreen extends AppCompatActivity implements View.OnClickListener {
    private static final String PAGE_ID = "Page_Key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_screen);
        getSupportActionBar().hide();

        Button go_history = (Button) findViewById(R.id.btn_history);
        Button btn_home = (Button) findViewById(R.id.btn_home_screen);
        go_history.setOnClickListener(this);
        btn_home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_history:
                GoHistory();
                break;
            case R.id.btn_home_screen:
                HomeScreen();
                break;
        }
    }

    public void GoHistory(){
        Intent intent = new Intent(OrderSuccessScreen.this, History.class);
        intent.putExtra(PAGE_ID,"Home");
        startActivity(intent);
        finish();
    }

    public void HomeScreen(){
        Intent intent = new Intent(OrderSuccessScreen.this, MainPage.class);
        intent.putExtra(PAGE_ID,"Home");
        startActivity(intent);
        finish();
    }
}
