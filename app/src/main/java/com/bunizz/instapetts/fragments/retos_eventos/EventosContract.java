package com.bunizz.instapetts.fragments.retos_eventos;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface EventosContract {

    interface Presenter {
        void getEventos(int type_Search,int paginador,int id_usuario,int filter);
    }

    interface View{
        void showEventos(ArrayList<PostBean> posts);
    }
}
