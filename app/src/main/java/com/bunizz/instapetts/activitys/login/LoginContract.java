package com.bunizz.instapetts.activitys.login;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.UserBean;

public interface LoginContract {


    interface Presenter {
        void RegisterUser(UserBean userBean);
        void updateUser(UserBean userBean);
        void getFileBackup();
        void getMyStories();
    }

    interface View{
        void registerCompleted();
        void HistoriesSaved();
        void loginCompleted(UserBean userBean);
        void registerError();
        void isFirstUser(int id_from_web);
        void UpdateFirsUserCompleted();
        void fileBackupDownloaded();

    }
}
