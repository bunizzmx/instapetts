package com.bunizz.instapetts.listeners;

import com.facebook.AccessToken;

public interface login_listener {
    void onLoginSuccess(boolean success);
    void loginWithGmail();
    void loginWithFacebook(AccessToken currentAccessToken);
    void loginWithEmail(String correo,String password);
    void sigInWithEmail(String correo,String password);
}
