package com.bunizz.instapetts.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
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
        notificationBean = new NotificationBean();
        String id_resource="";
        String title = remoteMessage.getNotification().getTitle();
        String body =  remoteMessage.getNotification().getBody();
        String type_notification = remoteMessage.getData().get("type_notification");
        String url_resource = remoteMessage.getData().get("url_foto");
        String id_usuario = remoteMessage.getData().get("id_usuario");
        String url_extra = remoteMessage.getData().get("url_extra");
        if(Integer.parseInt(type_notification) <=2){
           id_resource = remoteMessage.getData().get("id_resource");
           //notificationBean.setId_recurso(Integer.parseInt(id_resource));
        }
        notificationBean.setBody(body);
        notificationBean.setTitle(title);
        notificationBean.setType_notification( Integer.parseInt(type_notification));
        notificationBean.setId_usuario( Integer.parseInt(id_usuario));
        notificationBean.setUrl_resource(url_resource);
        notificationBean.setUrl_image_extra(url_extra);
        notificationHelper.saveNotification(notificationBean);
        if(notificationBean.getId_usuario()!=App.read(PREFERENCES.ID_USER_FROM_WEB,0)) {

            switch (notificationBean.getType_notification()) {
                case 0:
                    if (App.read(PREFERENCES.PUSH_ME_GUSTAS, true))
                        buil_notification_image(title, body, notificationBean);
                    else
                        buil_notification_image(title, body, notificationBean);
                    break;
            }
            buil_notification_image(title, body, notificationBean);
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

    void buil_notification_image(String title,String body,NotificationBean notificationBean){
            final Bitmap[] bitmap = {null};

            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(notificationBean.getUrl_resource())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            bitmap[0] = resource;
                            // TODO Do some work: pass this bitmap
                            buildNotification(title,body,notificationBean,bitmap[0]);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
    }

    void buildNotification(String title,String body,NotificationBean notificationBean,Bitmap bitmap){
        Intent intent;
        intent = new Intent(this, Main.class);
        intent.putExtra("FROM_PUSH",1);
        intent.putExtra("LOGIN_AGAIN",0);
        if(notificationBean.getType_notification() == 0){
            intent.putExtra("ID_RESOURCE",notificationBean.getId_recurso());
            intent.putExtra("TYPE_FRAGMENT", FragmentElement.INSTANCE_PREVIEW_PROFILE);
        }else if(notificationBean.getType_notification() == 1){
            intent.putExtra("ID_RESOURCE",notificationBean.getId_recurso());
            intent.putExtra("TYPE_FRAGMENT", FragmentElement.INSTANCE_COMENTARIOS);
        }else if(notificationBean.getType_notification() == 2){
            intent.putExtra("ID_RESOURCE",notificationBean.getId_recurso());
            intent.putExtra("TYPE_FRAGMENT", FragmentElement.INSTANCE_PREVIEW_PROFILE);
        }
        else if(notificationBean.getType_notification() == 3){
            intent.putExtra("TYPE_FRAGMENT", FragmentElement.INSTANCE_FEED);
        }
        else if(notificationBean.getType_notification() == 4){
            intent.putExtra("TYPE_FRAGMENT", FragmentElement.INSTANCE_TIPS);
        }

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
        @SuppressLint("WrongConstant")
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setBadgeIconType(R.drawable.ic_stat_name)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
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
