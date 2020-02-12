package com.bunizz.instapetts.activitys.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.searchingPets.SearchPetActivity;
import com.bunizz.instapetts.activitys.wizardPets.WizardPetActivity;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.tips.FragmentTips;
import com.bunizz.instapetts.listeners.change_instance;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity implements change_instance {


    private Stack<FragmentElement> stack_feed;
    private Stack<FragmentElement> stack_profile_pet;
    private Stack<FragmentElement> stack_tips;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;
    static final int NEW_PET_REQUEST = 1;



    @BindView(R.id.icon_tips)
    ImageView icon_tips;
    @BindView(R.id.icon_feed)
    ImageView icon_feed;
    @BindView(R.id.icon_chat)
    ImageView icon_chat;
    @BindView(R.id.icon_profile_pet)
    ImageView icon_profile_pet;


    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_profile_pet)
    void tab_profile_pet() {
        Log.e("CHANGE_INSTANCE", "profile pet");
        Intent i = new Intent(Main.this, WizardPetActivity.class);
        startActivityForResult(i, NEW_PET_REQUEST);
        // changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.searc_pet_now)
    void searc_pet_now() {
        Log.e("CHANGE_INSTANCE", "profile pet");
        Intent i = new Intent(Main.this, SearchPetActivity.class);
        startActivityForResult(i, NEW_PET_REQUEST);
        // changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tap_tips)
    void tap_tips() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_TIPS) {
            changeOfInstance(FragmentElement.INSTANCE_TIPS);
            repaint_nav(R.id.tap_tips);
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_top)
    void tab_top() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_FEED) {
            changeOfInstance(FragmentElement.INSTANCE_FEED);
            repaint_nav(R.id.tab_top);
        }
    }






    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_p)
    void tab_p() {
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
        stack_tips = new Stack<>();
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
            if (stack_feed.size() <= 0) {
                stack_feed.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_profile_pet(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_profile_pet.size() <= 0) {
                stack_profile_pet.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_tips(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_tips.size() <= 0) {
                stack_tips.push(mCurrentFragment);
            }
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
        } else if (intanceType == FragmentElement.INSTANCE_PROFILE_PET) {
            if (stack_profile_pet.size() == 0) {
                change_profile_pet(new FragmentElement<>("", FragmentProfileUserPet.newInstance(), FragmentElement.INSTANCE_PROFILE_PET));
            } else {
                change_profile_pet(stack_profile_pet.pop());
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_TIPS) {
            if (stack_tips.size() == 0) {
                change_tips(new FragmentElement<>("", FragmentTips.newInstance(), FragmentElement.INSTANCE_TIPS));
            } else {
                change_tips(stack_tips.pop());
            }
        }


    }

    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (mOldFragment != null) {
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

            } else {
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_main, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_PET_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,"Pet configurado",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void repaint_nav(int id ){
        icon_tips.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet));
        icon_feed.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet));
        icon_chat.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet));
        icon_profile_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet));

        if(id == R.id.tap_tips)
            icon_tips.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet_black));
        else if(id == R.id.tab_profile_pet)
            icon_feed.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet_black));
        else if(id == R.id.tab_p)
            icon_chat.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet_black));
        else if(id == R.id.tab_top)
            icon_profile_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet_black));

    }

}
