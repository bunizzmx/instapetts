package com.bunizz.instapetts.activitys.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.Splash;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.login_listener;

import java.util.Stack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_LOGUEDD;

public class LoginActivity extends AppCompatActivity implements change_instance, login_listener {

    private Stack<FragmentElement> stack_login;
    private Stack<FragmentElement> stack_sigin;
    private Stack<FragmentElement> stack_main_login;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        changeStatusBarColor(R.color.white);
        stack_sigin = new Stack<>();
        stack_main_login = new Stack<>();
        stack_login = new Stack<>();
        setupFirstFragment();
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    }
    private void setupFirstFragment() {
        mCurrentFragment = new FragmentElement<>(null, MainLogin.newInstance(), FragmentElement.INSTANCE_MAIN_LOGIN, true);
        change_main(mCurrentFragment);
    }

    private void change_login(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_login.size()<=0){stack_login.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_main(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_main_login.size()<=0){stack_main_login.push(mCurrentFragment);}
        }
        inflateFragment();
    }

    private void change_sigin(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_sigin.size()<=0){stack_sigin.push(mCurrentFragment);}
        }
        inflateFragment();
    }

    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_LOGIN) {
            if (stack_login.size() == 0) {
                change_login(new FragmentElement<>("", FragmentLogin.newInstance(), FragmentElement.INSTANCE_LOGIN));
            } else {
                change_login(stack_login.pop());
            }
        }else if(intanceType == FragmentElement.INSTANCE_SIGIN) {
            if (stack_sigin.size() == 0) {
                change_sigin(new FragmentElement<>("", FragmentSigin.newInstance(), FragmentElement.INSTANCE_SIGIN));
            } else {
                change_sigin(stack_sigin.pop());
            }

        }
        else if(intanceType == FragmentElement.INSTANCE_MAIN_LOGIN) {
            if (stack_main_login.size() == 0) {
                change_main(new FragmentElement<>("", MainLogin.newInstance(), FragmentElement.INSTANCE_MAIN_LOGIN));
            } else {
                change_main(stack_main_login.pop());
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(mOldFragment!=null) {
                if (mCurrentFragment.getFragment().isAdded()) {
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left)
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .show(mCurrentFragment.getFragment()).commit();
                } else {
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .add(R.id.root_login, mCurrentFragment.getFragment()).commit();
                }

            }else{
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_login, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {}
    }

    @Override
    public void change(int fragment_element) {
        changeOfInstance(fragment_element);
    }

    @Override
    public void onback() {
        changeOfInstance(FragmentElement.INSTANCE_MAIN_LOGIN);
    }

    @Override
    public void open_sheet() {

    }

    @Override
    public void open_wizard_pet() {

    }

    @Override
    public void onBackPressed() {
            changeOfInstance(FragmentElement.INSTANCE_MAIN_LOGIN);
    }

    @Override
    public void onLoginSuccess(boolean success) {
        App.write(IS_LOGUEDD,true);
        Intent i ;
        i = new Intent(LoginActivity.this, Main.class);
        startActivity(i);
    }



}
