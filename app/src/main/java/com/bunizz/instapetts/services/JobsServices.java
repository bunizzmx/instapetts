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
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.beans.NotificationBeanFirestore;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.web.CONST;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class JobsServices {

    private ScheduledExecutorService scheduler;
    private ScheduledExecutorService scheduler_file_backup;
    private static final String TAG = "JobsServices";
    Context context;
    FirebaseFirestore db;
    NotificationHelper notificationHelper;
    IdsUsersHelper idsUsersHelper;
    ArrayList<String> ids = new ArrayList<>();
    int index_sync =0;
    MyStoryHelper myStoryHelper;
    StorageMetadata metadata;
    private StorageReference storageReference;
    ArrayList<Integer> list_ids = new ArrayList<>();
    ArrayList<IndividualDataPetHistoryBean> histories= new ArrayList<>();
    public JobsServices(Context context) {
        this.context = context;
        this.db = App.getIntanceFirestore();
        notificationHelper = new NotificationHelper(this.context);
        myStoryHelper = new MyStoryHelper(this.context);
        idsUsersHelper = new IdsUsersHelper(this.context);
        storageReference = FirebaseStorage.getInstance(CONST.BUCKET_FILES_BACKUP).getReference();
    }


    public void startNotificationsRequest() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(() -> {
            try {
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
                                   NotificationBeanFirestore notification = document.toObject(NotificationBeanFirestore.class);
                                   NotificationBean NOTIFICACION = new NotificationBean(
                                           getTitleForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE()),
                                           getBodyForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE()),
                                           notification.getFOTO_REMITENTE(),
                                           notification.getTYPE_NOTIFICATION(),
                                           notification.getID_REMITENTE(),
                                           notification.getURL_EXTRA(),
                                           notification.getFECHA()
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


        scheduler_file_backup = Executors.newScheduledThreadPool(1);
        scheduler_file_backup.scheduleWithFixedDelay(() -> {
            try {
                list_ids = idsUsersHelper.getMyFriendsForPost();
                Log.e("CURRENT_FRIEND","-->:" + list_ids.size() + "/" + App.read(PREFERENCES.CURRENTS_FRIENDS,0));
                if(list_ids.size() == App.read(PREFERENCES.CURRENTS_FRIENDS,0) && list_ids.size() > 0){
                  Log.e("CURRENT_FRIEND","TENGO LOS MISMOS AMIGOS NO HAGO NADA");
                }else{
                    Log.e("CURRENT_FRIEND","YA TENGO MAS AMIGOS ..ACTUALIZO FILE");
                    String fileToWrite = App.read(PREFERENCES.UUID, "INVALID") + ".txt";
                    File file = new File(fileToWrite);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    try {
                        if(list_ids.size()>0) {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileToWrite, Context.MODE_PRIVATE));
                            for (int i = 0; i < list_ids.size(); i++) {
                                if (i==list_ids.size()-1)
                                    outputStreamWriter.write(list_ids.get(i) + "");
                                else
                                    outputStreamWriter.write(list_ids.get(i) + ",");
                            }
                            outputStreamWriter.close();
                            sendFileBackup();
                        }else{

                        }
                    }
                    catch (IOException e) {
                        Log.e("SEND_FILE","error: " + e.getMessage());
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                }
                histories.addAll(myStoryHelper.getMyStories());
                for(int i =0;i<histories.size();i++){
                    int horas = App.horas_restantes_top(histories.get(i).getDate_story());
                    Log.e("DELETE_MY_HISTORY","horas:" + horas);
                    if(horas > 25){
                        Log.e("DELETE_MY_HISTORY","si");
                        myStoryHelper.deleteHistory(histories.get(i).getIdentificador());
                    }
                }


            } catch (Exception ex) {
                Log.e(TAG, "Error en la ejecución de peticion de tramas", ex);
            }
        }, 0, 120, TimeUnit.SECONDS);
    }


   public void  synchronizedNotifications(String id_document){
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
                                Log.e("DATA_NOTIFICATIOON","->" + NOTIFICACION.getId_recurso() + "/" + NOTIFICACION.getId_usuario());
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
            intent.putExtra("ID_RESOURCE",notificationBean.getId_usuario());
            intent.putExtra("TYPE_FRAGMENT", FragmentElement.INSTANCE_PREVIEW_PROFILE);
        }else if(notificationBean.getType_notification() == 1){
            intent.putExtra("ID_RESOURCE",notificationBean.getId_recurso());
            intent.putExtra("TYPE_FRAGMENT", FragmentElement.INSTANCE_COMENTARIOS);
        }else if(notificationBean.getType_notification() == 2){
            intent.putExtra("ID_RESOURCE",notificationBean.getId_usuario());
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


   /*   if(App.read(PREFERENCES.FECHA_BACKUP_FILE,"-").equals("-"))
    {
        Log.e("SEND_FILE","1");
        Main.GenerateFileTask tskFile = new Main.GenerateFileTask();
        tskFile.execute();
    }else{
        if(App.read(PREFERENCES.FECHA_BACKUP_FILE,"-").equals(App.formatDateSimple(new Date()))){
            Log.e("SEND_FILE","2");
        }else{
            Log.e("SEND_FILE","3");
            Main.GenerateFileTask tskFile = new Main.GenerateFileTask();
            tskFile.execute();
        }
    }*/


    public void sendFileBackup() {
        Log.e("SEND_FILE","TRUE");
        UploadTask uploadTask;
        metadata = new StorageMetadata.Builder()
                .setContentType("text/txt")
                .build();
        String filename = App.read(PREFERENCES.UUID,"INVALID") + "_BACKUP.txt";
        final StorageReference reference = storageReference.child(filename);
        uploadTask  = reference.putFile(Uri.fromFile(new File(context.getFilesDir()+"/" + App.read(PREFERENCES.UUID,"INVALID")+".txt")),metadata);
        uploadTask.addOnFailureListener(exception -> {}).addOnSuccessListener(taskSnapshot -> {
        }).addOnProgressListener(taskSnapshot -> {});
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return reference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                list_ids = idsUsersHelper.getMyFriendsForPost();
                App.write(PREFERENCES.CURRENTS_FRIENDS,list_ids.size());
                App.write(PREFERENCES.FECHA_BACKUP_FILE,App.formatDateSimple(new Date()));
            }
        });
    }
}
