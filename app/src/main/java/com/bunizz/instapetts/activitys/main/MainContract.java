package com.bunizz.instapetts.activitys.main;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;

public interface MainContract {

    interface Presenter {
        void UpdateProfile(UserBean userBean);
        void saveMyStory(HistoriesBean historiesBean);
        void followUser(UserBean userBean);
        void favoritePet(UserBean userBean, PetBean petBean);
    }

    interface View{
       void psuccessProfileUpdated();
    }
}
