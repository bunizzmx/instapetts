package com.bunizz.instapetts.activitys.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.security.keystore.KeyGenParameterSpec;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.BuildConfig;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.camera_history.CameraHistoryActivity;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.activitys.searchqr.QrSearchActivity;
import com.bunizz.instapetts.activitys.share_post.ShareActivity;
import com.bunizz.instapetts.activitys.side_menus_activities.SideMenusActivities;
import com.bunizz.instapetts.activitys.wizardPets.WizardPetActivity;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.comentarios.ComentariosFragment;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.follows.FollowsFragment;
import com.bunizz.instapetts.fragments.info.InfoPetFragment;
import com.bunizz.instapetts.fragments.notifications.NotificationsFragment;
import com.bunizz.instapetts.fragments.post.FragmentListOfPosts;
import com.bunizz.instapetts.fragments.previewProfile.FragmentProfileUserPetPreview;
import com.bunizz.instapetts.fragments.profile.FragmentEditProfileUser;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.search.FragmentSearchPet;
import com.bunizz.instapetts.fragments.search.posts.FragmentPostPublics;
import com.bunizz.instapetts.fragments.side.SideFragment;
import com.bunizz.instapetts.fragments.tips.detail.FragmentTipDetail;
import com.bunizz.instapetts.fragments.tips.FragmentTips;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.open_side_menu;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.services.FetchAddressIntentService;
import com.bunizz.instapetts.services.ImageService;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.bottom_sheet.SlidingUpPanelLayout;
import com.bunizz.instapetts.utils.dilogs.DialogLogout;
import com.bunizz.instapetts.utils.slidemenu.SlideMenuLayout;
import com.bunizz.instapetts.utils.smoot.SmoothProgressBar;
import com.bunizz.instapetts.utils.target.TapTarget;
import com.bunizz.instapetts.utils.target.TapTargetView;
import com.bunizz.instapetts.web.CONST;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.parceler.Parcels;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity implements change_instance,
        changue_fragment_parameters_listener,
        uploads,MainContract.View ,
        open_camera_histories_listener,
        folowFavoriteListener, open_side_menu {

    public static final String POST_SUCCESFULL = "com.bunizz.instapetts.activitys.main.Main.POST_SUCCESFULL";
    public static final String POST_SENDEND_START = "com.bunizz.instapetts.activitys.main.Main.POST_SENDEND_START";
    private Stack<FragmentElement> stack_feed;
    private Stack<FragmentElement> stack_profile_pet;
    private Stack<FragmentElement> stack_tips;
    private Stack<FragmentElement> stack_serch_pet;
    private Stack<FragmentElement> stack_follows;
    private Stack<FragmentElement> stack_notifications;
    private Stack<FragmentElement> stack_tip_detail;
    private Stack<FragmentElement> stack_edit_profile;
    private Stack<FragmentElement> stack_preview_perfil;
    private Stack<FragmentElement> stack_posts_publics_search;
    private Stack<FragmentElement> stack_posts_search_advanced;
    private Stack<FragmentElement> stack_comentarios;
    private Stack<FragmentElement> stack_side_menu;

    private FragmentElement mCurrentFragment;

    private FragmentElement mCurrenSheet;
    private FragmentElement mOldFragment;
    static final int NEW_PET_REQUEST = 1;
    static final int NEW_POST_REQUEST = 2;
    static final int NEW_PHOTO_UPLOADED= 3;
    static final int NEW_PHOTO_FOR_HISTORY= 4;
    static final int NEW_PHOTO_QR_SCAN= 5;

    @BindView(R.id.icon_tips)
    ImageView icon_tips;
    @BindView(R.id.icon_feed_pet)
    ImageView icon_feed_pet;
    @BindView(R.id.icon_add_image_pet)
    ImageView icon_add_image_pet;
    @BindView(R.id.icon_search_pet)
    ImageView icon_search_pet;
    @BindView(R.id.icon_profile_pet)
    ImagenCircular icon_profile_pet;

    @BindView(R.id.image_preview_smoot)
    ImageView image_preview_smoot;

    @BindView(R.id.text_tips)
    TextView text_tips;
    @BindView(R.id.text_feed_pet)
    TextView text_feed_pet;
    @BindView(R.id.text_search_pet)
    TextView text_search_pet;
    @BindView(R.id.text_profile_pet)
    TextView text_profile_pet;

    @BindView(R.id.root_progres_publish)
    RelativeLayout root_progres_publish;

    @BindView(R.id.smoot_progress)
    SmoothProgressBar smoot_progress;




    @BindView(R.id.text_smoot)
    TextView text_smoot;

    @BindView(R.id.close_smoot)
    RelativeLayout close_smoot;

    @BindView(R.id.root_bottom_nav)
    RelativeLayout root_bottom_nav;

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
    private boolean mSaved;

    RxPermissions rxPermissions ;

    MainPresenter presenter;
    PetHelper petHelper;
    Intent i ;
    int is_login_again =0;
    boolean DOWNLOAD_INFO =false;
    boolean IS_SHEET_OPEN =false;
    boolean NEW_USER=false;
    boolean SIDE_OPEN=false;

    String addressOutput="";
    int TYPE_FRAGMENT_PUSH = 999;
    int FROM_PUSH =0;
    Bundle b_from_push = new Bundle();

    private AddressResultReceiver resultReceiver;


    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_profile_pet)
    void tab_profile_pet() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_PROFILE_PET) {
            changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET, null, false);
            repaint_nav(R.id.tab_profile_pet);
        }
    }


    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main_sliding)
    void back_to_main_sliding() {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }





    @SuppressLint("MissingPermission")
    @OnClick(R.id.tap_tips)
    void tap_tips() {
     if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_TIPS) {
         changeOfInstance(FragmentElement.INSTANCE_TIPS, null,false);
         repaint_nav(R.id.tap_tips);
     }
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
            changeOfInstance(FragmentElement.INSTANCE_FEED,null,false);
            repaint_nav(R.id.tab_feed);
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.tab_search_pet)
    void tab_search_pet() {
        if(mCurrentFragment.getInstanceType() != FragmentElement.INSTANCE_GET_POSTS_PUBLICS) {
            changeOfInstance(FragmentElement.INSTANCE_GET_POSTS_PUBLICS,null,false);
            repaint_nav(R.id.tab_search_pet);
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(this,this);
        i = new Intent(Main.this, SideMenusActivities.class);
        changeStatusBarColor(R.color.white);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        IntentFilter server_connected = new IntentFilter(POST_SUCCESFULL);
        registerReceiver(mainPagerReceiver, server_connected);
        if(b!=null)
        {
            FROM_PUSH = b.getInt("FROM_PUSH");
            int res  = b.getInt(BUNDLES.DOWNLOADS_INFO);
            int is_login_again = b.getInt("LOGIN_AGAIN");
            int new_u = b.getInt("NEW_USER");
            if(new_u == 1)
                NEW_USER =true;
            else
                NEW_USER = false;
            if(res == 1){
                petHelper = new PetHelper(this);
                DOWNLOAD_INFO = true;
            }
            if(is_login_again == 1){
                presenter.getFileBackup();
            }
            if(FROM_PUSH == 1){
                int ID_RESOURCE=0;
                TYPE_FRAGMENT_PUSH = b.getInt("TYPE_FRAGMENT");
                ID_RESOURCE = b.getInt("ID_RESOURCE");
                if(TYPE_FRAGMENT_PUSH == FragmentElement.INSTANCE_PREVIEW_PROFILE){
                    b_from_push.putInt(BUNDLES.ID_USUARIO,ID_RESOURCE);
                }else if(TYPE_FRAGMENT_PUSH == FragmentElement.INSTANCE_COMENTARIOS){
                    b_from_push.putInt(BUNDLES.ID_POST,ID_RESOURCE);
                    b_from_push.putBoolean(BUNDLES.CAN_COMMENT,true);
                }
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
        stack_follows = new Stack<>();
        stack_comentarios = new Stack<>();
        stack_side_menu = new Stack<>();
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

        if(DOWNLOAD_INFO)
            download_pets();





        presenter.have_pets();
        getLocation();
        resultReceiver = new AddressResultReceiver(new Handler());
        if (!Geocoder.isPresent()) {
            Toast.makeText(Main.this,
                    R.string.no_geocoder_available,
                    Toast.LENGTH_LONG).show();
            return;
        }
        Log.e("LAT_LON","-->" + App.read(PREFERENCES.LAT,0f) + "/" + App.read(PREFERENCES.LON,0f));
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String token = instanceIdResult.getToken();
            UserBean U_TOK = new UserBean();
            U_TOK.setToken(token);
            U_TOK.setTarget("TOKEN");
            U_TOK.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
            presenter.update_token(U_TOK);
        });

        Glide.with(Main.this).load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(getResources().getDrawable(R.drawable.ic_holder)).into(icon_profile_pet);
        presenter.getIdentificadoresHistories();


    }

    void download_pets(){
        UserBean userBean = new UserBean();
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        presenter.downloadMyPets(userBean);
    }

    private void setupFirstFragment() {
        if(FROM_PUSH == 0){
            repaint_nav(R.id.tab_feed);
            mCurrentFragment = new FragmentElement<>(null, FeedFragment.newInstance(), FragmentElement.INSTANCE_FEED, true);
            change_main(mCurrentFragment);
        }else{
                changeOfInstance(TYPE_FRAGMENT_PUSH,b_from_push,false);
        }
    }


    private void change_main(FragmentElement fragment) {
            if (fragment != null) {
                mCurrentFragment = fragment;
                if (stack_feed.size() <= 0) {
                    stack_feed.push(mCurrentFragment);
                }
            }
            inflateFragment(false);
    }

    private void change_detail_tip(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_tip_detail.size() <= 0) {
                stack_tip_detail.push(mCurrentFragment);
            }
        }
        ((FragmentTipDetail) mCurrentFragment.getFragment()).refill_data(data);
        inflateFragment(false);
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
        inflateFragment(false);
    }

    private void change_notifications(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_notifications.size() <= 0) {
                stack_notifications.push(mCurrentFragment);
            }
        }
        inflateFragment(false);
    }

    private void change_profile_pet(FragmentElement fragment,Bundle bundle,boolean is_back) {
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
        ((FragmentProfileUserPet) mCurrentFragment.getFragment()).reloadMyData();
        inflateFragment(is_back);
    }

    private void change_tips(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_tips.size() <= 0) {
                stack_tips.push(mCurrentFragment);
            }
        }
        inflateFragment(false);
    }

    private void change_search_pet(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_serch_pet.size() <= 0) {
                stack_serch_pet.push(mCurrentFragment);
            }
        }
        inflateFragment(false);
    }

    private void change_to_search_posts_publics(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if (stack_posts_publics_search.size() <= 0) {
                stack_posts_publics_search.push(mCurrentFragment);
            }
        }

        inflateFragment(false);
    }


    private void change_to_preview_erfil(FragmentElement fragment,Bundle data,boolean back) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            //Log.e("EARCH_USERS","fragment : " + data.getInt(BUNDLES.ID_USUARIO));
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_preview_perfil.size() <= 0) {
                stack_preview_perfil.push(mCurrentFragment);
            }
        }
         inflateFragment(false);
        if(!back)
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
        inflateFragment(false);
        ((FragmentListOfPosts) mCurrentFragment.getFragment()).update_lists();
    }

    private void changue_to_follows(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_follows.size() <= 0) {
                stack_follows.push(mCurrentFragment);
            }
        }
        ((FollowsFragment) mCurrentFragment.getFragment()).updateInfo(data);
        inflateFragment(false);
    }

    private void change_to_comentarios(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_comentarios.size() <= 0) {
                stack_comentarios.push(mCurrentFragment);
            }
        }
       ((ComentariosFragment) mCurrentFragment.getFragment()).refresh_coments();
        inflateFragment(false);
    }

    private void changue_to_side(FragmentElement fragment,Bundle data,boolean is_back) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if (stack_side_menu.size() <= 0) {
                stack_side_menu.push(mCurrentFragment);
            }
        }
        inflateFragment(is_back);
    }





    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle bundle,boolean back) {
        saveFragment();

        if(intanceType!=FragmentElement.INSTANCE_COMENTARIOS && intanceType!=FragmentElement.INSTANCE_EDIT_PROFILE_USER && intanceType != FragmentElement.INSTANCE_SIDE_MENU) {
            runOnUiThread(() -> root_bottom_nav.setVisibility(View.VISIBLE));
        }
        else {
            runOnUiThread(() -> root_bottom_nav.setVisibility(View.GONE));
        }
        if(mOldFragment!=null) {

            if (mOldFragment.getInstanceType() == FragmentElement.INSTANCE_FEED) {
                ((FeedFragment) mOldFragment.getFragment()).stop_player();
            }

            if (mOldFragment.getInstanceType() == FragmentElement.INSTANCE_TIPS)
                ((FragmentTips) mOldFragment.getFragment()).stop_player();
        }


        if (intanceType == FragmentElement.INSTANCE_FEED) {
            if (stack_feed.size() == 0) {
                change_main(new FragmentElement<>("", FeedFragment.newInstance(), FragmentElement.INSTANCE_LOGIN));
            } else {
                change_main(stack_feed.pop());
            }
        } else if (intanceType == FragmentElement.INSTANCE_PROFILE_PET) {
            if (stack_profile_pet.size() == 0) {
                change_profile_pet(new FragmentElement<>("", FragmentProfileUserPet.newInstance(), FragmentElement.INSTANCE_PROFILE_PET),bundle,back);
            } else {
                change_profile_pet(stack_profile_pet.pop(),bundle,back);
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
                change_detail_tip(new FragmentElement<>("", FragmentTipDetail.newInstance(), FragmentElement.INSTANCE_TIP_DETAIL),bundle);
            } else {
                change_detail_tip(stack_tip_detail.pop(),bundle);
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
                change_to_preview_erfil(new FragmentElement<>("", FragmentProfileUserPetPreview.newInstance(), FragmentElement.INSTANCE_PREVIEW_PROFILE),bundle,back);
            } else {
                change_to_preview_erfil(stack_preview_perfil.pop(),bundle,back);
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
        else if(intanceType == FragmentElement.INSTANCE_FOLLOWS_USER) {
            if (stack_follows.size() == 0) {
                changue_to_follows(new FragmentElement<>("", FollowsFragment.newInstance(), FragmentElement.INSTANCE_FOLLOWS_USER),bundle);
            } else {
                changue_to_follows(stack_follows.pop(),bundle);
            }
        }
        else if(intanceType == FragmentElement.INSTANCE_COMENTARIOS) {
            if (stack_comentarios.size() == 0) {
                change_to_comentarios(new FragmentElement<>("", ComentariosFragment.newInstance(), FragmentElement.INSTANCE_COMENTARIOS),bundle);
            } else {
                change_to_comentarios(stack_comentarios.pop(),bundle);
            }
        }
        else if(intanceType == FragmentElement.INSTANCE_SIDE_MENU){
            if (stack_side_menu.size() == 0) {
                changue_to_side(new FragmentElement<>("", SideFragment.newInstance(), FragmentElement.INSTANCE_SIDE_MENU),bundle,back);
            } else {
                changue_to_side(stack_side_menu.pop(),bundle,back);
            }
        }

    }

    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment(boolean is_back) {
        try {

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (mOldFragment != null) {
                Log.e("current_fragment","" + mCurrentFragment.getInstanceType() + "/" + mOldFragment.getInstanceType());
                if (mCurrentFragment.getFragment().isAdded()) {
                    if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_SIDE_MENU || (mOldFragment.getInstanceType() == FragmentElement.INSTANCE_SIDE_MENU  && mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_PROFILE_PET )){
                        Log.e("ANIMATIONXX","1");
                        if(is_back){
                            fragmentManager
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .setCustomAnimations(R.anim.enter_left, R.anim.exit_right)
                                    .hide(mOldFragment.getFragment())
                                    .show(mCurrentFragment.getFragment()).commit();
                        }else{
                            fragmentManager
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .setCustomAnimations(R.anim.enter_right, R.anim.exit_left)
                                    .hide(mOldFragment.getFragment())
                                    .show(mCurrentFragment.getFragment()).commit();
                        }

                    }else{
                        Log.e("ANIMATIONXX","2");
                        fragmentManager
                                .beginTransaction()
                                .addToBackStack(null)
                                .hide(mOldFragment.getFragment())
                                .show(mCurrentFragment.getFragment()).commit();
                    }
                } else {
                    if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_SIDE_MENU || (mOldFragment.getInstanceType() == FragmentElement.INSTANCE_SIDE_MENU  && mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_PROFILE_PET )){
                        Log.e("ANIMATIONXX","3");
                        if(is_back){
                            fragmentManager
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .setCustomAnimations(R.anim.enter_left, R.anim.exit_right)
                                    .hide(mOldFragment.getFragment())
                                    .add(R.id.root_main, mCurrentFragment.getFragment()).commit();
                        }else{
                            fragmentManager
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .setCustomAnimations(R.anim.enter_right, R.anim.exit_left)
                                    .hide(mOldFragment.getFragment())
                                    .add(R.id.root_main, mCurrentFragment.getFragment()).commit();

                        }
                    }else{
                        Log.e("ANIMATIONXX","4");
                        fragmentManager
                                .beginTransaction()
                                .addToBackStack(null)
                                .hide(mOldFragment.getFragment())
                                .add(R.id.root_main, mCurrentFragment.getFragment()).commit();
                    }

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
        changeOfInstance(fragment_element,null,false);
    }

    @Override
    public void onback() {
        changeOfInstance(FragmentElement.INSTANCE_FEED,null,false);
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
        Log.e("ONBACKKK","111");
        if(mOldFragment.getInstanceType() == FragmentElement.INSTANCE_TIPS)
            ((FragmentTips) mOldFragment.getFragment()).stop_player();


        if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_TIP_DETAIL){
            repaint_nav(R.id.tap_tips);
            changeOfInstance(FragmentElement.INSTANCE_TIPS,null,false);
        }
        else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_SEARCH || mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED){
            Log.e("ONBACKKK","444");
            if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED){
                ((FragmentListOfPosts) mCurrentFragment.getFragment()).stop_player();
            }
           if(mOldFragment.getInstanceType()==FragmentElement.INSTANCE_PROFILE_PET) {
               repaint_nav(R.id.tab_profile_pet);
               changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET, null,false);
           }else{
               changeOfInstance(FragmentElement.INSTANCE_GET_POSTS_PUBLICS, null,false);
           }
        }
        else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_PREVIEW_PROFILE && mOldFragment.getInstanceType()== FragmentElement.INSTANCE_GET_POSTS_PUBLICS){
                 changeOfInstance(FragmentElement.INSTANCE_GET_POSTS_PUBLICS, null,false);
        }else if(mCurrentFragment.getInstanceType()  == FragmentElement.INSTANCE_PREVIEW_PROFILE && mOldFragment.getInstanceType() == FragmentElement.INSTANCE_SEARCH){
            changeOfInstance(FragmentElement.INSTANCE_SEARCH,null,false);
        }
        else if(IS_SHEET_OPEN || SIDE_OPEN){
            Log.e("ONBACKKK","555");
            IS_SHEET_OPEN= false;
            SIDE_OPEN =false;
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            if(mOldFragment.getInstanceType() == FragmentElement.INSTANCE_PROFILE_PET)
             changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET, null,true);

        }
        else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_EDIT_PROFILE_USER){
            Log.e("ONBACKKK","666");
            Bundle b = new Bundle();
            b.putString(BUNDLES.PHOTO_LOCAL,App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
            changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET,b,false);
        }else if(mCurrentFragment.getInstanceType() == FragmentElement.INSTANCE_FOLLOWS_USER){
            Log.e("ONBACKKK","777");
            if(mOldFragment.getInstanceType() == FragmentElement.INSTANCE_PREVIEW_PROFILE){
                changeOfInstance(FragmentElement.INSTANCE_PREVIEW_PROFILE,null,true);
            }else if(mOldFragment.getInstanceType() == FragmentElement.INSTANCE_PROFILE_PET){
                Bundle b = new Bundle();
                b.putString(BUNDLES.PHOTO_LOCAL,App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
                changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET,b,false);
            }
        }
        else{
            if(mCurrentFragment.getInstanceType()== FragmentElement.INSTANCE_FEED) {
                Log.e("ONBACKKK","222");
                finish();
            }
            else {
                Log.e("ONBACKKK","333");
                repaint_nav(R.id.tab_feed);
                changeOfInstance(FragmentElement.INSTANCE_FEED, null, false);
            }
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
            if(data!=null) {
                if(data.getStringExtra("PET_REQUEST")!=null){
                    if(data.getStringExtra("PET_REQUEST").equals("PET_REQUEST")) {
                        Intent i = new Intent(Main.this, WizardPetActivity.class);
                        startActivityForResult(i, NEW_PET_REQUEST);
                    }else{
                        Glide.with(Main.this).load(App.read(PREFERENCES.URI_TEMP_SMOOT,"INVALID"))
                                .placeholder(R.drawable.ic_holder)
                                .error(R.drawable.ic_holder)
                                .into(image_preview_smoot);
                        smoot_progress.setVisibility(View.VISIBLE);
                        root_progres_publish.setVisibility(View.VISIBLE);
                        close_smoot.setVisibility(View.GONE);
                        text_smoot.setText("En progreso...");
                    }
                }
            }
        }

        else if(requestCode == NEW_PHOTO_UPLOADED){
            if(data!=null) {
               String url =  data.getStringExtra(BUNDLES.URI_FOTO);
                   if (mCurrentFragment.getFragment() instanceof FragmentEditProfileUser) {
                       ((FragmentEditProfileUser) mCurrentFragment.getFragment()).change_image_profile(url);
                   }
                    else{
                       Log.e("REFRESH_OFTO_PET","-->: " + url);
                       ((InfoPetFragment) mCurrenSheet.getFragment()).refresh_data_on_pet(url);
                   }
            }
        }
        else if(requestCode == NEW_PHOTO_FOR_HISTORY){
            if(data!=null) {
                String url =  data.getStringExtra(BUNDLES.URI_FOTO);
                int id_pet= data.getIntExtra(BUNDLES.ID_PET,0);
                String name_pet = data.getStringExtra(BUNDLES.NAME_PET);
                String photo_pet = data.getStringExtra(BUNDLES.URL_PHOTO_PET);
                upload_story(url,name_pet,id_pet,photo_pet);
            }
        }

        else if(requestCode == NEW_PHOTO_QR_SCAN){
            if(data!=null) {
                String url =  data.getStringExtra(BUNDLES.UUID);
                int id_user= data.getIntExtra(BUNDLES.ID_USUARIO,0);
                Bundle b = new Bundle();
                b.putInt(BUNDLES.ID_USUARIO,id_user);
                changeOfInstance(FragmentElement.INSTANCE_PREVIEW_PROFILE,b,false);
            }
        }

    }

    private void repaint_nav(int id ){
        icon_tips.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet));
        icon_add_image_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hand_pet_stroke));
        icon_feed_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet));
        icon_search_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_search));
        icon_profile_pet.setBorderColor(getResources().getColor(R.color.white));
        text_profile_pet.setTextColor(Color.BLACK);
        text_tips.setTextColor(Color.BLACK);
        text_search_pet.setTextColor(Color.BLACK);
        text_feed_pet.setTextColor(Color.BLACK);

        if(id == R.id.tap_tips) {
            text_tips.setTextColor(this.getResources().getColor(R.color.primary));
            icon_tips.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_food_pet_black));
        }
        else if(id == R.id.tab_profile_pet){
            text_profile_pet.setTextColor(this.getResources().getColor(R.color.primary));
            icon_profile_pet.setBorderColor(getResources().getColor(R.color.primary));
        }
        else if(id == R.id.tab_feed) {
            text_feed_pet.setTextColor(this.getResources().getColor(R.color.primary));
            icon_feed_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_home_pet_black));
        }
        else if(id == R.id.tab_search_pet) {
            text_search_pet.setTextColor(this.getResources().getColor(R.color.primary));
            icon_search_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_search_black));
        }
        else if(id == R.id.tab_add_image) {
            icon_add_image_pet.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_hand_pet_stroke));
        }
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
        if(type_fragment==999){
            Intent i = new Intent(this , QrSearchActivity.class);
            startActivityForResult( i,NEW_PHOTO_QR_SCAN);
        }else
        changeOfInstance(type_fragment,data,false);
    }


    @SuppressLint("CheckResult")
    @Override
    public void onImageProfileUpdated(String from) {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        Intent i = new Intent(Main.this, ShareActivity.class);
                        i.putExtra("FROM",from);
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
            intent.putStringArrayListExtra(ImageService.INTENT_KEY_NAME, uri_profile);
            intent.putExtra(BUNDLES.NOTIFICATION_TIPE,1);
            intent.putExtra(ImageService.INTENT_TRANSFER_OPERATION, ImageService.TRANSFER_OPERATION_UPLOAD);
            startService(intent);
        }else{
            Log.e("NO_MODIFICO_FOTO","xxxx");
        }
        UserBean userBean = new UserBean();
        userBean.setDescripcion(bundle.getString(BUNDLES.DESCRIPCION));
        userBean.setPhoto_user(bundle.getString(BUNDLES.PHOTO));
        userBean.setPhoto_user_thumbh(bundle.getString(BUNDLES.PHOTO_TUMBH));
        userBean.setTarget(WEBCONSTANTS.UPDATE);
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        presenter.UpdateProfile(userBean);
        changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET,bundle,false);
    }

    @Override
    public void new_pet() {

    }

    void  upload_story(String url,String name_pet,int id_pet,String phohto_pet){
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

        IndividualDataPetHistoryBean historiesBean = new IndividualDataPetHistoryBean();
        historiesBean.setUrl_photo(App.getInstance().make_uri_bucket_history(filename));
        historiesBean.setName_pet(name_pet);
        historiesBean.setDate_story(App.formatDateGMT(new Date()));
        historiesBean.setId_pet(id_pet);
        historiesBean.setPhoto_pet(phohto_pet);
        historiesBean.setIdentificador(""+UUID.randomUUID());
        presenter.saveMyStory(historiesBean);
    }

    @Override
    public void psuccessProfileUpdated() {
        if (mCurrentFragment.getFragment() instanceof FragmentProfileUserPet) {
            ((FragmentProfileUserPet) mCurrentFragment.getFragment()).change_descripcion_profile();
        }
         Toast.makeText(Main.this,"PERFIL ACTUALIZADO",Toast.LENGTH_LONG).show();
        Glide.with(Main.this).load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(getResources().getDrawable(R.drawable.ic_holder)).into(icon_profile_pet);
    }

    @Override
    public void saveMyPets(ArrayList<PetBean> pets) {
        if(petHelper!=null) {
            if (pets != null){
                petHelper.cleanTable();
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

    @Override
    public void havePetsResult(boolean result) {
        if(!result) {
            if (NEW_USER) {
                Log.e("ES_NUEVO","LO MUESTRO");
                final SpannableString spannedDesc = new SpannableString("Que tal una primera publicacion de tu mascota?");
                TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.tab_profile_pet), "Hey que tal!!", spannedDesc)
                        .cancelable(false)
                        .drawShadow(true)
                        .tintTarget(false), new TapTargetView.Listener() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET, null,false);
                        repaint_nav(R.id.tab_profile_pet);
                    }

                    @Override
                    public void onOuterCircleClick(TapTargetView view) {
                        super.onOuterCircleClick(view);
                        view.dismiss(true);
                    }

                    @Override
                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                        Log.d("TapTargetViewSample", "You dismissed me :(");
                    }
                });
            }else{
                Log.e("ES_NUEVO","NO LO MUESTRO PORQUE YA ES USUARIO");
            }
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
    public void followUser(UserBean userBeanx,boolean follow_unfollow) {
        if(follow_unfollow)
            presenter.followUser(userBeanx);
        else
            presenter.unfollowUser(userBeanx.getUuid());
    }

    @Override
    public void favoritePet(UserBean userBean, PetBean petBean) {
      presenter.favoritePet(userBean,petBean);
    }

    @Override
    public void open_side() {
        SIDE_OPEN = true;
            changeOfInstance(FragmentElement.INSTANCE_SIDE_MENU,null,false);
    }





    @Override
    public void open_url(String url) {

    }

    @Override
    public void open_pet_code() {
        Intent i = new Intent(this , QrSearchActivity.class);
        startActivityForResult( i,NEW_PHOTO_QR_SCAN);
    }

    @Override
    public void open_post_saved() {

    }

    @Override
    public void logout() {
         DialogLogout dialogLogout = new DialogLogout(this);
        dialogLogout.setListener(() -> {
            FirebaseAuth.getInstance().signOut();
            presenter.logout();
            presenter.delete_data();
            App.getInstance().clear_preferences();
            Intent i = new Intent(Main.this, LoginActivity.class);
            startActivity(i);
        });
        dialogLogout.show();
    }


    @Override
   public void open_target_post(){
       final SpannableString spannedDesc = new SpannableString(getApplicationContext().getResources().getString(R.string.config_fist_foto));
       TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.tab_add_image), getApplicationContext().getResources().getString(R.string.add_first_photo), spannedDesc)
               .cancelable(false)
               .drawShadow(true)
               .tintTarget(false), new TapTargetView.Listener() {
           @SuppressLint("CheckResult")
           @Override
           public void onTargetClick(TapTargetView view) {
               super.onTargetClick(view);
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

           @Override
           public void onOuterCircleClick(TapTargetView view) {
               super.onOuterCircleClick(view);
           }

           @Override
           public void onTargetDismissed(TapTargetView view, boolean userInitiated) { }
       });
    }

    @Override
    public void share_my_profile() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        startActivity(intent);
    }


    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(CONST.RECEIVER, resultReceiver);
        intent.putExtra(CONST.LOCATION_DATA_EXTRA_LON, App.read(PREFERENCES.LON,0f));
        intent.putExtra(CONST.LOCATION_DATA_EXTRA_LAT, App.read(PREFERENCES.LAT,0f));
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
           String DOM_CUT="";
            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            addressOutput = resultData.getString(CONST.RESULT_DATA_KEY);
            if (addressOutput == null) {
                addressOutput = "";
            }
            String splits_addres[];
            splits_addres = addressOutput.split(",");
            if(splits_addres.length >=3){
                int CP=0;
                String splits_cp[] = splits_addres[splits_addres.length-3].split(" ");
                if(splits_cp.length >1){
                    try {
                        CP = Integer.parseInt(splits_cp[0]);
                    } catch (NumberFormatException nfe){
                        try {
                            CP = Integer.parseInt(splits_cp[1]);
                        }catch (NumberFormatException ex){
                            CP = 0;
                        }
                    }
                }else{
                    CP =0 ;
                }
                App.write(PREFERENCES.CP,CP);
                DOM_CUT = splits_addres [splits_addres.length-3] + " " + splits_addres[splits_addres.length-2] + " " + splits_addres[splits_addres.length-1] ;
            }else if(splits_addres.length == 2){
                DOM_CUT = splits_addres[splits_addres.length-2] + " " + splits_addres[splits_addres.length-1] ;
            }
            else if(splits_addres.length == 1){
                DOM_CUT = splits_addres[splits_addres.length-1] ;
            }
            Log.e("ADDRES","OUTPUT: " + addressOutput);
            Log.e("ADDRES","CUT: " + DOM_CUT);
            if(!DOM_CUT.equals(R.string.no_address_found))
             App.write(PREFERENCES.ADDRESS_USER,DOM_CUT);
            // Show a toast message if an address was found.
            if (resultCode == CONST.SUCCESS_RESULT) {
                Log.e("ADDRES","ENCONTRADO");
            }

        }
    }


    @SuppressLint("CheckResult")
    void getLocation(){
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        LocationServices.getFusedLocationProviderClient(Main.this).getLastLocation().addOnSuccessListener(location -> {
                            if(location!=null){
                                Log.e("LOCALIZACION","-->" + location.getLatitude()  + "/" + location.getLongitude());
                                if(location!=null){
                                    App.write(PREFERENCES.LAT,(float)location.getLatitude());
                                    App.write(PREFERENCES.LON,(float)location.getLongitude());
                                    startIntentService();
                                }
                            }
                        });
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainPagerReceiver != null) {
            Log.e("UNREGISTER","RESERIVER");
            this.unregisterReceiver(mainPagerReceiver);
        }
    }

    private final BroadcastReceiver mainPagerReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("BROADCAST","-->" + action);
            if (POST_SUCCESFULL.equals(action)) {
                close_smoot.setVisibility(View.VISIBLE);
                smoot_progress.setVisibility(View.GONE);
                text_smoot.setText(getString(R.string.completed));
                close_smoot.setOnClickListener(view -> {
                    root_progres_publish.setVisibility(View.GONE);
                });
                File cacheDir = new File(context.getCacheDir() + File.separator + "THUMBS_VIDEOS" + File.separator);
                File[] files = cacheDir.listFiles();
                if (files != null) {
                    for (File file : files)
                        file.delete();
                }
            }
        }
    };

    private class GenerateFileTask extends AsyncTask<Void, Integer, Boolean> {
       ArrayList<Integer> list_ids = new ArrayList<>();
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Boolean doInBackground(Void... params) {
                String fileToWrite = App.read(PREFERENCES.UUID, "INVALID") + ".txt";
                File file = new File(fileToWrite);
                if (!file.exists()) {
                    file.mkdirs();
                }
                    try {
                        list_ids = presenter.getIdsFolows();
                        if(list_ids.size()>0) {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileToWrite, Context.MODE_PRIVATE));
                            for (int i = 0; i < list_ids.size(); i++) {
                                if (i==list_ids.size()-1)
                                    outputStreamWriter.write(list_ids.get(i) + "");
                                else
                                    outputStreamWriter.write(list_ids.get(i) + ",");
                            }
                            outputStreamWriter.close();
                            return  true;
                        }else{
                            return  false;
                        }
                    }
                    catch (IOException e) {
                        Log.e("SEND_FILE","error: " + e.getMessage());
                        Log.e("Exception", "File write failed: " + e.toString());
                        return  false;
                    }
        }

        // actualiza el progreso
        @Override
        protected void onProgressUpdate(Integer... values) {}

        // despues de ejecutar el hilo secundario principal
        @Override
        protected void onPostExecute(Boolean result) {
           if(result){
               Log.e("SEND_FILE","REGRESO TRUE");
               presenter.sendFileBackup();
           }else{
               Log.e("SEND_FILE","REGRESO FALSO");
           }
        }
    }




}
