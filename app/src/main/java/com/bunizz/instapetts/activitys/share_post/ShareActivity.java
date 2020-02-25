package com.bunizz.instapetts.activitys.share_post;

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
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.share_post.Picker.FragmentPickerGalery;
import com.bunizz.instapetts.fragments.share_post.Share.FragmentSharePost;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class ShareActivity extends AppCompatActivity implements changue_fragment_parameters_listener {

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    private Stack<FragmentElement> stack_share;
    private Stack<FragmentElement> stack_picker;
    RxPermissions rxPermissions ;
    ArrayList<String> paths_themp = new ArrayList<>();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);
        rxPermissions = new RxPermissions(this);
        changeStatusBarColor(R.color.white);
        stack_picker = new Stack<>();
        stack_share = new Stack<>();
        setupFirstFragment();
        rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {

                    } else {
                        App.getInstance().show_dialog_permision(ShareActivity.this,getResources().getString(R.string.permision_storage),
                                getResources().getString(R.string.permision_storage_body),0);
                    }
                });
    }


    private void setupFirstFragment() {
        mCurrentFragment = new FragmentElement<>(null, FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_PICKER, true);
        change_picker(mCurrentFragment,null);
    }


    private synchronized void changeOfInstance(int intanceType,Bundle bundle) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_PICKER) {
            if (stack_picker.size() == 0) {
                change_picker(new FragmentElement<>("", FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_PICKER),bundle);
            } else {
                change_picker(stack_picker.pop(),bundle);
            }
        } else if (intanceType == FragmentElement.INSTANCE_SHARE) {
            if (stack_share.size() == 0) {
                change_share(new FragmentElement<>("", FragmentSharePost.newInstance(), FragmentElement.INSTANCE_SHARE),bundle);
            } else {
                change_share(stack_share.pop(),bundle);
            }
        }
    }


    private void change_share(FragmentElement fragment,Bundle bundle) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            paths_themp.clear();
            paths_themp.addAll(bundle.getStringArrayList("data_pahs"));
            mCurrentFragment.getFragment().setArguments(bundle);
            if (stack_share.size() <= 0) {
                stack_share.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_picker(FragmentElement fragment,Bundle bundle) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(bundle);
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

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
        changeOfInstance(type_fragment,data);
    }


    @Override
    public void onBackPressed() {
        if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_PICKER){
           finish();
        }else{
            Log.e("VERIFICACION","VERIFICO SI HAY PATHS PENDIENTES");
            if(paths_themp.size()>0){
                delete_files();
                changeOfInstance(FragmentElement.INSTANCE_PICKER,null);
            }

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    void delete_files(){
        File file;
        for (int i =0;i < paths_themp.size();i++){
            file = new File(paths_themp.get(i));
            Log.e("DELETED_FILE","---> SI" );
            file.delete();
        }
    }


    void set_results_uris(){
        Intent data = new Intent();
        data.putExtra("URIS_PATHS",false);
        setResult(RESULT_OK,data);
    }
}
