package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.listeners.actions_dialog_profile;
import com.bunizz.instapetts.listeners.change_instance_wizard;

import androidx.appcompat.app.AlertDialog;

public class DialogOptionsPosts extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Context context;

    String title_permision;
    actions_dialog_profile listener;
    RelativeLayout delete_post;
    int ID_POST =0;
    public actions_dialog_profile getListener() {
        return listener;
    }

    public void setListener(actions_dialog_profile listener) {
        this.listener = listener;
    }

    public DialogOptionsPosts(Context context,int id_post,int id_usuario,String uuid){
        this.context = context;
        ID_POST = id_post;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_options_posts, null);
        delete_post = dialogView.findViewById(R.id.delete_post);
        delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.delete_post(id_post);
                dismiss();
            }
        });
        if(uuid.equals(App.read(PREFERENCES.UUID,"INVALID")) ||  id_post == App.read(PREFERENCES.ID_USER_FROM_WEB,0))
            delete_post.setVisibility(View.VISIBLE);
        else
            delete_post.setVisibility(View.GONE);

        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
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
