package com.bunizz.instapetts.activitys.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.BuildConfig;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.camera_history.CameraHistoryActivity;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.share_post.ShareActivity;
import com.bunizz.instapetts.activitys.side_menus_activities.SideMenusActivities;
import com.bunizz.instapetts.activitys.wizardPets.WizardPetActivity;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.info.InfoPetFragment;
import com.bunizz.instapetts.fragments.notifications.NotificationsFragment;
import com.bunizz.instapetts.fragments.post.FragmentListOfPosts;
import com.bunizz.instapetts.fragments.previewProfile.FragmentProfileUserPetPreview;
import com.bunizz.instapetts.fragments.profile.FragmentEditProfileUser;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.FragmentSearchPet;
import com.bunizz.instapetts.fragments.search.posts.FragmentPostPublics;
import com.bunizz.instapetts.fragments.tips.FragmentTipDetail;
import com.bunizz.instapetts.fragments.tips.FragmentTips;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.listeners.logout_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.open_side_menu;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.services.ImageService;
import com.bunizz.instapetts.services.UploadsService;
import com.bunizz.instapetts.utils.bottom_sheet.SlidingUpPanelLayout;
import com.bunizz.instapetts.utils.dilogs.DialogLogout;
import com.bunizz.instapetts.utils.slidemenu.SlideMenuLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity implements change_instance,
        changue_fragment_parameters_listener,
        uploads,MainContract.View ,
        open_camera_histories_listener,
        folowFavoriteListener, open_side_menu {


    private Stack<FragmentElement> stack_feed;
    private Stack<FragmentElement> stack_profile_pet;
    private Stack<FragmentElement> stack_tips;
    private Stack<FragmentElement> stack_serch_pet;
    private Stack<FragmentElement> stack_notifications;
    private Stack<FragmentElement> stack_tip_detail;
    private Stack<FragmentElement> stack_edit_profile;
    private Stack<FragmentElement> stack_preview_perfil;
    private Stack<FragmentElement> stack_posts_publics_search;
    private Stack<FragmentElement> stack_posts_search_advanced;


    private FragmentElement mCurrentFragment;

    private FragmentElement mCurrenSheet;
    private FragmentElement mOldFragment;
    static final int NEW_PET_REQUEST = 1;
    static final int NEW_POST_REQUEST = 2;
    static final int NEW_PHOTO_UPLOADED= 3;
    static final int NEW_PHOTO_FOR_HISTORY= 4;


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

    @BindView(R.id.mainSlideMenu)
    SlideMenuLayout mainSlideMenu;

    @BindView(R.id.app_name_user)
    TextView app_name_user;

    @BindView(R.id.version_app)
    TextView version_app;


    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
    private boolean mSaved;

    RxPermissions rxPermissions ;

    MainPresenter presenter;
    PetHelper petHelper;
    Intent i ;
    boolean DOWNLOAD_INFO =false;
    boolean IS_SHEET_OPEN =false;


    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_profile_pet)
    void tab_profile_pet() {
       /* Log.e("CHANGE_INSTANCE", "profile pet");

         changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);*/
        changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET,null);
        repaint_nav(R.id.tab_profile_pet);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.menu_centro_ayuda)
    void menu_centro_ayuda() {
        i.putExtra("TYPE_MENU",0);
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.side_menu_administrate_account)
    void side_menu_administrate_account() {
        i.putExtra("TYPE_MENU",1);
        startActivity(i);
    }



    @SuppressLint("MissingPermission")
    @OnClick(R.id.menu_side_guardado)
    void menu_side_guardado() {
        i.putExtra("TYPE_MENU",2);
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.side_menu_open_push)
    void side_menu_open_push() {
        i.putExtra("TYPE_MENU",3);
        startActivity(i);
    }



    @SuppressLint("MissingPermission")
    @OnClick(R.id.tap_tips)
    void tap_tips() {

     if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_TIPS) {
         changeOfInstance(FragmentElement.INSTANCE_TIPS, null);
         repaint_nav(R.id.tap_tips);
     }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.side_menu_share_profile)
    void side_menu_share_profile() {
        share_profile();
    }



    @SuppressLint("MissingPermission")
    @OnClick(R.id.logout)
    void logout() {
        DialogLogout dialogLogout = new DialogLogout(this);
        dialogLogout.setListener(new logout_listener() {
            @Override
            public void logout() {
                App.getInstance().clear_preferences();
                Intent i = new Intent(Main.this, LoginActivity.class);
                startActivity(i);
            }
        });
        dialogLogout.show();
    }



    @SuppressLint({"MissingPermission", "CheckResult"})
    @OnClick(R.id.tab_add_image)
    void tab_add_image() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        App.write(PREFERENCES.FROM_PICKER,"POST");
                        Intent i = new Intent(Main.this, ShareActivity.class);
                        startActivityForResult(i,NEW_POST_REQUEST);
                    } else {
                        App.getInstance().show_dialog_permision(Main.this,getResources().getString(R.string.permision_storage),
                                getResources().getString(R.string.permision_storage_body),0);
                    }
                });


    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_feed)
    void tab_feed() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_FEED) {
            changeOfInstance(FragmentElement.INSTANCE_FEED,null);
            repaint_nav(R.id.tab_feed);
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_search_pet)
    void tab_search_pet() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_GET_POSTS_PUBLICS) {
            changeOfInstance(FragmentElement.INSTANCE_GET_POSTS_PUBLICS,null);
            repaint_nav(R.id.tab_search_pet);
        }
    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        i = new Intent(Main.this, SideMenusActivities.class);
        changeStatusBarColor(R.color.white);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            int res  = b.getInt("DOWNLOADS_INFO");
            if(res == 1){
                petHelper = new PetHelper(this);
                DOWNLOAD_INFO = true;
            }
        }
        rxPermissions = new RxPermissions(this);
        stack_feed = new Stack<>();
        stack_profile_pet = new Stack<>();
        stack_tips = new Stack<>();
        stack_serch_pet = new Stack<>();
        stack_notifications = new Stack<>();
        stack_tip_detail = new Stack<>();
        stack_edit_profile= new Stack<>();
        stack_preview_perfil = new Stack<>();
        stack_posts_publics_search = new Stack<>();
        stack_posts_search_advanced = new Stack<>();
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
        presenter = new MainPresenter(this,this);
        if(DOWNLOAD_INFO)
            download_pets();

        app_name_user.setText("@" + App.read(PREFERENCES.NAME_USER,"INVALID"));
        version_app.setText("Version : " + BuildConfig.VERSION_NAME);



    }

    void download_pets(){
        Log.e("DONLOAD_MY_PETS","si");
        UserBean userBean = new UserBean();
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        presenter.downloadMyPets(userBean);
    }

    public void changeStatusBarCNAME_USUARIOSolor(int color) {
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

    private void change_detail_tip(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_tip_detail.size() <= 0) {
                stack_tip_detail.push(mCurrentFragment);
            }
        }
        inflateFragment();
    }

    private void change_edit_profile(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_edit_profile.size() <= 0) {
                stack_edit_profile.push(mCurrentFragment);
            }
        }
        Bundle b = new Bundle();
        b.putInt("CONFIG",1);
        mCurrentFragment.getFragment().setArguments(b);
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

    private void change_profile_pet(FragmentElement fragment,Bundle bundle) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(bundle!=null){
                mCurrentFragment.getFragment().setArguments(bundle);
                if (mCurrentFragment.getFragment() instanceof FragmentProfileUserPet) {
                    ((FragmentProfileUserPet) mCurrentFragment.getFragment()).change_image_profile(bundle.getString(BUNDLES.PHOTO_LOCAL));
                }
            }
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

    private void change_to_search_posts_publics(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_posts_publics_search.size() <= 0) {
                stack_posts_publics_search.push(mCurrentFragment);
            }
        }

        inflateFragment();
    }


    private void change_to_preview_erfil(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            //Log.e("EARCH_USERS","fragment : " + data.getInt(BUNDLES.ID_USUARIO));
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_preview_perfil.size() <= 0) {
                stack_preview_perfil.push(mCurrentFragment);
            }
        }
        inflateFragment();
        ((FragmentProfileUserPetPreview) mCurrentFragment.getFragment()).refresh_info();
    }

    private void change_list_of_posts_advanced(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_posts_search_advanced.size() <= 0) {
                stack_posts_search_advanced.push(mCurrentFragment);
            }
        }
        inflateFragment();
        ((FragmentListOfPosts) mCurrentFragment.getFragment()).update_lists();
    }


    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle bundle) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_FEED) {
            if (stack_feed.size() == 0) {
                change_main(new FragmentElement<>("", FeedFragment.newInstance(), FragmentElement.INSTANCE_LOGIN));
            } else {
                change_main(stack_feed.pop());
            }
        } else if (intanceType == FragmentElement.INSTANCE_PROFILE_PET) {
            if (stack_profile_pet.size() == 0) {
                change_profile_pet(new FragmentElement<>("", FragmentProfileUserPet.newInstance(), FragmentElement.INSTANCE_PROFILE_PET),bundle);
            } else {
                change_profile_pet(stack_profile_pet.pop(),bundle);
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
        else if (intanceType == FragmentElement.INSTANCE_TIP_DETAIL) {
            if (stack_tip_detail.size() == 0) {
                change_detail_tip(new FragmentElement<>("", FragmentTipDetail.newInstance(), FragmentElement.INSTANCE_TIP_DETAIL));
            } else {
                change_detail_tip(stack_tip_detail.pop());
            }
        }

        else if (intanceType == FragmentElement.INSTANCE_EDIT_PROFILE_USER) {
            if (stack_edit_profile.size() == 0) {
                change_edit_profile(new FragmentElement<>("", FragmentEditProfileUser.newInstance(), FragmentElement.INSTANCE_EDIT_PROFILE_USER));
            } else {
                change_edit_profile(stack_edit_profile.pop());
            }
        }



        else if(intanceType == FragmentElement.INSTANCE_PREVIEW_PROFILE) {
            if (stack_preview_perfil.size() == 0) {
                change_to_preview_erfil(new FragmentElement<>("", FragmentProfileUserPetPreview.newInstance(), FragmentElement.INSTANCE_PREVIEW_PROFILE),bundle);
            } else {
                change_to_preview_erfil(stack_preview_perfil.pop(),bundle);
            }

        }
        else if (intanceType == FragmentElement.INSTANCE_GET_POSTS_PUBLICS) {
            if (stack_posts_publics_search.size() == 0) {
                change_to_search_posts_publics(new FragmentElement<>("", FragmentPostPublics.newInstance(), FragmentElement.INSTANCE_GET_POSTS_PUBLICS));
            } else {
                change_to_search_posts_publics(stack_posts_publics_search.pop());
            }
        }
        else if(intanceType == FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED) {
            if (stack_posts_search_advanced.size() == 0) {
                change_list_of_posts_advanced(new FragmentElement<>("", FragmentListOfPosts.newInstance(), FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED),bundle);
            } else {
                change_list_of_posts_advanced(stack_posts_search_advanced.pop(),bundle);
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
        changeOfInstance(fragment_element,null);
    }

    @Override
    public void onback() {
        changeOfInstance(FragmentElement.INSTANCE_FEED,null);
    }

    @Override
    public void open_sheet(PetBean petBean,int is_me) {
        Bundle b = new Bundle();
        b.putParcelable(BUNDLES.PETBEAN, Parcels.wrap(petBean));
        b.putInt(BUNDLES.IS_ME, is_me);
        changue_instance_sheet(b);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void open_wizard_pet() {
        Intent i = new Intent(Main.this, WizardPetActivity.class);
        startActivityForResult(i, NEW_PET_REQUEST);
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_TIP_DETAIL){
            changeOfInstance(FragmentElement.INSTANCE_TIPS,null);
        }
        else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_SEARCH || mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED){
            changeOfInstance(FragmentElement.INSTANCE_GET_POSTS_PUBLICS,null);
        }
        else if(IS_SHEET_OPEN){
            IS_SHEET_OPEN= false;
           mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }else{
            changeOfInstance(FragmentElement.INSTANCE_FEED,null);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_PET_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (mCurrentFragment.getFragment() instanceof FragmentProfileUserPet) {
                            ((FragmentProfileUserPet) mCurrentFragment.getFragment()).refresh_list_pets();
                }
            }
        }else if(requestCode == NEW_POST_REQUEST){
            Log.e("ACTIVITYRES","4");
            if(data!=null) {
                if(data.getStringExtra("PET_REQUEST")!=null){
                    Log.e("ACTIVITYRES","2");
                    Intent i = new Intent(Main.this, WizardPetActivity.class);
                    startActivityForResult(i, NEW_PET_REQUEST);
                }
                data.getStringArrayListExtra(BUNDLES.URI_FOTO);
            }
        }

        else if(requestCode == NEW_PHOTO_UPLOADED){
            if(data!=null) {
               String url =  data.getStringExtra(BUNDLES.URI_FOTO);

                   if (mCurrentFragment.getFragment() instanceof FragmentEditProfileUser) {
                       ((FragmentEditProfileUser) mCurrentFragment.getFragment()).change_image_profile(url);
                   }

            }
        }
        else if(requestCode == NEW_PHOTO_FOR_HISTORY){
            if(data!=null) {
                String url =  data.getStringExtra(BUNDLES.URI_FOTO);
                upload_story(url);
            }
        }

    }

    private void repaint_nav(int id ){
        icon_tips.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet));
        icon_profile_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_usuario));
        icon_add_image_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hand_pet_white));
        icon_feed_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet));
        icon_search_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_search));

        if(id == R.id.tap_tips)
            icon_tips.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet_black));
        else if(id == R.id.tab_profile_pet)
            icon_profile_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_usuario));
        else if(id == R.id.tab_feed)
            icon_feed_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet_black));
        else if(id == R.id.tab_search_pet)
            icon_search_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_search_black));
        else if(id == R.id.tab_add_image)
            icon_add_image_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hand_pet_white));
    }

    void changue_instance_sheet(Bundle data){
        IS_SHEET_OPEN = true;
        changue_sheet(new FragmentElement<>("", InfoPetFragment.newInstance(), FragmentElement.INSTANCE_PROFILE_PET),data);
    }

    private void changue_sheet(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrenSheet = fragment;
            mCurrenSheet.getFragment().setArguments(data);
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

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
        changeOfInstance(type_fragment,data);
    }


    void share_images(){

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
                        Intent i = new Intent(Main.this, ShareActivity.class);
                        i.putExtra("FROM","PROFILE_PHOTO");
                        startActivityForResult(i,NEW_PHOTO_UPLOADED);
                    } else {
                        App.getInstance().show_dialog_permision(Main.this,getResources().getString(R.string.permision_storage),
                                getResources().getString(R.string.permision_storage_body),0);
                    }
                });
    }

    @Override
    public void setResultForOtherChanges(String url) {

    }

    @Override
    public void UpdateProfile(Bundle bundle) {
        ArrayList<String> uri_profile = new ArrayList<>();
        uri_profile.add(bundle.getString(BUNDLES.PHOTO_LOCAL));
        if(!bundle.getString(BUNDLES.PHOTO_LOCAL).equals("INVALID")){
            Intent intent = new Intent(Main.this, ImageService.class);
            intent.putStringArrayListExtra(UploadsService.INTENT_KEY_NAME, uri_profile);
            intent.putExtra(BUNDLES.NOTIFICATION_TIPE,1);
            intent.putExtra(UploadsService.INTENT_TRANSFER_OPERATION, UploadsService.TRANSFER_OPERATION_UPLOAD);
            startService(intent);
        }else{
            Log.e("NO_MODIFICO_FOTO","xxxx");
        }
        UserBean userBean = new UserBean();
        userBean.setDescripcion(bundle.getString(BUNDLES.DESCRIPCION));
        userBean.setPhoto_user(bundle.getString(BUNDLES.PHOTO));
        userBean.setPhoto_user_thumbh(bundle.getString(BUNDLES.PHOTO_TUMBH));
        userBean.setTarget("UPDATE");
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        presenter.UpdateProfile(userBean);
        changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET,bundle);
    }

    @Override
    public void new_pet() {

    }

    void  upload_story(String url){
        ArrayList<String> uri_profile = new ArrayList<>();
        uri_profile.add(url);

        Intent intent = new Intent(Main.this, ImageService.class);
        intent.putStringArrayListExtra(ImageService.INTENT_KEY_NAME, uri_profile);
        intent.putExtra(BUNDLES.NOTIFICATION_TIPE,2);
        intent.putExtra(ImageService.INTENT_TRANSFER_OPERATION, ImageService.TRANSFER_OPERATION_UPLOAD);
        startService(intent);

        String splits[] =url.split("/");
        int index  = splits.length;
        String filename = splits[index -1];


        HistoriesBean historiesBean = new HistoriesBean();
        historiesBean.setUris_stories(App.getInstance().make_uri_bucket_history(filename));
        historiesBean.setName_pet("FIRULAIS");
        historiesBean.setName_user(App.read(PREFERENCES.NAME_USER,"INVALID"));
        historiesBean.setDate_story(App.formatDateGMT(new Date()));
        historiesBean.setId_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        historiesBean.setId_pet(1);
        historiesBean.setUrl_photo_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
        presenter.saveMyStory(historiesBean);
    }

    @Override
    public void psuccessProfileUpdated() {
         Toast.makeText(Main.this,"PERFIL ACTUALIZADO",Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveMyPets(ArrayList<PetBean> pets) {
        if(petHelper!=null) {
            if (pets != null){
                petHelper.cleanTable();
                Log.e("DONLOAD_MY_PETS","si" + pets.size());
                for (int i = 0; i < pets.size(); i++)
                    petHelper.savePet(pets.get(i));
            }
        }
    }

    @Override
    public void onError(int error) {
        if(error == 1){
            download_pets();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void open() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        Intent i = new Intent(Main.this, CameraHistoryActivity.class);
                        startActivityForResult(i,NEW_PHOTO_FOR_HISTORY);
                    } else {
                        App.getInstance().show_dialog_permision(Main.this,getResources().getString(R.string.permision_storage),
                                getResources().getString(R.string.permision_storage_body),0);
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
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), color));
        }
    }

    @Override
    public void followUser(UserBean userBeanx) {
       presenter.followUser(userBeanx);
    }

    @Override
    public void favoritePet(UserBean userBean, PetBean petBean) {
      presenter.favoritePet(userBean,petBean);
    }

    @Override
    public void open_side() {
        mainSlideMenu.openRightSlide();
    }


    void share_profile(){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            startActivity(intent);
    }
}
