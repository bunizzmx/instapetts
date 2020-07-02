package com.bunizz.instapetts.fragments.notifications;

import android.content.Context;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.beans.NotificationBeanFirestore;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.web.WebServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class NotificationsPresenter implements  NotificationsContract.Presenter {

    private NotificationsContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    NotificationHelper notificationHelper;
    FirebaseFirestore db;
    ArrayList<NotificationBean> notificationBeans = new ArrayList<>();
    NotificationsPresenter(NotificationsContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        notificationHelper = new NotificationHelper(mContext);
        db = App.getIntanceFirestore();
        }


    @Override
    public void getNotifications() {
        ArrayList<NotificationBean> notificationBeans = new ArrayList<>();
        notificationBeans = notificationHelper.getAllNotifications();
        mView.showNotifications(notificationBeans);
    }

    @Override
    public void deleteNotification(int id) {
        notificationHelper.deleteNotificacion(id);
    }

    @Override
    public void deleteAll() {
         notificationHelper.deleteAll();
         mView.deleteAllComplete();
    }

}
