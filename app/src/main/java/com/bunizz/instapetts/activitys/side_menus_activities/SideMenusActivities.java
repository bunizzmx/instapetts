package com.bunizz.instapetts.activitys.side_menus_activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.notifications.NotificationsFragment;
import com.bunizz.instapetts.fragments.post.FragmentListOfPosts;
import com.bunizz.instapetts.fragments.previewProfile.FragmentProfileUserPetPreview;
import com.bunizz.instapetts.fragments.profile.FragmentEditProfileUser;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.FragmentSearchPet;
import com.bunizz.instapetts.fragments.search.posts.FragmentPostPublics;
import com.bunizz.instapetts.fragments.side_menus_activities.FragmentAdministrateAccount;
import com.bunizz.instapetts.fragments.side_menus_activities.FragmentConfigEmail;
import com.bunizz.instapetts.fragments.side_menus_activities.FragmentConfigPhone;
import com.bunizz.instapetts.fragments.side_menus_activities.FragmentNotificacionesConfig;
import com.bunizz.instapetts.fragments.side_menus_activities.FragmentWebTerms;
import com.bunizz.instapetts.fragments.side_menus_activities.postsSaved.FragmentPostPublicsSaved;
import com.bunizz.instapetts.fragments.tips.FragmentTipDetail;
import com.bunizz.instapetts.fragments.tips.FragmentTips;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.bottom_sheet.SlidingUpPanelLayout;

import java.util.Stack;

public class SideMenusActivities extends AppCompatActivity implements changue_fragment_parameters_listener {

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;
    private Stack<FragmentElement> stack_web_terms;
    private Stack<FragmentElement> stack_admin_account;
    private Stack<FragmentElement> stack_config_phone;
    private Stack<FragmentElement> stack_config_email;
    private Stack<FragmentElement> stack_config_password;
    private Stack<FragmentElement> saved_post;
    private Stack<FragmentElement> stack_push;
    int TYPE_MENU =0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_menu_activities);
        stack_web_terms = new Stack<>();
        stack_admin_account = new Stack<>();
        stack_config_email = new Stack<>();
        stack_config_phone = new Stack<>();
        stack_config_password = new Stack<>();
        stack_push= new Stack<>();
        saved_post = new Stack<>();
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            TYPE_MENU = b.getInt("TYPE_MENU");
        }
        changeStatusBarColor(R.color.white);
        setupFirstFragment(TYPE_MENU);
    }

    private void setupFirstFragment(int type_fragment) {
        Bundle b = new Bundle();
        switch (type_fragment){
            case 0:
                mCurrentFragment = new FragmentElement<>(null, FragmentWebTerms.newInstance(), FragmentElement.INSTANCE_WEB_TERMS, true);
                change_to_terms(mCurrentFragment,null);
                break;
            case 1:
                mCurrentFragment = new FragmentElement<>(null, FragmentAdministrateAccount.newInstance(), FragmentElement.INSTANCE_ADMINISTRATE_ACCOUNT, true);
                change_to_admin(mCurrentFragment,null);
                break;
            case 2:
                mCurrentFragment = new FragmentElement<>(null, FragmentPostPublicsSaved.newInstance(), FragmentElement.INSTANCE_GET_POSTS_PUBLICS, true);
                b.putInt("SAVED_POST",1);
                change_to_saved_posts(mCurrentFragment,b);
                break;

            case 3:
                mCurrentFragment = new FragmentElement<>(null, FragmentNotificacionesConfig.newInstance(), FragmentElement.INSTANCE_COONFIG_PUSH, true);
                change_to_push(mCurrentFragment,b);
                break;

        }


    }


    private synchronized void changeOfInstance(int intanceType,Bundle bundle) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_WEB_TERMS) {
            if (stack_web_terms.size() == 0) {
                change_to_terms(new FragmentElement<>("", FragmentWebTerms.newInstance(), FragmentElement.INSTANCE_WEB_TERMS),bundle);
            } else {
                change_to_terms(stack_web_terms.pop(),bundle);
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_ADMINISTRATE_ACCOUNT) {
            if (stack_admin_account.size() == 0) {
                change_to_admin(new FragmentElement<>("", FragmentAdministrateAccount.newInstance(), FragmentElement.INSTANCE_ADMINISTRATE_ACCOUNT),bundle);
            } else {
                change_to_admin(stack_admin_account.pop(),bundle);
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_ADMINISTRATE_PHONE) {
            if (stack_config_phone.size() == 0) {
                change_to_phone(new FragmentElement<>("", FragmentConfigPhone.newInstance(), FragmentElement.INSTANCE_ADMINISTRATE_PHONE),bundle);
            } else {
                change_to_phone(stack_config_phone.pop(),bundle);
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_ADMINISTRATE_EMAIL) {
            if (stack_config_email.size() == 0) {
                change_to_email(new FragmentElement<>("", FragmentConfigEmail.newInstance(), FragmentElement.INSTANCE_ADMINISTRATE_EMAIL),bundle);
            } else {
                change_to_email(stack_config_email.pop(),bundle);
            }
        }


    }

    private void change_to_terms(FragmentElement fragment,Bundle bundle) {
        Log.e("INFLATE_TERMS","siii");
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(bundle!=null){
                mCurrentFragment.getFragment().setArguments(bundle);
            }
            if (stack_web_terms.size() <= 0) {
                stack_web_terms.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_to_saved_posts(FragmentElement fragment,Bundle bundle) {
        Log.e("INFLATE_TERMS","siii");
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(bundle!=null){
                mCurrentFragment.getFragment().setArguments(bundle);
            }
            if (saved_post.size() <= 0) {
                saved_post.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_to_push(FragmentElement fragment,Bundle bundle) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(bundle!=null){
                mCurrentFragment.getFragment().setArguments(bundle);
            }
            if (stack_push.size() <= 0) {
                stack_push.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_to_phone(FragmentElement fragment,Bundle bundle) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(bundle!=null){
                mCurrentFragment.getFragment().setArguments(bundle);
            }
            if (stack_config_phone.size() <= 0) {
                stack_config_phone.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_to_email(FragmentElement fragment,Bundle bundle) {
        Log.e("INFLATE_TERMS","siii");
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(bundle!=null){
                mCurrentFragment.getFragment().setArguments(bundle);
            }
            if (stack_config_email.size() <= 0) {
                stack_config_email.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }


    private void change_to_admin(FragmentElement fragment,Bundle bundle) {
        Log.e("INFLATE_TERMS","siii");
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(bundle!=null){
                mCurrentFragment.getFragment().setArguments(bundle);
            }
            if (stack_admin_account.size() <= 0) {
                stack_admin_account.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }


    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (mOldFragment != null) {
                if (mCurrentFragment.getFragment().isAdded()) {
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .show(mCurrentFragment.getFragment()).commit();
                } else {
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .add(R.id.root_side_terms, mCurrentFragment.getFragment()).commit();
                }

            } else {
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_side_terms, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {
        }
    }

    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), color));
        }
    }

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
         changeOfInstance(type_fragment,data);
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_WEB_TERMS || mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_ADMINISTRATE_ACCOUNT){
            finish();
        }
        else if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_ADMINISTRATE_ACCOUNT){
            finish();
           // changeOfInstance(FragmentElement.INSTANCE_ADMINISTRATE_ACCOUNT,null);
        }
    }

}
