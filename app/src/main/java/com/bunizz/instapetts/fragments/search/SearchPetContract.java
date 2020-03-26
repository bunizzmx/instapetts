package com.bunizz.instapetts.fragments.search;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;
import com.bunizz.instapetts.beans.UserBean;

import java.util.ArrayList;

public interface SearchPetContract {
    interface Presenter {
        void searchusers(String word);
        void searchPets(String word);
    }

    interface View{
        void shoPetsResults(ArrayList<SearchPetBean> pets);
        void shoUsersResults(ArrayList<SearchUserBean> users);
    }
}
