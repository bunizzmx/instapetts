package com.bunizz.instapetts.fragments.retos_eventos;

import com.bunizz.instapetts.beans.EventBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.parameters.PostActions;

import java.util.ArrayList;

public interface EventosContract {

    interface Presenter {
        void getEventosPosts(int type_Search,int paginador,int id_usuario,int filter);
        void getEventos(int type_Search,int paginador,int id_usuario,int filter);
    }

    interface View{
        void showEventosPost(ArrayList<PostBean> posts);
        void showEventos(ArrayList<EventBean> events);
    }
}
