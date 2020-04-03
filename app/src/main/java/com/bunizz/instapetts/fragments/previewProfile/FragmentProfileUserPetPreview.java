package com.bunizz.instapetts.fragments.previewProfile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.post.FragmentPostList;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.fragments.profile.PetsPropietaryAdapter;
import com.bunizz.instapetts.fragments.profile.ProfileUserContract;
import com.bunizz.instapetts.fragments.profile.ProfileUserPresenter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;
import com.bunizz.instapetts.utils.tabs2.SmartTabLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentProfileUserPetPreview extends Fragment implements  ProfileUserContract.View {

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

    @BindView(R.id.follow_edit)
    Button follow_edit;

    @BindView(R.id.descripcion_perfil_user)
    TextView descripcion_perfil_user;

    @BindView(R.id.title_name_preview)
    TextView title_name_preview;

    @BindView(R.id.num_posts)
    TextView num_posts;

    @BindView(R.id.num_pets)
    TextView num_pets;

    @BindView(R.id.num_followers)
    TextView num_followers;




    @BindView(R.id.name_property_pet)
    TextView name_property_pet;
    folowFavoriteListener listener_follow;

    String URL_UPDATED="INVALID";

    ArrayList<PetBean> PETS = new ArrayList<>();
    UserBean USERBEAN = new UserBean();
    ArrayList<PostBean> POSTS = new ArrayList<>();

    ViewPagerAdapter adapter_pager;
    PetHelper petHelper;
    PreviewProfileUserPresenter presenter;

    PetsPropietaryAdapter petsPropietaryAdapter;
    int ID_USER_PARAMETER=0;
    UserBean userBean = new UserBean();
    boolean IS_MISMO_USER=false;
    public static FragmentProfileUserPetPreview newInstance() {
        return new FragmentProfileUserPetPreview();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petsPropietaryAdapter = new PetsPropietaryAdapter(getContext());
        adapter_pager = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter_pager.addFragment(new FragmentPostGalery(), "Post");
        adapter_pager.addFragment(new FragmentPostGalery(), "Favoritos");
        adapter_pager.addFragment(new FragmentPostGalery(), "Post Privados");
        petHelper = new PetHelper(getContext());
        presenter = new PreviewProfileUserPresenter(this,getContext());
        Bundle bundle=getArguments();
        PETS.clear();
        ArrayList<PetBean> my_pets_database = new ArrayList<>();
        if(bundle!=null){
            ID_USER_PARAMETER = bundle.getInt(BUNDLES.ID_USUARIO);
        }
        if(ID_USER_PARAMETER == App.read(PREFERENCES.ID_USER_FROM_WEB,0) && ID_USER_PARAMETER!=0){
            IS_MISMO_USER = true;
            my_pets_database = petHelper.getMyPets();
            if(my_pets_database.size()>0){
                PETS.addAll(my_pets_database);
            }
            PETS.add(new PetBean());
            petsPropietaryAdapter.setPets(PETS);
        }else{
            IS_MISMO_USER = false;
            userBean.setId(ID_USER_PARAMETER);
            userBean.setUuid("xxxx");
            presenter.getInfoUser(userBean);
            presenter.getPostUser(true,userBean.getId());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_pet_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_pets_propietary.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        petsPropietaryAdapter.setListener(new open_sheet_listener() {
            @Override
            public void open(PetBean petBean,int is_me) {
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


        if(IS_MISMO_USER)
            show_myInfo();
        else
            paint_buttons();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_follow =(folowFavoriteListener)context;
    }


    public void change_image_profile(String url){
        if(image_profile_property_pet!=null)
            Glide.with(getContext()).load(url).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_profile_property_pet);
    }

    public void refresh_info(){
        if(petHelper!=null && petsPropietaryAdapter!=null && presenter!= null) {
            PETS.clear();
            Bundle bundle = getArguments();
            ArrayList<PetBean> my_pets_database = new ArrayList<>();
            if (bundle != null) {
                ID_USER_PARAMETER = bundle.getInt(BUNDLES.ID_USUARIO);
            }
            if (ID_USER_PARAMETER == App.read(PREFERENCES.ID_USER_FROM_WEB, 0) && ID_USER_PARAMETER != 0) {
                IS_MISMO_USER = true;
                my_pets_database = petHelper.getMyPets();
                if (my_pets_database.size() > 0) {
                    PETS.addAll(my_pets_database);
                }
                PETS.add(new PetBean());
                petsPropietaryAdapter.setPets(PETS);
                Log.e("RFRESH_PETS", "3");
                show_myInfo();
            } else {
                IS_MISMO_USER = false;
                Log.e("MANDO_PETICION_POR_USER", "SI" + ID_USER_PARAMETER);
                userBean.setId(ID_USER_PARAMETER);
                userBean.setUuid("xxxx");
                presenter.getInfoUser(userBean);
                presenter.getPostUser(true,userBean.getId());

            }
            paint_buttons();
        }
    }

    void paint_buttons(){
        if(IS_MISMO_USER){
            follow_edit.setText(R.string.edit_profile);
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_edit_profile));
            follow_edit.setTextColor(Color.BLACK);
            follow_edit.setOnClickListener(view1 -> listener.change(FragmentElement.INSTANCE_EDIT_PROFILE_USER));
        }else{
            follow_edit.setText("Sueguir");
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_follow));
            follow_edit.setTextColor(Color.WHITE);
            follow_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("FOLLOW_USER","-->" + USERBEAN.getId());
                    listener_follow.followUser(USERBEAN);
                }
            });
        }
    }

    @Override
    public void showInfoUser(UserBean userBean, ArrayList<PetBean> pets) {
        PETS.addAll(pets);
        USERBEAN = userBean;
        name_property_pet.setText("@" + USERBEAN.getName_user());
        descripcion_perfil_user.setText(USERBEAN.getDescripcion());
        Glide.with(getContext()).load(USERBEAN.getPhoto_user()).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_profile_property_pet);
        petsPropietaryAdapter.setPetsforOtherUser(PETS);
        title_name_preview.setText(USERBEAN.getName_user());
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

    void show_myInfo(){
        paint_buttons();
        name_property_pet.setText("@" + App.read(PREFERENCES.NAME_USER,"USUARIO"));
        title_name_preview.setText( App.read(PREFERENCES.NAME_USER,"USUARIO"));
        descripcion_perfil_user.setText(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
        URL_UPDATED = App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID");
        Glide.with(getContext()).load(URL_UPDATED).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_profile_property_pet);
    }

    @Override
    public void Error() {
        presenter.getInfoUser(userBean);
    }

    @Override
    public void ErrorPostUsers() {

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

