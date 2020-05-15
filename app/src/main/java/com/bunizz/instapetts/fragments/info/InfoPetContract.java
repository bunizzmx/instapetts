package com.bunizz.instapetts.fragments.info;

import com.bunizz.instapetts.beans.PetBean;

public interface InfoPetContract {
    interface Presenter {
        void delete(int id_pet);
        void ratePet(int id_pet,int stars);
        void updatePet(PetBean petBean);
    }

    interface View{
        void petRated();
        void petUpdated();
    }
}
