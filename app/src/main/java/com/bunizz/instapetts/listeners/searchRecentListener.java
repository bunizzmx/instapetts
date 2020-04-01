package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.beans.SearchUserBean;

public interface searchRecentListener {
    void onSearch(SearchPetBean searchPetBean,int type_search);
    void onSearchUser(SearchUserBean searchUserBean,int type_search);
    void deleteRecent(int id);
}
