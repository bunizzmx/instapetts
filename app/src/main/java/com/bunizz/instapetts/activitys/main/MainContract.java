package com.bunizz.instapetts.activitys.main;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface MainContract {

    interface Presenter {
        void UpdateProfile(UserBean userBean);
        void saveMyStory(HistoriesBean historiesBean);
        void followUser(UserBean userBean);
        void favoritePet(UserBean userBean, PetBean petBean);
        void downloadMyPets(UserBean userBean);
    }

    interface View{
       void psuccessProfileUpdated();
       void saveMyPets(ArrayList<PetBean> pets);
       void onError(int error);
    }
}
