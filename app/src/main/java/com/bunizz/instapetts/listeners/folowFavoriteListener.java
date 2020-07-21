package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;

public interface folowFavoriteListener {
    void followUser(UserBean userBean,boolean follow_unfollow);
    void delete_of_my_friends(UserBean userBean,boolean follow_unfollow);
}
