package com.bunizz.instapetts.services;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.beans.NotificationBeanFirestore;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class JobsServices {

    private ScheduledExecutorService scheduler;
    private static final String TAG = "JobsServices";
    Context context;
    FirebaseFirestore db;
    NotificationHelper notificationHelper;
    ArrayList<String> ids = new ArrayList<>();
    int index_sync =0;
    MyStoryHelper myStoryHelper;
    public JobsServices(Context context) {
        this.context = context;
        this.db = App.getIntanceFirestore();
        notificationHelper = new NotificationHelper(this.context);
        myStoryHelper = new MyStoryHelper(this.context);
    }


    public void startNotificationsRequest() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                Log.e("SINCRONIZACION","RUN");
               db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document(""+App.read(PREFERENCES.ID_USER_FROM_WEB,0))
                       .collection(FIRESTORE.COLLECTION_NOTIFICATIONS).get()
                       .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                           @Override
                           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                           }
                       })
                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               for (QueryDocumentSnapshot document : task.getResult()) {
                                   Log.e("SINCRONIZACION","encontre");
                                   NotificationBeanFirestore notification = document.toObject(NotificationBeanFirestore.class);
                                   NotificationBean NOTIFICACION = new NotificationBean(
                                           getTitleForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE()),
                                           getBodyForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE()),
                                           notification.getFOTO_REMITENTE(),
                                           notification.getTYPE_NOTIFICATION(),
                                           notification.getID_REMITENTE(),
                                           notification.getURL_EXTRA(),
                                           App.getInstance().fecha_lenguaje_humano(notification.getFECHA())
                                   );
                                   NOTIFICACION.setId_recurso(notification.getID_RECURSO());
                                   NOTIFICACION.setId_document_notification(""+document.getId());
                                   notificationHelper.saveNotification(NOTIFICACION);
                               }
                               ids =  notificationHelper.getNotificationForSincronized();
                               synchronizedNotifications("-");
                       }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                           }
                       });
            } catch (Exception ex) {
                Log.e(TAG, "Error en la ejecución de peticion de tramas", ex);
            }
        }, 0, 60, TimeUnit.SECONDS);
    }


   public void  synchronizedNotifications(String id_document){
       Log.e("SINCRONIZACION","BORRO WESTA:" + id_document);
        if(ids.size() > 0) {
            String ID_DOCUMENT = "";
            if (id_document.equals("-")) {
                ID_DOCUMENT = ids.get(0);
            } else {
                ID_DOCUMENT = ids.get(index_sync);
            }
            db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document("" + App.read(PREFERENCES.ID_USER_FROM_WEB, 0))
                    .collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document(ID_DOCUMENT)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.e("NOTIFICACION_SYN", "->" + ids.get(index_sync));
                            notificationHelper.updateSync(ids.get(index_sync));
                            if(ids.size() -1 < index_sync){
                                index_sync ++ ;
                                Log.e("NOTIFICACION_SYN", "-> HAY MAS" );
                                synchronizedNotifications(ids.get(index_sync));
                            }else{
                                NotificationBean NOTIFICACION ;
                                NOTIFICACION = notificationHelper.getLastNotifications();
                                if(NOTIFICACION!=null)
                                 makeLocalNotification(NOTIFICACION);
                                Log.e("NOTIFICACION_SYN", "-> YA SINCRONICE TODO" );
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("NOTIFICACION_SYN", "1");
                }
            });
        }else{
            Log.e("NOTIFICACION_SYN", "NO HAY MAS QUE SINCRONIZAR");
        }
    }

    String getTitleForNotification(int type_notification,String name){
        String title="";
        switch (type_notification){
            case 0:
                title = context.getResources().getString(R.string.title_follow);
                break;
            case 1:
                title = name  + " a  "  + context.getResources().getString(R.string.title_comment);
                break;
        }

        return title;
    }


    String getBodyForNotification(int type_notification,String name){
        String body="";
        switch (type_notification){
            case 0:
                body =  "A <b>" + name  + "</b>  "  + context.getResources().getString(R.string.body_follow);
                break;
            case 1:
                body = name  + " "  + context.getResources().getString(R.string.body_comment);
                break;
        }
        return body;
    }



    void makeLocalNotification(NotificationBean notificationBean){
        final Bitmap[] bitmap = {null};
        Glide.with(context)
                .asBitmap()
                .load(notificationBean.getUrl_resource())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        bitmap[0] = resource;
                        // TODO Do some work: pass this bitmap
                        buildNotification(notificationBean.getTitle().replace("<b>","").replace("</b>",""),
                                notificationBean.getBody().replace("<b>","").replace("</b>","")
                                ,notificationBean,bitmap[0]);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }



    void buildNotification(String title,String body,NotificationBean notificationBean,Bitmap bitmap){
        Intent intent;
        intent = new Intent(context, Main.class);
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

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
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
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
