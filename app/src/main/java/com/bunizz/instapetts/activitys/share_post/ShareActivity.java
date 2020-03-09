package com.bunizz.instapetts.activitys.share_post;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.camera.CameraFragment;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.share_post.ContainerFragmentsShare;
import com.bunizz.instapetts.fragments.share_post.Picker.image.FragmentPickerGalery;
import com.bunizz.instapetts.fragments.share_post.Picker.video.FragmentPickerVideo;
import com.bunizz.instapetts.fragments.share_post.Share.FragmentSharePost;
import com.bunizz.instapetts.fragments.share_post.cropImages.ImageCropFragment;
import com.bunizz.instapetts.fragments.share_post.cropVideo.VideoCropFragment;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends AppCompatActivity implements changue_fragment_parameters_listener, uploads {

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    private Stack<FragmentElement> stack_share;
    private Stack<FragmentElement> stack_picker_images;
    private Stack<FragmentElement> stack_picker_videos;
    private Stack<FragmentElement> stack_picker_camera;
    private Stack<FragmentElement> stack_crop_video;
    private Stack<FragmentElement> stack_crop_images;
    RxPermissions rxPermissions ;
    ArrayList<String> paths_themp = new ArrayList<>();
    boolean is_single_selection = false;
    boolean is_from_profile = false;

    @BindView(R.id.tabs_camera)
    LinearLayout tabs_camera;



    @OnClick(R.id.changue_to_pictures)
    void changue_to_pictures()
    {
        if (mCurrentFragment.getFragment() instanceof CameraFragment) {
            ((CameraFragment) mCurrentFragment.getFragment()).stop_camera();
        }
        changeOfInstance(FragmentElement.INSTANCE_PICKER_IMAGES,null);
    }

    @OnClick(R.id.changue_to_videos)
    void changue_to_videos()
    {
        if (mCurrentFragment.getFragment() instanceof CameraFragment) {
            ((CameraFragment) mCurrentFragment.getFragment()).stop_camera();
        }
        changeOfInstance(FragmentElement.INSTANCE_PICKER_VIDEOS,null);
    }

    @OnClick(R.id.changue_to_camera)
    void changue_to_camera()
    {
        changeOfInstance(FragmentElement.INSTANCE_PICKER_CAMERA,null);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);
        ButterKnife.bind(this);
        rxPermissions = new RxPermissions(this);
        changeStatusBarColor(R.color.white);
        stack_picker_videos = new Stack<>();
        stack_picker_images= new Stack<>();
        stack_picker_camera = new Stack<>();
        stack_crop_video = new Stack<>();
        stack_crop_images = new Stack<>();
        stack_share = new Stack<>();   Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            String j =(String) b.get("FROM");
            if(j.equals("PROFILE_PHOTO")){
                Log.e("SOLO1","foto");
                is_single_selection = true;
                is_from_profile = true;
            }
        }
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
        mCurrentFragment = new FragmentElement<>(null, FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_PICKER_IMAGES, true);
        change_picker(mCurrentFragment,null);
    }


    private synchronized void changeOfInstance(int intanceType,Bundle bundle) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_PICKER_IMAGES) {
            if (stack_picker_images.size() == 0) {
                change_picker(new FragmentElement<>("", FragmentPickerGalery.newInstance(), FragmentElement.INSTANCE_PICKER_IMAGES),bundle);
            } else {
                change_picker(stack_picker_images.pop(),bundle);
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_PICKER_VIDEOS) {
            if (stack_picker_videos.size() == 0) {
                change_picker_video(new FragmentElement<>("", FragmentPickerVideo.newInstance(), FragmentElement.INSTANCE_PICKER_VIDEOS),bundle);
            } else {
                change_picker_video(stack_picker_videos.pop(),bundle);
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_PICKER_CAMERA) {
            if (stack_picker_camera.size() == 0) {
                change_picker_camera(new FragmentElement<>("", CameraFragment.newInstace(), FragmentElement.INSTANCE_PICKER_CAMERA),bundle);
            } else {
                change_picker_camera(stack_picker_camera.pop(),bundle);
            }
        }
        else if (intanceType == FragmentElement.INSTANCE_SHARE) {
             Log.e("CHANGE_SHARE","yes");
            if (stack_share.size() == 0) {
                change_share(new FragmentElement<>("", FragmentSharePost.newInstance(), FragmentElement.INSTANCE_SHARE),bundle);
            } else {
                change_share(stack_share.pop(),bundle);
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_CROP_VIDEO) {

            if (stack_crop_video.size() == 0) {
                change_to_crop_video(new FragmentElement<>("", VideoCropFragment.newInstance(), FragmentElement.INSTANCE_CROP_VIDEO),bundle);
            } else {
                change_to_crop_video(stack_crop_video.pop(),bundle);
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_CROP_IMAGE) {

            if (stack_crop_images.size() == 0) {
                change_to_crop_image(new FragmentElement<>("", ImageCropFragment.newInstance(), FragmentElement.INSTANCE_CROP_IMAGE),bundle);
            } else {
                change_to_crop_image(stack_crop_images.pop(),bundle);
            }
        }

    }

    private void change_to_crop_video(FragmentElement fragment,Bundle bundle) {
        tabs_camera.setVisibility(View.GONE);
        changeStatusBarColor(R.color.black);
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(bundle);
            if (stack_crop_video.size() <= 0) {
                stack_crop_video.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_to_crop_image(FragmentElement fragment,Bundle bundle) {
        tabs_camera.setVisibility(View.GONE);
        changeStatusBarColor(R.color.white);
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(bundle);

            if (stack_crop_images.size() <= 0) {
                stack_crop_images.push(mCurrentFragment);
            }
        }
        inflateFragment();
        ((ImageCropFragment) mCurrentFragment.getFragment()).update_croper();
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

    private void change_picker_video(FragmentElement fragment,Bundle bundle) {
        if(bundle==null){
            bundle = new Bundle();
        }
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(bundle);
            if (stack_picker_videos.size() <= 0) {
                stack_picker_videos.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }
    private void change_picker_camera(FragmentElement fragment,Bundle bundle) {
        if(bundle==null){
            bundle = new Bundle();
        }
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(bundle);
            if (stack_picker_camera.size() <= 0) {
                stack_picker_camera.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }


    private void change_picker(FragmentElement fragment,Bundle bundle) {
        tabs_camera.setVisibility(View.VISIBLE);
        if(bundle==null){
            bundle = new Bundle();
        }
        if (fragment != null) {
            if(is_from_profile){
                bundle.putInt("FROM_PROFILE",1);
                Log.e("FROM_PROFILE","--->uuuuuuuuuu" + is_from_profile);
            }
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(bundle);
            if (stack_picker_images.size() <= 0) {
                stack_picker_images.push(mCurrentFragment);
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
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), color));
        }
    }

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
        if(is_from_profile){
            String u = "";
            if(data.getStringArrayList("data_pahs").size() == 1){
                u = data.getStringArrayList("data_pahs").get(0);
            }
            Intent intent = new Intent();
            intent.putExtra("URI_URL",u);
            setResult(RESULT_OK,intent);
            finish();
        }else{
            changeOfInstance(type_fragment,data);
        }

    }


    @Override
    public void onBackPressed() {
        if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_PICKER_IMAGES){
            Log.e("VERIFICACION","xxxxxxxxxxxxxxxxxxxxx");
            super.onBackPressed();
           finish();

        }else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_CROP_IMAGE){
            changeOfInstance(FragmentElement.INSTANCE_PICKER_IMAGES,null);
        }else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_CROP_VIDEO){
            changeOfInstance(FragmentElement.INSTANCE_PICKER_VIDEOS,null);
        }else{
            Log.e("VERIFICACION","VERIFICO SI HAY PATHS PENDIENTES" + mCurrentFragment.getInstanceType());
            tabs_camera.setVisibility(View.VISIBLE);
            Log.e("VERIFICACION","VERIFICO SI HAY PATHS PENDIENTES");
            if(paths_themp.size()>0){
                delete_files();
                changeOfInstance(FragmentElement.INSTANCE_PICKER_IMAGES,null);
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

    public static File get_dir(){
       String  filename = "perra" + UUID.randomUUID();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "Instapetts");
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "Instapetts" + File.separator);
        return  file;
    }






    public static void initData() {
     /*   transferRecordMaps.clear();
        // Use TransferUtility to get all upload transfers.
        observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        TransferListener listener = new MyService.UploadListener();
        for (TransferObserver observer : observers) {
            observer.refresh();
            HashMap<String, Object> map = new HashMap<String, Object>();
            util.fillMap(map, observer, false);
            transferRecordMaps.add(map);

            // Sets listeners to in progress transfers
            if (TransferState.WAITING.equals(observer.getState())
                    || TransferState.WAITING_FOR_NETWORK.equals(observer.getState())
                    || TransferState.IN_PROGRESS.equals(observer.getState())) {
                observer.setTransferListener(listener);
            }
        }
        simpleAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onImageProfileUpdated() {
       finish();
    }


}
