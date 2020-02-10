package com.bunizz.instapetts.fragments.login.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentLogin extends Fragment {

    change_instance listener;

    @OnClick(R.id.register_user)
    void register_user()
    {
       listener.change(FragmentElement.INSTANCE_SIGIN);
    }

    @BindView(R.id.title_login)
    TextView title_login;
    @BindView(R.id.user_login)
    TextView user_login;

    @BindView(R.id.xxx)
    TextView xxx;

    @BindView(R.id.label_facebook)
    TextView label_facebook;




    public static FragmentLogin newInstance() {
        return new FragmentLogin();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        title_login.setTypeface(App.avenir_black);
        user_login.setTypeface(App.fuente);
        xxx.setTypeface(App.fuente);
        label_facebook.setTypeface(App.fuente);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
    }
}
