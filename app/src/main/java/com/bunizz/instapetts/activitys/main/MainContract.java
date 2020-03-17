package com.bunizz.instapetts.activitys.main;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.UserBean;

public interface MainContract {

    interface Presenter {
        void UpdateProfile(UserBean userBean);
        void saveMyStory(HistoriesBean historiesBean);
    }

    interface View{
       void psuccessProfileUpdated();
    }
}
