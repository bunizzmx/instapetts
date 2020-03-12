package com.bunizz.instapetts.listeners;

public interface login_listener {
    void onLoginSuccess(boolean success);
    void loginWithGmail();
    void loginWithFacebook();
    void loginWithEmail(String correo,String password);
}
