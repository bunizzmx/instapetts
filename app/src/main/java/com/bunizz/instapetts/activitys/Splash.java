package com.bunizz.instapetts.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.activitys.intro.IntroActivity;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.constantes.PREFERENCES;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_INTRO_COMPLETED;
import static com.bunizz.instapetts.constantes.PREFERENCES.IS_LOGUEDD;


public class Splash extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(App.read(IS_INTRO_COMPLETED,false)) {
            Log.e("STATUS_USER","INTRO_COMPLETED");
            if(App.read(IS_LOGUEDD,false) || App.read(PREFERENCES.ID_USER_FROM_WEB,0) == 0) {
                Log.e("STATUS_USER","IS LOGUEADO");
                i = new Intent(Splash.this, Main.class);
                i.putExtra("LOGIN_AGAIN",0);
                i.putExtra("FROM_PUSH",0);
            }else{
                Log.e("STATUS_USER","NO ESTA LOGUEADO");
                i = new Intent(Splash.this, LoginActivity.class);
            }
        }else{
            Log.e("STATUS_USER","NO PASO EL INTROP");
            i = new Intent(Splash.this, IntroActivity.class);
        }
        startActivity(i);
        finish();
    }



}
