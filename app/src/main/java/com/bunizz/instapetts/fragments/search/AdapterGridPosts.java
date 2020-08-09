package com.bunizz.instapetts.fragments.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity2;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.UnifiedAddHolder;
import com.bunizz.instapetts.fragments.tips.adapters.TipsAdapter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogPreviewPost;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import dagger.multibindings.ElementsIntoSet;
import retrofit2.http.POST;

public class AdapterGridPosts extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_POST=1;
        private static final int TYPE_ADD = 2;


        ArrayList<Object> posts = new ArrayList<>();
        Context context;

        changue_fragment_parameters_listener listener;

        postsListener listener_post;


    public void setListener_post(postsListener listener_post) {
        this.listener_post = listener_post;
    }

    public changue_fragment_parameters_listener getListener() {
            return listener;
        }
        DialogPreviewPost dialogPreviewPost;

        public void setListener(changue_fragment_parameters_listener listener) {
            this.listener = listener;
        }

        public int get_ultimo_id(){
            Object recyclerViewItem = posts.get(posts.size() - 1);
            if(recyclerViewItem instanceof PostBean)
                 return ((PostBean) recyclerViewItem).getId_post_from_web();
            else if( posts.get(posts.size() - 2) instanceof  PostBean){
                return ((PostBean) recyclerViewItem).getId_post_from_web();
            }else{
                return 0;
            }
        }

        public AdapterGridPosts(Context context) {
            this.context = context;
        }

        @Override
        public int getItemViewType(int position) {
            Object recyclerViewItem = posts.get(position);
                if(recyclerViewItem instanceof UnifiedNativeAd){
                    return  TYPE_ADD;
                }else{
                        return TYPE_POST;
                }
        }

    private View getInflatedView(ViewGroup parent, int resourceLayout){
        return LayoutInflater
                .from(parent.getContext())
                .inflate(resourceLayout, parent, false);
    }



        public ArrayList<Object> getPosts() {
            return posts;
        }

        public void setPosts(ArrayList<Object> posts) {
            this.posts.clear();
            this.posts.addAll(posts);
            notifyDataSetChanged();
        }

    public void addMorePosts(ArrayList<Object> posts) {
        int index = this.posts.size() ;
        this.posts.addAll(posts);
        notifyItemInserted(index);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_POST:
                view = getInflatedView(parent, R.layout.item_feed_posts_simple);
                return new postsPublicsHolder(view);
            default:
                view = getInflatedView(parent, R.layout.ad_unified);
                return new UnifiedAddHolder(view);
        }
    }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            int viewtype = getItemViewType(position);
            switch (viewtype) {
                case TYPE_POST:
                    postsPublicsHolder h = (postsPublicsHolder) holder;
                    PostBean data_parsed =(PostBean) posts.get(position);

                    h.root_posts_search_item.setOnClickListener(view -> {
                        if(listener!=null){
                            Bundle data_selected = new Bundle();
                            data_selected.putInt("POSITION",position);
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED,data_selected);
                        }
                    });
                    if(data_parsed.getType_post()==1){
                        h.duration_item.setVisibility(View.VISIBLE);
                        h.duration_item.setText(data_parsed.getDuracion() + " seg.");
                        h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                        Glide.with(context).load(data_parsed.getThumb_video()).placeholder(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.fisrt_stack_foto);
                    }else{
                        h.duration_item.setVisibility(View.GONE);
                        String splits[]  = data_parsed.getUrls_posts().split(",");
                        if(splits.length == 1){
                            h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_camera));
                        }else{
                            h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_album));
                        }
                        Glide.with(context).load(data_parsed.getThumb_video()).placeholder(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.fisrt_stack_foto);
                    }

                    Glide.with(context).load(data_parsed.getUrl_photo_user()).placeholder(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.image_user_item);
                    h.name_user_item.setText(data_parsed.getName_user());
                    h.root_posts_search_item.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if(((PostBean) posts.get(position)).getType_post() == 0) {
                                dialogPreviewPost = new DialogPreviewPost(context, data_parsed);
                                dialogPreviewPost.setListener_post(new postsListener() {
                                    @Override
                                    public void onLike(int id_post, boolean type_like, int id_usuario, String url_image) {
                                        listener_post.onLike(id_post,type_like,id_usuario,url_image);
                                    }

                                    @Override
                                    public void onFavorite(int id_post, PostBean postBean) {
                                      listener_post.onFavorite(id_post,postBean);
                                    }

                                    @Override
                                    public void onDisfavorite(int id_post) {
                                       listener_post.onDisfavorite(id_post);
                                    }

                                    @Override
                                    public void openMenuOptions(int id_post, int id_usuario, String uuid) {

                                    }

                                    @Override
                                    public void commentPost(int id_post, boolean can_comment,int id_usuario) {

                                    }

                                    @Override
                                    public void reproduceVideoActivity(PostBean postBean) {

                                    }
                                });
                                dialogPreviewPost.show();
                            }else{
                               listener_post.reproduceVideoActivity(data_parsed);
                            }
                            return false;
                        }
                    });

                    h.root_posts_search_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(((PostBean) posts.get(position)).getType_post() == 0) {
                                dialogPreviewPost = new DialogPreviewPost(context, data_parsed);
                                dialogPreviewPost.setListener_post(new postsListener() {
                                    @Override
                                    public void onLike(int id_post, boolean type_like, int id_usuario, String url_image) {
                                        listener_post.onLike(id_post,type_like,id_usuario,url_image);
                                    }

                                    @Override
                                    public void onFavorite(int id_post, PostBean postBean) {
                                        listener_post.onFavorite(id_post,postBean);
                                    }

                                    @Override
                                    public void onDisfavorite(int id_post) {
                                        listener_post.onDisfavorite(id_post);
                                    }

                                    @Override
                                    public void openMenuOptions(int id_post, int id_usuario, String uuid) {

                                    }

                                    @Override
                                    public void commentPost(int id_post, boolean can_comment,int id_usuario) {

                                    }

                                    @Override
                                    public void reproduceVideoActivity(PostBean postBean) {

                                    }
                                });
                                dialogPreviewPost.show();
                            }else{
                                listener_post.reproduceVideoActivity(data_parsed);
                            }
                        }
                    });
                    break;

                case TYPE_ADD:
                    UnifiedNativeAd nativeAd = (UnifiedNativeAd) posts.get(position);
                    populateNativeAdView(nativeAd, ((UnifiedAddHolder) holder).getAdView());
            }


        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        public class postsPublicsHolder extends RecyclerView.ViewHolder{
            ImageView multiple_images_posts,fisrt_stack_foto;
            CardView root_posts_search_item;
            TextView name_user_item;
            ImagenCircular image_user_item;
            TextView duration_item;
            public postsPublicsHolder(@NonNull View itemView) {
                super(itemView);
                fisrt_stack_foto = itemView.findViewById(R.id.fisrt_stack_foto);
                multiple_images_posts = itemView.findViewById(R.id.multiple_images_posts);
                root_posts_search_item = itemView.findViewById(R.id.root_posts_search_item);
                image_user_item = itemView.findViewById(R.id.image_user_item);
                name_user_item = itemView.findViewById(R.id.name_user_item);
                duration_item = itemView.findViewById(R.id.duration_item);
            }
        }

        private RequestManager initGlide() {
            RequestOptions options = new RequestOptions();
            return Glide.with(context)
                    .setDefaultRequestOptions(options);
        }


    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        ((Button) adView.getCallToActionView()).setBackground(context.getResources().getDrawable(R.drawable.button_create_acount));
        ((Button) adView.getCallToActionView()).setTextColor(Color.WHITE);
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
    }

