package com.bunizz.instapetts.fragments.notifications;

import android.content.Context;

import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.web.WebServices;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class NotificationsPresenter implements  NotificationsContract.Presenter {

    private NotificationsContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    NotificationHelper notificationHelper;

    NotificationsPresenter(NotificationsContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        notificationHelper = new NotificationHelper(mContext);
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
