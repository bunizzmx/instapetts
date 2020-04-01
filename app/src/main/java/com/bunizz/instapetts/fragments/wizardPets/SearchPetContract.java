package com.bunizz.instapetts.fragments.wizardPets;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.RazaBean;

import java.util.ArrayList;

public interface SearchPetContract {
    interface Presenter {
         void saveRaza(RazaBean razaBean);
         void downloadCatalogo(int type_pet);
         void search_query(String query);
         void clean_table();

    }

    interface View{
        void showCatalogo(ArrayList<RazaBean> data,String query);
        void saveRazas(ArrayList<RazaBean> data);
        void onCatalogoError();
    }
}
