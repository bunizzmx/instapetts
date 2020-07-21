package com.bunizz.instapetts.fragments.follows;

import com.bunizz.instapetts.beans.FollowsBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface FollowsContract {
    interface Presenter {
        void getFirstFolowers(String uuid);
        void getFirstFollowed(String uuid);
        void nextFollowers();
        void nextFollowed();
        void unfollowUser(String uuid,String name_tag,int id_usuario,boolean delete_me_friends);
    }

    interface View{
        void showFirstFollowers(ArrayList<FollowsBean> followsBeans);
        void showNextFollowers(ArrayList<FollowsBean> followsBeans);
        void showFirstFollowed(ArrayList<FollowsBean> followsBeans);
        void showNextFollowed(ArrayList<FollowsBean> followsBeans);
        void noInternet();
        void Error();
        void UnfollowSuccess();
    }
}
