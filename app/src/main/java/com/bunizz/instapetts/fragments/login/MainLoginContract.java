package com.bunizz.instapetts.fragments.login;

import com.bunizz.instapetts.beans.PetBean;

public interface MainLoginContract {
    interface Presenter {
         void registerUser(PetBean user);
         void loginUser();
         void getDataUser();

    }

    interface View{
        void registerCompleted(String corrdenadas);
        void registerError();
        void loginCompleted();
        void loginError();
    }
}
