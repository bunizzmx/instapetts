package com.bunizz.instapetts.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.utils.compresor.Compressor;
import com.bunizz.instapetts.web.CONST;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;

public class ImageService extends Service {

    private TransferUtility transferUtility;

    public final static String INTENT_KEY_NAME = "key";
    public final static String INTENT_TRANSFER_OPERATION = "transferOperation";
    public final static String TRANSFER_OPERATION_UPLOAD = "upload";
    File file;
    private final static String TAG = ImageService.class.getSimpleName();
    public static  NotificationManager notificationManager;
    public static int TOTAL_LENGTH_ARCHIVES = 0;
    public static int TOTAL_PERCENTAGE= 0;
    int PROGRESS_CURRENT = 0;
    public  static int notificationId = 1;
    public  static  NotificationCompat.Builder mBuilder=null;
    public static int INDEX_OF_COMPLETE=0;
    public static int SIZE_OF_FILES=0;
    public static int TYPE_NOTIFICATION=0;
    public static String TITLE="";
    public static String TITLE_SUCCESS="";
    public static String BODY="";
    String NAME_PET ="";
    private StorageReference storageReference;
    StorageMetadata metadata;
    @Override
    public void onCreate() {
        super.onCreate();
        storageReference = FirebaseStorage.getInstance(CONST.BUCKET_PROFILE).getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TYPE_NOTIFICATION = intent.getIntExtra("NOTIFICATION_TIPE",0);
        if(TYPE_NOTIFICATION == 0) {
            TITLE = "SUBIENDO POST";
            TITLE_SUCCESS = "COMPLETADO";
        }
        else if(TYPE_NOTIFICATION == 1) {
            TITLE_SUCCESS ="PERFIL ACTUALIZADO";
            TITLE = "ACTUALIZANDO PERFIL";
        }
        else if(TYPE_NOTIFICATION == 2) {
            TITLE_SUCCESS ="HISTORIA SUBIDA";
            TITLE = "SUBIENDO HISTORIAS";
        }
        else if(TYPE_NOTIFICATION == 3) {
            TITLE_SUCCESS ="MASCOTA CREADA";
            TITLE = "CONFIGURANDO TU MASCOTA";
            NAME_PET = intent.getStringExtra(BUNDLES.NAME_PET);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setSmallIcon(R.mipmap.ic_splash_foreground)
                    .setContentTitle(TITLE)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }else {
            mBuilder.setSmallIcon(R.mipmap.ic_splash_foreground)
                    .setContentTitle(TITLE)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_splash_foreground));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder
                .setProgress(100,1,false);
        notificationManager.notify(notificationId, mBuilder.build());

        final ArrayList<String> key = intent.getStringArrayListExtra(INTENT_KEY_NAME);
        SIZE_OF_FILES = key.size();
        for(int i =0;i<key.size();i++){
            if(TYPE_NOTIFICATION == 2){
                Log.e("COMPRESION_IMAGEN","HISTORIA :" + key.get(i));
                file = new Compressor(this).compressToFile(new File(key.get(i)));
            }else{
                file = new File(key.get(i));
            }

            String splits[] = key.get(i).split("/");
            int index  = splits.length;
            String filename = "";
            if(TYPE_NOTIFICATION == 1){
                filename =App.read(PREFERENCES.UUID,"INVALID") +"/" +  CONST.FOLDER_PROFILE  + "/" + App.read(PREFERENCES.UUID,"INVALID") + ".jpg";
            }else if(TYPE_NOTIFICATION == 0){
                filename = App.read(PREFERENCES.UUID,"INVALID") + "/" +  CONST.FOLDER_POSTS + "/" +  splits[index - 1];
            }else if (TYPE_NOTIFICATION == 3){
                filename = App.read(PREFERENCES.UUID,"INVALID") + "/" +  CONST.FOLDER_PETS + "/" +  NAME_PET + ".jpg" ;
            }else{
                filename =  App.read(PREFERENCES.UUID,"INVALID") + "/" +  CONST.FOLDER_STORIES + "/" +  splits[index - 1];
            }

            upload_image(filename);
        }


        return START_STICKY;
    }

    void upload_image(String filename){
        UploadTask uploadTask;
        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
        final StorageReference reference = storageReference.child(filename);
        uploadTask  = reference.putFile(Uri.fromFile(file),metadata);
        uploadTask.addOnFailureListener(exception -> {}).addOnSuccessListener(taskSnapshot -> {
        }).addOnProgressListener(taskSnapshot -> {
        });
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return reference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mBuilder.setProgress(100,100,false);
                mBuilder.setContentTitle(TITLE_SUCCESS);
                notificationManager.notify(notificationId, mBuilder.build());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




}
