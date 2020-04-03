package com.rikkeisoft.rikkonbi.Api;

import com.rikkeisoft.rikkonbi.Model.ModelAPI.ContactModel.Contact;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.HistoryModel.HistoryItem;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.LoginModel.LoginToken;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.NotificationFCM;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.UnreadNotification;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.isReadNotification;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.OrderModel.Orders;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.AssetImageModel.PiSignage;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.TokenModel.TokenRekkei;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.UserModel.User;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.ItemProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import io.reactivex.Observable;

public interface APIInterface {
    @POST("Auth/login")
    Call<User> getToken2Server(@Header("Content-Type") String content_type,@Body LoginToken token);

    @GET("Products")
    Call<ItemProduct> getProductServer(@Header("Content-Type") String content_type, @Header("Authorization") String bearer); //แก้ตรงนี้


    @GET("PiSignage/assets")
    Call<List<PiSignage>> getAssetPiSignage(@Header("Content-Type") String content_type, @Header("Authorization") String bearer);

    @GET("Products/{id}")
    Call<Product>  getProductDetailServer(@Header("Content-Type") String content_type, @Header("Authorization") String bearer, @Path("id") int id);

    @POST("Orders")
    Call<Void>  postOrderItems(@Header("Content-Type") String content_type,@Header("Authorization") String bearer, @Body Orders orders);

    @GET("Orders")
    Call<List<HistoryItem>> getOrderItem(@Header("Content-Type") String content_type, @Header("Authorization") String bearer);

    @DELETE("Orders/{id}")
    Call<Void> postDeleteItem(@Header("Content-Type") String content_type,@Header("Authorization") String bearer,@Path("id") int id);

    @GET("Wallets/balances")
    Call<String> getMyOwnWallets(@Header("Content-Type") String content_type,@Header("Authorization") String bearer);

    /*@GET("/api/Notifications/List")
    Observable<List<Notification>> getNotification(@Header("Content-Type") String content_type, @Header("Authorization") String bearer);
*/

    @GET("/api/Notifications/List/{pageNum}")
    Call<List<Notification>> getNotification(@Header("Content-Type") String content_type, @Header("Authorization") String bearer, @Path("pageNum") int pageNum);

    @DELETE("/api/Notifications/{Id}")
    Call<Void> postDeleteNotification(@Header("Content-Type") String content_type,@Header("Authorization") String bearer,@Path("Id") int id);

    @PUT("/api/Notifications")
    Call<Void> postReadNotification(@Header("Content-Type") String content_type,@Header("Authorization") String bearer,@Body isReadNotification isReadNotification);

    @GET("/api/Notifications/UnReadNotification")
    Observable<UnreadNotification> getTotalNotification(@Header("Content-Type") String content_type, @Header("Authorization") String bearer);

    @POST("/api/Notifications")
    Observable<Void> postFCMNotification(@Header("Content-Type") String content_type, @Header("Authorization") String bearer, @Body NotificationFCM notificationFCM);

    @POST("/api/Tokens")
    Call<Void> postTokenRekkei(@Header("Content-Type") String content_type, @Header("Authorization") String bearer, @Body TokenRekkei tokenRekkei);

    @DELETE("/api/Tokens/{token}")
    Call<Void> delTokenRekkei(@Header("Content-Type") String content_type, @Header("Authorization") String bearer, @Path("token") String token);

    @GET("/api/Contact")
    Call<Contact> getContact(@Header("Content-Type") String content_type, @Header("Authorization") String bearer);
}



