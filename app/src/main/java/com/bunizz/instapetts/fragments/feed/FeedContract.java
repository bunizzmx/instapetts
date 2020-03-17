package com.bunizz.instapetts.fragments.feed;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface FeedContract {
    interface Presenter {
        public void get_feed();
        void likePost(PostActions postActions);
        void saveFavorite(PostActions postActions,PostBean postBean);
        void deleteFavorite(int id_post);
        ArrayList<HistoriesBean> getMyStories();

    }

    interface View{
        void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories);
        void peticion_error();
    }
}
