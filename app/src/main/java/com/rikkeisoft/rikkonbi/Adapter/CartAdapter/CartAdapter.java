package com.rikkeisoft.rikkonbi.Adapter.CartAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.OrderModel.Orders;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ProductModel.Product;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.CartSqlite;
import com.rikkeisoft.rikkonbi.UI.Cart.CartListItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> implements
        MainActivityPresenter.View.ConnectedWifi{

    private CartListItem mCartScreen;
    private List<String> mCartItems;
    private final String TAG = "CardAdapter";
    int total = 0;
    private boolean mCheck;
    private PresenterCheckWiFi mCheckWiFi;

    public CartAdapter(CartListItem mCartScreen, List<String> mCartItems){
        this.mCartScreen = mCartScreen;
        this.mCartItems = mCartItems;

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cart,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        checkConnect();
        Gson gson = new Gson();
        final Product product = gson.fromJson(mCartItems.get(position),Product.class);
           int id = product.getId();
           String name = product.getName();
           String image = product.getImageUrl();
           int price = product.getPrice();
           int quantity = product.getQuantity();
           int maxQuantity = product.getMaxOrderQuantity();

           Log.d(TAG,String.valueOf(maxQuantity));

            Picasso.get()
                    .load(image)
                    .into(holder.img_cart_product);

            holder.title_cart_product.setText(name);
            holder.price_cart_prodcut.setText(String.valueOf(price));
            holder.cart_edt_num.setText(String.valueOf(quantity));

            holder.btn_del_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkConnect();
                        if(mCheck) {
                            int k = 0;
                            if (mCartItems.size() <= 1) {
                                mCartScreen.OnClickItem(-5);
                            }
                            else{
                                mCartScreen.OnClickItem(id);
                            }

                            total = 0;
                            mCartScreen.OnClickItem(1);

                        }else{
                            mCartScreen.OnClickItem(404);

                        }
                    }
                });
                holder.cart_btn_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       checkConnect();
                       if(mCheck) {
                           int total_show = Integer.parseInt(holder.cart_edt_num.getText().toString());
                           if (total_show < maxQuantity) {
                               int total_plus = total_show;
                               total += price;
                               total_plus += 1;
                               holder.cart_edt_num.setText(String.valueOf(total_plus));
                               mCartScreen.checkQuantity(id+"_"+total_plus+"_"+maxQuantity);
                           }
                           mCartScreen.showMessage("total_" + String.valueOf(total));
                       }
                       else{
                           mCartScreen.OnClickItem(404);

                       }
                    }
                });

                holder.cart_btn_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkConnect();
                        if(mCheck) {
                            int total_show = Integer.parseInt(holder.cart_edt_num.getText().toString());
                            if (total_show > 1) {
                                int total_min = total_show;
                                total -= price;
                                total_min -= 1;
                                holder.cart_edt_num.setText(String.valueOf(total_min));
                                mCartScreen.checkQuantity(id+"_"+total_min+"_"+maxQuantity);
                            }
                            mCartScreen.showMessage("total_" + String.valueOf(total));
                        }
                        else{
                            mCartScreen.OnClickItem(404);

                        }

                    }
                });

            if(mCheck) {
                total += price * quantity;
                mCartScreen.showMessage("total_" + String.valueOf(total));
            }


    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }


    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }
    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            mCheck = false;
        }
        else{
            mCheck = true;
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_cart_product;
        TextView title_cart_product;
        TextView price_cart_prodcut;
        ImageButton cart_btn_minus;
        EditText cart_edt_num;
        ImageButton cart_btn_plus;
        ImageButton btn_del_cart;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            img_cart_product = (ImageView) view.findViewById(R.id.cart_product_img);
            title_cart_product = (TextView) view.findViewById(R.id.cart_product_title);
            price_cart_prodcut = (TextView) view.findViewById(R.id.cart_product_price);
            cart_btn_minus = (ImageButton) view.findViewById(R.id.btn_minus);
            cart_edt_num = (EditText) view.findViewById(R.id.edit_number);
            cart_btn_plus = (ImageButton) view.findViewById(R.id.btn_plus);
            btn_del_cart = (ImageButton) view.findViewById(R.id.delete_cart_list);
        }
    }
}
