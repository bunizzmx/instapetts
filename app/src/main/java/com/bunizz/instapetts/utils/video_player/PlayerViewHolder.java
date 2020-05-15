package com.bunizz.instapetts.utils.video_player;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.bunizz.instapetts.utils.loadings.SpinKitView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    public FrameLayout mediaContainer;
    public ImageView mediaCoverImage, volumeControl;
    public RequestManager requestManager;
    public CardView card_view_mute;
    public RelativeLayout root_preview_perfil_click,open_options_posts;
    public TextView description_posts;
    public ImagenCircular image_pet;
    public TextView name_pet,name_user_posts,num_likes_posts;
    public  RelativeLayout root_multiple_image;
    public ImageView icon_like;
    public TextView date_post;
    public DoubleTapLikeView layout_double_tap_like;
    public ImageView save_posts;
    public ImagenCircular mini_user_photo;
    public TextView addres_post;
    public ImageView icon_commentar;
    public View parent;
    public TextView num_coments;
    public LinearLayout num_comments_layout;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        mediaContainer = itemView.findViewById(R.id.mediaContainer);
        mediaCoverImage = itemView.findViewById(R.id.ivMediaCoverImage);
        volumeControl = itemView.findViewById(R.id.ivVolumeControl);
        card_view_mute = itemView.findViewById(R.id.card_view_mute);
        root_preview_perfil_click = itemView.findViewById(R.id.root_preview_perfil_click);
        image_pet = itemView.findViewById(R.id.image_pet);
        description_posts = itemView.findViewById(R.id.description_posts);
        name_pet = itemView.findViewById(R.id.name_pet);
        root_multiple_image = itemView.findViewById(R.id.root_multiple_image);
        date_post = itemView.findViewById(R.id.date_post);
        layout_double_tap_like = itemView.findViewById(R.id.layout_double_tap_like);
        icon_like = itemView.findViewById(R.id.icon_like);
        save_posts = itemView.findViewById(R.id.save_posts);
        name_user_posts = itemView.findViewById(R.id.name_user_posts);
        num_likes_posts = itemView.findViewById(R.id.num_likes_posts);
        open_options_posts = itemView.findViewById(R.id.open_options_posts);
        mini_user_photo = itemView.findViewById(R.id.mini_user_photo);
        addres_post = itemView.findViewById(R.id.addres_post);
        icon_commentar = itemView.findViewById(R.id.icon_commentar);
        num_coments = itemView.findViewById(R.id.num_coments);
        num_comments_layout = itemView.findViewById(R.id.num_comments_layout);
    }


}
