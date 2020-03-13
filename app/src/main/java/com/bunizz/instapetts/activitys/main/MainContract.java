package com.bunizz.instapetts.activitys.main;

import com.bunizz.instapetts.beans.UserBean;

public interface MainContract {

    interface Presenter {
        void UpdateProfile(UserBean userBean);
    }

    interface View{
       void psuccessProfileUpdated();
    }
}
