package com.bunizz.instapetts.fragments.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.post.FragmentPostList;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.listeners.open_side_menu;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;
import com.bunizz.instapetts.utils.tabs2.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentProfileUserPet extends Fragment implements  ProfileUserContract.View {

    change_instance listener;
    FeedAdapter feedAdapter;

    ArrayList<Object> data = new ArrayList<>();

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

    @BindView(R.id.num_pets)
    TextView num_pets;

    @BindView(R.id.num_followers)
    TextView num_followers;

    @BindView(R.id.descripcion_perfil_user)
    TextView descripcion_perfil_user;

    @BindView(R.id.name_property_pet)
    TextView name_property_pet;
    folowFavoriteListener listener_follow;
    open_side_menu listener_open_side;


    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_side_menu)
    void open_side_menu() {
        listener_open_side.open_side();
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
            petsPropietaryAdapter.setPets(PETS);
            Log.e("RFRESH_PETS","3");
        adapter_pager = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter_pager.addFragment(new FragmentPostGalery(), "Post");
        adapter_pager.addFragment(new FragmentPostGalery(), "Favoritos");
        adapter_pager.addFragment(new FragmentPostGalery(), "Post Privados");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
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
        viewpager_profile.setAdapter(adapter_pager);
        tabs_profile_propietary.setViewPager(viewpager_profile);
        Log.e("NAME_USUARIOS","-->" +App.read(PREFERENCES.NAME_USER,"USUARIO") );
        title_toolbar.setText(App.read(PREFERENCES.NAME_USER,"USUARIO"));
        name_property_pet.setText("@" + App.read(PREFERENCES.NAME_USER,"USUARIO"));
            follow_edit.setText(R.string.edit_profile);
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_edit_profile));
            follow_edit.setTextColor(Color.BLACK);
            follow_edit.setOnClickListener(view1 -> listener.change(FragmentElement.INSTANCE_EDIT_PROFILE_USER));
        descripcion_perfil_user.setText(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
        URL_UPDATED = App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID");
        Glide.with(getContext()).load(URL_UPDATED).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_profile_property_pet);
        viewpager_profile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                POSITION_PAGER = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0));

      /*  refresh_profile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserBean userBean = new UserBean();
                userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                presenter.getInfoUser(userBean);
                presenter.getPostUser(true,App.read(PREFERENCES.ID_USER_FROM_WEB,0));
            }
        });*/
        UserBean userBean = new UserBean();
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        presenter.getInfoUser(userBean);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_follow =(folowFavoriteListener)context;
        listener_open_side =(open_side_menu)context;
    }


    public void change_image_profile(String url){
        if(image_profile_property_pet!=null) {
            if(!url.equals("INVALID"))
            Glide.with(getContext()).load(url).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_profile_property_pet);
        }
    }

    public void refresh_list_pets(){
        Log.e("RFRESH_PETS","1");
        ArrayList<PetBean> my_pets_database = new ArrayList<>();
        my_pets_database.addAll(petHelper.getMyPets());
        my_pets_database.add(new PetBean());
        petsPropietaryAdapter.setPets(my_pets_database);
    }

    @Override
    public void showInfoUser(UserBean userBean, ArrayList<PetBean> pets) {
       // refresh_profile.setRefreshing(false);
        Log.e("RFRESH_PETS","2");
        if(PETS.size()==1) {
            PETS.addAll(pets);
        }
        USERBEAN = userBean;
        title_toolbar.setText(USERBEAN.getName_user());
        name_property_pet.setText("@" + USERBEAN.getName_user());
        descripcion_perfil_user.setText(USERBEAN.getDescripcion());
        Glide.with(getContext()).load(USERBEAN.getPhoto_user()).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_profile_property_pet);
        petsPropietaryAdapter.setPets(PETS);
        num_posts.setText(String.valueOf(USERBEAN.getNum_pets()));
        num_pets.setText(String.valueOf(USERBEAN.getRate_pets()));
        num_followers.setText(String.valueOf(USERBEAN.getFolowers()));
    }


    @Override
    public void showPostUser(ArrayList<PostBean> posts) {
        Log.e("SETDATA","ONMY_PROFILE" + posts.size());
        Fragment frag = adapter_pager.getItem(0);
        POSTS.addAll(posts);
        ArrayList<Object> results = new ArrayList<>();
        results.addAll(POSTS);
        if (frag instanceof FragmentPostGalery) {
            ((FragmentPostGalery) frag).setData(results);
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
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

