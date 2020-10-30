package com.bunizz.instapetts.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.record.RecordActivity;
import com.bunizz.instapetts.constantes.PREFERENCES;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_INTRO_COMPLETED;
import static com.bunizz.instapetts.constantes.PREFERENCES.IS_LOGUEDD;


public class Splash extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.read(IS_LOGUEDD, false) && App.read(PREFERENCES.ID_USER_FROM_WEB, 0) != 0 && !App.read(PREFERENCES.NAME_TAG_INSTAPETTS, "INVALID").contains("INVALID")) {
            i = new Intent(Splash.this, RecordActivity.class);
            i.putExtra("LOGIN_AGAIN", 0);
            i.putExtra("FROM_PUSH", 0);
        } else {
            if (App.read(PREFERENCES.PRIMER_USUARIO_INVITADO, false)) {
                i = new Intent(Splash.this, RecordActivity.class);
                i.putExtra("LOGIN_AGAIN", 0);
                i.putExtra("FROM_PUSH", 0);
            } else
                i = new Intent(Splash.this, RecordActivity.class);
        }
        startActivity(i);
        finish();
    }
}
