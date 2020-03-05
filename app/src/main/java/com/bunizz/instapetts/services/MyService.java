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
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.share_post.ShareActivity;

import java.io.File;
import java.util.HashMap;

public class MyService extends Service {

    private TransferUtility transferUtility;

    public final static String INTENT_KEY_NAME = "key";
    public  final static String INTENT_FILE = "file";
    public final static String INTENT_TRANSFER_OPERATION = "transferOperation";

    public final static String TRANSFER_OPERATION_UPLOAD = "upload";
    public final static String TRANSFER_OPERATION_DOWNLOAD = "download";

    private final static String TAG = MyService.class.getSimpleName();
   public static  NotificationManager notificationManager;
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    public  static int notificationId = 1;
    public  static  NotificationCompat.Builder mBuilder=null;
    @Override
    public void onCreate() {
        super.onCreate();
        Util util = new Util();
        transferUtility = util.getTransferUtility(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String key = intent.getStringExtra(INTENT_KEY_NAME);
        final File file = (File) intent.getSerializableExtra(INTENT_FILE);
        final String transferOperation = intent.getStringExtra(INTENT_TRANSFER_OPERATION);
        TransferObserver transferObserver;
         transferObserver = transferUtility.upload(key, file);

         transferObserver.setTransferListener(new UploadListener());
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
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("xxxx"))
                    .setContentTitle("Subiendo Post")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle("Subiendo Post")
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
            float total = ((float) bytesCurrent / (float) bytesTotal) * 100;
            int porcentaje = (int) total;
            Log.e("PROGRESO_SUBIDA","-->" + porcentaje);
            mBuilder.setContentText("Progreso " + porcentaje + "%")
                    .setProgress(100,porcentaje,false);
            notificationManager.notify(notificationId, mBuilder.build());
        }

        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.e("PROGRESO_SUBIDA","-->" + state);
        }
    }


}
