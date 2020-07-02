package com.bunizz.instapetts.fragments.post;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface PostGaleryContract {

    interface Presenter {
        void getMorePost(int type_Search,int paginador);
        void likePost(PostActions postActions);
        void saveFavorite(PostActions postActions, PostBean postBean);
    }

    interface View{
        void showMorePost(ArrayList<PostBean> posts);
    }

}
