package com.bunizz.instapetts.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.dilogs.DialogPreviewPost;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterGridPostsProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Object> posts = new ArrayList<>();

    Context context;

    changue_fragment_parameters_listener listener;

    postsListener listener_post;

    public postsListener getListener_post() {
        return listener_post;
    }

    public void setListener_post(postsListener listener_post) {
        this.listener_post = listener_post;
    }


    public changue_fragment_parameters_listener getListener() {
        return listener;
    }

    public void setListener(changue_fragment_parameters_listener listener) {
        this.listener = listener;
    }

    public AdapterGridPostsProfile(Context context) {
        this.context = context;
    }

    public ArrayList<Object> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Object> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_posts_simple,parent,false);
        return new postsPublicsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        postsPublicsHolder h = (postsPublicsHolder) holder;
        PostBean data_parsed =(PostBean) posts.get(position);
        String splits[]  = data_parsed.getUrls_posts().split(",");
        h.root_info_item_profile.setVisibility(View.GONE);


        if(data_parsed.getType_post()==0) {
            if(splits.length == 1){
                h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_camera));
            }else{
                h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_album));
            }
            Log.e("ADAPTER_GRID_POIST","--> : " + data_parsed.getThumb_video());
            if(data_parsed.getCensored() == 1){
                h.fisrt_stack_foto.setVisibility(View.GONE);
                h.layout_censored.setVisibility(View.VISIBLE);
            }else{
                h.layout_censored.setVisibility(View.GONE);
                h.fisrt_stack_foto.setVisibility(View.VISIBLE);
                Glide.with(context).load(data_parsed.getThumb_video())
                        .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                        .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.fisrt_stack_foto);
            }

        }else{
            h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
            if(data_parsed.getCensored() == 1){
                h.fisrt_stack_foto.setVisibility(View.GONE);
                h.layout_censored.setVisibility(View.VISIBLE);
            }else{
                h.layout_censored.setVisibility(View.GONE);
                h.fisrt_stack_foto.setVisibility(View.VISIBLE);
                Glide.with(context).load(data_parsed.getThumb_video())
                        .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                        .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.fisrt_stack_foto);
            }

        }
        h.root_posts_search_item.setOnClickListener(view -> {
        });
        h.root_posts_search_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(data_parsed.getType_post()==1){
                    Intent i = new Intent(context, PlayVideoActivity.class);
                    i.putExtra("TYPE_PLAYER",1);
                    i.putExtra("BEAN", Parcels.wrap(data_parsed));
                    context.startActivity(i);
                }else{
                    DialogPreviewPost dialogPreviewPost = new DialogPreviewPost(context,data_parsed);
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
                        public void commentPost(int id_post, boolean can_comment) {

                        }
                    });
                    dialogPreviewPost.show();
                }

                return false;
            }
        });
        h.root_posts_search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    Bundle data_selected = new Bundle();
                    data_selected.putInt("POSITION",position);
                    listener.change_fragment_parameter(FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED,data_selected);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class postsPublicsHolder extends RecyclerView.ViewHolder{
        ImageView multiple_images_posts,fisrt_stack_foto;
        CardView root_posts_search_item;
        LinearLayout root_info_item_profile;
        LinearLayout layout_censored;
        public postsPublicsHolder(@NonNull View itemView) {
            super(itemView);
            fisrt_stack_foto = itemView.findViewById(R.id.fisrt_stack_foto);
            multiple_images_posts = itemView.findViewById(R.id.multiple_images_posts);
            root_posts_search_item = itemView.findViewById(R.id.root_posts_search_item);
            root_info_item_profile = itemView.findViewById(R.id.root_info_item_profile);
            layout_censored = itemView.findViewById(R.id.layout_censored);
        }
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
        return Glide.with(context)
                .setDefaultRequestOptions(options);
    }
}

