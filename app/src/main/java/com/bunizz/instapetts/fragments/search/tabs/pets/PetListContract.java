package com.bunizz.instapetts.fragments.search.tabs.pets;

import com.bunizz.instapetts.beans.SearcRecentBean;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;

import java.util.ArrayList;

public interface PetListContract {
    interface Presenter {
        void saveSearch(SearcRecentBean searcRecentBean);
        void  deleteRecent(int id);
    }

    interface View{
        void deleteSuccess();
        void searchSaved();
    }
}
