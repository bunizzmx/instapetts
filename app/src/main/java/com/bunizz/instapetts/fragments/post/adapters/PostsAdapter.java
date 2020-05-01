package com.bunizz.instapetts.fragments.post.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.AspectBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.FeedAdapterHistories;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.bunizz.instapetts.utils.video_player.PlayerViewHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> current_images = new ArrayList<>();
    ArrayList<Object> data = new ArrayList<>();
    changue_fragment_parameters_listener listener;
    postsListener listener_post;
    open_camera_histories_listener listener_open_h;
    private RequestManager requestManager_param;
    public postsListener getListener_post() {
        return listener_post;
    }

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

    private static final int TYPE_POST=1;
    private static final int TYPE_POST_VIDEO=2;

    public PostsAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Object> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        PostBean validate = (PostBean) data.get(position);
        if(validate.getType_post() == 0)
            return TYPE_POST;
        else
            return TYPE_POST_VIDEO;
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
            case TYPE_POST_VIDEO:
                view = getInflatedView(parent, R.layout.item_feed_post_video);
                return new PlayerViewHolder(view);
            default:
                view = getInflatedView(parent, R.layout.item_feed_post);
                return new FeedHolder(view);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewtype = getItemViewType(position);
        switch (viewtype) {

            case TYPE_POST_VIDEO:
                PlayerViewHolder vid_h=(PlayerViewHolder)holder;
                PostBean mo = (PostBean) data.get(position);
                vid_h.requestManager = requestManager_param;
                vid_h.parent.setTag(this);
                Glide.with(context).load(mo.getThumb_video()).into(vid_h.mediaCoverImage);
                vid_h.mediaCoverImage.setVisibility(View.VISIBLE);
                if(!mo.getAddress().equals("INVALID")) {
                    vid_h.addres_post.setVisibility(View.VISIBLE);
                    vid_h.addres_post.setText(mo.getAddress());
                }
                else
                    vid_h.addres_post.setVisibility(View.GONE);

                AspectBean aspectBean = new AspectBean();
                aspectBean = App.getInstance().getAspect(mo.getAspect());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(aspectBean.getWidth(), aspectBean.getHeight());
                vid_h.root_multiple_image.setLayoutParams(layoutParams);

                vid_h.icon_like.setOnClickListener(view -> {
                    if(!mo.isLiked()) {
                        vid_h.layout_double_tap_like.setVisibility(View.VISIBLE);
                        vid_h.layout_double_tap_like.animate_icon(vid_h.layout_double_tap_like);
                    }
                    if(!mo.isLiked()) {
                        mo.setLiked(true);
                        vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                        listener_post.onLike(mo.getId_post_from_web(), true, mo.getId_usuario(), mo.getThumb_video());
                    }else{
                        mo.setLiked(false);
                        vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
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
                        vid_h.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
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
                    vid_h.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                }

                if(mo.isLiked()){
                    vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                }else{
                    vid_h.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                }
                vid_h.open_options_posts.setOnClickListener(view -> listener_post.openMenuOptions(mo.getId_post_from_web(),mo.getId_usuario(),mo.getUuid()));

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
                        if(!data_parsed.isLiked()) {
                            f.layout_double_tap_like.setVisibility(View.VISIBLE);
                            f.layout_double_tap_like.animate_icon(f.layout_double_tap_like);
                        }
                        if(!data_parsed.isLiked()) {
                            data_parsed.setLiked(true);
                            f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                            if(is_multiple(data_parsed.getUrls_posts())) {
                                String s[]= data_parsed.getUrls_posts().split(",");
                                listener_post.onLike(data_parsed.getId_post_from_web(), true, data_parsed.getId_usuario(), s[0]);
                            }else{
                                listener_post.onLike(data_parsed.getId_post_from_web(), true, data_parsed.getId_usuario(), data_parsed.getUrls_posts());
                            }
                        }else{
                            data_parsed.setLiked(false);
                            f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                        }
                    }
                });
                f.root_preview_perfil_click.setOnClickListener(view ->{
                    Bundle b = new Bundle();
                    b.putString(BUNDLES.UUID,data_parsed.getUuid());
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

                Glide.with(context).load(data_parsed.getUrl_photo_user()).into(f.image_pet);

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
        TextView description_posts;
        ImagenCircular image_pet;
        TextView name_pet,name_user_posts,num_likes_posts;
        RelativeLayout root_multiple_image;
        ImageView single_image,icon_like;
        TextView date_post;
        DoubleTapLikeView layout_double_tap_like;
        ImageView save_posts;
        ProgressBar progres_image;
        TextView addres_post;
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

    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }
}
