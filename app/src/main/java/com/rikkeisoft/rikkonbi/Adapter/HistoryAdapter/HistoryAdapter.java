package com.rikkeisoft.rikkonbi.Adapter.HistoryAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.HistoryModel.HistoryItem;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.History.History;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private History mHistory;
    private List<String> mHistoryList;


    public HistoryAdapter(History mHistory, List<String> mHistoryList){
        this.mHistory = mHistory;
        this.mHistoryList = mHistoryList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Gson gson = new Gson();
        final HistoryItem historyItem = gson.fromJson(mHistoryList.get(position),HistoryItem.class);
        String name = historyItem.getItems().get(0).getProductName();
        int price = historyItem.getItems().get(0).getPrice();
        int quantity = historyItem.getItems().get(0).getQuantity();
        String date = historyItem.getOrderDate();
        String detail = historyItem.getPaymentStatusName();
        int statusPay = historyItem.getPaymentStatusId();

        String price_product = mHistory.getResources().getString(R.string.total_amount)+" "+
                quantity +" x "+
                price +
                mHistory.getResources().getString(R.string.currency);

        String date_product = mHistory.getResources().getString(R.string.Order)+" "+DateTimeFormat(date);

        String detail_product = mHistory.getResources().getString(R.string.Status)+" "+detail;

        String status_product = mHistory.getResources().getString(R.string.Status)+" "+
                mHistory.getResources().getString(R.string.not_pay)+" ";

        holder.name_product.setText(name);
        holder.price_product.setText(price_product);

        holder.date_product.setText(date_product);
        if(statusPay == 2){
            holder.ic_del.
                    setImageDrawable(mHistory.getResources().getDrawable(R.drawable.ic_delete_forever_black_24dp));
            holder.ic_del.setEnabled(true);
            holder.detail_product.setText(detail_product);
            holder.detail_not_paid.setText("");
        }
        else if(statusPay == 1){
            holder.ic_del
                    .setImageDrawable(mHistory.getResources().getDrawable(R.drawable.ic_delete_forever_2_24dp));
            holder.ic_del.setEnabled(false);
            holder.detail_product
                    .setText(status_product);
            holder.detail_not_paid.setTextColor(mHistory.getResources().getColor(R.color.orange_txt));
            holder.detail_not_paid.setText(detail);
        }

        holder.ic_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String positionItem = "0_" + historyItem.getId() + "_" + position;
                    mHistory.OnClickItem(positionItem);


            }
        });
    }

    @Override
    public int getItemCount(){
        return mHistoryList.size();
    }

    public String DateTimeFormat(String time){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        SimpleDateFormat output = new SimpleDateFormat("HH:mm dd/MM/yyyy",Locale.getDefault());

        Date d = null;
        try
        {
            d = input.parse(time);
        }
        catch (ParseException e)
        {
            //Check if SimpleDateFormat is wrong
            e.printStackTrace();
        }
        String formatted = output.format(d);
        return formatted;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        View view;
        ImageButton ic_del;
        TextView name_product,
                price_product,
                date_product,
                detail_product,
                detail_not_paid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            ic_del = (ImageButton) view.findViewById(R.id.delete_history);
            name_product = (TextView) view.findViewById(R.id.name_product_his);
            price_product = (TextView) view.findViewById(R.id.price_product_his);
            date_product = (TextView) view.findViewById(R.id.date_product_his);
            detail_product = (TextView) view.findViewById(R.id.detail_product_his);
            detail_not_paid = (TextView) view.findViewById(R.id.detail_not_pay);
        }
    }
}
