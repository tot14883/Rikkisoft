package com.rikkeisoft.rikkonbi.Presenter.PresenterOrder;

import android.content.Context;
import android.util.Log;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncOrderModel.PostOrderModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PresenterOrder implements  MainActivityPresenter.Presenter.PostOrderBackground,
        MainActivityPresenter.Presenter.CartBackground,
        MainActivityPresenter.Model.PostOrder.OnSuccessPostOrder{

    private final String TAG = "PresenterMain";
    private MainActivityPresenter.View.CartView mView;
    private MainActivityPresenter.Model.InfoMember mInfoModel;
    private MainActivityPresenter.Model.PostOrder mPostModel;
    private List<String> len_cart = new ArrayList<>();

    public PresenterOrder (MainActivityPresenter.View.CartView view){
        this.mView = view;
        mInfoModel = new UserModelAPI();
        mPostModel = new PostOrderModelAPI();
    }




    @Override
    public void PreferenceCart(String myCart, Context context) {

    }

    @Override
    public void ProductDetailBackground(String tokenUser, int Id) {

    }


    @Override
    public void onSuccessPostOrder(String status) {
        if(status.equals("Success")){
           mView.showProgress("Success");
        }

    }

    @Override
    public void onFailOrder(int fail,int len) {
               if (len_cart.size() > 0) {
                    int num = Integer.parseInt(len_cart.get(0)) + fail;
                    len_cart.clear();
                    len_cart.add(String.valueOf(num));
                    if (Integer.parseInt(len_cart.get(0)) == len) {
                       onSuccessPostOrder("Success");
                    } else if (fail <= 0) {
                        mView.showMessage("Fail");
                    } //แก้
                } else {
                    len_cart.add(String.valueOf(fail));
                   if (Integer.parseInt(len_cart.get(0)) == len) {
                       onSuccessPostOrder("Success");
                   } else if (fail <= 0) {
                       mView.showMessage("Fail");
                   }
                }


    }

    @Override
    public void PostOrder(List<String> itemProduct, String tokenUser,int len) {
        try {
            mPostModel.postInfoOrder(this,itemProduct,tokenUser,len);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
