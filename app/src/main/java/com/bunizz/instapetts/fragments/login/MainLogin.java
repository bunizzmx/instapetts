package com.bunizz.instapetts.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.login_listener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class MainLogin extends Fragment implements  MainLoginContract.View{

    change_instance listener;
    login_listener listener_login;

    @BindView(R.id.mail_user)
    EditText mail_user;

    @BindView(R.id.password_email)
    EditText password_email;


    @BindView(R.id.login_with_facebook)
    LoginButton login_with_facebook;


    @OnClick(R.id.login_with_gmail)
    void login_with_gmail()
    {
        listener_login.loginWithGmail();
    }

    @OnClick(R.id.login_user_email)
    void login_user_email()
    {
       listener_login.loginWithEmail(mail_user.getText().toString(),password_email.getText().toString());
    }
    CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    @OnClick(R.id.change_to_create_account)
    void change_to_create_account()
    {
       listener.change(FragmentElement.INSTANCE_SIGIN);
    }

    public static MainLogin newInstance() {
        return new MainLogin();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManager = CallbackManager.Factory.create();
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
        login_with_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ACCES_TOKENN","-->EXECUTE CALBACK");
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                listener_login.loginWithFacebook(accessToken);
            }

            @Override
            public void onCancel() {
                Log.e("ACCES_TOKENN","-->CANCELADO:");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("ACCES_TOKENN","-->ERROR:"+ exception.getMessage());
            }
        });
    }

    public void setData(int requestCode, int resultCode, Intent data){
        Log.e("ACTIVITY_RESULT","SET DATA LOGIN 2");
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
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

