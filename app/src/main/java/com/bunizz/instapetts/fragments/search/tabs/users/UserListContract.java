package com.bunizz.instapetts.fragments.search.tabs.users;

import com.bunizz.instapetts.beans.SearcRecentBean;

public interface UserListContract {
    interface Presenter {
        void saveSearch(SearcRecentBean searcRecentBean);
    }

    interface View{
        void searchSaved();
    }
}
