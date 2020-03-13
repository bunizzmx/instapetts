package com.bunizz.instapetts.activitys.wizardPets;

import android.Manifest;
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

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.share_post.ShareActivity;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.profile.FragmentEditProfileUser;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.wizardPets.FragmentDataPet;
import com.bunizz.instapetts.fragments.wizardPets.FragmentFinalConfigPet;
import com.bunizz.instapetts.fragments.wizardPets.FragmentSearchPet;
import com.bunizz.instapetts.fragments.wizardPets.FragmentTypePet;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.process_save_pet_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Stack;

import butterknife.ButterKnife;

public class WizardPetActivity extends AppCompatActivity implements change_instance_wizard, process_save_pet_listener, uploads {

    private Stack<FragmentElement> stack_type_pet;
    private Stack<FragmentElement> stack_search_raza_pet;
    private Stack<FragmentElement> stack_data_pet;
    private Stack<FragmentElement> final_config_pet;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;
    PetHelper petHelper;
    PetBean infoPet = new PetBean();
    RxPermissions rxPermissions;
    static final int NEW_PHOTO_UPLOADED= 3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_pet);
        changeStatusBarColor(R.color.white);
        ButterKnife.bind(this);
        stack_data_pet = new Stack<>();
        stack_type_pet = new Stack<>();
        stack_search_raza_pet = new Stack<>();
        final_config_pet = new Stack<>();
        setupFirstFragment();
        petHelper = PetHelper.getInstance(this);
        rxPermissions = new RxPermissions(this);


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

    private void change_final_config_pet(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(final_config_pet.size()<=0){final_config_pet.push(mCurrentFragment);}
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
                change_first(new FragmentElement<>("", FragmentTypePet.newInstance(), FragmentElement.INSTANCE_TYPE_PET));
            } else {
                change_first(stack_type_pet.pop());
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_TYPE_SEARCH_RAZA) {
            if (stack_search_raza_pet.size() == 0) {
                change_search_raza(new FragmentElement<>("", FragmentSearchPet.newInstance(), FragmentElement.INSTANCE_TYPE_SEARCH_RAZA));
            } else {
                change_search_raza(stack_search_raza_pet.pop());
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_DATA_PET) {
            if (stack_data_pet.size() == 0) {
                change_data_pet(new FragmentElement<>("", FragmentDataPet.newInstance(), FragmentElement.INSTANCE_DATA_PET));
            } else {
                change_data_pet(stack_data_pet.pop());
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_FINAL_CONFIG_PET) {
            if (final_config_pet.size() == 0) {
                change_final_config_pet(new FragmentElement<>("", FragmentFinalConfigPet.newInstance(), FragmentElement.INSTANCE_FINAL_CONFIG_PET));
            } else {
                change_final_config_pet(final_config_pet.pop());
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
                            .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
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
           if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_DATA_PET){
               changeOfInstance(FragmentElement.INSTANCE_TYPE_SEARCH_RAZA,null);
           }
        else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_TYPE_SEARCH_RAZA){
            changeOfInstance(FragmentElement.INSTANCE_TYPE_PET,null);
        }
        else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_TYPE_PET){
               Intent data = new Intent();
               data.putExtra("pet_saved",false);
               setResult(RESULT_OK,data);
               finish();
        }
           else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_FINAL_CONFIG_PET){
               changeOfInstance(FragmentElement.INSTANCE_DATA_PET,null);
           }
    }

    @Override
    public void onchange(int type_fragment, Bundle data) {
        changeOfInstance(type_fragment,data);
    }

    @Override
    public void onpetFinish(boolean pet_saved) {
        Intent data = new Intent();
        data.putExtra("pet_saved",true);
        setResult(RESULT_OK,data);
        finish();
    }


    @Override
    public void SaveDataPet(Bundle b, int donde) {
        switch (donde){
            case 1:
                  infoPet.setType_pet(b.getInt("TYPE_PET"));
                  infoPet.setRate_pet("0");
                  infoPet.setDescripcion_pet("");
                  infoPet.setUrl_photo("");
                break;
            case 2:
                infoPet.setRate_pet(b.getString("RAZA_PET"));
                break;
            case 3:
                infoPet.setEdad_pet(b.getString("EDAD_PET"));
                infoPet.setGenero_pet(b.getString("GENERO_PET"));
                infoPet.setPeso_pet(b.getString("RAZA_PET"));
                break;
            default:break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onImageProfileUpdated() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        App.write(PREFERENCES.FROM_PICKER,"PROFILE");
                        Intent i = new Intent(WizardPetActivity.this, ShareActivity.class);
                        i.putExtra("FROM","PROFILE_PHOTO");
                        startActivityForResult(i,NEW_PHOTO_UPLOADED);
                    } else {
                        App.getInstance().show_dialog_permision(WizardPetActivity.this,getResources().getString(R.string.permision_storage),
                                getResources().getString(R.string.permision_storage_body),0);
                    }
                });
    }

    @Override
    public void setResultForOtherChanges(String url) {

    }

    @Override
    public void UpdateProfile(Bundle bundle) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == NEW_PHOTO_UPLOADED){
            if(data!=null) {
                String url =  data.getStringExtra(BUNDLES.URI_FOTO);
                if (mCurrentFragment.getFragment() instanceof FragmentFinalConfigPet) {
                    ((FragmentFinalConfigPet) mCurrentFragment.getFragment()).change_image_profile(url);
                }
            }
        }

    }
}
