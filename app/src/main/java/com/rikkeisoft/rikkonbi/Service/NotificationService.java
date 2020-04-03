package com.rikkeisoft.rikkonbi.Service;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.myDBHelper;
import com.rikkeisoft.rikkonbi.UI.Login.LoginPage;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Set;


public class NotificationService extends BroadcastReceiver {
    private final String TAG = "NotificationService";
    private final String PAGE_ID = "Page_Key";
    private myDBHelper helper;

    @Override
    public void onReceive(Context context, Intent intent) {
        helper = new myDBHelper(context);

        Bundle bundle = intent.getExtras();

        context.sendBroadcast(new Intent("NEW_MASSAGE"));


        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                Log.e("Notification Key", key);
                if (bundle.get(key) != null) {
                    Log.e("Notification Value 1", String.valueOf(bundle.get(key)));
                }
            }
        }
        int id = Integer.parseInt(bundle.get("id").toString());
        String title = bundle.get("gcm.notification.title").toString();
        String body = bundle.get("gcm.notification.body").toString();



       // helper.insertData(id, title, "", body, "false", c.getTime().toString());


        sendNotification(context,body,title);

    }

    private void sendNotification(Context context, String body, String title) {

        int notificationId = new Random().nextInt();
        Intent intent = new Intent(context, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(PAGE_ID,"Notification");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = context.getString(R.string.app_name);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, body, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(title);
            //channel.set
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(notificationId , notificationBuilder.build());

    }

}


