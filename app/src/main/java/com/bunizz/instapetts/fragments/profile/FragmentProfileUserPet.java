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
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.side_menus_activities.SideMenusActivities;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
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

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;




    Style style = Style.values()[12];
    Sprite drawable = SpriteFactory.create(style);

    boolean IS_REFRESHING_REQUEST = false;

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
        petsPropietaryAdapter = new PetsPropietaryAdapter(getContext());
        petHelper = new PetHelper(getContext());
        presenter = new ProfileUserPresenter(this,getContext());
        ArrayList<PetBean> my_pets_database = new ArrayList<>();
            my_pets_database = petHelper.getMyPets();
            if(my_pets_database.size()>0){
                PETS.addAll(my_pets_database);
            }
           PETS.add(new PetBean());
        Log.e("REFRESH_MY_PETS","-->3"  +PETS.size());
            petsPropietaryAdapter.setPets(PETS);
        adapter_pager = new ViewPagerAdapter(getChildFragmentManager());
        adapter_pager.addFragment(new FragmentPostGalery(), "Publicaciones");
        adapter_pager.addFragment(new FragmentPostGalery(), "Solo Videos");
        adapter_pager.addFragment(new FragmentPostGalery(), "Fotos y galerias");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_pet, container, false);
    }

    public void reloadMyData(){
        if(adapter_pager!=null && list_pets_propietary!=null)
           presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0),POSITION_PAGER);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        title_toolbar.setText(App.read(PREFERENCES.NAME_USER,"USUARIO"));
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
                        Log.e("DATOS_ADDEDS","POST_PROFILES : " + POSITION_PAGER);
                        presenter.getPostUser(true, App.read(PREFERENCES.ID_USER_FROM_WEB, 0), POSITION_PAGER);
                    }
                    else
                        Log.e("DATOS_ADDEDS","POST_PROFILES");

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
        root_info_ptofile.setVisibility(View.GONE);
        title_toolbar.setText(App.read(PREFERENCES.NAME_USER,"USUARIO"));
        name_property_pet.setText("@" + App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"USUARIO"));
            follow_edit.setText(R.string.edit_profile);
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_edit_profile));
            follow_edit.setTextColor(Color.BLACK);
            follow_edit.setOnClickListener(view1 -> listener.change(FragmentElement.INSTANCE_EDIT_PROFILE_USER));
        descripcion_perfil_user.setText(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
        URL_UPDATED = App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID");
        Glide.with(getContext()).load(URL_UPDATED)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_profile_property_pet);

        presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0),POSITION_PAGER);

       refresh_profile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserBean userBean = new UserBean();
                userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                presenter.getInfoUser(userBean);
                presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0),POSITION_PAGER);
            }
        });
        UserBean userBean = new UserBean();
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        presenter.getInfoUser(userBean);
        image_profile_property_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPreviewImage dialogPreviewImage = new DialogPreviewImage(getContext(),USERBEAN.getPhoto_user());
                dialogPreviewImage.show();
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener_instance= (changue_fragment_parameters_listener) context;
        listener= (change_instance) context;
        listener_follow =(folowFavoriteListener)context;
        listener_open_side =(open_side_menu)context;
        listener_conexion =(conexion_listener)context;
    }


    public void change_image_profile(String url){
        if(image_profile_property_pet!=null) {
            if(!url.equals("INVALID"))
            Glide.with(getContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_profile_property_pet);
        }
    }

    public void change_descripcion_profile(){
        if(descripcion_perfil_user!=null)
          descripcion_perfil_user.setText(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
    }

    public void refresh_list_pets(){

        ArrayList<PetBean> my_pets_database = new ArrayList<>();
        my_pets_database.addAll(petHelper.getMyPets());
        my_pets_database.add(new PetBean());
        Log.e("REFRESH_MY_PETS","-->1" + my_pets_database.size());
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

        descripcion_perfil_user.setText(USERBEAN.getDescripcion());
        Glide.with(getContext()).load(USERBEAN.getPhoto_user())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_profile_property_pet);
        Log.e("REFRESH_MY_PETS","-->2" + PETS.size());
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
       // refresh_profile.setRefreshing(false);
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
            if(mFragmentList.size()==1)
              ((FragmentPostGalery) fragment).setIdUser(App.read(PREFERENCES.ID_USER_FROM_WEB,0),1);
            else if((mFragmentList.size()==2))
                ((FragmentPostGalery) fragment).setIdUser(App.read(PREFERENCES.ID_USER_FROM_WEB,0),2);
            else
                ((FragmentPostGalery) fragment).setIdUser(App.read(PREFERENCES.ID_USER_FROM_WEB,0),0);

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
                .cancelable(false)
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
        refresh_list_pets();
    }
}

