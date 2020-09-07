package com.bunizz.instapetts.utils.dilogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.AspectBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.listeners.postsListener;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.ParcelerRuntimeException;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;

public class DialogPreviewPost extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Context context;
    chose_pet_listener listener;
    PostBean postBean;
    TextView addres_post_dialog;
    ImagenCircular image_pet_dialog;
    TextView num_likes_posts_dialog,name_pet_post_dialog;
    ImageView image_preview_dialog;
    ImageView save_posts;
    changue_fragment_parameters_listener listener_fragment;


    LinearLayout open_profile_user;

    RelativeLayout l_like_post,l_saved_post;
    ImageView icon_like;
    postsListener listener_post;
    LikePostHelper likePostHelper;
    SavedPostHelper savedPostHelper;
    DotsIndicator dots_indicator;
    TextView label_number_indicator;
    CardView card_number_indicator;

    ViewPager list_fotos;

    public postsListener getListener_post() {
        return listener_post;
    }

    public changue_fragment_parameters_listener getListener_fragment() {
        return listener_fragment;
    }

    public void setListener_fragment(changue_fragment_parameters_listener listener_fragment) {
        this.listener_fragment = listener_fragment;
    }

    public void setListener_post(postsListener listener_post) {
        this.listener_post = listener_post;
    }

    public chose_pet_listener getListener() {
        return listener;
    }

    public void setListener(chose_pet_listener listener) {
        this.listener = listener;
    }

    public DialogPreviewPost(Context context,PostBean postBean){
        this.context = context;
        this.postBean =postBean;
        Log.e("DATA_FOR_PREVIEW","PARAMETRO _ id_usuario : " + postBean.getId_usuario());
        likePostHelper = new LikePostHelper(this.context);
        savedPostHelper = new SavedPostHelper(this.context);

        if(likePostHelper.searchPostById(this.postBean.getId_post_from_web()))
            this.postBean.setLiked(true);
        else
            this.postBean.setLiked(false);

        if(savedPostHelper.searchPostById(this.postBean.getId_post_from_web()))
            this.postBean.setSaved(true);
        else
            this.postBean.setSaved(false);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_preview_post, null);
        image_pet_dialog = dialogView.findViewById(R.id.image_pet_dialog);
        open_profile_user = dialogView.findViewById(R.id.open_profile_user);
        addres_post_dialog = dialogView.findViewById(R.id.addres_post_dialog);
        name_pet_post_dialog = dialogView.findViewById(R.id.name_pet_post_dialog);
        num_likes_posts_dialog = dialogView.findViewById(R.id.num_likes_posts_dialog);
        image_preview_dialog = dialogView.findViewById(R.id.image_preview_dialog);
        save_posts = dialogView.findViewById(R.id.save_posts);
        l_like_post = dialogView.findViewById(R.id.l_like_post);
        l_saved_post = dialogView.findViewById(R.id.l_saved_post);
        icon_like = dialogView.findViewById(R.id.icon_like);
        list_fotos = dialogView.findViewById(R.id.list_fotos);
        dots_indicator = dialogView.findViewById(R.id.dots_indicator);
        label_number_indicator = dialogView.findViewById(R.id.label_number_indicator);
        card_number_indicator = dialogView.findViewById(R.id.card_number_indicator);



        open_profile_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Bundle b = new Bundle();
                Log.e("DATA_FOR_PREVIEW","id_usuario : " + postBean.getId_usuario());
                b.putString(BUNDLES.UUID,postBean.getUuid());
                b.putInt(BUNDLES.ID_USUARIO,postBean.getId_usuario());
                if(listener_fragment!=null)
                   listener_fragment.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,b);
            }
        });

        if(is_multiple(this.postBean.getUrls_posts())) {
            list_fotos.setVisibility(View.VISIBLE);
            dots_indicator.setVisibility(View.VISIBLE);
            card_number_indicator.setVisibility(View.VISIBLE);
            String splits[]  = this.postBean.getUrls_posts().split(",");
            int splits_number = splits.length;
            ViewPagerAdapter adapter = new ViewPagerAdapter(context);
            if (postBean.getUrls_posts() != null)
                adapter.setUris_not_parsed(postBean.getUrls_posts());
            list_fotos.setAdapter(adapter);
            label_number_indicator.setText("1/"+splits_number);
           list_fotos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    label_number_indicator.setText((position + 1) + "/"+ splits_number);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            dots_indicator.setViewPager(list_fotos);
        }else{
            card_number_indicator.setVisibility(View.GONE);
            list_fotos.setVisibility(View.GONE);
            dots_indicator.setVisibility(View.GONE);
            AspectBean aspect_image = new AspectBean();
            aspect_image = App.getInstance().getAspect(postBean.getAspect());
            RelativeLayout.LayoutParams tam_img = new RelativeLayout.LayoutParams(aspect_image.getWidth(), aspect_image.getHeight());
            image_preview_dialog.setLayoutParams(tam_img);
            Glide.with(this.context).load(this.postBean.getUrls_posts()).placeholder(context.getResources().getDrawable(R.drawable.ic_holder)).into(image_preview_dialog);
        }
        name_pet_post_dialog.setText(this.postBean.getName_pet());
        if(this.postBean.getLikes() == 0)
           num_likes_posts_dialog.setText(getContext().getResources().getString(R.string.first_like));
        else
            num_likes_posts_dialog.setText("a " + this.postBean.getLikes() + " " + getContext().getResources().getString(R.string.people_like_this));
        Glide.with(this.context).load(this.postBean.getUrl_photo_user()).placeholder(context.getResources().getDrawable(R.drawable.ic_holder)).into(image_pet_dialog);
        if(postBean.getAddress()!=null) {
            if (!postBean.getAddress().equals("INVALID")) {
                addres_post_dialog.setVisibility(View.VISIBLE);
                addres_post_dialog.setText(postBean.getAddress());
            } else
                addres_post_dialog.setVisibility(View.GONE);
        }else{
            addres_post_dialog.setVisibility(View.GONE);
        }

        l_like_post.setOnClickListener(view -> {
            if(!this.postBean.isLiked()) {
                this.postBean.setLiked(true);
                 if(!App.read(PREFERENCES.MODO_INVITADO,false))
                  icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                if(is_multiple(this.postBean.getUrls_posts())) {
                    String s[]= this.postBean.getUrls_posts().split(",");
                    listener_post.onLike(this.postBean.getId_post_from_web(), true, this.postBean.getId_usuario(),this.postBean.getThumb_video());
                }else{
                    listener_post.onLike(this.postBean.getId_post_from_web(), true, this.postBean.getId_usuario(), this.postBean.getThumb_video());
                }
            }else{
                this.postBean.setLiked(false);
                if(!App.read(PREFERENCES.MODO_INVITADO,false))
                 icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
            }
        });

        l_saved_post.setOnClickListener(view -> {
            if(this.postBean.isSaved()) {
                this.postBean.setSaved(false);
                listener_post.onDisfavorite(this.postBean.getId_post_from_web());
                save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
            }
            else {
                this.postBean.setSaved(true);
                if(!App.read(PREFERENCES.MODO_INVITADO,false))
                  save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_fill));
                listener_post.onFavorite(this.postBean.getId_post_from_web(), this.postBean);
            }
        });
        if(this.postBean.isSaved()){
            if(!App.read(PREFERENCES.MODO_INVITADO,false))
              save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_fill));
        }else{
            save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        }

        if(this.postBean.isLiked()){
            if(!App.read(PREFERENCES.MODO_INVITADO,false))
              icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
        }else{
            if(!App.read(PREFERENCES.MODO_INVITADO,false))
              icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
        }



        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
    }
    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }



    @Override
    public void show(){
        dialog.setCancelable(this.cancelable);
        dialog.show();
    }

    /**
     * Dismiss the dialog.
     */
    @Override
    public void dismiss(){
        if(!isLocked){
            dialog.dismiss();
        }
    }

    public void setTitleVisable(boolean show){
        try{

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public Context getContext() {
        return context;
    }


    @Override
    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }
    @Override
    public AlertDialog getDialog() {
        return dialog;
    }





}
