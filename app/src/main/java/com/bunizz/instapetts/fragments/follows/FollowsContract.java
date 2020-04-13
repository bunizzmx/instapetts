package com.bunizz.instapetts.fragments.follows;

import com.bunizz.instapetts.beans.FollowsBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface FollowsContract {
    interface Presenter {
        void getFirstFollows(String uuid);
        void nextFollows();
    }

    interface View{
        void showFirstFollows(ArrayList<FollowsBean> followsBeans);
        void showNextFollows(ArrayList<PostBean> posts);
        void noInternet();
        void Error();
    }
}
