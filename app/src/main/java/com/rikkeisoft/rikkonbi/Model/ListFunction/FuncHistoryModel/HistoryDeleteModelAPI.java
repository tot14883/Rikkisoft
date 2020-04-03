package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncHistoryModel;

import android.os.AsyncTask;
import android.util.Log;

import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryDeleteModelAPI implements MainActivityPresenter.Model.deleteHistory {
    private final String TAG = "HistorDeleteyModel";
    private APIInterface mApiInterface;
    @Override
    public void deleteHistory(final OnSuccessDeleteOrder onSuccessDeleteOrder, final String tokenUser, final int id, final String userId) {
        mApiInterface = ApiUtils.getTokenAccess();
        new delHistory(onSuccessDeleteOrder,tokenUser,id,userId).execute();
    }

    public class delHistory extends AsyncTask<String,String ,String > {
        private String tokenUser;
        private int id;
        private  String userId;
        private OnSuccessDeleteOrder onSuccessDeleteOrder;
        public delHistory(OnSuccessDeleteOrder onSuccessDeleteOrder, String tokenUser,int id, String userId){
            this.onSuccessDeleteOrder = onSuccessDeleteOrder;
            this.tokenUser = tokenUser;
            this.id = id;
            this.userId = userId;
        }
        @Override
        protected String doInBackground(String... strings) {
            Call<Void> call = mApiInterface.postDeleteItem("application/json",tokenUser,id);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        onSuccessDeleteOrder.OnSuccessDelete("Delete_Success",tokenUser,userId);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                    onSuccessDeleteOrder.OnFailOrderDelete("Delete_Fail",tokenUser,userId,id);
                }
            });
            return call.toString();
        }
    }
}
