package com.bunizz.instapetts.fragments.notifications;

import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.beans.PetBean;

import java.util.ArrayList;

public interface NotificationsContract {
    interface Presenter {
      void getNotifications();
    }

    interface View{
       void showNotifications(ArrayList<NotificationBean> notificationBeans);
    }
}
