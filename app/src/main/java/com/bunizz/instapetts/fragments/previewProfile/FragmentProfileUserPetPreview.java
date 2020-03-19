package com.bunizz.instapetts.fragments.previewProfile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.post.FragmentPostGalery;
import com.bunizz.instapetts.fragments.post.FragmentPostList;
import com.bunizz.instapetts.fragments.profile.PetsPropietaryAdapter;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.open_sheet_listener;
import com.bunizz.instapetts.utils.tabs.SlidingFragmentPagerAdapter;
import com.bunizz.instapetts.utils.tabs.SlidingTabLayout;
import com.bunizz.instapetts.utils.tabs.TabType;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentProfileUserPetPreview extends Fragment {

    changue_fragment_parameters_listener listener;
   change_instance  listener_simple;
    FeedAdapter feedAdapter;

    ArrayList<Object> data = new ArrayList<>();

    @BindView(R.id.list_pets_propietary)
    RecyclerView list_pets_propietary;

    @BindView(R.id.tabs_profile_propietary)
    SlidingTabLayout tabs_profile_propietary;

    @BindView(R.id.viewpager_profile)
    ViewPager viewpager_profile;

    @BindView(R.id.follow_edit)
    Button follow_edit;

    ArrayList<PetBean> pets = new ArrayList<>();
    private TabAdapter adapter;
    String UUID="";
    int ID_USUSARIO=0;

    PetsPropietaryAdapter petsPropietaryAdapter;

    public static FragmentProfileUserPetPreview newInstance() {
        return new FragmentProfileUserPetPreview();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petsPropietaryAdapter = new PetsPropietaryAdapter(getContext());
        adapter = new TabAdapter(getFragmentManager(), getContext());
        pets.add(new PetBean());
        petsPropietaryAdapter.setPets(pets);
        Bundle bundle=getArguments();
        if(bundle!=null){
            UUID  = bundle.getString(BUNDLES.UUID,"INVALID");
            ID_USUSARIO = bundle.getInt(BUNDLES.ID_USUARIO,0);
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
            public void open() {
                listener_simple.open_sheet();
            }
            @Override
            public void open_wizard_pet() {
                listener_simple.open_wizard_pet();
            }
        });
        list_pets_propietary.setAdapter(petsPropietaryAdapter);
        viewpager_profile.setAdapter(adapter);
        tabs_profile_propietary.setTabType(TabType.ICON_ONLY);
        tabs_profile_propietary.setViewPager(viewpager_profile);
        tabs_profile_propietary.setTextSize(18);
        tabs_profile_propietary.setDistributeEvenly(true);
        tabs_profile_propietary.setCustomUnfocusedColor(R.color.black);
        tabs_profile_propietary.setSelectedIndicatorColors(getResources().getColor(R.color.primary));
        if(UUID.equals(App.read(PREFERENCES.UUID,"INVALID"))){
            follow_edit.setText(R.string.edit_profile);
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_edit_profile));
            follow_edit.setTextColor(Color.BLACK);
        }else{
            follow_edit.setText("Seguir");
            follow_edit.setBackground(getContext().getResources().getDrawable(R.drawable.button_follow));
            follow_edit.setTextColor(Color.WHITE);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
        listener_simple= (change_instance) context;

    }

    public void refresh_preview(Bundle bundle){
        if(bundle!=null){
            UUID  = bundle.getString(BUNDLES.UUID,"INVALID");
            ID_USUSARIO = bundle.getInt(BUNDLES.ID_USUARIO,0);
        }
    }

    public void refresh_list_pets(){
        petsPropietaryAdapter.add_new_pet(new PetBean("El pochilais","","","","","","","","","",1));
    }

    public class TabAdapter extends SlidingFragmentPagerAdapter {

        private String[] titles = {
                "Contacts",
                "Favorites",
                "Groups",
        };

        private int[] icons = {
                R.drawable.ic_menu,
                R.drawable.ic_favorito,
                R.drawable.ic_lista
        };
        private Context context;

        public TabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("x", position + 1);
            Fragment fragment;
            if(position == 0){
                fragment = new FragmentPostList();
            }else{
                fragment = new FragmentPostGalery();
            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return icons.length == titles.length ? icons.length : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Drawable getPageDrawable(int position) {
            return context.getResources().getDrawable(icons[position]);
        }

        @Override
        public String getToolbarTitle(int position) {
            return titles[position];
        }
    }
}

