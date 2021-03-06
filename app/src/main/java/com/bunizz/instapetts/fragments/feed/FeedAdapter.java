package com.bunizz.instapetts.fragments.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.MotionEventCompat;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.DataEmptyBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.listeners.SelectUserListener;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.OnSwipeTouchListener;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import com.bunizz.instapetts.utils.video_player.PlayerViewHolder;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> current_images = new ArrayList<>();
    ArrayList<Object> data = new ArrayList<>();
    ArrayList<Object> data_recomended = new ArrayList<>();
    ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
    changue_fragment_parameters_listener listener;
    postsListener listener_post;
    open_camera_histories_listener listener_open_h;
    Style style = Style.values()[6];
    Sprite drawable = SpriteFactory.create(style);
    private RequestManager requestManager_param;
    public postsListener getListener_post() {
        return listener_post;
    }
    boolean POST_EMPTY = false;
    public void setListener_post(postsListener listener_post) {
        this.listener_post = listener_post;
    }

    public RequestManager getRequestManager() {
        return requestManager_param;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager_param = requestManager;
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

    public ArrayList<Object> getData_recomended() {
        return data_recomended;
    }

    public void setData_recomended(ArrayList<Object> data_recomended) {
        this.data_recomended = data_recomended;
        notifyDataSetChanged();
    }

    private static final int TYPE_POST=1;
    private static final int TYPE_POST_VIDEO=4;
    private static final int TYPE_HISTORI = 2;
    private static final int TYPE_EMPTY = 3;
    public FeedAdapter(Context context,ArrayList<Object> data) {
        this.context = context;
        this.data.addAll(data);
    }

    public ArrayList<HistoriesBean> getHistoriesBeans() {
        return historiesBeans;
    }

    public void setHistoriesBeans(ArrayList<HistoriesBean> historiesBeans) {
        Log.e("HISTORIES_BEANS","-->" + historiesBeans.size());
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
        if(this.data.size() > 1){
            Log.e("SIZE_EMPTY","no empty");
            POST_EMPTY = false;
        }else{
            Log.e("SIZE_EMPTY","add_empty");
            this.data.add(new DataEmptyBean("xxx","xx"));
            POST_EMPTY = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = data.get(position);
        if (recyclerViewItem instanceof HistoriesBean) {
            return TYPE_HISTORI;
        }else if(recyclerViewItem instanceof DataEmptyBean){
            return TYPE_EMPTY;
        }else{
            PostBean validate = (PostBean) data.get(position);
            if(validate.getType_post() == 0)
                 return TYPE_POST;
            else
                return TYPE_POST_VIDEO;
        }
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
            case TYPE_EMPTY:
                view = getInflatedView(parent, R.layout.no_data_feed1);
                return new EmptyHolder(view);
            case TYPE_POST_VIDEO:
                view = getInflatedView(parent, R.layout.item_feed_post_video);
                return new PlayerViewHolder(view);

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

            case TYPE_EMPTY:
                EmptyHolder e =(EmptyHolder)holder;
                FeedAdapterRecomended feedAdapterRecomended = new FeedAdapterRecomended(context);
                feedAdapterRecomended.setListener((id_user, uuid) -> {
                    Bundle b = new Bundle();
                    b.putString(BUNDLES.UUID,uuid);
                    b.putInt(BUNDLES.ID_USUARIO,id_user);
                    listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,b);
                });
                e.list_post_recomended.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                feedAdapterRecomended.setData(data_recomended);
                e.list_post_recomended.setAdapter(feedAdapterRecomended);
                e.title_no_data.setText("Aun no sigues a nadie");
                e.body_no_data.setText("Te recomendamos a estas hermosas mascotas,porque no les echas un ojo.");

                break;

            case TYPE_POST_VIDEO:
                PlayerViewHolder vid_h=(PlayerViewHolder)holder;
                PostBean mo = (PostBean) data.get(position);
                vid_h.requestManager = requestManager_param;
                vid_h.parent.setTag(this);
                if(!mo.getAddress().equals("INVALID")) {
                    vid_h.addres_post.setVisibility(View.VISIBLE);
                    vid_h.addres_post.setText(mo.getAddress());
                }
                else
                    vid_h.addres_post.setVisibility(View.GONE);
                vid_h.icon_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mo.isLiked()) {
                            vid_h.layout_double_tap_like.setVisibility(View.VISIBLE);
                            vid_h.layout_double_tap_like.animate_icon(vid_h.layout_double_tap_like);
                        }
                        if(!mo.isLiked()) {
                            mo.setLiked(true);
                            vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                            listener_post.onLike(mo.getId_post_from_web(),true);
                        }else{
                            mo.setLiked(false);
                            vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_w));
                        }
                    }
                });
                vid_h.root_preview_perfil_click.setOnClickListener(view ->{
                    Bundle b = new Bundle();
                    b.putString(BUNDLES.UUID,mo.getUuid());
                    Log.e("ID_USUARIO_POST","-->" + mo.getId_usuario());
                    b.putInt(BUNDLES.ID_USUARIO,mo.getId_usuario());
                    listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,b);
                });
                vid_h.name_pet.setText(mo.getName_pet());
                vid_h.name_user_posts.setText(mo.getName_user());
                vid_h.num_likes_posts.setText("" + mo.getLikes() );
                if(mo.getDescription().isEmpty()){
                    vid_h.description_posts.setVisibility(View.GONE);
                }else{
                    vid_h.description_posts.setVisibility(View.VISIBLE);
                    vid_h.description_posts.setText(mo.getDescription());
                }

                Glide.with(context).load(mo.getUrl_photo_pet()).into(vid_h.image_pet);
                Glide.with(context).load(mo.getUrl_photo_user()).into(vid_h.mini_user_photo);
                vid_h.date_post.setText(App.fecha_lenguaje_humano(mo.getDate_post()));
                vid_h.save_posts.setOnClickListener(view -> {
                    if(mo.isSaved()) {
                        mo.setSaved(false);
                        listener_post.onDisfavorite(mo.getId_post_from_web());
                        vid_h.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_w));
                    }
                    else {
                        mo.setSaved(true);
                        vid_h.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_fill));
                        listener_post.onFavorite(mo.getId_post_from_web(), mo);
                    }
                });
                if(mo.isSaved()){
                    vid_h.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_fill));
                }else{
                    vid_h.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_w));
                }

                if(mo.isLiked()){
                    vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                }else{
                    vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_w));
                }
                vid_h.open_options_posts.setOnClickListener(view -> listener_post.openMenuOptions(mo.getId_post_from_web(),mo.getId_usuario(),mo.getUuid()));
                vid_h.requestManager
                        .load(mo.getUrls_posts())
                        .into(vid_h.mediaCoverImage);

                break;
            default:
                FeedHolder f = (FeedHolder)holder;
                PostBean data_parsed = (PostBean) data.get(position);
                if(!data_parsed.getAddress().equals("INVALID")) {
                    f.addres_post.setVisibility(View.VISIBLE);
                    f.addres_post.setText(data_parsed.getAddress());
                }
                else
                    f.addres_post.setVisibility(View.GONE);
                if(is_multiple(data_parsed.getUrls_posts())) {
                    f.card_number_indicator.setVisibility(View.VISIBLE);
                    String s[]= data_parsed.getUrls_posts().split(",");
                    int splits_number = s.length;
                    f.label_number_indicator.setText("1/"+splits_number);
                    f.progres_image.setVisibility(View.GONE);
                    f.root_multiple_image.setVisibility(View.VISIBLE);
                    f.single_image.setVisibility(View.GONE);
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context);
                    if (data_parsed.getUrls_posts() != null)
                        adapter.setUris_not_parsed(data_parsed.getUrls_posts());
                    f.list_fotos.setAdapter(adapter);
                    f.list_fotos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            f.label_number_indicator.setText((position + 1) + "/"+splits_number);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    f.dots_indicator.setViewPager(f.list_fotos);

                }else{
                    f.progres_image.setIndeterminateDrawable(drawable);
                    f.progres_image.setColor(context.getResources().getColor(R.color.primary));
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
                        if(!data_parsed.isLiked()) {
                            f.layout_double_tap_like.setVisibility(View.VISIBLE);
                            f.layout_double_tap_like.animate_icon(f.layout_double_tap_like);
                        }
                        if(!data_parsed.isLiked()) {
                            data_parsed.setLiked(true);
                            f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                            listener_post.onLike(data_parsed.getId_post_from_web(),true);
                        }else{
                            data_parsed.setLiked(false);
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
                         data_parsed.setSaved(false);
                         listener_post.onDisfavorite(data_parsed.getId_post_from_web());
                         f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                     }
                     else {
                         data_parsed.setSaved(true);
                         f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_fill));
                         listener_post.onFavorite(data_parsed.getId_post_from_web(), data_parsed);
                     }
                 });
                 if(data_parsed.isSaved()){
                     f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_fill));
                 }else{
                     f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                 }

                if(data_parsed.isLiked()){
                    f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                }else{
                    f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                }
                f.open_options_posts.setOnClickListener(view -> listener_post.openMenuOptions(data_parsed.getId_post_from_web(),data_parsed.getId_usuario(),data_parsed.getUuid()));


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
        EmojiTextView description_posts;
        ImagenCircular image_pet;
        TextView name_pet,name_user_posts,num_likes_posts;
        RelativeLayout root_multiple_image;
        ImageView single_image,icon_like;
        TextView date_post;
        DoubleTapLikeView layout_double_tap_like;
        ImageView save_posts;
        SpinKitView progres_image;
        ImagenCircular mini_user_photo;
        TextView addres_post;
        CardView card_number_indicator;
        TextView label_number_indicator;
        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            card_number_indicator = itemView.findViewById(R.id.card_number_indicator);
            label_number_indicator = itemView.findViewById(R.id.label_number_indicator);
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
            addres_post = itemView.findViewById(R.id.addres_post);
        }
    }


    public class HistoriesHolder extends RecyclerView.ViewHolder{
        RecyclerView list_histories;
        public HistoriesHolder(@NonNull View itemView) {
            super(itemView);
            list_histories = itemView.findViewById(R.id.list_histories);
        }
    }



    public class EmptyHolder extends RecyclerView.ViewHolder{
        RecyclerView list_post_recomended;
        TextView title_no_data,body_no_data;
        public EmptyHolder(@NonNull View itemView) {
            super(itemView);
            list_post_recomended = itemView.findViewById(R.id.list_post_recomended);
            body_no_data = itemView.findViewById(R.id.body_no_data);
            title_no_data = itemView.findViewById(R.id.title_no_data);
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
