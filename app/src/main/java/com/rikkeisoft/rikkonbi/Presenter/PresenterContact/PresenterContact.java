package com.rikkeisoft.rikkonbi.Presenter.PresenterContact;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncContact.ContactModelAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

public class PresenterContact implements MainActivityPresenter.Presenter.ContactBackground,
       MainActivityPresenter.Model.GetContact.OnSuccessGetContact{
    private final String TAG = "PresneterContact";
    private MainActivityPresenter.View.ContactView mView;
    private MainActivityPresenter.Model.GetContact mModel;

    public PresenterContact(MainActivityPresenter.View.ContactView mView){
        this.mView = mView;
        mModel = new ContactModelAPI();
    }

    @Override
    public void OnSuccessContact(String content, String hotline) {
        mView.ShowMessage(content,hotline);
    }

    @Override
    public void OnFailContact(String status) {

    }

    @Override
    public void ContactBackground(String tokenUser) {
          mModel.getContact(this,tokenUser);
    }
}
