package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;

public interface folowFavoriteListener {
    void followUser(UserBean userBean,boolean follow_unfollow);
    void favoritePet(UserBean userBean, PetBean petBean);
}
