package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.PostBean;

public interface postsListener {
    void onLike(int id_post,boolean type_like,int id_usuario,String url_image);
    void onFavorite(int id_post, PostBean postBean);
    void onDisfavorite(int id_post);
    void openMenuOptions(int id_post,int id_usuario,String uuid);
    void commentPost(int id_post,boolean can_comment,int id_usuario);
}
