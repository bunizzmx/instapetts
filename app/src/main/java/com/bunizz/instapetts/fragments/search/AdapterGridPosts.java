package com.bunizz.instapetts.fragments.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogPreviewPost;

import org.parceler.Parcels;

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
                Glide.with(context).load(data_parsed.getThumb_video()).placeholder(context.getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(h.fisrt_stack_foto);
            }else{
                h.duration_item.setVisibility(View.GONE);
                String splits[]  = data_parsed.getUrls_posts().split(",");
                if(splits.length == 1){
                    h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_camera));
                }else{
                    h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_album));
                }
                Glide.with(context).load(splits[0]).placeholder(context.getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(h.fisrt_stack_foto);
            }

            Glide.with(context).load(data_parsed.getUrl_photo_user()).placeholder(context.getResources().getDrawable(R.drawable.ic_hand_pet_preload)).into(h.image_user_item);
            h.name_user_item.setText(data_parsed.getName_user());
            h.root_posts_search_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(((PostBean) posts.get(position)).getType_post() == 0) {
                        dialogPreviewPost = new DialogPreviewPost(context, data_parsed);
                        dialogPreviewPost.show();
                    }else{
                        Intent i = new Intent(context, PlayVideoActivity.class);
                        i.putExtra("TYPE_PLAYER",1);
                        i.putExtra("BEAN", Parcels.wrap(data_parsed));
                        context.startActivity(i);
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
    }

