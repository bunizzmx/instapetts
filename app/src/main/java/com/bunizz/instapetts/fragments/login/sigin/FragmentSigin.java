package com.bunizz.instapetts.fragments.login.sigin;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.login_listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSigin extends Fragment {

    change_instance listener;
    login_listener login_listener;

    @BindView(R.id.email_signin)
    EditText email_signin;

    @BindView(R.id.password_signin)
    EditText password_signin;

    @BindView(R.id.password_signin_confirm)
    EditText password_signin_confirm;

    @BindView(R.id.label_check_password)
    TextView label_check_password;


    @BindView(R.id.label_check_password_first)
    TextView label_check_password_first;


    @BindView(R.id.see_password)
    ImageView see_password;
     boolean SHOW_PASS = false;
     boolean IS_PASSWORD_CHECHED =false;


    @OnClick(R.id.back_to_main)
    void back_to_main()
    {
        listener.onback();
    }

    @OnClick(R.id.button_signin)
    void button_signin()
    {
        if(IS_PASSWORD_CHECHED && !email_signin.getText().toString().isEmpty()) {
            if (login_listener != null)
                login_listener.sigInWithEmail(email_signin.getText().toString(), password_signin.getText().toString());
        }else{
            Toast.makeText(getContext(),"Revisa correo o contraseña",Toast.LENGTH_LONG).show();
        }
    }



    public static FragmentSigin newInstance() {
        return new FragmentSigin();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sigin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        see_password.setVisibility(View.GONE);
        label_check_password.setVisibility(View.GONE);
        label_check_password_first.setVisibility(View.GONE);
        password_signin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password_signin.getText().length() > 6){
                    label_check_password_first.setText(getContext().getString(R.string.buena));
                    label_check_password_first.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                    label_check_password_first.setVisibility(View.VISIBLE);
                    see_password.setVisibility(View.VISIBLE);
                }else{
                    label_check_password_first.setText(getContext().getString(R.string.corta));
                    label_check_password_first.setTextColor(getActivity().getResources().getColor(R.color.primary));
                    label_check_password_first.setVisibility(View.VISIBLE);
                    see_password.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password_signin_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password_signin.getText().length() > 6){
                    if(password_signin.getText().toString().trim().equals(password_signin_confirm.getText().toString().trim())){
                        IS_PASSWORD_CHECHED = true;
                        label_check_password.setVisibility(View.VISIBLE);
                        label_check_password.setText(getContext().getString(R.string.correcto));
                        label_check_password.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                    }else{
                        IS_PASSWORD_CHECHED = false;
                        label_check_password.setTextColor(getActivity().getResources().getColor(R.color.primary));
                        label_check_password.setVisibility(View.VISIBLE);
                        label_check_password.setText(getContext().getString(R.string.no_coinciden));
                    }
                }else{
                    IS_PASSWORD_CHECHED = false;
                    see_password.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        see_password.setOnClickListener(view1 ->
        {
            if(SHOW_PASS){
                SHOW_PASS = false;
            }else{
                SHOW_PASS = true;
            }
            if(SHOW_PASS){
                password_signin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_signin_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                see_password.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_invisible));
            }else{
                see_password.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_visible));
                password_signin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password_signin_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            password_signin.setSelection(password_signin.getText().length());
            password_signin_confirm.setSelection(password_signin_confirm.getText().length());
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        login_listener= (login_listener) context;
    }
}
