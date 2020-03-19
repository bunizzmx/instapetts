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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.utils.compresor.Compressor;

import java.io.File;
import java.util.ArrayList;

public class NotificationsService extends Service {

    NotificationHelper notificationHelper;
    NotificationBean notificationBean;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper = new NotificationHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationBean = new NotificationBean();
        notificationBean.setBody(intent.getStringExtra(BUNDLES.BODY));
        notificationBean.setTitle(intent.getStringExtra(BUNDLES.TITLE));
        notificationBean.setType_notification(intent.getIntExtra(BUNDLES.TYPE_NOTIFICACION,0));
        notificationBean.setId_usuario(intent.getIntExtra(BUNDLES.ID_USUARIO,0));
        notificationBean.setUrl_resource(intent.getStringExtra(BUNDLES.URL_RESOURCE));
        notificationHelper.saveNotification(notificationBean);
        Log.e("GUARDE_NOTIFICACION","si");
        Toast.makeText(this,"SE GUARDO NOTIFICATION",Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
