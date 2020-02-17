package com.bunizz.instapetts.activitys.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.info.InfoPetFragment;
import com.bunizz.instapetts.fragments.notifications.NotificationsFragment;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.FragmentSearchPet;
import com.bunizz.instapetts.fragments.tips.FragmentTips;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.utils.bottom_sheet.SlidingUpPanelLayout;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Main extends AppCompatActivity implements change_instance {


    private Stack<FragmentElement> stack_feed;
    private Stack<FragmentElement> stack_profile_pet;
    private Stack<FragmentElement> stack_tips;
    private Stack<FragmentElement> stack_serch_pet;
    private Stack<FragmentElement> stack_notifications;

    private FragmentElement mCurrentFragment;

    private FragmentElement mCurrenSheet;
    private FragmentElement mOldFragment;
    static final int NEW_PET_REQUEST = 1;



    @BindView(R.id.icon_tips)
    ImageView icon_tips;
    @BindView(R.id.icon_feed_pet)
    ImageView icon_feed_pet;
    @BindView(R.id.icon_add_image_pet)
    ImageView icon_add_image_pet;
    @BindView(R.id.icon_search_pet)
    ImageView icon_search_pet;
    @BindView(R.id.icon_profile_pet)
    ImageView icon_profile_pet;


    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_profile_pet)
    void tab_profile_pet() {
       /* Log.e("CHANGE_INSTANCE", "profile pet");
        Intent i = new Intent(Main.this, WizardPetActivity.class);
        startActivityForResult(i, NEW_PET_REQUEST);
         changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);*/
        changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
        repaint_nav(R.id.tab_profile_pet);
    }

    /*@SuppressLint("MissingPermission")
    @OnClick(R.id.searc_pet_now)
    void searc_pet_now() {
        Log.e("CHANGE_INSTANCE", "profile pet");
        Intent i = new Intent(Main.this, FragmentSearchPet.class);
        startActivityForResult(i, NEW_PET_REQUEST);
        // changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
    }*/

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tap_tips)
    void tap_tips() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_TIPS) {
            changeOfInstance(FragmentElement.INSTANCE_TIPS);
            repaint_nav(R.id.tap_tips);
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_add_image)
    void tab_add_image() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_FEED) {
            changeOfInstance(FragmentElement.INSTANCE_FEED);
            repaint_nav(R.id.tab_feed);
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_feed)
    void tab_feed() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_FEED) {
            changeOfInstance(FragmentElement.INSTANCE_FEED);
            repaint_nav(R.id.tab_feed);
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_search_pet)
    void tab_search_pet() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_SEARCH) {
            changeOfInstance(FragmentElement.INSTANCE_SEARCH);
            repaint_nav(R.id.tab_search_pet);
        }
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
        stack_serch_pet = new Stack<>();
        stack_notifications = new Stack<>();
        setupFirstFragment();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                } else if (newState == SlidingUpPanelLayout.PanelState.HIDDEN) {


                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {

                }
            }
        });
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
        repaint_nav(R.id.tab_feed);
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

    private void change_notifications(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_notifications.size() <= 0) {
                stack_notifications.push(mCurrentFragment);
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

    private void change_search_pet(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_serch_pet.size() <= 0) {
                stack_serch_pet.push(mCurrentFragment);
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
        else if (intanceType == FragmentElement.INSTANCE_SEARCH) {
            if (stack_serch_pet.size() == 0) {
                change_search_pet(new FragmentElement<>("", FragmentSearchPet.newInstance(), FragmentElement.INSTANCE_SEARCH));
            } else {
                change_search_pet(stack_serch_pet.pop());
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_NOTIFICATIONS) {
            if (stack_notifications.size() == 0) {
                change_notifications(new FragmentElement<>("", NotificationsFragment.newInstance(), FragmentElement.INSTANCE_NOTIFICATIONS));
            } else {
                change_notifications(stack_notifications.pop());
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
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .show(mCurrentFragment.getFragment()).commit();
                } else {
                    fragmentManager
                            .beginTransaction()
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
        changeOfInstance(fragment_element);
    }

    @Override
    public void onback() {
        changeOfInstance(FragmentElement.INSTANCE_FEED);
    }

    @Override
    public void open_sheet() {
        changue_instance_sheet();
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void onBackPressed() {
        changeOfInstance(FragmentElement.INSTANCE_FEED);
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
        icon_profile_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hand_pet));
        icon_add_image_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_add_image));
        icon_feed_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet));
        icon_search_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_search));

        if(id == R.id.tap_tips)
            icon_tips.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet_black));
        else if(id == R.id.tab_profile_pet)
            icon_profile_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hand_pet_black));
        else if(id == R.id.tab_feed)
            icon_feed_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet_black));
        else if(id == R.id.tab_search_pet)
            icon_search_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_search_black));
        else if(id == R.id.tab_add_image)
            icon_add_image_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_add_image_black));
    }

    void changue_instance_sheet(){
        changue_sheet(new FragmentElement<>("", InfoPetFragment.newInstance(), FragmentElement.INSTANCE_PROFILE_PET));
    }

    private void changue_sheet(FragmentElement fragment) {
        if (fragment != null) {
            mCurrenSheet = fragment;
        }
        inflate_sheet();
    }

    @SuppressLint("RestrictedApi")
    private synchronized void inflate_sheet() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_scrool, mCurrenSheet.getFragment()).commit();

        } catch (IllegalStateException ignored) {
        }
    }

}
