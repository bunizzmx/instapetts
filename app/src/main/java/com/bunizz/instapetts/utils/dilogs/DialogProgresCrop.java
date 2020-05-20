package com.bunizz.instapetts.utils.dilogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.List;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Tony Zaitoun on 5/14/2016.
 */
public class DialogProgresCrop extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = false;
    private boolean isLocked = false;
    private boolean showActionIcons = true;
    Activity context;
    int tipo_permision;
     TextView percentage;
     ImagenCircular image_crop_video;


    public DialogProgresCrop(Activity context){
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_progress_crop, null);
        percentage = dialogView.findViewById(R.id.percentage);
        image_crop_video = dialogView.findViewById(R.id.image_crop_video);
        percentage.setText("0 %");
        Glide.with(context).load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"))
                .placeholder(context.getResources().getDrawable(R.drawable.ic_hand_pet_preload))
                .error(context.getResources().getDrawable(R.drawable.ic_hand_pet_preload))
                .into(image_crop_video);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;

    }

  public   void set_progress_percentage(double progress){
      context.runOnUiThread(() -> percentage.setText("" + (int) progress + " %"));

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

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public boolean isAllowAnimation() {
        return allowAnimation;
    }

    public void setAllowAnimation(boolean allowAnimation) {
        this.allowAnimation = allowAnimation;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isShowActionIcons() {
        return showActionIcons;
    }

    public void setShowActionIcons(boolean showActionIcons) {
        this.showActionIcons = showActionIcons;
    }

    /*public TextView getDescription() {
        return description;
    }*/


    /**
     * ListView Adapter Class.
     */

}
