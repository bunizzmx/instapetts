package com.bunizz.instapetts.fragments.side_menus_activities.postsSaved;

import com.bunizz.instapetts.beans.PostBean;

import java.util.ArrayList;

public interface PostPublicsSavedContract {
    interface Presenter {
        void getPostPublics();
    }

    interface View{
        void showPosts(ArrayList<PostBean> posts);
        void Error();
    }

}
