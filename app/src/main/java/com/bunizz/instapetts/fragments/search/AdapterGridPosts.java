package com.bunizz.instapetts.fragments.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.dilogs.DialogPreviewPost;
import com.bunizz.instapetts.utils.dilogs.DialogPreviewPostVideo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

    public class AdapterGridPosts extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ArrayList<Object> posts = new ArrayList<>();

        Context context;

        changue_fragment_parameters_listener listener;

        public changue_fragment_parameters_listener getListener() {
            return listener;
        }

        DialogPreviewPost dialogPreviewPost;
        DialogPreviewPostVideo dialogPreviewPostVideo;
        public void setListener(changue_fragment_parameters_listener listener) {
            this.listener = listener;
        }

        public AdapterGridPosts(Context context) {
            this.context = context;
        }

        public ArrayList<Object> getPosts() {
            return posts;
        }

        public void setPosts(ArrayList<Object> posts) {
            this.posts = posts;
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
            if(splits.length == 1){
                h.multiple_images_posts.setVisibility(View.GONE);
            }else{
                h.multiple_images_posts.setVisibility(View.VISIBLE);
            }
            Glide.with(context).load(splits[0]).placeholder(context.getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(h.fisrt_stack_foto);
            h.root_posts_search_item.setOnClickListener(view -> {
                if(listener!=null){
                    Bundle data_selected = new Bundle();
                    data_selected.putInt("POSITION",position);
                    listener.change_fragment_parameter(FragmentElement.INSTANCE_GET_POSTS_PUBLICS_ADVANCED,data_selected);
                }
            });
            h.root_posts_search_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(((PostBean) posts.get(position)).getType_post() == 0) {
                        dialogPreviewPost = new DialogPreviewPost(context, data_parsed);
                        dialogPreviewPost.show();
                    }else{
                        dialogPreviewPostVideo = new DialogPreviewPostVideo(context,data_parsed,initGlide());
                        dialogPreviewPostVideo.show();
                    }
                    return false;
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
            public postsPublicsHolder(@NonNull View itemView) {
                super(itemView);
                fisrt_stack_foto = itemView.findViewById(R.id.fisrt_stack_foto);
                multiple_images_posts = itemView.findViewById(R.id.multiple_images_posts);
                root_posts_search_item = itemView.findViewById(R.id.root_posts_search_item);
            }
        }

        private RequestManager initGlide() {
            RequestOptions options = new RequestOptions();
            return Glide.with(context)
                    .setDefaultRequestOptions(options);
        }
    }

