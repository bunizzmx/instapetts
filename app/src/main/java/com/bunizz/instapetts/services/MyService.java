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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.share_post.ShareActivity;
import com.bunizz.instapetts.utils.compresor.Compressor;
import com.google.android.gms.common.util.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MyService extends Service {

    private TransferUtility transferUtility;

    public final static String INTENT_KEY_NAME = "key";
    public final static String INTENT_TRANSFER_OPERATION = "transferOperation";
    public final static String TRANSFER_OPERATION_UPLOAD = "upload";
    File file;
    private final static String TAG = MyService.class.getSimpleName();
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
    @Override
    public void onCreate() {
        super.onCreate();
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

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle(TITLE)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle(TITLE)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground));
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
        TransferObserver transferObserver;
        SIZE_OF_FILES = key.size();
        for(int i =0;i<key.size();i++){
            if(TYPE_NOTIFICATION == 2){
                Log.e("COMPRESION_IMAGEN","HISTORIA");
                file = new Compressor(this).compressToFile(new File(key.get(i)));
            }else{
                file = new File(key.get(i));
            }

            String splits[] = key.get(i).split("/");
            int index  = splits.length;
            String filename = splits[index -1];
            transferObserver = transferUtility.upload(filename, file);
            transferObserver.setTransferListener(new UploadListener());
            TOTAL_LENGTH_ARCHIVES+= transferObserver.getBytesTotal() *2;
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static class UploadListener implements TransferListener {

        private boolean notifyUploadActivityNeeded = true;

        // Simply updates the list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "onError: " + id, e);
            if (notifyUploadActivityNeeded) {
                ShareActivity.initData();
                notifyUploadActivityNeeded = false;
            }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            TOTAL_PERCENTAGE +=bytesCurrent;
            Log.e("PROGRESO_SUBIDA","-->" + TOTAL_PERCENTAGE + "/"  + TOTAL_LENGTH_ARCHIVES);
            float total = ((float) TOTAL_PERCENTAGE / (float) TOTAL_LENGTH_ARCHIVES) * 100;
            int porcentaje = (int) total;
            mBuilder.setContentText("Progreso " + porcentaje + "%")
                    .setProgress(100,porcentaje,false);
            notificationManager.notify(notificationId, mBuilder.build());
        }

        @Override
        public void onStateChanged(int id, TransferState state) {
            if(state == TransferState.COMPLETED){
                INDEX_OF_COMPLETE ++;
                if(INDEX_OF_COMPLETE == SIZE_OF_FILES){
                    mBuilder.setProgress(100,100,false);
                    mBuilder.setContentTitle(TITLE_SUCCESS);
                    notificationManager.notify(notificationId, mBuilder.build());
                    App.getInstance().vibrate();
                }
            }


        }
    }


}
