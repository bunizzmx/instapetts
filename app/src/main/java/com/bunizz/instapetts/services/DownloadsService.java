package com.bunizz.instapetts.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import androidx.annotation.Nullable;

public class DownloadsService extends Service {

    LikePostHelper likePostHelper;
    FollowsHelper followsHelper;
    MyStoryHelper myStoryHelper;
    SavedPostHelper savedPostHelper;
    FirebaseFirestore db;
    @Override
    public void onCreate() {
        super.onCreate();
        likePostHelper = new LikePostHelper(this);
        followsHelper = new FollowsHelper(this);
        myStoryHelper = new MyStoryHelper(this);
        savedPostHelper = new SavedPostHelper(this);
        db = App.getIntanceFirestore();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("DOWNLOAD","SEGUIDOS");
        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDOS).get()
                .addOnCompleteListener(task -> {
                    Log.e("DOWNLOAD","SEGUIDOS COMPLETED");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserBean userBean = document.toObject(UserBean.class);
                        followsHelper.saveNewFriend(userBean);
                    }
                })
                .addOnSuccessListener(queryDocumentSnapshots -> {

                })
                .addOnFailureListener(e -> {

                });
        Log.e("DOWNLOAD","FAVORITOS");
        db.collection(FIRESTORE.FAVORITES).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.FAVORITES).get()
                .addOnCompleteListener(task -> {
                    Log.e("DOWNLOAD","FAVORITOS COMPLETED");
                 })
                .addOnSuccessListener(queryDocumentSnapshots -> {})
                .addOnFailureListener(e -> { });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("DOWNLOAD","ON DESTROY SERVICE DOWNLOAD");
        super.onDestroy();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
