package com.bunizz.instapetts.activitys.wizardPets;

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
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.wizardPets.FragmentDataPet;
import com.bunizz.instapetts.fragments.wizardPets.FragmentSearchPet;
import com.bunizz.instapetts.fragments.wizardPets.FragmentTypePet;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.change_instance_wizard;

import java.util.Stack;

import butterknife.ButterKnife;

public class WizardPetActivity extends AppCompatActivity implements change_instance_wizard {

    private Stack<FragmentElement> stack_type_pet;
    private Stack<FragmentElement> stack_search_raza_pet;
    private Stack<FragmentElement> stack_data_pet;


    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_pet);
        changeStatusBarColor(R.color.white);
        ButterKnife.bind(this);
        stack_data_pet = new Stack<>();
        stack_type_pet = new Stack<>();
        stack_search_raza_pet = new Stack<>();
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
        mCurrentFragment = new FragmentElement<>(null, FragmentTypePet.newInstance(), FragmentElement.INSTANCE_TYPE_PET, true);
        change_first(mCurrentFragment);
    }


    private void change_first(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_type_pet.size()<=0){stack_type_pet.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_search_raza(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_search_raza_pet.size()<=0){stack_search_raza_pet.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_data_pet(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_data_pet.size()<=0){stack_data_pet.push(mCurrentFragment);}
        }
        inflateFragment();
    }


    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle data) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_TYPE_PET) {
            if (stack_type_pet.size() == 0) {
                change_first(new FragmentElement<>("", FragmentTypePet.newInstance(), FragmentElement.INSTANCE_LOGIN));
            } else {
                change_first(stack_type_pet.pop());
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_TYPE_SEARCH_RAZA) {
            if (stack_search_raza_pet.size() == 0) {
                change_search_raza(new FragmentElement<>("", FragmentSearchPet.newInstance(), FragmentElement.INSTANCE_PROFILE_PET));
            } else {
                change_search_raza(stack_search_raza_pet.pop());
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_DATA_PET) {
            if (stack_data_pet.size() == 0) {
                change_data_pet(new FragmentElement<>("", FragmentDataPet.newInstance(), FragmentElement.INSTANCE_PROFILE_PET));
            } else {
                change_data_pet(stack_data_pet.pop());
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
                            .add(R.id.root_wizard, mCurrentFragment.getFragment()).commit();
                }

            }else{
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_wizard, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {}
    }



    @Override
    public void onBackPressed() {

    }

    @Override
    public void onchange(int type_fragment, Bundle data) {
        changeOfInstance(type_fragment,data);
    }
}
