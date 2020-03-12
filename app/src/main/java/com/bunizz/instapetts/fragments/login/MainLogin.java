package com.bunizz.instapetts.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.login_listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainLogin extends Fragment implements  MainLoginContract.View{

    change_instance listener;
    MainLoginPresenter presenter;
    login_listener listener_login;

    @OnClick(R.id.login_with_facebook)
    void login_with_facebook()
    {
        listener_login.loginWithFacebook();
       // listener.change(FragmentElement.INSTANCE_LOGIN);
    }

    @OnClick(R.id.login_with_gmail)
    void login_with_gmail()
    {
        listener_login.loginWithGmail();
       // listener.change(FragmentElement.INSTANCE_SIGIN);
    }

    @OnClick(R.id.change_to_create_account)
    void change_to_create_account()
    {
       listener.change(FragmentElement.INSTANCE_SIGIN);
    }

    @OnClick(R.id.change_to_login)
    void change_to_login()
    {
        listener.change(FragmentElement.INSTANCE_LOGIN);
    }

    public static MainLogin newInstance() {
        return new MainLogin();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainLoginPresenter(this,getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_login =(login_listener)context;
    }

    @Override
    public void registerCompleted(String corrdenadas) {

    }

    @Override
    public void registerError() {

    }

    @Override
    public void loginCompleted() {

    }

    @Override
    public void loginError() {

    }

}

