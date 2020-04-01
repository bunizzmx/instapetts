package com.bunizz.instapetts.fragments.feed;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface FeedContract {
    interface Presenter {
        void get_feed(boolean one_user,int id_one);
        void geet_feed_recomended(boolean one_user,int id_one);
        void likePost(PostActions postActions);
        void saveFavorite(PostActions postActions,PostBean postBean);
        void deleteFavorite(int id_post);
        void deletePost(PostBean postBean);
        ArrayList<HistoriesBean> getMyStories();

    }

    interface View{
        void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories);
        void show_feed_recomended(ArrayList<PostBean> data);
        void peticion_error();
        void deletePostError(boolean deleted);
        void LikeEror();
        void LikeSuccess();
    }
}
