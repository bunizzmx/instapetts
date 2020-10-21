package com.bunizz.instapetts.fragments.info;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.RazaBean;

import java.util.ArrayList;

public interface InfoPetContract {
    interface Presenter {
        void delete(int id_pet);
        void ratePet(int id_pet,int stars);
        void updatePet(PetBean petBean);
        void getRazas(int id_type_pet);
    }

    interface View{
        void petRated();
        void petUpdated();
        void petDeleted();
        void showRazas(ArrayList<RazaBean> razaBeans);
    }
}
