package com.bunizz.instapetts.fragments.profile;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface ProfileUserContract {
    interface Presenter {
        void getInfoUser(UserBean user);
        void getPostUser(boolean one_user,int id_one);
        void follow(int id_user,boolean follow);
        boolean is_user_followed(int id_user);
    }

    interface View{
        void showInfoUser(UserBean userBean, ArrayList<PetBean> pets);
        void showPostUser(ArrayList<PostBean> posts);
        void Error();
        void ErrorPostUsers();
        void successFollow(boolean follow,int id_user);
    }
}
