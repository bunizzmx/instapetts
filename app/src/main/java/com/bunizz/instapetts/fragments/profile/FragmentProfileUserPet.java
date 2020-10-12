package com.bunizz.instapetts.fragments.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.side_menus_activities.SideMenusActivities;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.profile.adapters.PetsPropietaryAdapter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.listeners.login_invitado_listener;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.listeners.open_side_menu;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogPreviewImage;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.tabs2.SmartTabLayout;
import com.bunizz.instapetts.utils.target.TapTarget;
import com.bunizz.instapetts.utils.target.TapTargetView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentProfileUserPet extends Fragment implements  ProfileUserContract.View {

    change_instance listener;
    changue_fragment_parameters_listener listener_instance;
   conexion_listener  listener_conexion;

    @BindView(R.id.list_pets_propietary)
    RecyclerView list_pets_propietary;

    @BindView(R.id.image_profile_property_pet)
    ImagenCircular image_profile_property_pet;

    @BindView(R.id.tabs_profile_propietary)
    SmartTabLayout tabs_profile_propietary;

    @BindView(R.id.viewpager_profile)
    ViewPager viewpager_profile;

    @BindView(R.id.title_toolbar)
    TextView title_toolbar;

    @BindView(R.id.icon_toolbar)
    ImageView icon_toolbar;

    @BindView(R.id.follow_edit)
    Button follow_edit;

    @BindView(R.id.num_posts)
    TextView num_posts;

    @BindView(R.id.num_rate_pets)
    TextView num_rate_pets;

    @BindView(R.id.num_followers)
    TextView num_followers;

    @BindView(R.id.descripcion_perfil_user)
    TextView descripcion_perfil_user;

    @BindView(R.id.num_followed)
    TextView num_followed;


    @BindView(R.id.name_property_pet)
    TextView name_property_pet;
    folowFavoriteListener listener_follow;
    open_side_menu listener_open_side;

    @BindView(R.id.root_info_ptofile)
    LinearLayout root_info_ptofile;

    @BindView(R.id.loanding_preview_root)
    RelativeLayout loanding_preview_root;

    @BindView(R.id.spinky_loading_profile_info)
    SpinKitView spinky_loading_profile_info;

    @BindView(R.id.refresh_profile)
    SwipeRefreshLayout refresh_profile;

    @BindView(R.id.rotate_refresh)
    ImageView rotate_refresh;


    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.toolbar_prfile)
    Toolbar toolbar_prfile;

    @BindView(R.id.root_invitado_profile)
    RelativeLayout root_invitado_profile;


    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;




    Style style = Style.values()[12];
    Sprite drawable = SpriteFactory.create(style);
    login_invitado_listener login_listener;
    boolean IS_REFRESHING_REQUEST = false;
    CallbackManager mCallbackManager;
    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_side_menu)
    void open_side_menu() {
        listener_open_side.open_side();
    }


    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_saved)
    void open_saved() {
        Intent i;
        i = new Intent(getActivity(), SideMenusActivities.class);
        i.putExtra("TYPE_MENU",2);
        startActivity(i);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.login_with_gmail)
    void login_with_gmail() {
       login_listener.loginGmail();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.login_with_facebook)
    void login_with_facebook() {

    }

    @BindView(R.id.login_with_facebook)
    LoginButton login_with_facebook;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_followers)
    void open_followers() {
        Log.e("CHANGUE_FOLLOOWS","true");
        Bundle b = new Bundle();
         b.putString(BUNDLES.NAME_USUARIO,USERBEAN.getName_user());
         b.putInt(BUNDLES.ID_USUARIO,USERBEAN.getId());
         b.putString(BUNDLES.UUID,USERBEAN.getUuid());
        b.putInt("TIPO_DESCARGA",2);
        listener_instance.change_fragment_parameter(FragmentElement.INSTANCE_FOLLOWS_USER,b);
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.refresh_profile_item)
    void refresh_profile_item() {
        presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0),POSITION_PAGER);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        rotate_refresh.startAnimation(rotation);
        IS_REFRESHING_REQUEST = true;
    }



    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_followiongs)
    void open_followiongs() {
        Log.e("CHANGUE_FOLLOOWS","true");
        Bundle b = new Bundle();
        b.putString(BUNDLES.NAME_USUARIO,USERBEAN.getName_user());
        b.putInt(BUNDLES.ID_USUARIO,USERBEAN.getId());
        b.putString(BUNDLES.UUID,USERBEAN.getUuid());
        b.putInt("TIPO_DESCARGA",1);
        listener_instance.change_fragment_parameter(FragmentElement.INSTANCE_FOLLOWS_USER,b);
    }



    String URL_UPDATED="INVALID";

    ArrayList<PetBean> PETS = new ArrayList<>();
    UserBean USERBEAN = new UserBean();
    ArrayList<PostBean> POSTS = new ArrayList<>();
   int POSITION_PAGER =0;
    PetHelper petHelper;
    ProfileUserPresenter presenter;
    ViewPagerAdapter adapter_pager;
    PetsPropietaryAdapter petsPropietaryAdapter;
    public static FragmentProfileUserPet newInstance() {
        return new FragmentProfileUserPet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ESTATUS_PROIFLE","onCreate");
        mCallbackManager = CallbackManager.Factory.create();
        petsPropietaryAdapter = new PetsPropietaryAdapter(getContext());
        petHelper = new PetHelper(getContext());
        presenter = new ProfileUserPresenter(this,getContext());
        ArrayList<PetBean> my_pets_database = new ArrayList<>();
            my_pets_database = petHelper.getMyPets();
            if(my_pets_database.size()>0){
                PETS.addAll(my_pets_database);
            }
           PETS.add(new PetBean());
            petsPropietaryAdapter.setPets(PETS);
        adapter_pager = new ViewPagerAdapter(getChildFragmentManager());
        adapter_pager.addFragment(new FragmentPostGalery(), getContext().getString(R.string.post));
        adapter_pager.addFragment(new FragmentPostGalery(), getContext().getString(R.string.only_videos));
        adapter_pager.addFragment(new FragmentPostGalery(), getContext().getString(R.string.only_photos));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_pet, container, false);
    }

    public void reloadMyData(){
        if(!App.read(PREFERENCES.MODO_INVITADO,false)) {
            App.write(PREFERENCES.OPEN_POST_ADVANCED_FROM, 1);
            if (adapter_pager != null && list_pets_propietary != null) {
                loanding_preview_root.setVisibility(View.GONE);
                root_invitado_profile.setVisibility(View.GONE);
                appbar.setVisibility(View.VISIBLE);
                refresh_profile.setVisibility(View.VISIBLE);
                UserBean userBean = new UserBean();
                userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB, 0));
                if(USERBEAN.getName_tag().isEmpty()) {
                    Log.e("STATUS_RELOAD","SI LO CARGO");
                    presenter.getInfoUser(userBean);
                    presenter.getPostUser(true, App.read(PREFERENCES.ID_USER_FROM_WEB, 0), POSITION_PAGER);
                }else{
                    Log.e("STATUS_RELOAD","NO CARGO NADA");
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Log.e("ESTATUS_PROIFLE","onViewCreated");
        App.write(PREFERENCES.OPEN_POST_ADVANCED_FROM,1);
        title_toolbar.setText(App.read(PREFERENCES.NAME_USER,"-"));
        icon_toolbar.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_settings));
        list_pets_propietary.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        petsPropietaryAdapter.setListener(new open_sheet_listener() {
            @Override
            public void open(PetBean petBean, int is_me) {
                listener.open_sheet(petBean,is_me);
            }

            @Override
            public void open_wizard_pet() {
                listener.open_wizard_pet();
            }
        });
        create_items();
        if(!App.read(PREFERENCES.MODO_INVITADO,false)){
            presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0),POSITION_PAGER);
            UserBean userBean = new UserBean();
            userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
            presenter.getInfoUser(userBean);
        }else{
            appbar.setVisibility(View.GONE);
            refresh_profile.setVisibility(View.GONE);
            root_invitado_profile.setVisibility(View.VISIBLE);
            login_with_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.e("ACCES_TOKENN","-->EXECUTE CALBACK");
                    AccessToken accessToken = loginResult.getAccessToken();
                    Profile profile = Profile.getCurrentProfile();
                    if(profile!=null) {
                        App.write(PREFERENCES.FOTO_PROFILE_USER_THUMBH, String.valueOf(profile.getProfilePictureUri(500, 500)));
                        App.write(PREFERENCES.FOTO_PROFILE_USER, String.valueOf(profile.getProfilePictureUri(500, 500)));
                        login_listener.loginFacebook(accessToken);
                    }
                }

                @Override
                public void onCancel() {
                    Log.e("ACCES_TOKENN","-->CANCELADO:");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.e("ACCES_TOKENN","-->ERROR:"+ exception.getMessage());
                }
            });
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener_instance= (changue_fragment_parameters_listener) context;
        listener= (change_instance) context;
        listener_follow =(folowFavoriteListener)context;
        listener_open_side =(open_side_menu)context;
        listener_conexion =(conexion_listener)context;
       login_listener = (login_invitado_listener) context;
    }


    public void change_image_profile(String url){
        if(image_profile_property_pet!=null) {
            if(!url.equals("INVALID"))
            Glide.with(getContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                    .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_profile_property_pet);
        }
    }

    public void setData(int requestCode, int resultCode, Intent data){
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public void change_descripcion_profile(){
        if(descripcion_perfil_user!=null){
            if(!App.read(PREFERENCES.DESCRIPCCION,"INVALID").equals("INVALID")){
                descripcion_perfil_user.setText(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
            }else{
                descripcion_perfil_user.setText("-");
            }

        }

    }

    public void refresh_list_pets(){

        ArrayList<PetBean> my_pets_database = new ArrayList<>();
        my_pets_database.addAll(petHelper.getMyPets());
        my_pets_database.add(new PetBean());
        petsPropietaryAdapter.setPets(my_pets_database);
        if(listener_open_side!=null){
            if(my_pets_database.size() < 1)
                listener_open_side.open_target_post();
        }
    }

    @Override
    public void showInfoUser(UserBean userBean, ArrayList<PetBean> pets) {
        loanding_preview_root.setVisibility(View.GONE);
        root_info_ptofile.setVisibility(View.VISIBLE);
        if(pets.size()>0)
            petHelper.cleanTable();

        for (int i =0;i<pets.size();i++){
            petHelper.savePet(pets.get(i));
        }
        float RATE_PETS=0;
        float ACOMULATIVO_RATE=0;
        if(pets.size()>0) {
            PETS.clear();
            PETS.addAll(pets);
            PETS.add(new PetBean());
        }
        if(PETS.size()==1) {
            if(pets.size()<1 && PETS.size() ==1){
                fist_pet();
            }
        }
        for(int i=0;i <pets.size();i++){
            presenter.updateMyPetLocal(pets.get(i));
            ACOMULATIVO_RATE +=pets.get(i).getRate_pet();
        }
        RATE_PETS = ACOMULATIVO_RATE / pets.size();
        USERBEAN = userBean;
        title_toolbar.setText(USERBEAN.getName_user());
        if(USERBEAN.getName_tag()!=null)
            name_property_pet.setText("@" + USERBEAN.getName_tag());
        else
            name_property_pet.setText("@" + App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"));

        if(!USERBEAN.getDescripcion().equals("INVALID"))
          descripcion_perfil_user.setText(USERBEAN.getDescripcion());
        Glide.with(getContext()).load(USERBEAN.getPhoto_user())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_profile_property_pet);
        petsPropietaryAdapter.setPets(PETS);
        num_posts.setText(String.valueOf(USERBEAN.getPosts()));
        if(RATE_PETS > 0) {
            num_rate_pets.setText(String.format("%.2f", RATE_PETS));
        }else{
            num_rate_pets.setText("5");
        }

        if(USERBEAN.getFolowers() < 0)
            num_followers.setText("0");
        else
            num_followers.setText(String.valueOf(USERBEAN.getFolowers()));



        if(USERBEAN.getFollowed() < 0)
          num_followed.setText("0");
        else
            num_followed.setText(""+USERBEAN.getFollowed());
    }


    @Override
    public void showPostUser(ArrayList<PostBean> posts) {
        rotate_refresh.clearAnimation();
        refresh_profile.setRefreshing(false);
        Fragment frag = adapter_pager.getItem(POSITION_PAGER);
        POSTS.clear();
        POSTS.addAll(posts);
        ArrayList<Object> results = new ArrayList<>();
        results.addAll(POSTS);
        if (frag instanceof FragmentPostGalery) {
            ((FragmentPostGalery) frag).setData_posts(results);
        }
       if( IS_REFRESHING_REQUEST){
           listener_conexion.refreshedComplete();
           IS_REFRESHING_REQUEST = false;
       }
    }

    @Override
    public void showPostUserPaginate(ArrayList<PostBean> post) {
        rotate_refresh.clearAnimation();
        refresh_profile.setRefreshing(false);
        Fragment frag = adapter_pager.getItem(POSITION_PAGER);
        POSTS.clear();
        POSTS.addAll(post);
        ArrayList<Object> results = new ArrayList<>();
        results.addAll(POSTS);
        if (frag instanceof FragmentPostGalery) {
            ((FragmentPostGalery) frag).setData_posts(results);
        }
    }

    @Override
    public void Error() {
        UserBean userBean = new UserBean();
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        presenter.getInfoUser(userBean);
    }

    @Override
    public void ErrorPostUsers() {
        refresh_profile.setRefreshing(false);
        loanding_preview_root.setVisibility(View.GONE);
        listener_conexion.noWifiRequest();
    }

    @Override
    public void successFollow(boolean follow, int id_user) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            ((FragmentPostGalery) fragment).setListener_pager(() -> POSITION_PAGER);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    void fist_pet(){
        final SpannableString spannedDesc = new SpannableString(getContext().getResources().getString(R.string.config_pet_step));
        TapTargetView.showFor(getActivity(), TapTarget.forView(getView().findViewById(R.id.list_pets_propietary), getContext().getResources().getString(R.string.add_pet), spannedDesc)
                .cancelable(true)
                .drawShadow(true)
                .tintTarget(false), new TapTargetView.Listener() {
            @SuppressLint("CheckResult")
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                if(listener!=null)
                listener.open_wizard_pet();
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
    public void onResume() {
        super.onResume();
        Log.e("ESTATUS_PROIFLE","ONRSUME");
        if(!App.read(PREFERENCES.MODO_INVITADO,false))
          refresh_list_pets();
    }

    public void create_items(){
        list_pets_propietary.setAdapter(petsPropietaryAdapter);
        viewpager_profile.setOffscreenPageLimit(3);
        viewpager_profile.setAdapter(adapter_pager);
        viewpager_profile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                POSITION_PAGER = position;
                Fragment frag = adapter_pager.getItem(POSITION_PAGER);
                if (frag instanceof FragmentPostGalery) {
                    if(!((FragmentPostGalery) frag).isDataAdded()) {
                        if(!App.read(PREFERENCES.MODO_INVITADO,false))
                          presenter.getPostUser(true, App.read(PREFERENCES.ID_USER_FROM_WEB, 0), POSITION_PAGER);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs_profile_propietary.setViewPager(viewpager_profile);
        spinky_loading_profile_info.setIndeterminateDrawable(drawable);
        spinky_loading_profile_info.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        loanding_preview_root.setVisibility(View.VISIBLE);
        title_toolbar.setText(App.read(PREFERENCES.NAME_USER,"USUARIO"));
        name_property_pet.setText("@" + App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"USUARIO"));
        follow_edit.setText(R.string.edit_profile);
        follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_edit_profile));
        follow_edit.setTextColor(Color.BLACK);
        follow_edit.setOnClickListener(view1 -> listener.change(FragmentElement.INSTANCE_EDIT_PROFILE_USER));
        if(!App.read(PREFERENCES.DESCRIPCCION,"INVALID").equals("INVALID")){
            descripcion_perfil_user.setText(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
        }else{
            descripcion_perfil_user.setText("-");
        }
        URL_UPDATED = App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID");
        Glide.with(getContext()).load(URL_UPDATED)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_profile_property_pet);
        refresh_profile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserBean userBean = new UserBean();
                userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                presenter.getInfoUser(userBean);
                presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0),POSITION_PAGER);
            }
        });
        image_profile_property_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPreviewImage dialogPreviewImage = new DialogPreviewImage(getContext(),USERBEAN.getPhoto_user());
                dialogPreviewImage.show();
            }
        });
    }
}

