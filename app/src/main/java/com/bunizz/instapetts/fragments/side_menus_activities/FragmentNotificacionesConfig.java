package com.bunizz.instapetts.fragments.side_menus_activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentNotificacionesConfig extends Fragment {

    @BindView(R.id.switch_me_gusta)
    Switch switch_me_gusta;

    @BindView(R.id.switch_nuevos_seguidores)
    Switch switch_nuevos_seguidores;

    @BindView(R.id.switch_noticias_tips)
    Switch switch_noticias_tips;

    @BindView(R.id.switch_contenido_seguidos)
    Switch switch_contenido_seguidos;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main)
    void back_to_main() {
        getActivity().onBackPressed();
    }


    public static FragmentNotificacionesConfig newInstance() {
        return new FragmentNotificacionesConfig();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificaciones_config, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(App.read(PREFERENCES.PUSH_TIPS_NOTICIAS,true))
            switch_noticias_tips.setChecked(true);
        else
            switch_noticias_tips.setChecked(false);

        if(App.read(PREFERENCES.PUSH_CONTENIDO_SEGUIDOS,true))
            switch_contenido_seguidos.setChecked(true);
        else
            switch_contenido_seguidos.setChecked(false);

        if(App.read(PREFERENCES.PUSH_ME_GUSTAS,true))
            switch_me_gusta.setChecked(true);
        else
            switch_me_gusta.setChecked(false);


        if(App.read(PREFERENCES.PUSH_SEGUIDORES,true))
            switch_nuevos_seguidores.setChecked(true);
        else
            switch_nuevos_seguidores.setChecked(false);


        switch_contenido_seguidos.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                App.write(PREFERENCES.PUSH_CONTENIDO_SEGUIDOS,true);
            else
                App.write(PREFERENCES.PUSH_CONTENIDO_SEGUIDOS,false);
        });
        switch_me_gusta.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                App.write(PREFERENCES.PUSH_ME_GUSTAS,true);
            else
                App.write(PREFERENCES.PUSH_ME_GUSTAS,false);
        });
        switch_nuevos_seguidores.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                App.write(PREFERENCES.PUSH_SEGUIDORES,true);
            else
                App.write(PREFERENCES.PUSH_SEGUIDORES,false);
        });
        switch_noticias_tips.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                App.write(PREFERENCES.PUSH_TIPS_NOTICIAS,true);
            else
                App.write(PREFERENCES.PUSH_TIPS_NOTICIAS,false);
        });

    }
}
