package com.bunizz.instapetts.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import com.google.android.gms.location.LocationServices;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

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
    Activity context;
    FirebaseFirestore db;
    NotificationHelper notificationHelper;
    IdsUsersHelper idsUsersHelper;
   // ArrayList<String> ids = new ArrayList<>();
    int index_sync =0;
    MyStoryHelper myStoryHelper;
    StorageMetadata metadata;
    private StorageReference storageReference;
    ArrayList<Integer> list_ids = new ArrayList<>();
    ArrayList<IndividualDataPetHistoryBean> histories= new ArrayList<>();
    RxPermissions rxPermissions ;
    private AddressResultReceiver resultReceiver;
    String addressOutput="";
    ArrayList<String> notificaciones = new ArrayList<>();


    public JobsServices(Activity context) {
        this.context = context;
        rxPermissions = new RxPermissions(this.context);
        resultReceiver = new AddressResultReceiver(new Handler());
        this.db = App.getIntanceFirestore();
        notificationHelper = new NotificationHelper(this.context);
        myStoryHelper = new MyStoryHelper(this.context);
        idsUsersHelper = new IdsUsersHelper(this.context);
        storageReference = FirebaseStorage.getInstance(CONST.BUCKET_FILES_BACKUP).getReference();
    }


    public void startNotificationsRequest() {
        notificaciones.clear();
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
                       .addOnCompleteListener(task -> {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               NotificationBeanFirestore notification = document.toObject(NotificationBeanFirestore.class);
                               String boddy = "";
                               if(notification.getTYPE_NOTIFICATION() == 3)
                                   boddy = getBodyForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE(),"");
                               else
                                   boddy = getBodyForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE(),notification.getURL_EXTRA());

                               NotificationBean NOTIFICACION = new NotificationBean(
                                       getTitleForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE()),
                                       boddy,
                                       notification.getFOTO_REMITENTE(),
                                       notification.getTYPE_NOTIFICATION(),
                                       notification.getID_REMITENTE(),
                                       notification.getURL_EXTRA(),
                                       notification.getFECHA()
                               );
                               NOTIFICACION.setId_recurso(notification.getID_RECURSO());
                               NOTIFICACION.setId_document_notification(""+document.getId());
                               notificaciones.add(""+document.getId());
                               notificationHelper.saveNotification(NOTIFICACION);
                           }
                          // ids =  notificationHelper.getNotificationForSincronized();
                            index_sync = notificaciones.size();
                           if(notificaciones.size()>0)
                             synchronizedNotifications(0);
                   })
                       .addOnFailureListener(e -> {

                       });
            } catch (Exception ex) {
                Log.e(TAG, "Error en la ejecución de peticion de tramas", ex);
            }
        }, 0, 60, TimeUnit.SECONDS);


        scheduler_file_backup = Executors.newScheduledThreadPool(1);
        scheduler_file_backup.scheduleWithFixedDelay(() -> {

            if(App.read(PREFERENCES.ADDRESS_USER,"INVALID").equals("INVALID") || App.read(PREFERENCES.LAT,0.0f) == 0.0f )
                obtener_localizacion();


            try {
                list_ids = idsUsersHelper.getMyFriendsForPost();
                if(list_ids.size() == App.read(PREFERENCES.CURRENTS_FRIENDS,0) && list_ids.size() > 0){
                  Log.e("CURRENT_FRIEND","TENGO LOS MISMOS AMIGOS NO HAGO NADA");
                }else{

                    try {
                        if(list_ids.size()>0) {
                            Log.e("CURRENT_FRIEND","YA TENGO MAS AMIGOS ..ACTUALIZO FILE");
                            String fileToWrite = App.read(PREFERENCES.UUID, "INVALID") + ".txt";
                            File file = new File(fileToWrite);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
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


   public void  synchronizedNotifications(int posicion){
        if(posicion == notificaciones.size()) {
            Log.e("NOTIFICACION_SYN", "RETURN");
            notificaciones.clear();
            index_sync = 0;
            if (notificaciones.size() > 0){
                NotificationBean NOTIFICACION;
            NOTIFICACION = notificationHelper.getLastNotifications();
            if (NOTIFICACION != null)
                makeLocalNotification(NOTIFICACION);
              }
            return;
        }
            db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document("" + App.read(PREFERENCES.ID_USER_FROM_WEB, 0))
                    .collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document(notificaciones.get(posicion))
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    notificationHelper.updateSync(notificaciones.get(posicion));
                    if(posicion > 0){
                        index_sync -=1;
                        synchronizedNotifications(posicion +1);
                    }else{
                        NotificationBean NOTIFICACION ;
                        NOTIFICACION = notificationHelper.getLastNotifications();
                        if(NOTIFICACION!=null)
                            makeLocalNotification(NOTIFICACION);
                    }
                }
            });

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
            case 3:
                title = name  + " " + context.getResources().getString(R.string.comented_your_post);
                break;
        }

        return title;
    }


    String getBodyForNotification(int type_notification,String name,String texto_extra){
        String body="";
        switch (type_notification){
            case 0:
                body =  "A <b>" + name  + "</b>  "  + context.getResources().getString(R.string.body_follow);
                break;
            case 1:
                body = name  + " "  + context.getResources().getString(R.string.body_comment);
                break;
            case 3:
                body = "" + texto_extra;
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


    @SuppressLint({"CheckResult", "MissingPermission"})
    public void obtener_localizacion() {
        if (rxPermissions != null) {
            try {
                rxPermissions
                        .request(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribe(granted -> {
                            if (granted) {
                                Log.e("LOCALIZACION", "permitido");
                                LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(location -> {
                                    if (location != null) {
                                        Log.e("LOCALIZACION", "-->" + location.getLatitude() + "/" + location.getLongitude());
                                        if (location.getLatitude() != App.read(PREFERENCES.LAT, 0f)) {
                                            Log.e("LOCALIZACION", "SOSTITUYO VALOR");
                                            App.write(PREFERENCES.LOCATION_CHANGED, true);
                                        }
                                        App.write(PREFERENCES.LAT, (float) location.getLatitude());
                                        App.write(PREFERENCES.LON, (float) location.getLongitude());
                                        startIntentService();
                                    } else {
                                        Log.e("LOCALIZACION", "ES NULA");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("LOCALIZACION", "fallo : " + e.getMessage());
                                            }
                                        });
                            } else {
                                Log.e("LOCALIZACION", "rechazado");
                            }
                        },error->{
                            Log.e("ESXCEPCION", "LOCALIZACION");
                        });
            }catch (Exception e){
                Log.e("ESXCEPCION", "LOCALIZACION");
            }
        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(CONST.RECEIVER, resultReceiver);
        intent.putExtra(CONST.LOCATION_DATA_EXTRA_LON, App.read(PREFERENCES.LON,0f));
        intent.putExtra(CONST.LOCATION_DATA_EXTRA_LAT, App.read(PREFERENCES.LAT,0f));
        context.startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String DOM_CUT="";
            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            addressOutput = resultData.getString(CONST.RESULT_DATA_KEY);
            if (addressOutput == null) {
                addressOutput = "";
            }
            String splits_addres[];
            splits_addres = addressOutput.split(",");
            if(splits_addres.length >=3){
                int CP=0;
                String splits_cp[] = splits_addres[splits_addres.length-3].split(" ");
                if(splits_cp.length >1){
                    try {
                        CP = Integer.parseInt(splits_cp[0]);
                    } catch (NumberFormatException nfe){
                        try {
                            CP = Integer.parseInt(splits_cp[1]);
                        }catch (NumberFormatException ex){
                            CP = 0;
                        }
                    }
                }else{
                    CP =0 ;
                }
                App.write(PREFERENCES.CP,CP);
                DOM_CUT = splits_addres [splits_addres.length-3] + " " + splits_addres[splits_addres.length-2] + " " + splits_addres[splits_addres.length-1] ;
            }else if(splits_addres.length == 2){
                DOM_CUT = splits_addres[splits_addres.length-2] + " " + splits_addres[splits_addres.length-1] ;
            }
            else if(splits_addres.length == 1){
                DOM_CUT = splits_addres[splits_addres.length-1] ;
            }
            Log.e("ADDRES","OUTPUT: " + addressOutput);
            Log.e("ADDRES","CUT: " + DOM_CUT);
            if(!DOM_CUT.equals(R.string.no_address_found))
                App.write(PREFERENCES.ADDRESS_USER,DOM_CUT);
            // Show a toast message if an address was found.
            if (resultCode == CONST.SUCCESS_RESULT) {
                Log.e("ADDRES","ENCONTRADO");
            }

        }
    }


}
