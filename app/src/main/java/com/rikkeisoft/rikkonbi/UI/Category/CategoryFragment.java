package com.rikkeisoft.rikkonbi.UI.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCategory.PresenterCategoryItem;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.Adapter.ProductAdapter.ProductAdapter;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.UI.Cart.CartScreen;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements MainActivityPresenter.View.CategoryView,
        MainActivityPresenter.View.ConnectedWifi{

    private final String TAG = "CategoryFragment";
    private PresenterCategoryItem mUserPresenterCompl;
    private List<Product> mProducts;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private ProductAdapter mProductAdapter;
    private static final String PRODUCT_ID = "product";
    private PresenterCheckWiFi mCheckWiFi;
    private boolean mShowConnect;
    private static final String PAGE_ID = "Page_Key";
    private boolean mCheck;
    private TextView textCartItemCount;
    long mCartItemCount = 0;
    private ImageView mImageCart;
    private CartSqlite helper;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);


        //String account = PreferencesProvider.getInstance(getContext()).getCommitLogin(getContext());

        mUserPresenterCompl = new PresenterCategoryItem(this);

        helper = new CartSqlite(getContext());

        mCheckWiFi = new PresenterCheckWiFi(this);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycle_category);
        mProducts = new ArrayList<>();
        mProductAdapter = new ProductAdapter(this,mProducts);

        mLinearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mProductAdapter);

        textCartItemCount = (TextView) root.findViewById(R.id.cart_badge);
        textCartItemCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CartScreen.class);
                intent.putExtra(PAGE_ID,"Category");
                startActivity(intent);
            }
        });
        mImageCart = (ImageView) root.findViewById(R.id.action_btn_cart);
        mImageCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CartScreen.class);
                intent.putExtra(PAGE_ID,"Category");
                startActivity(intent);
            }
        });

         updateUI();
        ((MainPage) getActivity()).cPage("Category");



        return root;
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

    public void updateUI(){
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(getContext()).getTokenBearer(getContext()));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");

            mUserPresenterCompl.ProductBackground(getResources().getString(R.string.bearer)+token);
            mCheckWiFi.CheckWiFiBackground();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupBadge();

    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(mCheckWiFi);
    }

    @Override
    public void OnClickItem(int position) {
        mCheckWiFi.CheckWiFiBackground();
        if(position == -1){
            return;
        }
        if(mShowConnect == true) {
            Intent detailProduct = new Intent(getActivity(), DetailCategoryProduct.class);
            detailProduct.putExtra(PRODUCT_ID, mProducts.get(position).getId());
            startActivity(detailProduct);
            //getActivity().finish();
        }else{
            showConnectedWifi(false);
        }
    }

    @Override
    public void setDataToRecycleView(List<Product> productArrayList) {
        mProducts.addAll(productArrayList);
        mProductAdapter.notifyDataSetChanged();
    }


    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            CheckInternetFragment checkInternetFragment = new CheckInternetFragment();
            Bundle bundle = new Bundle();
            bundle.putString("TAG", TAG);
            checkInternetFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, checkInternetFragment, null)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                ((MainPage) getActivity()).setTabBottom("false");

            mShowConnect = false;
        }
        else{
           /* getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, this, null)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();*/
            ((MainPage) getActivity()).setTabBottom("true");
            mShowConnect = true;
        }
    }





}
