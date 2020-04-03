package com.rikkeisoft.rikkonbi.Service;

import com.google.firebase.messaging.FirebaseMessagingService;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MessagingServiceFCM extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}
   // private static final String TAG = "MessagingServiceFCM";

    /**
     * Schedule async work using WorkManager.
     */
    /*private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }*/
    /*@Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
          /*  if ( true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
            }*/

           /* Log.d(TAG, "Message Notification Body: " + remoteMessage.getData());
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            showNotification(body, title);
        }
        // Check if message contains a data payload.

            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getData());
                String title = remoteMessage.getNotification().getTitle();
                String body = remoteMessage.getNotification().getBody();

                showNotification(body, title);


            }
                   /* LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
                    Map<String, String> data = remoteMessage.getData();
                    Bundle msg = new Bundle();
                    for (String key : data.keySet()) {
                        Log.e(key, data.get(key));
                        msg.putString(key, data.get(key));
                    }

                    Intent intent = new Intent("com.google.android.c2dm.intent.RECEIVE");
                    intent.setAction("com.google.android.c2dm.intent.RECEIVE");
                    intent.putExtra("hashmap", msg);

                    broadcaster.sendBroadcast(intent);
*/

                /*} catch (Exception e) {
                    Log.e("error" + "receiver", e.toString());
                }*/

               /* Intent intent = new Intent("com.google.android.c2dm.intent.RECEIVE");
                intent.putExtra("title", title);
                intent.putExtra("body", body);
                //intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);*/

           // }
    /* }

     private void showNotification(String messageBody, String title){
         int notificationId = new Random().nextInt();
         Intent intent = new Intent(this, MainPage.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                 PendingIntent.FLAG_ONE_SHOT);

         String channelId = getString(R.string.app_name);
         Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         NotificationCompat.Builder notificationBuilder =
                 new NotificationCompat.Builder(this, channelId)
                         .setSmallIcon(R.drawable.ic_stat_ic_notification)
                         .setContentTitle(title)
                         .setContentText(messageBody)
                         .setAutoCancel(true)
                         .setSound(defaultSoundUri)
                         .setContentIntent(pendingIntent)
                         .setPriority(NotificationCompat.PRIORITY_HIGH);

         NotificationManager notificationManager =
                 (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

         // Since android Oreo notification channel is needed.
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             NotificationChannel channel = new NotificationChannel(channelId,
                     "No",
                     NotificationManager.IMPORTANCE_HIGH);
             channel.setDescription("Yo");
             notificationManager.createNotificationChannel(channel);
         }

         notificationManager.notify(notificationId, notificationBuilder.build());
       }

}*/
