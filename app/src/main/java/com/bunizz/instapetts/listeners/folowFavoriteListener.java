package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;

public interface folowFavoriteListener {
    void followUser(UserBean userBean);
    void favoritePet(UserBean userBean, PetBean petBean);
}
