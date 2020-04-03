package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncPreferenceModel;

import android.content.Context;

import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PreferenceModel implements MainActivityPresenter.Model.CartPre {
    private final String TAG = "PreferenceModel";
    //Call in PresenterCategory
    @Override
    public void PreferenceCartModel(OnSuccessDataCart onSuccessData, List<String> myCart, Context context) {
        onSuccessData.OnSuccessPreferenceCart(myCart,context);
    }

    @Override
    public void PreferenceCartDuplicateModel(OnSuccessDataCart onSuccessData, String myCart, Context context) {
        onSuccessData.OnSuccessPreferenceCartDuplicate(myCart,context);
    }

}
