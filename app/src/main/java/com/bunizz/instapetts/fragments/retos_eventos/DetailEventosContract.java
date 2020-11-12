package com.bunizz.instapetts.fragments.retos_eventos;

import com.bunizz.instapetts.beans.EventBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.RankingBean;

import java.util.ArrayList;

public interface DetailEventosContract {

    interface Presenter {
        void getPoliticasDetailReto(int id_politica);
    }

    interface View{
        void showPolitica(String politica);
    }
}
