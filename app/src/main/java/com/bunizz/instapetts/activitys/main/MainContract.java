package com.bunizz.instapetts.activitys.main;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface MainContract {

    interface Presenter {
        void UpdateProfile(UserBean userBean);
        void saveMyStory(IndividualDataPetHistoryBean historiesBean);
        void followUser(UserBean userBean);
        void unfollowUser(String id_document,int id_usuario,String name_tag);
        void downloadMyPets(UserBean userBean);
        void have_pets();
        void update_token(UserBean userBean);
        void logout();
        void getIdentificadoresHistories();
        ArrayList<Integer> getIdsFolows();
        void sendFileBackup();
        void getFileBackup();
        void delete_data();
    }

    interface View{
       void psuccessProfileUpdated();
       void saveMyPets(ArrayList<PetBean> pets);
       void onError(int error);
       void havePetsResult(boolean result);
    }
}
