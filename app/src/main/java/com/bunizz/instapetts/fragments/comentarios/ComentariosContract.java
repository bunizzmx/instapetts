package com.bunizz.instapetts.fragments.comentarios;

import com.bunizz.instapetts.beans.CommentariosBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface ComentariosContract {
    interface Presenter {
       void comment(CommentariosBean commentariosBean);
       void getComentarios(int id_post);
       void likeComment(int id_post,String id_document);
       void loadNextComments(int id_post);
       void deleteComment(int id_post,String document);

    }

    interface View{
        void showComments(ArrayList<CommentariosBean> commentariosBeans);
        void showNextComments(ArrayList<CommentariosBean> commentariosBeans);
    }
}
