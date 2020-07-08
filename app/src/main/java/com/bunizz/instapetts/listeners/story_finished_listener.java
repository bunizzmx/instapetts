package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;

public interface story_finished_listener {
    void on_finish();
    void onItemView(String identificador,int id_usuario);
    void onItemLiked(String identificador,int id_usuario);
    void onItemDeleted(String history);
    IdentificadoresHistoriesBean getIdenTificador(String identificador);
}
