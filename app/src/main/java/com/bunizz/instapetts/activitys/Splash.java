package com.bunizz.instapetts.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.activitys.intro.IntroActivity;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.main.Main;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_INTRO_COMPLETED;
import static com.bunizz.instapetts.constantes.PREFERENCES.IS_LOGUEDD;


public class Splash extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(App.read(IS_INTRO_COMPLETED,false)) {
            if(App.read(IS_LOGUEDD,false)) {
                i = new Intent(Splash.this, Main.class);
            }else{
                i = new Intent(Splash.this, LoginActivity.class);
            }
        }else{
            i = new Intent(Splash.this, IntroActivity.class);
        }
        startActivity(i);
        finish();
    }



}
