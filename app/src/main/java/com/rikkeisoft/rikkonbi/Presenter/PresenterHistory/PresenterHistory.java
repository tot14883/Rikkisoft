package com.rikkeisoft.rikkonbi.Presenter.PresenterHistory;

import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncHistoryModel.HistoryDeleteModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncHistoryModel.HistoryModelAPI;
import com.rikkeisoft.rikkonbi.Model.ListFunction.FuncUserModel.UserModelAPI;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

public class PresenterHistory implements MainActivityPresenter.Presenter.HistoryBackground,
        MainActivityPresenter.Model.GetOrder.OnSuccessGetOrder,
        MainActivityPresenter.Model.deleteHistory.OnSuccessDeleteOrder {

    private final String TAG = "PresenterHistory";
    private MainActivityPresenter.View.HistoryView mView;
    private MainActivityPresenter.Model.GetOrder mOrderModel;
    private MainActivityPresenter.Model.deleteHistory mDel;
    public PresenterHistory(MainActivityPresenter.View.HistoryView view){
        this.mView = view;
        mOrderModel = new HistoryModelAPI();
        mDel = new HistoryDeleteModelAPI();

    }
    @Override
    public void OnSuccessGetOrder(List<String> historyItems) {
        mView.setDataToHistory(historyItems);
    }

    @Override
    public void onFailOrderHistory(String fail) {
        mView.showMessage("Delete_Fail");
    }


    @Override
    public void HistoryBackground(String tokenUser,String userId) {
        mOrderModel.getInfoOrder(this,tokenUser,userId);
    }

    @Override
    public void HistoryDelete(String tokenUser, int id, String userId,int position) {
        mDel.deleteHistory(this,tokenUser,id,userId);
        //this.position = position;
    }



    @Override
    public void OnSuccessDelete(String status, String tokenUser, String userId) {
        if(status.equals("Delete_Success")) {
            mView.showMessage("Delete");
        }
    }

    @Override
    public void OnFailOrderDelete(String status, String tokenId, String userId, int productId) {
        if(status.equals("Delete_Fail")){
            mView.showMessage("Delete_"+String.valueOf(productId)+"_"+String.valueOf(0));
        }
    }
}
