package com.bunizz.instapetts.fragments.profile;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface ProfileUserContract {
    interface Presenter {
        void getInfoUser(UserBean user);
    }

    interface View{
        void showInfoUser(UserBean userBean, ArrayList<PetBean> pets, ArrayList<PostBean> posts);
    }
}
