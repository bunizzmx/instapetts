package com.bunizz.instapetts.activitys.story_player;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface StoryPlayerContract {
    interface Presenter {
        void ViewHistory(String identificador,int id_usuario);
        void LikeHistory(String identificador,int id_usuario);
        IdentificadoresHistoriesBean getIdentificador(String identificador);
        void followUser(UserBean userBean);
        void unfollowUser(String id_document);
        void favoritePet(UserBean userBean, PetBean petBean);
        void deleteMyHistory(String id_ide_tificador);
    }

    interface View{
        void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories);
    }
}
