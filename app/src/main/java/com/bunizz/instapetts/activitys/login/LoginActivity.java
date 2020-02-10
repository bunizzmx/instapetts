package com.bunizz.instapetts.activitys.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.listeners.change_instance;

import java.util.Stack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class LoginActivity extends AppCompatActivity implements change_instance {

    private Stack<FragmentElement> stack_login;
    private Stack<FragmentElement> stack_sigin;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        stack_sigin = new Stack<>();
        stack_login = new Stack<>();
        setupFirstFragment();
    }

    private void setupFirstFragment() {
        mCurrentFragment = new FragmentElement<>(null, FragmentLogin.newInstance(), FragmentElement.INSTANCE_LOGIN, true);
        change_login(mCurrentFragment, false);
    }

    private void change_login(FragmentElement fragment, boolean savePreviusFragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_login.size()<=0){stack_login.push(mCurrentFragment);}
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
                change_login(new FragmentElement<>("", FragmentLogin.newInstance(), FragmentElement.INSTANCE_LOGIN), false);
            } else {
                change_login(stack_login.pop(), false);
            }
        }else if(intanceType == FragmentElement.INSTANCE_SIGIN) {
            if (stack_sigin.size() == 0) {
                change_sigin(new FragmentElement<>("", FragmentSigin.newInstance(), FragmentElement.INSTANCE_SIGIN));
            } else {
                change_sigin(stack_sigin.pop());
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
}
