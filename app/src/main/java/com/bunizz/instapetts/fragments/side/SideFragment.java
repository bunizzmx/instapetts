package com.bunizz.instapetts.fragments.side;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.BuildConfig;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.searchqr.QrSearchActivity;
import com.bunizz.instapetts.activitys.side_menus_activities.SideMenusActivities;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.side_menus_activities.FragmentConfigEmail;
import com.bunizz.instapetts.listeners.open_side_menu;
import com.bunizz.instapetts.utils.dilogs.DialogLogout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SideFragment extends  Fragment{
    Intent i=null;

    open_side_menu listener;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.menu_side_guardado)
    void menu_side_guardado() {
        i.putExtra("TYPE_MENU",2);
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.side_menu_open_push)
    void side_menu_open_push() {
        i.putExtra("TYPE_MENU",3);
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.politicas)
    void politicas() {
        i.putExtra("TYPE_MENU",0);
        i.putExtra("URL","privacidad");
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.terms_use)
    void terms_use() {
        i.putExtra("TYPE_MENU",0);
        i.putExtra("URL","terminos");
        startActivity(i);
    }


    @SuppressLint("MissingPermission")
    @OnClick(R.id.side_menu_administrate_account)
    void side_menu_administrate_account() {
        i.putExtra("TYPE_MENU",1);
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_my_pet_code)
    void open_my_pet_code() {
        listener.open_pet_code();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.side_menu_share_profile)
    void side_menu_share_profile() {
       listener.share_my_profile();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_feed)
    void back_to_feed() {
      getActivity().onBackPressed();
    }



    @SuppressLint("MissingPermission")
    @OnClick(R.id.logout)
    void logout() {
      listener.logout();
    }

    @BindView(R.id.app_name_user)
    TextView app_name_user;

    @BindView(R.id.version_app)
    TextView version_app;

    public static SideFragment newInstance() {
        return new SideFragment();
    }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.side_layout, container, false);
    }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i = new Intent(getContext(), SideMenusActivities.class);
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
            app_name_user.setText("@" + App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"));
            version_app.setText("Version : " + BuildConfig.VERSION_NAME);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener =(open_side_menu) context;

    }
}
