package com.bunizz.instapetts.fragments.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;
import static com.bunizz.instapetts.utils.trimVideo.utils.UIThreadUtil.runOnUiThread;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> current_images = new ArrayList<>();
    ArrayList<Object> data = new ArrayList<>();
    ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
    changue_fragment_parameters_listener listener;
    postsListener listener_post;
    open_camera_histories_listener listener_open_h;

    public postsListener getListener_post() {
        return listener_post;
    }

    public void setListener_post(postsListener listener_post) {
        this.listener_post = listener_post;
    }

    public open_camera_histories_listener getListener_open_h() {
        return listener_open_h;
    }

    public void setListener_open_h(open_camera_histories_listener listener_open_h) {
        this.listener_open_h = listener_open_h;
    }

    public changue_fragment_parameters_listener getListener() {
        return listener;
    }

    public void setListener(changue_fragment_parameters_listener listener) {
        this.listener = listener;
    }

    private static final int TYPE_POST=1;
    private static final int TYPE_HISTORI = 2;

    public FeedAdapter(Context context,ArrayList<Object> data) {
        this.context = context;
        this.data.addAll(data);
    }

    public ArrayList<HistoriesBean> getHistoriesBeans() {
        return historiesBeans;
    }

    public void setHistoriesBeans(ArrayList<HistoriesBean> historiesBeans) {
        Log.e("DATA_HISTORIES","xxxx" + historiesBeans.size());
        this.historiesBeans = historiesBeans;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public void addData(ArrayList<Object> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = data.get(position);
        if (recyclerViewItem instanceof HistoriesBean) {
            return TYPE_HISTORI;
        }
        return TYPE_POST;

    }


    private View getInflatedView(ViewGroup parent, int resourceLayout){
        return LayoutInflater
                .from(parent.getContext())
                .inflate(resourceLayout, parent, false);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_POST:
                view = getInflatedView(parent, R.layout.item_feed_post);
                return new FeedHolder(view);

            case TYPE_HISTORI:
            default:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_histories_feed,
                        parent, false);
                return new HistoriesHolder(unifiedNativeLayoutView);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewtype = getItemViewType(position);
        switch (viewtype) {
            case TYPE_HISTORI:
                HistoriesHolder h = (HistoriesHolder)holder;
                FeedAdapterHistories adapterHistories = new FeedAdapterHistories(context);
                adapterHistories.setListener(() -> {
                    if(listener_open_h!=null){
                        listener_open_h.open();
                    }
                });
                adapterHistories.setHistoriesBeans(historiesBeans);
                h.list_histories.setAdapter(adapterHistories);
                h.list_histories.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));

                break;
            default:
                FeedHolder f = (FeedHolder)holder;
                PostBean data_parsed = (PostBean) data.get(position);
                if(is_multiple(data_parsed.getUrls_posts())) {
                    f.progres_image.setVisibility(View.GONE);
                    f.root_multiple_image.setVisibility(View.VISIBLE);
                    f.single_image.setVisibility(View.GONE);
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context);
                    if (data_parsed.getUrls_posts() != null)
                        adapter.setUris_not_parsed(data_parsed.getUrls_posts());
                    f.list_fotos.setAdapter(adapter);
                    f.dots_indicator.setViewPager(f.list_fotos);

                }else{
                    f.root_multiple_image.setVisibility(View.GONE);
                    f.single_image.setVisibility(View.VISIBLE);
                    Glide.with(context).load(data_parsed.getUrls_posts()).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            f.progres_image.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            f.progres_image.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(f.single_image);

                }
                f.icon_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f.layout_double_tap_like.setVisibility(View.VISIBLE);
                        f.layout_double_tap_like.animate_icon(f.layout_double_tap_like);
                        if(!data_parsed.isLiked()) {
                            f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                            listener_post.onLike(data_parsed.getId_post_from_web());
                        }else{
                            f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                        }
                    }
                });
                f.root_preview_perfil_click.setOnClickListener(view ->{
                    Bundle b = new Bundle();
                    b.putString(BUNDLES.UUID,data_parsed.getUuid());
                    Log.e("ID_USUARIO_POST","-->" + data_parsed.getId_usuario());
                    b.putInt(BUNDLES.ID_USUARIO,data_parsed.getId_usuario());
                    listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,b);
                });
                f.name_pet.setText(data_parsed.getName_pet());
                f.name_user_posts.setText(data_parsed.getName_user());
                f.num_likes_posts.setText("a " + data_parsed.getLikes() + " usuarios les gusta esto");
                if(data_parsed.getDescription().isEmpty()){
                    f.description_posts.setVisibility(View.GONE);
                }else{
                    f.description_posts.setVisibility(View.VISIBLE);
                    f.description_posts.setText(data_parsed.getDescription());
                }

                Glide.with(context).load(data_parsed.getUrl_photo_pet()).into(f.image_pet);
                Glide.with(context).load(data_parsed.getUrl_photo_user()).into(f.mini_user_photo);
                f.date_post.setText(App.fecha_lenguaje_humano(data_parsed.getDate_post()));
                f.save_posts.setOnClickListener(view -> {
                     if(data_parsed.isSaved()) {
                         listener_post.onDisfavorite(data_parsed.getId_post_from_web());
                         f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_estrella));
                     }
                     else {
                         f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_estrella_black));
                         listener_post.onFavorite(data_parsed.getId_post_from_web(), data_parsed);
                     }
                 });
                 if(data_parsed.isSaved()){
                     f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_estrella_black));
                 }else{
                     f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_estrella));
                 }

                if(data_parsed.isLiked()){
                    f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                }else{
                    f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                }
                f.open_options_posts.setOnClickListener(view -> listener_post.openMenuOptions());


                break;


        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FeedHolder extends RecyclerView.ViewHolder{
        ViewPager list_fotos;
        DotsIndicator dots_indicator;
        RelativeLayout root_preview_perfil_click,open_options_posts;
        TextView description_posts;
        ImagenCircular image_pet;
        TextView name_pet,name_user_posts,num_likes_posts;
        RelativeLayout root_multiple_image;
        ImageView single_image,icon_like;
        TextView date_post;
        DoubleTapLikeView layout_double_tap_like;
        ImageView save_posts;
        ProgressBar progres_image;
        ImagenCircular mini_user_photo;
        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            root_preview_perfil_click = itemView.findViewById(R.id.root_preview_perfil_click);
            dots_indicator = itemView.findViewById(R.id.dots_indicator);
            list_fotos = itemView.findViewById(R.id.list_fotos);
            image_pet = itemView.findViewById(R.id.image_pet);
            description_posts = itemView.findViewById(R.id.description_posts);
            name_pet = itemView.findViewById(R.id.name_pet);
            root_multiple_image = itemView.findViewById(R.id.root_multiple_image);
            single_image = itemView.findViewById(R.id.single_image);
            date_post = itemView.findViewById(R.id.date_post);
            layout_double_tap_like = itemView.findViewById(R.id.layout_double_tap_like);
            icon_like = itemView.findViewById(R.id.icon_like);
            save_posts = itemView.findViewById(R.id.save_posts);
            progres_image = itemView.findViewById(R.id.progres_image);
            name_user_posts = itemView.findViewById(R.id.name_user_posts);
            num_likes_posts = itemView.findViewById(R.id.num_likes_posts);
            open_options_posts = itemView.findViewById(R.id.open_options_posts);
            mini_user_photo = itemView.findViewById(R.id.mini_user_photo);
        }
    }


    public class HistoriesHolder extends RecyclerView.ViewHolder{
        RecyclerView list_histories;
        public HistoriesHolder(@NonNull View itemView) {
            super(itemView);
            list_histories = itemView.findViewById(R.id.list_histories);
        }
    }

    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }
}
