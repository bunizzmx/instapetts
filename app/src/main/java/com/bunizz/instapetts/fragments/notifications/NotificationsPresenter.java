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
    public void getNotificationsFromWeb() {

        db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document(""+App.read(PREFERENCES.ID_USER_FROM_WEB,0))
                .collection(FIRESTORE.COLLECTION_NOTIFICATIONS)
                .get()
                .addOnFailureListener(e -> {})
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        NotificationBeanFirestore notification = document.toObject(NotificationBeanFirestore.class);
                        NotificationBean NOTIFICACION = new NotificationBean(
                                getTitleForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE()),
                                getBodyForNotification(notification.getTYPE_NOTIFICATION(),notification.getNAME_REMITENTE()),
                                notification.getFOTO_REMITENTE(),
                                notification.getTYPE_NOTIFICATION(),
                                notification.getID_REMITENTE(),
                                notification.getURL_EXTRA(),
                                App.getInstance().fecha_lenguaje_humano(notification.getFECHA())
                        );
                        NOTIFICACION.setId_recurso(notification.getID_RECURSO());
                        NOTIFICACION.setId_document_notification(""+document.getId());
                        notificationHelper.saveNotification(NOTIFICACION);
                        notificationBeans.add( NOTIFICACION );
                    }
                    notificationBeans = notificationHelper.getAllNotifications();
                    mView.showNotificationaFromWeb(notificationBeans);
                })
                .addOnSuccessListener(aVoid -> {});
    }
    @Override
    public void deleteAll() {
         notificationHelper.deleteAll();
         mView.deleteAllComplete();
    }

    String getTitleForNotification(int type_notification,String name){
      String title="";
      switch (type_notification){
          case 0:
              title = mContext.getResources().getString(R.string.title_follow);
              break;
          case 1:
              title = name  + " a  "  + mContext.getResources().getString(R.string.title_comment);
              break;
      }

      return title;
    }

    String getBodyForNotification(int type_notification,String name){
        String body="";
        switch (type_notification){
            case 0:
                body =  "A <b>" + name  + "</b>  "  + mContext.getResources().getString(R.string.body_follow);
                break;
            case 1:
                body = name  + " "  + mContext.getResources().getString(R.string.body_comment);
                break;
        }
        return body;
    }
}
