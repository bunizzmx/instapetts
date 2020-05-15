package com.bunizz.instapetts.activitys.story_player;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface StoryPlayerContract {
    interface Presenter {
        void ViewHistory(String identificador,int id_usuario);
        void LikeHistory(String identificador,int id_usuario);
        IdentificadoresHistoriesBean getIdentificador(String identificador);
    }

    interface View{
        void show_feed(ArrayList<PostBean> data, ArrayList<HistoriesBean> data_stories);
    }
}
