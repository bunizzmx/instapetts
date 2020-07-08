package com.bunizz.instapetts.fragments.side;

import android.annotation.SuppressLint;
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
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.side_menus_activities.FragmentConfigEmail;
import com.bunizz.instapetts.utils.dilogs.DialogLogout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SideFragment extends  Fragment{
    Intent i=null;

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
    @OnClick(R.id.menu_centro_ayuda)
    void menu_centro_ayuda() {
        i.putExtra("TYPE_MENU",0);
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
        //Intent i = new Intent(this , QrSearchActivity.class);
       // startActivityForResult( i,NEW_PHOTO_QR_SCAN);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.side_menu_share_profile)
    void side_menu_share_profile() {
        //share_profile();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_feed)
    void back_to_feed() {
      getActivity().onBackPressed();
    }



    @SuppressLint("MissingPermission")
    @OnClick(R.id.logout)
    void logout() {
       /* DialogLogout dialogLogout = new DialogLogout(this);
        dialogLogout.setListener(() -> {
            presenter.logout();
            presenter.delete_data();
            App.getInstance().clear_preferences();
            Intent i = new Intent(Main.this, LoginActivity.class);
            startActivity(i);
        });
        dialogLogout.show();*/
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
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
            app_name_user.setText("@" + App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"));
            version_app.setText("Version : " + BuildConfig.VERSION_NAME);


    }


}