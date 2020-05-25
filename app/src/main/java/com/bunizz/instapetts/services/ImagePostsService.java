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
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
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

import androidx.core.app.NotificationCompat;

public class ImagePostsService extends Service {

    private TransferUtility transferUtility;

    public final static String INTENT_KEY_NAME = "key";
    public final static String INTENT_TRANSFER_OPERATION = "transferOperation";
    public final static String TRANSFER_OPERATION_UPLOAD = "upload";
    File file;
    File file_thumbh;
    String name_file_thumbh="";
    private final static String TAG = ImagePostsService.class.getSimpleName();
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
    private StorageReference storageReference_thumbs;
    StorageMetadata metadata;
    Intent intent_broadcast = new Intent();
    @Override
    public void onCreate() {
        super.onCreate();
        storageReference = FirebaseStorage.getInstance(CONST.BUCKET_POSTS).getReference();
        storageReference_thumbs = FirebaseStorage.getInstance(CONST.BUCKET_THUMBS_VIDEO).getReference();
        Util util = new Util();
        transferUtility = util.getTransferUtility(this);
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

        mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setSmallIcon(R.mipmap.ic_splash_foreground)
                    .setContentTitle(TITLE)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }else {
            mBuilder.setSmallIcon(R.mipmap.ic_splash_foreground)
                    .setContentTitle(TITLE)
                    .setAutoCancel(true)
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
        final ArrayList<String> key = intent.getStringArrayListExtra(INTENT_KEY_NAME);
        TransferObserver transferObserver;
        SIZE_OF_FILES = key.size();
        for(int i =0;i<key.size();i++){
            if(TYPE_NOTIFICATION == 2){
                file = new Compressor(this).compressToFile(new File(key.get(i)));
            }else{
                file = new File(key.get(i));
                if(intent.getIntExtra(BUNDLES.POST_TYPE,0) ==1){
                    file_thumbh = new File(intent.getStringExtra(BUNDLES.PHOTO_TUMBH));
                    String splits[] =intent.getStringExtra(BUNDLES.PHOTO_TUMBH).split("/");
                    Log.e("NAME_DEL_TUMBH","--> 1" + intent.getStringExtra(BUNDLES.PHOTO_TUMBH).split("/"));
                    name_file_thumbh = splits[splits.length-1];
                    Log.e("NAME_DEL_TUMBH","--> 2" + name_file_thumbh);
                }
            }

            String splits[] = key.get(i).split("/");
            int index  = splits.length;
            String filename = "";
            if(TYPE_NOTIFICATION == 1){
                filename =App.read(PREFERENCES.UUID,"INVALID") +"/" +  CONST.FOLDER_PROFILE  + "/" + App.read(PREFERENCES.UUID,"INVALID") + ".jpg";
            }else if(TYPE_NOTIFICATION == 0){
                filename = App.read(PREFERENCES.UUID,"INVALID") + "/" +  CONST.FOLDER_POSTS + "/" +  splits[index - 1];
            }else if (TYPE_NOTIFICATION == 3){
                filename = App.read(PREFERENCES.UUID,"INVALID") + "/" +  CONST.FOLDER_PETS + "/" + App.read(PREFERENCES.UUID,"INVALID")  +  NAME_PET + ".jpg" ;
            }else{
                filename =  App.read(PREFERENCES.UUID,"INVALID") + "/" +  CONST.FOLDER_STORIES + "/" +  splits[index - 1];
            }
            if( intent.getIntExtra(BUNDLES.POST_TYPE,0) ==1) {
                upload_video(filename);
                upload_video_image_thumbh(name_file_thumbh);
            }
            else
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
            int progreso = (int) ((int) (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
            mBuilder.setContentText("Progreso " + progreso + "%")
                    .setProgress(100,progreso,false);
            notificationManager.notify(notificationId, mBuilder.build());
        });
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return reference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                intent_broadcast.putExtra("COMPLETED", false);
                intent_broadcast.setAction(Main.POST_SUCCESFULL);
                sendBroadcast(intent_broadcast);
            }
        });
    }

    void upload_video_image_thumbh(String filename){
        UploadTask uploadTask;
        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
        final StorageReference reference = storageReference_thumbs.child(App.read(PREFERENCES.UUID,"INVALID")+"/" + filename);
        uploadTask  = reference.putFile(Uri.fromFile(file_thumbh),metadata);
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
            }
        });
    }

    void upload_video(String filename){
        TransferObserver transferObserver;
        transferObserver = transferUtility.upload(filename, file);
        transferObserver.setTransferListener(new UploadListener());
    }



    public  class UploadListener implements TransferListener {
        private boolean notifyUploadActivityNeeded = true;
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "onError: " + id, e);
            if (notifyUploadActivityNeeded) {
                notifyUploadActivityNeeded = false;
            }
        }
        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

        }
        @Override
        public void onStateChanged(int id, TransferState state) {
            if(state == TransferState.COMPLETED){
                notificationManager.notify(notificationId, mBuilder.build());
                intent_broadcast.putExtra("COMPLETED", false);
                intent_broadcast.setAction(Main.POST_SUCCESFULL);
                sendBroadcast(intent_broadcast);
            }
        }
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
