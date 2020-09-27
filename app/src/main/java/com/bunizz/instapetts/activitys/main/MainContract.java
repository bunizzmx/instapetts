package com.bunizz.instapetts.activitys.main;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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
        void isAdsActive();
        void updateConexion();
        void sendPostVideo(PostBean postBean);
        void getPostVideo();
        void updateLocations();
        void firebaseAuthWithGoogle(GoogleSignInAccount acct);
        void startGoggleSignin();
        void getNameAvailable(String name);
        void updateUser(UserBean userBean);
    }

    interface View{
       void psuccessProfileUpdated();
       void saveMyPets(ArrayList<PetBean> pets);
       void onError(int error);
       void havePetsResult(boolean result);
       void noWifi();
       void setActivateAds(boolean activated);
       void sendPostVideoView(PostBean postBean);
       void login_invalid();
       void messageLogin(String message);
       void loginInvitadoCompleted(UserBean userBean);
       void loginFirstUserInvitado(int id_from_web);
        void showUsersAvailables(boolean available);
        void completeInfoInvitado();
        void UpdateAvailable(String version);
    }
}
