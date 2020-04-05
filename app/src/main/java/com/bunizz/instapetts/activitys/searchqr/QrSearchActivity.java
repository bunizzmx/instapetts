package com.bunizz.instapetts.activitys.searchqr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.camera.CameraPreviewStoryFragment;
import com.bunizz.instapetts.fragments.camera.CameraStoryt;
import com.bunizz.instapetts.fragments.previewProfile.FragmentProfileUserPetPreview;
import com.bunizz.instapetts.fragments.qr.FragmentDetectQR;
import com.bunizz.instapetts.fragments.qr.FragmentMyQRPreview;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.utils.ViewExtensionsKt;
import com.bunizz.instapetts.utils.dilogs.DialogQrResult;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;



public class QrSearchActivity extends AppCompatActivity implements changue_fragment_parameters_listener, change_instance {

    private Stack<FragmentElement> stack_qr;
    private Stack<FragmentElement> stack_qr_my_pet;
    private Stack<FragmentElement> stack_preview_perfil;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    @BindView(R.id.root_inmersive)
    RelativeLayout root_inmersive;

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        changeStatusBarColor(R.color.white);
        stack_qr = new Stack<>();
        stack_qr_my_pet = new Stack<>();
        stack_preview_perfil = new Stack<>();
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
        mCurrentFragment = new FragmentElement<>(null, FragmentMyQRPreview.newInstance(), FragmentElement.INSTANCE_VIEW_MY_QR, true);
        change_my_qr(mCurrentFragment);
    }

    private void change_my_qr(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_qr_my_pet.size()<=0){stack_qr_my_pet.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_detect_qr(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if(stack_qr.size()<=0){stack_qr.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_to_preview(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if(stack_qr.size()<=0){stack_qr.push(mCurrentFragment);}
        }
        inflateFragment();
    }


    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle data) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_VIEW_MY_QR) {
            if (stack_qr_my_pet.size() == 0) {
                change_my_qr(new FragmentElement<>("", FragmentMyQRPreview.newInstance(), FragmentElement.INSTANCE_VIEW_MY_QR));
            } else {
                change_my_qr(stack_qr_my_pet.pop());
            }
        }else if(intanceType == FragmentElement.INSTANCE_DETECT_QR) {
            if (stack_qr.size() == 0) {
                change_detect_qr(new FragmentElement<>("", FragmentDetectQR.newInstance(), FragmentElement.INSTANCE_DETECT_QR),data);
            } else {
                change_detect_qr(stack_qr.pop(),data);
            }

        }
        else if(intanceType == FragmentElement.INSTANCE_PREVIEW_PROFILE) {
            if (stack_preview_perfil.size() == 0) {
                change_to_preview(new FragmentElement<>("", FragmentProfileUserPetPreview.newInstance(), FragmentElement.INSTANCE_PREVIEW_PROFILE),data);
            } else {
                change_to_preview(stack_preview_perfil.pop(),data);
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
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .show(mCurrentFragment.getFragment()).commit();
                } else {
                    fragmentManager
                            .beginTransaction()
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
    public void onBackPressed() {
        if(mCurrentFragment.getInstanceType() ==  FragmentElement.INSTANCE_VIEW_MY_QR){
            finish();
        }else if(mCurrentFragment.getInstanceType() ==  FragmentElement.INSTANCE_PREVIEW_PROFILE){
            changeOfInstance(FragmentElement.INSTANCE_VIEW_MY_QR,null);
        }

    }

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
        changeOfInstance(type_fragment,data);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void change(int fragment_element) {

    }

    @Override
    public void onback() {

    }

    @Override
    public void open_sheet(PetBean petBean, int is_me) {

    }



    @Override
    public void open_wizard_pet() {

    }

}
