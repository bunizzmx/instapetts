package com.bunizz.instapetts.activitys.login;

import com.bunizz.instapetts.beans.UserBean;

public interface LoginContract {


    interface Presenter {
       void RegisterUser(UserBean userBean);
    }

    interface View{
        void registerCompleted();
        void loginCompleted(UserBean userBean);
        void registerError();
    }
}
