package com.bunizz.instapetts.fragments.login.first_user;

import com.bunizz.instapetts.beans.PetBean;

import java.util.ArrayList;

public interface FirstUserContract
{
    interface Presenter {
        void getNameAvailable(String name);
    }

    interface View{
        void showUsersAvailables(boolean available);
    }
}
