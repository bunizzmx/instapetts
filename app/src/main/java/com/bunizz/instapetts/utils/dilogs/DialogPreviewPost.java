package com.bunizz.instapetts.utils.dilogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;

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


    public chose_pet_listener getListener() {
        return listener;
    }

    public void setListener(chose_pet_listener listener) {
        this.listener = listener;
    }

    public DialogPreviewPost(Context context,PostBean postBean){
        this.context = context;
        this.postBean =postBean;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_preview_post, null);
        image_pet_dialog = dialogView.findViewById(R.id.image_pet_dialog);
        addres_post_dialog = dialogView.findViewById(R.id.addres_post_dialog);
        name_pet_post_dialog = dialogView.findViewById(R.id.name_pet_post_dialog);
        num_likes_posts_dialog = dialogView.findViewById(R.id.num_likes_posts_dialog);
        image_preview_dialog = dialogView.findViewById(R.id.image_preview_dialog);
        if(is_multiple(this.postBean.getUrls_posts())) {
            String splits[]  = this.postBean.getUrls_posts().split(",");
            Glide.with(this.context).load(splits[0]).into(image_preview_dialog);
        }else{
            Glide.with(this.context).load(this.postBean.getUrls_posts()).into(image_preview_dialog);
        }
        name_pet_post_dialog.setText(this.postBean.getName_pet());
        if(this.postBean.getLikes() == 0)
           num_likes_posts_dialog.setText("se el primero en darle like");
        else
            num_likes_posts_dialog.setText("a " + this.postBean.getLikes() + " personas les gusta esto");
        Glide.with(this.context).load(this.postBean.getUrl_photo_pet()).into(image_pet_dialog);
        if(!postBean.getAddress().equals("INVALID")) {
            addres_post_dialog.setVisibility(View.VISIBLE);
            addres_post_dialog.setText(postBean.getAddress());
        }
        else
            addres_post_dialog.setVisibility(View.GONE);

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
