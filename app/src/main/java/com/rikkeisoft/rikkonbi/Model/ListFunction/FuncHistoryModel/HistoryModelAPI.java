package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncHistoryModel;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.HistoryModel.HistoryItem;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryModelAPI implements MainActivityPresenter.Model.GetOrder {
    private final String TAG = "HistoryModel";
    private List<HistoryItem> mHistoryItemsArrayList;
    private APIInterface mApiInterface;
    @Override
    public void getInfoOrder(final OnSuccessGetOrder onSuccessGetOrder, String tokenUser, final String userId) {

           mApiInterface = ApiUtils.getTokenAccess();
           new delHistory(onSuccessGetOrder,tokenUser,userId).execute();

    }
    public class delHistory extends AsyncTask<String,String ,String > {
        private String tokenUser;

        private  String userId;
        private OnSuccessGetOrder onSuccessGetOrder;
        public delHistory(OnSuccessGetOrder onSuccessGetOrder, String tokenUser, String userId){
            this.onSuccessGetOrder = onSuccessGetOrder;
            this.tokenUser = tokenUser;
            this.userId = userId;
        }
        @Override
        protected String doInBackground(String... strings) {
            mHistoryItemsArrayList =new ArrayList<>();
            mHistoryItemsArrayList.clear();
            Call<List<HistoryItem>> call =mApiInterface.getOrderItem("application/json", tokenUser);
            call.enqueue(new Callback<List<HistoryItem>>() {
                @Override
                public void onResponse(Call<List<HistoryItem>> call, Response<List<HistoryItem>> response) {
                    if(response.isSuccessful()){
                        mHistoryItemsArrayList = response.body();

                        Gson gson = new Gson();
                        List<String> responseData = new ArrayList<String>();
                        responseData.clear();
                        for(int i = 0;i < mHistoryItemsArrayList.size();i++){
                            if(mHistoryItemsArrayList.get(i).getUserId().equals(userId)){
                                if(!mHistoryItemsArrayList.get(i).getItems().get(0).isDeleted()){
                                    responseData.add(gson.toJson(mHistoryItemsArrayList.get(i)));
                                }
                            }
                            else {
                                continue;
                            }
                        }
                        Log.d(TAG,responseData.toString());
                        onSuccessGetOrder.OnSuccessGetOrder(responseData);
                    }
                }

                @Override
                public void onFailure(Call<List<HistoryItem>> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                    onSuccessGetOrder.onFailOrderHistory("Fail");
                }
            });
            return call.toString();
        }
    }
}
