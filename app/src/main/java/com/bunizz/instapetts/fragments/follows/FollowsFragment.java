package com.bunizz.instapetts.fragments.follows;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.FollowsBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.folowFavoriteListener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowsFragment extends Fragment implements FollowsContract.View {


    public static FollowsFragment newInstance() {
        return new FollowsFragment();
    }
    String name_user;
    int id_user=0;
    String uuid="";
    @BindView(R.id.list_follows)
    RecyclerView list_follows;

    @BindView(R.id.name_user_folowers)
    TextView name_user_folowers;

    @BindView(R.id.title_no_internet)
    TextView title_no_internet;

    @BindView(R.id.body_no_data)
    TextView body_no_data;

    @BindView(R.id.icon_no_internet)
    ImageView icon_no_internet;


    @BindView(R.id.spin_kit)
    SpinKitView spin_kit;

    @BindView(R.id.root_no_internet)
    RelativeLayout root_no_internet;

    @BindView(R.id.loanding_more_users)
    RelativeLayout loanding_more_users;

    private boolean loading =true;
    private boolean IS_ALL = false;

    FollowsAdapter adapter;
    FollowsPresenter presenter;
    changue_fragment_parameters_listener listener;
    int tipo_descarga=1;


    @OnClick(R.id.back_to_main)
    void back_to_main()
    {
      getActivity().onBackPressed();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FollowsAdapter(getContext());
        presenter = new FollowsPresenter(this,getContext());
        Bundle bundle=getArguments();
        if(bundle!=null){
            name_user = bundle.getString(BUNDLES.NAME_USUARIO);
            id_user = bundle.getInt(BUNDLES.ID_USUARIO);
            uuid = bundle.getString(BUNDLES.UUID);
            tipo_descarga = bundle.getInt("TIPO_DESCARGA");
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_follows.setLayoutManager(new LinearLayoutManager(getContext()));
        list_follows.setAdapter(adapter);
        adapter.setListener((type_fragment, data) -> {
            if(listener!=null)
                listener.change_fragment_parameter(type_fragment,data);
        });
        adapter.setListener_follow(new folowFavoriteListener() {
            @Override
            public void followUser(UserBean userBean, boolean follow_unfollow) {
                presenter.unfollowUser(userBean.getUuid(),userBean.getName_tag(),userBean.getId(),false);
            }

            @Override
            public void delete_of_my_friends(UserBean userBean, boolean follow_unfollow) {
                presenter.unfollowUser(userBean.getUuid(),userBean.getName_tag(),userBean.getId(),true);
            }
        });
        adapter.setTipo_descarga(tipo_descarga);
        Style style = Style.values()[12];
        Sprite drawable = SpriteFactory.create(style);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        spin_kit.setVisibility(View.VISIBLE);
        if(tipo_descarga == 1) {
            name_user_folowers.setText(getContext().getString(R.string.Followed));
            presenter.getFirstFollowed(uuid);
        }
        else {
            name_user_folowers.setText(getContext().getString(R.string.followers));
            presenter.getFirstFolowers(uuid);
        }

        list_follows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (dy > 0) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if(loading){
                            loading = false;
                            if(IS_ALL == false) {
                                loanding_more_users.setVisibility(View.VISIBLE);
                                Log.e("DOWNLOAD_ORE_FOLLOWERS","SI");
                                presenter.nextFollowers();
                            }else {
                                Log.e("DOWNLOAD_ORE_FOLLOWERS","NO");
                            }
                        }
                    }
                }
            }
        });
    }



    public void updateInfo(Bundle b){

        if(presenter!=null) {
            Log.e("UPDATE_INFO_FOLLOWS","SI");
            if (b != null) {
                name_user = b.getString(BUNDLES.NAME_USUARIO);
                id_user = b.getInt(BUNDLES.ID_USUARIO);
                uuid = b.getString(BUNDLES.UUID);
                tipo_descarga = b.getInt("TIPO_DESCARGA");
            }
            adapter.setTipo_descarga(tipo_descarga);

            if(tipo_descarga == 1) {
                name_user_folowers.setText(getContext().getString(R.string.Followed));
                presenter.getFirstFollowed(uuid);
            }
            else {
                name_user_folowers.setText(getContext().getString(R.string.followers));
                presenter.getFirstFolowers(uuid);
            }
        }else{
            Log.e("UPDATE_INFO_FOLLOWS","NO");
        }
    }


    @Override
    public void showFirstFollowers(ArrayList<FollowsBean> followsBeans) {
        if(followsBeans.size()>0){
            adapter.clear();
            root_no_internet.setVisibility(View.GONE);
            spin_kit.setVisibility(View.GONE);
            adapter.setUserBeans(followsBeans);
        }else{
            body_no_data.setText(getContext().getString(R.string.when_follow));
            title_no_internet.setText(getContext().getString(R.string.no_sigues));
            icon_no_internet.setVisibility(View.GONE);
            spin_kit.setVisibility(View.GONE);
            root_no_internet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNextFollowers(ArrayList<FollowsBean> followsBeans) {
        loanding_more_users.setVisibility(View.GONE);
        loading =true;
        if(followsBeans.size()>0) {
            adapter.addMoreUsers(followsBeans);
        }else{
            Toast.makeText(getContext(),getContext().getString(R.string.no_more),Toast.LENGTH_LONG).show();
            IS_ALL =true;
        }
    }

    @Override
    public void showFirstFollowed(ArrayList<FollowsBean> followsBeans) {
        ArrayList<FollowsBean> FIRST_FOLLOWEDS = new ArrayList<>();
        FIRST_FOLLOWEDS.addAll(followsBeans);
        if(FIRST_FOLLOWEDS.size()>0){
            adapter.clear();
            spin_kit.setVisibility(View.GONE);
            adapter.setUserBeans(FIRST_FOLLOWEDS);
            root_no_internet.setVisibility(View.GONE);
        }else{
            body_no_data.setText(getContext().getString(R.string.when_follow));
            title_no_internet.setText(getContext().getString(R.string.no_sigues));
            icon_no_internet.setVisibility(View.GONE);
            spin_kit.setVisibility(View.GONE);
            root_no_internet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNextFollowed(ArrayList<FollowsBean> followsBeans) {
        ArrayList<FollowsBean> NEXT_FOLLOWEDS = new ArrayList<>();
        NEXT_FOLLOWEDS.addAll(followsBeans);
        loanding_more_users.setVisibility(View.GONE);
        loading =true;
        if(NEXT_FOLLOWEDS.size()>0) {
            adapter.addMoreUsers(NEXT_FOLLOWEDS);
        }else{
            Toast.makeText(getContext(),getContext().getString(R.string.no_more),Toast.LENGTH_LONG).show();
            IS_ALL =true;
        }
    }

    @Override
    public void noInternet() {
        spin_kit.setVisibility(View.GONE);
        root_no_internet.setVisibility(View.VISIBLE);
    }

    @Override
    public void Error() {
        presenter.getFirstFolowers(uuid);
    }

    @Override
    public void UnfollowSuccess() {
        adapter.notifyDataSetChanged();
        if(adapter.size_items()==0){
            body_no_data.setText(getContext().getString(R.string.when_follow));
            title_no_internet.setText(getContext().getString(R.string.no_sigues));
            icon_no_internet.setVisibility(View.GONE);
            spin_kit.setVisibility(View.GONE);
            root_no_internet.setVisibility(View.VISIBLE);
        }
    }


    public class FollowsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<FollowsBean> userBeans = new ArrayList<>();
        Context context;
        changue_fragment_parameters_listener listener;
        folowFavoriteListener listener_follow;
        int tipo_descarga=1;

        public int getTipo_descarga() {
            return tipo_descarga;
        }

        public void setTipo_descarga(int tipo_descarga) {
            this.tipo_descarga = tipo_descarga;
        }

        public changue_fragment_parameters_listener getListener() {
            return listener;
        }

        public void setListener(changue_fragment_parameters_listener listener) {
            this.listener = listener;
        }

        public folowFavoriteListener getListener_follow() {
            return listener_follow;
        }

        public void setListener_follow(folowFavoriteListener listener_follow) {
            this.listener_follow = listener_follow;
        }

        public FollowsAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<FollowsBean> getUserBeans() {
            return userBeans;
        }

        public void setUserBeans(ArrayList<FollowsBean> userBeans) {
            this.userBeans.clear();
            this.userBeans.addAll(userBeans);
            notifyDataSetChanged();
        }

        public void clear(){
            this.userBeans.clear();
            notifyDataSetChanged();
        }
        public int size_items(){
            return  this.userBeans.size();
        }

        public void addMoreUsers(ArrayList<FollowsBean> userBeans){
            this.userBeans.addAll(userBeans);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow,parent,false);
            return  new FollowsHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            FollowsHolder h =(FollowsHolder)holder;
            Glide.with(context).load(userBeans.get(position).getUrl_photo_user()).into(h.image_user_follow);
            h.name_follow.setText(userBeans.get(position).getName_user());
            h.root_follows_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putString(BUNDLES.UUID,userBeans.get(position).getUuid_user());
                    b.putInt(BUNDLES.ID_USUARIO,userBeans.get(position).getId_user());
                    listener.change_fragment_parameter(FragmentElement.INSTANCE_PREVIEW_PROFILE,b);
                }
            });
            h.delete_recent.setOnClickListener(v -> {
                UserBean user = new UserBean();
                user.setName_tag(userBeans.get(position).getName_nip_user());
                user.setUuid(userBeans.get(position).getUuid_user());
                user.setId(userBeans.get(position).getId_user());
                if(tipo_descarga == 2)
                      listener_follow.delete_of_my_friends(user,false);
                else
                      listener_follow.followUser(user,false);
                userBeans.remove(position);
            });

            h.name_propietary_pet_searching.setText("@" + userBeans.get(position).getName_nip_user());

            if(tipo_descarga ==2){
                h.label_unfollow.setText(getContext().getString(R.string.delete_button_alt));
            }else{
                h.label_unfollow.setText(getContext().getString(R.string.unfollow_user));
            }
            if(userBeans.get(position).getId_user() == App.read(PREFERENCES.ID_USER_FROM_WEB,0))
                h.delete_recent.setVisibility(View.GONE);
            else
                h.delete_recent.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return userBeans.size();
        }


        public class FollowsHolder extends RecyclerView.ViewHolder{
            ImagenCircular image_user_follow;
            TextView name_follow;
            RelativeLayout root_follows_user;
            RelativeLayout delete_recent;
            TextView label_unfollow;
            TextView name_propietary_pet_searching;
            public FollowsHolder(@NonNull View itemView) {
                super(itemView);
                name_follow = itemView.findViewById(R.id.name_follow);
                image_user_follow = itemView.findViewById(R.id.image_user_follow);
                root_follows_user = itemView.findViewById(R.id.root_follows_user);
                delete_recent = itemView.findViewById(R.id.delete_recent);
                label_unfollow = itemView.findViewById(R.id.label_unfollow);
                name_propietary_pet_searching = itemView.findViewById(R.id.name_propietary_pet_searching);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       listener = (changue_fragment_parameters_listener)context;
    }
}