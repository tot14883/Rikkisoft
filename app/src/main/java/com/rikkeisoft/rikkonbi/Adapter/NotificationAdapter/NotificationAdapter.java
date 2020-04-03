package com.rikkeisoft.rikkonbi.Adapter.NotificationAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;
import com.rikkeisoft.rikkonbi.UI.Notification.NotificationFragment;
import com.rikkeisoft.rikkonbi.UI.Notification.SettingNotification.SettingNotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private NotificationFragment mNotificationFragment;
    private List<String> mNotiList;
    private final String PAGE_ID = "Notification_Key";
    private LayoutInflater layoutInflater;

    public NotificationAdapter(NotificationFragment mNotificationFragment, List<String> mNotiList){
        this.mNotiList = mNotiList;
        this.mNotificationFragment = mNotificationFragment;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notification,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Gson gson = new Gson();
        final Notification NotificationItem = gson.fromJson(mNotiList.get(position),Notification.class);
        int id = NotificationItem.getId();
        String Topic = NotificationItem.getTitle();
        String date =  NotificationItem.getCreatedOn();
        String detail = NotificationItem.getDescription();
        boolean isRead =  NotificationItem.isRead();

        if(isRead){

            holder.mTopic.setTypeface(null, Typeface.NORMAL);
            holder.mTopic.setTextColor(Color.GRAY);
        }
        else{
            holder.mTopic.setTypeface(null, Typeface.BOLD);
            holder.mTopic.setTextColor(Color.BLACK);
        }
        holder.mTopic.setText(Topic);
        holder.mDate.setText(DateTimeFormat(date));
        holder.mDetail.setText(detail);
        holder.mOption_Noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* SettingNotification settingNotification = new SettingNotification();
                Bundle args = new Bundle();
                //รอแก้
                args.putString(PAGE_ID,String.valueOf(id));

                settingNotification.setArguments(args);
                settingNotification.setCancelable(false);
                settingNotification.show(mNotificationFragment.getFragmentManager(),"settingNotification");*/
                mNotificationFragment.setVisibelView(true);
                mNotificationFragment.mID(String.valueOf(id));


            }
        });
      /*  } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public int getItemCount() {
        return  mNotiList.size();
    }


    public String DateTimeFormat(String time){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());

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
        TextView mTopic;
        TextView mDate;
        TextView  mDetail;
        ImageButton mOption_Noti;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            mTopic = view.findViewById(R.id.topic_message);
            mDate = view.findViewById(R.id.date_message);
            mDetail = view.findViewById(R.id.detail_message);
            mOption_Noti = view.findViewById(R.id.option_notification);
        }
    }
}
