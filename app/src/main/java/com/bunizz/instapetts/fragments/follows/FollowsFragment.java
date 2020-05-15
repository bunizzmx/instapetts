package com.bunizz.instapetts.fragments.follows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.FollowsBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.info.InfoPetFragment;
import com.bunizz.instapetts.listeners.RatePetListener;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogRatePet;
import com.bunizz.instapetts.utils.fastScroll.FastScrollItemIndicator;
import com.bunizz.instapetts.utils.fastScroll.FastScrollerThumbView;
import com.bunizz.instapetts.utils.fastScroll.FastScrollerView;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bunizz.instapetts.constantes.BUNDLES.PETBEAN;

public class FollowsFragment extends Fragment implements FollowsContract.View {


    public static FollowsFragment newInstance() {
        return new FollowsFragment();
    }
    String ID_PET="";
    String NAME_PET="";
    PetHelper petHelper;
    PetBean petBean;
    String name_user;
    int id_user=0;
    String uuid="";
    ArrayList<UserBean> userBeans = new ArrayList<>();

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


    @BindView(R.id.sample_basic_fastscroller)
    FastScrollerView sample_basic_fastscroller;

    @BindView(R.id.sample_basic_fastscroller_thumb)
    FastScrollerThumbView sample_basic_fastscroller_thumb;

    FollowsAdapter adapter;
    FollowsPresenter presenter;
    changue_fragment_parameters_listener listener;
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

        sample_basic_fastscroller.setUseDefaultScroller(false);
        sample_basic_fastscroller.getItemIndicatorSelectedCallbacks().add(
                new FastScrollerView.ItemIndicatorSelectedCallback() {
                    @Override
                    public void onItemIndicatorSelected(
                            FastScrollItemIndicator indicator,
                            int indicatorCenterY,
                            int itemPosition
                    ) {
                        // Handle scrolling
                    }
                }
        );
        adapter.setListener((type_fragment, data) -> {
            if(listener!=null)
                listener.change_fragment_parameter(type_fragment,data);
        });
        Style style = Style.values()[14];
        Sprite drawable = SpriteFactory.create(style);
        spin_kit.setIndeterminateDrawable(drawable);
        spin_kit.setColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        spin_kit.setVisibility(View.VISIBLE);
        name_user_folowers.setText(name_user);
        sample_basic_fastscroller.setupWithRecyclerView(list_follows, (position) -> new FastScrollItemIndicator.Text(
                "AA".substring(0, 1).toUpperCase()
        ));
        sample_basic_fastscroller_thumb.setupWithFastScroller(sample_basic_fastscroller);
        presenter.getFirstFollows(uuid);
    }

    public void updateInfo(Bundle b){
        if(presenter!=null) {
            if (b != null) {
                name_user = b.getString(BUNDLES.NAME_USUARIO);
                id_user = b.getInt(BUNDLES.ID_USUARIO);
                uuid = b.getString(BUNDLES.UUID);
            }
            name_user_folowers.setText(name_user);
            presenter.getFirstFollows(uuid);
        }
    }


    @Override
    public void showFirstFollows(ArrayList<FollowsBean> followsBeans) {
        if(followsBeans.size()>0){
            spin_kit.setVisibility(View.GONE);
            adapter.setUserBeans(followsBeans);
            if(sample_basic_fastscroller==null)
                sample_basic_fastscroller.setupWithRecyclerView(list_follows,null);
            else
                sample_basic_fastscroller.getItemIndicators();
        }else{
            body_no_data.setText("Cuando sigas 1 o mas cuentas apareceran aqui");
            title_no_internet.setText("Aun no sigues a nadie");
            icon_no_internet.setVisibility(View.GONE);
            spin_kit.setVisibility(View.GONE);
            root_no_internet.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void showNextFollows(ArrayList<PostBean> posts) {

    }

    @Override
    public void noInternet() {
        spin_kit.setVisibility(View.GONE);
        root_no_internet.setVisibility(View.VISIBLE);
    }

    @Override
    public void Error() {
        presenter.getFirstFollows(uuid);
    }


    public class FollowsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<FollowsBean> userBeans = new ArrayList<>();
        Context context;
        changue_fragment_parameters_listener listener;

        public changue_fragment_parameters_listener getListener() {
            return listener;
        }

        public void setListener(changue_fragment_parameters_listener listener) {
            this.listener = listener;
        }

        public FollowsAdapter(Context context) {
            this.context = context;
        }

        public ArrayList<FollowsBean> getUserBeans() {
            return userBeans;
        }

        public void setUserBeans(ArrayList<FollowsBean> userBeans) {
            this.userBeans = userBeans;
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
                    b.putString(BUNDLES.UUID,userBeans.get(position).getUudi());
                    b.putInt(BUNDLES.ID_USUARIO,userBeans.get(position).getId_user());
                    listener.change_fragment_parameter(FragmentElement.INSTANCE_PREVIEW_PROFILE,b);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userBeans.size();
        }


        public class FollowsHolder extends RecyclerView.ViewHolder{
            ImagenCircular image_user_follow;
            TextView name_follow;
            RelativeLayout root_follows_user;
            public FollowsHolder(@NonNull View itemView) {
                super(itemView);
                name_follow = itemView.findViewById(R.id.name_follow);
                image_user_follow = itemView.findViewById(R.id.image_user_follow);
                root_follows_user = itemView.findViewById(R.id.root_follows_user);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       listener = (changue_fragment_parameters_listener)context;
    }
}