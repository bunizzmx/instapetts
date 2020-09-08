package com.bunizz.instapetts.fragments.feed;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public interface FeedContract {
    interface Presenter {
        void get_feed(boolean one_user,int id_one);
        void get_next_feed(boolean one_user,int id_one,int dias_pasados);
        void unfollowUser(String uuid_usuario,int id_usuario);
        void geet_feed_recomended(boolean one_user,int id_one);
        void getFeedParaTi();
        void likePost(PostActions postActions);
        void saveFavorite(PostActions postActions,PostBean postBean);
        void deleteFavorite(int id_post);
        void deletePost(PostBean postBean);
        HistoriesBean getMyStories();
        void haveNotificatiosn();

    }

    interface View{
        void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories);
        void show_next_feed(ArrayList<PostBean> data);
        void show_feed_recomended(ArrayList<PostBean> data, ArrayList<UserBean> users);
        void peticion_error();
        void deletePostError(boolean deleted);
        void LikeEror();
        void LikeSuccess();
        void noInternet();
        void showBadge(boolean show);
    }
}
