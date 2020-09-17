package com.bunizz.instapetts.fragments.vertical_videos;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface playVideoContract {
    interface Presenter {
        void getVideos(boolean one_user,int id_one);


    }

    interface View{
        void showVideos(ArrayList<PlayVideos> data);
    }
}
