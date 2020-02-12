package com.bunizz.instapetts.activitys.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.wizardPets.WizardPetActivity;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.listeners.change_instance;

import java.util.Stack;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity implements change_instance {


    private Stack<FragmentElement> stack_feed;
    private Stack<FragmentElement> stack_profile_pet;


    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_profile_pet)
    void tab_profile_pet()
    {
        Log.e("CHANGE_INSTANCE","profile pet");
        Intent i = new Intent(Main.this, WizardPetActivity.class);
        startActivity(i);
      // changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_p)
    void tab_p()
    {
         changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        changeStatusBarColor(R.color.white);
        stack_feed = new Stack<>();
        stack_profile_pet = new Stack<>();
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
        mCurrentFragment = new FragmentElement<>(null, FeedFragment.newInstance(), FragmentElement.INSTANCE_FEED, true);
        change_main(mCurrentFragment);
    }


    private void change_main(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_feed.size()<=0){stack_feed.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_profile_pet(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_profile_pet.size()<=0){stack_profile_pet.push(mCurrentFragment);}
        }
        inflateFragment();
    }


    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_FEED) {
            if (stack_feed.size() == 0) {
                change_main(new FragmentElement<>("", FeedFragment.newInstance(), FragmentElement.INSTANCE_LOGIN));
            } else {
                change_main(stack_feed.pop());
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_PROFILE_PET) {
            if (stack_profile_pet.size() == 0) {
                change_profile_pet(new FragmentElement<>("", FragmentProfileUserPet.newInstance(), FragmentElement.INSTANCE_PROFILE_PET));
            } else {
                change_profile_pet(stack_profile_pet.pop());
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
                            .add(R.id.root_main, mCurrentFragment.getFragment()).commit();
                }

            }else{
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_main, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {}
    }

    @Override
    public void change(int fragment_element) {

       // changeOfInstance(fragment_element);
    }

    @Override
    public void onback() {
        changeOfInstance(FragmentElement.INSTANCE_MAIN_LOGIN);
    }

    @Override
    public void onBackPressed() {
        changeOfInstance(FragmentElement.INSTANCE_MAIN_LOGIN);
    }
}
