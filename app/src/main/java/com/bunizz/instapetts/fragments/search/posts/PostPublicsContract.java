package com.bunizz.instapetts.fragments.search.posts;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface PostPublicsContract {
    interface Presenter {
        void getPostPublics(int type_search);
    }

    interface View{
        void showPosts(ArrayList<PostBean> posts);
        void noInternet();
        void peticionError();
    }

}
