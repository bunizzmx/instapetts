package com.bunizz.instapetts.fragments.comentarios;

import com.bunizz.instapetts.beans.CommentariosBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface ComentariosContract {
    interface Presenter {
       void comment(CommentariosBean commentariosBean,int type_resource_to_comment);
       void getComentarios(int id_post,int type_resoruce_to_comment);
       void likeComment(int id_post,String id_document,int type_resource_to_comment);
       void loadNextComments(int id_post,int type_resoruce_to_comment);
       void deleteComment(int id_post,String document,int type_resource_to_comment);

    }

    interface View{
        void showComments(ArrayList<CommentariosBean> commentariosBeans);
        void showNextComments(ArrayList<CommentariosBean> commentariosBeans);
    }
}
