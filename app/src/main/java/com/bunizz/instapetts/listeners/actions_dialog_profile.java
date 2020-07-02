package com.bunizz.instapetts.listeners;

public interface actions_dialog_profile {
    void delete_post(int id_post);
    void reportPost(int id_post);
    void unfollowUser(int id_user,String uuid);
}
