package com.bunizz.instapetts.fragments.search.tabs.users;

import com.bunizz.instapetts.beans.SearcRecentBean;

public interface UserListContract {
    interface Presenter {
        void saveSearch(SearcRecentBean searcRecentBean);
        void deleteRecent(int id);
    }

    interface View{
        void deleteSucess();
        void searchSaved();
    }
}
