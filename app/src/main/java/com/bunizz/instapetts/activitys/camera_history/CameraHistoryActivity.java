package com.bunizz.instapetts.activitys.camera_history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.camera.CameraFragment;
import com.bunizz.instapetts.fragments.camera.CameraPreviewStoryFragment;
import com.bunizz.instapetts.fragments.camera.CameraStoryt;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.fragments.share_post.Picker.image.FragmentPickerGalery;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.hisotry_listener;
import com.bunizz.instapetts.listeners.login_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.utils.ViewExtensionsKt;

import java.util.Stack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_LOGUEDD;
import static com.bunizz.instapetts.utils.ViewExtensionsKt.FLAGS_FULLSCREEN;

public class CameraHistoryActivity extends AppCompatActivity implements  changue_fragment_parameters_listener, hisotry_listener,uploads {

    private Stack<FragmentElement> stack_history_camera;
    private Stack<FragmentElement> stack_history_foto;
    private Stack<FragmentElement> stack_history_picker_foto;


    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    @BindView(R.id.root_inmersive)
    RelativeLayout root_inmersive;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        changeStatusBarColor(R.color.white);
        App.write(PREFERENCES.FROM_PICKER,"PROFILE");

        stack_history_foto = new Stack<>();
        stack_history_camera = new Stack<>();
        stack_history_picker_foto = new Stack<>();
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
        mCurrentFragment = new FragmentElement<>(null, CameraStoryt.newInstace(), FragmentElement.INSTANCE_HISTORY_CAMERA, true);
        changue_camera(mCurrentFragment);
    }

    private void changue_camera(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_history_camera.size()<=0){stack_history_camera.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_foto(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if(stack_history_foto.size()<=0){stack_history_foto.push(mCurrentFragment);}
        }
        inflateFragment();
    }

    private void change_to_picker(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            Bundle b = new Bundle();
            b.putInt("FROM_PROFILE",2);
            mCurrentFragment.getFragment().setArguments(b);
            if(stack_history_picker_foto.size()<=0){stack_history_picker_foto.push(mCurrentFragment);}
        }
        inflateFragment();
    }


    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle data) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_HISTORY_CAMERA) {
            if (stack_history_camera.size() == 0) {
                changue_camera(new FragmentElement<>("", CameraStoryt.newInstace(), FragmentElement.INSTANCE_HISTORY_CAMERA));
            } else {
                changue_camera(stack_history_camera.pop());
            }
        }else if(intanceType == FragmentElement.INSTANCE_HISTORY_FOTO_PICKED) {
            if (mCurrentFragment.getFragment() instanceof CameraStoryt) {
                Log.e("DETENGO_CAMERA","si");
                ((CameraStoryt) mCurrentFragment.getFragment()).stop_camera();
            }else{
                Log.e("DETENGO_CAMERA","NO");
            }
            if (stack_history_foto.size() == 0) {
                change_foto(new FragmentElement<>("", CameraPreviewStoryFragment.newInstance(), FragmentElement.INSTANCE_HISTORY_FOTO_PICKED),data);
            } else {
                change_foto(stack_history_foto.pop(),data);
            }

        }else if(intanceType == FragmentElement.INSTANCE_PICKER_IMAGES) {
            if (mCurrentFragment.getFragment() instanceof CameraStoryt) {
                Log.e("DETENGO_CAMERA","si");
                ((CameraStoryt) mCurrentFragment.getFragment()).stop_camera();
            }else{
                Log.e("DETENGO_CAMERA","NO");
            }
            if (stack_history_picker_foto.size() == 0) {
                change_to_picker(new FragmentElement<>("", FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_PICKER_IMAGES));
            } else {
                change_to_picker(stack_history_picker_foto.pop());
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
        if(mCurrentFragment.getInstanceType() ==  FragmentElement.INSTANCE_HISTORY_CAMERA){
            finish();
        }else{
            changeOfInstance(FragmentElement.INSTANCE_HISTORY_CAMERA,null);
        }

    }

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
         changeOfInstance(type_fragment,data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        root_inmersive.postDelayed(() -> root_inmersive.setSystemUiVisibility(ViewExtensionsKt.FLAGS_FULLSCREEN ),500);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void chose_complete(Bundle bundle) {
        Intent data = new Intent();
        data.putExtra(BUNDLES.URI_FOTO,bundle.getString(BUNDLES.PATH_SELECTED));
        data.putExtra(BUNDLES.ID_PET,bundle.getInt(BUNDLES.ID_PET));
        data.putExtra(BUNDLES.NAME_PET,bundle.getString(BUNDLES.NAME_PET));
        data.putExtra(BUNDLES.URL_PHOTO_PET,bundle.getString(BUNDLES.URL_PHOTO_PET));
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public void onImageProfileUpdated() {

    }

    @Override
    public void setResultForOtherChanges(String url) {

    }

    @Override
    public void UpdateProfile(Bundle bundle) {

    }

    @Override
    public void new_pet() {

    }
}