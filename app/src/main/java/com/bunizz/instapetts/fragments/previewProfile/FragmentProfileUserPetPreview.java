package com.bunizz.instapetts.fragments.previewProfile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.profile.PetsPropietaryAdapter;
import com.bunizz.instapetts.fragments.profile.ProfileUserContract;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;
import com.bunizz.instapetts.utils.tabs2.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentProfileUserPetPreview extends Fragment implements  ProfileUserContract.View {

    change_instance listener;
    FeedAdapter feedAdapter;
    changue_fragment_parameters_listener listener_instance;

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


    @BindView(R.id.open_saved)
    CardView open_saved;

    @BindView(R.id.name_property_pet)
    TextView name_property_pet;

    @BindView(R.id.root_info_ptofile)
    LinearLayout root_info_ptofile;

    @BindView(R.id.loanding_preview_root)
    RelativeLayout loanding_preview_root;

    @BindView(R.id.spinky_loading_profile_info)
    SpinKitView spinky_loading_profile_info;



    Style style = Style.values()[6];
    Sprite drawable = SpriteFactory.create(style);
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
    boolean IS_MY_FIRNED =false;
    public static FragmentProfileUserPetPreview newInstance() {
        return new FragmentProfileUserPetPreview();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.open_followers)
    void open_followers() {
        Log.e("CHANGUE_FOLLOOWS","true");
        Bundle b = new Bundle();
        b.putString(BUNDLES.NAME_USUARIO,USERBEAN.getName_user());
        b.putInt(BUNDLES.ID_USUARIO,USERBEAN.getId());
        b.putString(BUNDLES.UUID,USERBEAN.getUuid());
        listener_instance.change_fragment_parameter(FragmentElement.INSTANCE_FOLLOWS_USER,b);
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
        IS_MY_FIRNED = presenter.is_user_followed(ID_USER_PARAMETER);
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
        open_saved.setVisibility(View.GONE);
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
        spinky_loading_profile_info.setIndeterminateDrawable(drawable);
        spinky_loading_profile_info.setColor(getContext().getResources().getColor(R.color.primary));
        loanding_preview_root.setVisibility(View.VISIBLE);
        root_info_ptofile.setVisibility(View.GONE);
        paint_buttons();
        loanding_preview_root.setVisibility(View.VISIBLE);
        root_info_ptofile.setVisibility(View.GONE);
        spinky_loading_profile_info.setIndeterminateDrawable(drawable);
        spinky_loading_profile_info.setColor(getContext().getResources().getColor(R.color.primary));
        userBean.setId(ID_USER_PARAMETER);
        userBean.setUuid("xxxx");
        presenter.getInfoUser(userBean);
        presenter.getPostUser(true,userBean.getId());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_follow =(folowFavoriteListener)context;
        listener_instance= (changue_fragment_parameters_listener) context;
    }


    public void change_image_profile(String url){
        if(image_profile_property_pet!=null)
            Glide.with(getContext()).load(url).placeholder(getContext().getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(image_profile_property_pet);
    }

    public void refresh_info(){
        if(petHelper!=null && petsPropietaryAdapter!=null && presenter!= null) {
            loanding_preview_root.setVisibility(View.VISIBLE);
            root_info_ptofile.setVisibility(View.GONE);
            spinky_loading_profile_info.setIndeterminateDrawable(drawable);
            spinky_loading_profile_info.setColor(getContext().getResources().getColor(R.color.primary));
            PETS.clear();
            Bundle bundle = getArguments();
            ArrayList<PetBean> my_pets_database = new ArrayList<>();
            if (bundle != null) {
                ID_USER_PARAMETER = bundle.getInt(BUNDLES.ID_USUARIO);
            }
            IS_MY_FIRNED = presenter.is_user_followed(ID_USER_PARAMETER);
            if (ID_USER_PARAMETER == App.read(PREFERENCES.ID_USER_FROM_WEB, 0) && ID_USER_PARAMETER != 0) {
                IS_MISMO_USER = true;
                my_pets_database = petHelper.getMyPets();
                if (my_pets_database.size() > 0) {
                    PETS.addAll(my_pets_database);
                }
                PETS.add(new PetBean());
                petsPropietaryAdapter.setPets(PETS);
                userBean.setId(ID_USER_PARAMETER);
                userBean.setUuid("xxxx");
                presenter.getInfoUser(userBean);
                presenter.getPostUser(true,userBean.getId());
            } else {
                IS_MISMO_USER = false;
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
            if(IS_MY_FIRNED){
                follow_edit.setText("Dejar de seguir");
                follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_gmail));
                follow_edit.setTextColor(Color.BLACK);
                follow_edit.setOnClickListener(view -> {
                    listener_follow.followUser(USERBEAN,false);
                    presenter.follow(USERBEAN.getId(),false);
                });
            }else{
                follow_edit.setText("Seguir");
                follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_follow));
                follow_edit.setTextColor(Color.WHITE);
                follow_edit.setOnClickListener(view -> {
                    listener_follow.followUser(USERBEAN,true);
                    presenter.follow(USERBEAN.getId(),true);
                });
            }


        }
    }

    @Override
    public void showInfoUser(UserBean userBean, ArrayList<PetBean> pets) {
        loanding_preview_root.setVisibility(View.GONE);
        root_info_ptofile.setVisibility(View.VISIBLE);
        PETS.clear();
        PETS.addAll(pets);

        USERBEAN = userBean;
        Log.e("USER_BEAN","-->TOKEN:" + USERBEAN.getToken() );
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
        POSTS.clear();
        POSTS.addAll(posts);
        ArrayList<Object> results = new ArrayList<>();
        results.addAll(POSTS);
        if (frag instanceof FragmentPostGalery) {
            ((FragmentPostGalery) frag).setData_posts(results);
        }
    }

    @Override
    public void Error() {
        presenter.getInfoUser(userBean);
    }

    @Override
    public void ErrorPostUsers() {

    }

    @Override
    public void successFollow(boolean follow,int id_user) {
        if(follow){
            follow_edit.setText("Dejar de seguir");
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_gmail));
            follow_edit.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            follow_edit.setOnClickListener(view -> {
                listener_follow.followUser(USERBEAN,false);
                presenter.follow(USERBEAN.getId(),false);
            });
        }else{
            follow_edit.setText("Seguir");
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_follow));
            follow_edit.setTextColor(Color.WHITE);
            follow_edit.setOnClickListener(view -> {
                listener_follow.followUser(USERBEAN,true);
                presenter.follow(USERBEAN.getId(),true);
            });
        }

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

