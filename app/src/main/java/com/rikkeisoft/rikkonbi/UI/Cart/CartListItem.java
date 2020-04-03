package com.rikkeisoft.rikkonbi.UI.Cart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rikkeisoft.rikkonbi.Adapter.CartAdapter.CartAdapter;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCart.PresenterCart;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterOrder.PresenterOrder;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetForCart;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.OrderSuccess.OrderSuccessScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartListItem extends Fragment implements MainActivityPresenter.View.CartView
        ,View.OnClickListener,
        MainActivityPresenter.View.ConnectedWifi {
    private final String TAG = "CartListItem";
    private PresenterCart mUserPresenterCompl;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private CartAdapter mCartAdapter;
    private List<String> mCartsList;
    private EditText mEdtTotal;
    private String mUserId;
    private String mTokenId;
    private int mPosition;
    private List<String> mCartQua;
    private Button mBtnBuy;
    private String[] mCartItemsArray;
    List<String> mMaxQuan = new ArrayList<>();
    List<String> mDataCart = new ArrayList<>();
    private PresenterCheckWiFi mCheckWiFi;
    private boolean mIsCheckInternet;
    private static final String PAGE_ID = "Page_Key";
    private CartSqlite helper;

    private String mAccount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart_list_item, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        mAccount = PreferencesProvider.getInstance(getContext()).getCommitLogin(getContext());
        helper = new CartSqlite(getContext());


        mUserPresenterCompl = new PresenterCart(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_cart);

        mCartsList = new ArrayList<>();
        mCartAdapter = new CartAdapter(this,mCartsList);

        mEdtTotal = (EditText) view.findViewById(R.id.edit_total_amount);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();

        mBtnBuy = (Button) view.findViewById(R.id.btn_buy_r_a);
        mBtnBuy.setOnClickListener(this);

        mLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mCartAdapter);

        updateUI();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_buy_r_a:
                PayProduct();
                break;
        }
    }

    @Override
    public void setDataToRecycleOrder(List<String> carts) {
            ((CartScreen) getActivity()).showMessage(String.valueOf(helper.getProfilesCount()));
            if(mPosition == -1) {
                try {
                    mCartsList.clear();
                    mRecyclerView.removeViewAt(mPosition);
                    mCartAdapter.notifyItemRemoved(mPosition);
                    mCartAdapter.notifyItemRangeChanged(mPosition, mCartsList.size());
                } catch (Exception e) {
                    //Check error when mCartAdapter uses notifyItemRemoved function
                    //Message When it happen error
                    Log.e(TAG, e.getMessage());
                }
            }
           mCartsList.addAll(carts);
           mCartAdapter.notifyDataSetChanged();
           //Log.d(TAG,String.valueOf(carts.get(0)));

    }

    public void updateUI(){
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(getContext()).getTokenBearer(getContext()));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");
            String id = jobject.getString("id");

            this.mUserId = id;
            this.mTokenId = token;
          if(helper.getProfilesCount() > 0) {
              setDataToRecycleOrder(helper.getData());

          }
          else{
              CartEmpty cartEmpty = new CartEmpty();
              getActivity().getSupportFragmentManager().beginTransaction()
                      .replace(R.id.frameLayout_cart, cartEmpty, null)
                      .addToBackStack(null)
                      .commitAllowingStateLoss();
          }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnClickItem(int position) {
        checkConnect();
        if(mIsCheckInternet) {
            if (position == 1) {
                this.mPosition = -1;
                updateUI();
            }
            else if(position == 404){
                Toast.makeText(getContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                ((CartScreen) getActivity()).showConnectedWifi(false);

            }
            else if(position == -5){
                helper.deleteAll();
            }
            else{
                helper.delete(String.valueOf(position));
            }
        }
        else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            ((CartScreen) getActivity()).showConnectedWifi(false);

        }
    }

    @Override
    public void showMessage(String message) {
        if(mIsCheckInternet) {
            String[] strings = message.split("_");
            if (strings[0].equals("Delete")) {
                Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                this.mPosition = Integer.parseInt(strings[2]);

            } else {
                String[] checkMessage = message.split("_");
                if (checkMessage[0].equals("total")) {
                    mEdtTotal.setText(checkMessage[1] + " ₫");
                }
            }
        }else{
            Toast.makeText(getContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress(String message) {
        if(message.equals("Delete_Fail")){
            Toast.makeText(getContext(),getResources().getString(R.string.Delete_fail),Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("Over")){
            //Toast.makeText(getContext(),getResources().getString(R.string.More_than_max),Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("Min")){
            //Toast.makeText(getContext(),getResources().getString(R.string.More_than_min),Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("Fail")){
            ShowDialog();
        }
        else if(message.equals("Success")){
            helper.deleteAll();
            startActivity(new Intent(getActivity(), OrderSuccessScreen.class));
            getActivity().finish();
        }

    }

    @Override
    public void checkQuantity(String cartQuantity) {
         String[] splite = cartQuantity.split("_");
         helper.updataProduct(splite[0],Integer.parseInt(splite[1]),Integer.parseInt(splite[2]));
    }


    public void StartEmptyCart(){

        CartEmpty cartEmpty=new CartEmpty();
        this.getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_cart, cartEmpty, null)
                .addToBackStack(null)
                .commit();
    }

    public void PayProduct(){
        checkConnect();
        if(mIsCheckInternet == true) {

           PresenterOrder postOrder = new PresenterOrder(this);

           postOrder.PostOrder(helper.getData(), getResources().getString(R.string.bearer) + mTokenId, helper.getData().size());


       } else{
            ((CartScreen) getActivity()).showConnectedWifi(false);
        }
    }

    public void ShowDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("Failed")
                .setMessage(R.string.order_failed)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void showConnectedWifi(boolean isConnected) {
        if(isConnected){
            mIsCheckInternet = true;
        }
        else{

            mIsCheckInternet = false;

        }
    }
    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }
}
