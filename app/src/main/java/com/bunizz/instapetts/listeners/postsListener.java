package com.bunizz.instapetts.listeners;

import com.bunizz.instapetts.beans.PostBean;

public interface postsListener {
    void onLike(int id_post);
    void onFavorite(int id_post, PostBean postBean);
    void onDisfavorite(int id_post);
}
