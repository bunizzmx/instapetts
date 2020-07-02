package com.bunizz.instapetts.activitys.PlayVideo;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface PlayVideoContract {

    interface Presenter {
        void likeVideo(PostActions postActions);
        void saveFavorite(PostActions postActions,PostBean postBean);
        void deleteFavorite(int id_post);
        boolean isLiked(int id_post);
        boolean  isSaved(int id_post);
        void unfollowUser(String uuid_usuario,int id_usuario);
        void deleteVideo(PostBean postBean);
    }

    interface View{
        void LikeSuccess();
        void deletePostError(boolean deleted);
    }
}
