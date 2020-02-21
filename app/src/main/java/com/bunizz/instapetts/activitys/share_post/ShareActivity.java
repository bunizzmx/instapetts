package com.bunizz.instapetts.activitys.share_post;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.notifications.NotificationsFragment;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.FragmentSearchPet;
import com.bunizz.instapetts.fragments.share_post.FragmentPickerGalery;
import com.bunizz.instapetts.fragments.tips.FragmentTipDetail;
import com.bunizz.instapetts.fragments.tips.FragmentTips;

import java.util.Stack;

public class ShareActivity extends AppCompatActivity {

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    private Stack<FragmentElement> stack_share;
    private Stack<FragmentElement> stack_picker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);
        changeStatusBarColor(R.color.white);
        stack_picker = new Stack<>();
        stack_share = new Stack<>();
        setupFirstFragment();
    }


    private void setupFirstFragment() {
        mCurrentFragment = new FragmentElement<>(null, FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_PICKER, true);
        change_picker(mCurrentFragment);
    }


    private synchronized void changeOfInstance(int intanceType) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_PICKER) {
            if (stack_picker.size() == 0) {
                change_picker(new FragmentElement<>("", FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_PICKER));
            } else {
                change_picker(stack_picker.pop());
            }
        } else if (intanceType == FragmentElement.INSTANCE_SHARE) {
            if (stack_share.size() == 0) {
                change_share(new FragmentElement<>("", FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_SHARE));
            } else {
                change_share(stack_share.pop());
            }
        }
    }


    private void change_share(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_share.size() <= 0) {
                stack_share.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_picker(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_picker.size() <= 0) {
                stack_picker.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void saveFragment() {
        mOldFragment = mCurrentFragment;
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
                            .add(R.id.root_share, mCurrentFragment.getFragment()).commit();
                }

            } else {
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_share, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {
        }
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

}
