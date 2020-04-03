package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncOrderModel;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.OrderModel.Orders;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostOrderModelAPI implements MainActivityPresenter.Model.PostOrder {
    private APIInterface mApiInterface;
    private final String TAG = "PostOrderModelAPI";
    @Override
    public void postInfoOrder(final OnSuccessPostOrder onSuccessData, List<String> orderItems, String tokenUser, final int len){
        mApiInterface = ApiUtils.getTokenAccess();
        for(int i = 0;i < len;i++) {

        ArrayList orderArray = new ArrayList();
        try {

                orderArray.add(dataSend(orderItems.get(i)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Orders orders = new Orders();
        orders.setRegionId(0);
        orders.setmOrderItems(orderArray);
        Call<Void> call = mApiInterface.postOrderItems("application/json",tokenUser,orders);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    onSuccessData.onFailOrder(1,len);
                }
                else{
                    onSuccessData.onFailOrder(-1,len);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG,t.getMessage());
                onSuccessData.onFailOrder(-1,len);
            }
        });
        }
    }

    public JsonObject dataSend(String orderItems) throws JSONException {
        Gson gson = new Gson();

        final Product product = gson.fromJson(orderItems,Product.class);
        int id = product.getId();
        String name = product.getName();
        String image = product.getImageUrl();
        int price = product.getPrice();
        int quantity = product.getQuantity();
        int maxQuantity = product.getMaxOrderQuantity();

        JsonObject jsonObject = new JsonObject();

        if(name != "" && price != 0 && image != "" && quantity != 0) {
            jsonObject.addProperty("productId", id);
            jsonObject.addProperty("productName", name);
            jsonObject.addProperty("price", price);
            jsonObject.addProperty("imageUrl", image);
            jsonObject.addProperty("quantity", quantity);
        }
        return jsonObject;
    }


}
