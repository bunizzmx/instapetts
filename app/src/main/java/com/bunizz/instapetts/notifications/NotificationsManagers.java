package com.bunizz.instapetts.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.services.NotificationsService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationsManagers extends FirebaseMessagingService {
    NotificationHelper notificationHelper;
    NotificationBean notificationBean;

    public NotificationsManagers() {

    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("ONCREATE_APP","SERVICE NOTIF");
        notificationHelper = new NotificationHelper(this);
        String title = remoteMessage.getNotification().getTitle();
        String body =  remoteMessage.getNotification().getBody();
        String type_notification = remoteMessage.getData().get("type_notification");
        String url_resource = remoteMessage.getData().get("url_foto");
        String id_usuario = remoteMessage.getData().get("id_usuario");
        String url_extra = remoteMessage.getData().get("url_extra");

        notificationBean = new NotificationBean();
        notificationBean.setBody(body);
        notificationBean.setTitle(title);
        notificationBean.setType_notification( Integer.parseInt(type_notification));
        notificationBean.setId_usuario( Integer.parseInt(id_usuario));
        notificationBean.setUrl_resource(url_resource);
        notificationBean.setUrl_image_extra(url_extra);
        notificationHelper.saveNotification(notificationBean);

        switch (notificationBean.getType_notification()){
            case 0:
                if(App.read(PREFERENCES.PUSH_ME_GUSTAS,true))
                    buildNotification(title,body);
                else
                    Log.e("PUSH_BLOQUEADA","ME GUSTAST");
                break;
        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


    }

    void buildNotification(String title,String body){
        Intent intent;
        intent = new Intent(this, Main.class);
        intent.putExtra("LOGIN_AGAIN",0);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_hand_pet_preload)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[] { 1000, 1000})
                .setContentIntent(pendingIntent);
         mBuilder.setDefaults( Notification.DEFAULT_VIBRATE);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
