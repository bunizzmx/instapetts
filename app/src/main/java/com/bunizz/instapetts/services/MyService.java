package com.bunizz.instapetts.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
   public static  NotificationManagerCompat notificationManager;
    public static NotificationCompat.Builder builder;
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
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
         notificationManager = NotificationManagerCompat.from(this);
         builder = new NotificationCompat.Builder(this, "xxxx");
         builder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(1, builder.build());

        // Do the job here that tracks the progress.
        // Usually, this should be in a
        // worker thread
        // To show progress, update PROGRESS_CURRENT and update the notification with:


        // When done, update the notification one more time to remove the progress bar

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
            builder.setContentText("Download complete")
                    .setProgress(0,porcentaje,false);
            notificationManager.notify(1, builder.build());
        }

        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.e("PROGRESO_SUBIDA","-->" + state);
        }
    }


}
