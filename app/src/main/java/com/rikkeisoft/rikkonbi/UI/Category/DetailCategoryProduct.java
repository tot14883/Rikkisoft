package com.rikkeisoft.rikkonbi.UI.Category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCategory.PresenterCategoryDetail;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.SQLite.myDBHelper;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.UI.Cart.CartScreen;
import com.rikkeisoft.rikkonbi.UI.QRcode.CameraQRCode;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class DetailCategoryProduct extends AppCompatActivity implements MainActivityPresenter.View.CategoryDetailView,
        View.OnClickListener,
        MainActivityPresenter.View.ConnectedWifi{
    private ImageButton mBtnBack;
    private static final String PRODUCT_ID = "product";
    private int mPrimaryKeyProduct;
    private PresenterCategoryDetail mUserPresenterCompl;
    private ImageView mImgProductDetail;
    private ImageButton mBtnMinusProduct,mBtnPlusProduct;
    private EditText mEdtNumProduct;
    private FloatingActionButton mFabQrCode;
    private TextView mTxtCartDetail,mTxtCheckCart;
    private Button mBtnAddC,mBtnRA;
    private TextView mTxtNumProductDetail;
    private TextView mTxtPriceProductDetail;
    private String mProduct;
    private int mMaxQuan;
    private String mUserToken;
    private CartSqlite helper;
    private TextView textCartItemCount;
    long mCartItemCount = 0;
    private ImageView mImageCart;

    private static final String PAGE_ID = "Page_Key";
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private PresenterCheckWiFi mCheckWiFi;
    private boolean mShowConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category_product);
        getSupportActionBar().hide();

        mPrimaryKeyProduct = getIntent().getExtras().getInt(PRODUCT_ID);

        helper = new CartSqlite(this);

        //String account = PreferencesProvider.getInstance(getApplicationContext()).getCommitLogin(getApplicationContext());

        mUserPresenterCompl = new PresenterCategoryDetail(this);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();


        init();

        OnClickItem(0);

        updateUI();

    }


    public void init(){
        mImgProductDetail = (ImageView) findViewById(R.id.img_product_detail);
        mEdtNumProduct = (EditText) findViewById(R.id.edit_number);
        mEdtNumProduct.setText("0");
        mBtnMinusProduct = (ImageButton) findViewById(R.id.btn_minus);
        mBtnMinusProduct.setOnClickListener(this);
        mBtnPlusProduct = (ImageButton) findViewById(R.id.btn_plus);
        mBtnPlusProduct.setOnClickListener(this);
        mFabQrCode = (FloatingActionButton) findViewById(R.id.fab_qecode_nav);
        mFabQrCode.setOnClickListener(this);
        mTxtCheckCart = (TextView) findViewById(R.id.text_check_cart);
        mTxtCheckCart.setOnClickListener(this);
        mTxtCartDetail = (TextView) findViewById(R.id.text_cart_detail);
        mTxtNumProductDetail = (TextView) findViewById(R.id.name_product_detail);
        mBtnAddC = (Button) findViewById(R.id.btn_add_cart);
        mBtnAddC.setOnClickListener(this);
        mBtnRA = (Button) findViewById(R.id.btn_buy_right_away);
        mBtnRA.setOnClickListener(this);
        mTxtPriceProductDetail = (TextView) findViewById(R.id.price_product_detail);
        mBtnBack = (ImageButton) findViewById(R.id.btn_back_page);
        mBtnBack.setOnClickListener(this);
        textCartItemCount = (TextView) findViewById(R.id.cart_badge);
        textCartItemCount.setOnClickListener(this);
        mImageCart = (ImageView) findViewById(R.id.action_btn_cart);
        mImageCart.setOnClickListener(this);
    }

    private void setupBadge() {
         mCartItemCount =  helper.getProfilesCount();
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else if(mCartItemCount > 99){
                textCartItemCount.setText("99+");
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void showProgress(String message) {
        if(message.equals("Delete_Fail")){
            Toast.makeText(this,getResources().getString(R.string.Delete_fail),Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("Over")){
            //   Toast.makeText(this,getResources().getString(R.string.More_than_max),Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("Min")){
            //  Toast.makeText(this,getResources().getString(R.string.More_than_min),Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("Success")){
            mTxtCartDetail.setVisibility(View.VISIBLE);
            mTxtCheckCart.setVisibility(View.VISIBLE);
            Toast.makeText(this,R.string.completely,Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("Fail")){
            Toast.makeText(this,R.string.unable_2_cart,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(String message) {
        this.mProduct = message;
        Gson gson = new Gson();
        Product product = gson.fromJson(message,Product.class);
        /*String checkExist = PreferencesProvider.getInstanceProduct(this,String.valueOf(mPrimaryKeyProduct))
                .getProductNum(this,String.valueOf(mPrimaryKeyProduct));*/
       /*if(checkExist.length() <= 2) {
            mEdtNumProduct.setText(checkExist);
        }
        else{*/
        mEdtNumProduct.setText("1");
      //  }
        mTxtNumProductDetail.setText(product.getName());
        mTxtPriceProductDetail.setText(product.getPrice()+" ₫");
        Picasso.get()
                .load(product.getImageUrl())
                .into(mImgProductDetail);
        mMaxQuan = product.getMaxOrderQuantity();
    }
    public void updateUI(){
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(this).getTokenBearer(this));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");
            this.mUserToken = getResources().getString(R.string.bearer)+token;
            mUserPresenterCompl.ProductDetailBackground(mUserToken,mPrimaryKeyProduct);
            setupBadge();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnClickItem(int position) {
        if(position != 1){
            mTxtCartDetail.setVisibility(View.INVISIBLE);
            mTxtCheckCart.setVisibility(View.INVISIBLE);
        }
        if(position == 1){
            Gson gson = new Gson();
            Product product = gson.fromJson(this.mProduct,Product.class);

            long id = helper.InsertData(product.getId(),product.getName(),
                    product.getImageUrl(),product.getPrice()
                    ,Integer.parseInt(mEdtNumProduct.getText().toString())
                            ,product.getMaxOrderQuantity());

            if (id <= 0) {
                helper.updataData(String.valueOf(product.getId()),product.getName(),
                        product.getImageUrl(),product.getPrice()
                        ,Integer.parseInt(mEdtNumProduct.getText().toString())
                        ,product.getMaxOrderQuantity());

            }

            setupBadge();




            String mNumber = mEdtNumProduct.getText().toString();
            setNumProduct(mNumber);

            mTxtCartDetail.setVisibility(View.VISIBLE);
            mTxtCheckCart.setVisibility(View.VISIBLE);
        }
        if(position == 3){
            Gson gson = new Gson();
            Product product = gson.fromJson(this.mProduct,Product.class);


            long id = helper.InsertData(product.getId(),product.getName(),
                    product.getImageUrl(),product.getPrice()
                    ,Integer.parseInt(mEdtNumProduct.getText().toString())
                    ,product.getMaxOrderQuantity());

            if (id <= 0) {
                helper.updataData(String.valueOf(product.getId()),product.getName(),
                        product.getImageUrl(),product.getPrice()
                        ,Integer.parseInt(mEdtNumProduct.getText().toString())
                        ,product.getMaxOrderQuantity());
                //Log.d(TAG, "Insertion Unsuccessful");
            } else {
                //Log.d(TAG, "Insertion Successful");
            }
            setupBadge();


            String mNumber = mEdtNumProduct.getText().toString();
            setNumProduct(mNumber);

            Intent intent = new Intent(DetailCategoryProduct.this,CartScreen.class);
            intent.putExtra(PAGE_ID,"Category");
            startActivity(intent);
            finish();

            // Toast.makeText(this,R.string.add_complete,Toast.LENGTH_SHORT);
        }
    }


    public void ChooseClick(int position){
        if(mEdtNumProduct.getText().toString().equals("0")){
            showMessage("Problem");
        }
        else{
            OnClickItem(position);
        }
    }

    public void ChooseQuantity(String command){
        int sum_num;
        if(mEdtNumProduct.getText().equals("0")){
            showMessage("Problem");
        }
        else  if(command.equals("1")){
            sum_num = (Integer.parseInt(mEdtNumProduct.getText().toString())+1);
            if(sum_num > mMaxQuan){
                showProgress("Over");
            }
            else{
                mEdtNumProduct.setText(String.valueOf(sum_num));
            }
        }
        else if(command.equals("2")){
            sum_num = (Integer.parseInt(mEdtNumProduct.getText().toString())-1);
            if(sum_num <= 0){
                showProgress("Min");
            }
            else{
                mEdtNumProduct.setText(String.valueOf(sum_num));
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back_page:
                StartActivity();
                break;
            case R.id.btn_add_cart:
                checkBeforeChange(1);
                break;
            case R.id.btn_buy_right_away:
                checkBeforeChange(3);
                break;
            case R.id.text_check_cart:
                Intent intent = new Intent(DetailCategoryProduct.this,CartScreen.class);
                intent.putExtra(PAGE_ID,"Category");
                startActivity(intent);
                finish();
                break;
            case R.id.btn_minus:
                ChooseQuantity("2");
                break;
            case R.id.btn_plus:
                ChooseQuantity("1");
                break;
            case R.id.fab_qecode_nav:
                StarQRcode();
                break;
            case R.id.action_btn_cart:
                startCart();
                break;
            case R.id.cart_badge:
                startCart();
                break;
        }
    }

    public void startCart(){
        Intent intent = new Intent(DetailCategoryProduct.this,CartScreen.class);
        intent.putExtra(PAGE_ID,"Category");
        startActivity(intent);
        finish();
    }
    public void checkBeforeChange(int check){
        mCheckWiFi.CheckWiFiBackground();
        if(mShowConnect){
            ChooseClick(check);
        }
        else{
            Toast.makeText(DetailCategoryProduct.this,getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
    }

    public void StartActivity(){
        Intent intent = new Intent(DetailCategoryProduct.this,MainPage.class);
        intent.putExtra(PAGE_ID,"Category");
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        StartActivity();
    }
    public void StarQRcode(){
        if (ActivityCompat.checkSelfPermission(DetailCategoryProduct.this
                , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(DetailCategoryProduct.this,CameraQRCode.class);
            intent.putExtra(PAGE_ID,"Category");
            startActivity(intent);
            finish();
        } else {
            ActivityCompat.requestPermissions(DetailCategoryProduct.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(permissions[0].equals(Manifest.permission.CAMERA)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(DetailCategoryProduct.this,CameraQRCode.class);
                intent.putExtra(PAGE_ID,"Category");
                startActivity(intent);
                finish();
            }
        }
    }

    public void setNumProduct(String keyValue){
        PreferencesProvider.getInstanceProduct(this,String.valueOf(mPrimaryKeyProduct)).addProductNum(this,keyValue,String.valueOf(mPrimaryKeyProduct));
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if(isConnected){
            mShowConnect = true;
        }
        else{
            mShowConnect = false;
        }
    }
}
