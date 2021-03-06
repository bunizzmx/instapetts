package com.bunizz.instapetts.utils.dilogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bunizz.instapetts.R;

import java.util.List;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Tony Zaitoun on 5/14/2016.
 */
public class DialogPermision extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    private boolean showActionIcons = true;
    Activity context;
    TextView label_activate,label_body;
    ImageView close_dialog;
    String title_permision;
    String body_permision;
    int tipo_permision;
    Button configure_permision;

    public String getTitle_permision() {
        return title_permision;
    }

    public void setTitle_permision(String title_permision) {
        this.title_permision = title_permision;
        label_activate.setText(this.title_permision);
    }

    public String getBody_permision() {
        return body_permision;
    }

    public void setBody_permision(String body_permision) {
        this.body_permision = body_permision;
        label_body.setText(this.body_permision);
    }

    public int getTipo_permision() {
        return tipo_permision;
    }

    public void setTipo_permision(int tipo_permision) {
        this.tipo_permision = tipo_permision;
    }

    public DialogPermision(Activity context){
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_permision, null);
        label_body = dialogView.findViewById(R.id.label_body);
        label_activate= dialogView.findViewById(R.id.label_activate);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;

    ;

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

    public void setTitulo(List<String> titulo){
        try {
            setTitleVisable(true);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    public void setTitleVisable(boolean show){
        try{

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * set the description visibility.
     *
     * @param show if true visibility will be VISIBLE, if false it will be set to GONE.
     */
    public void setDescriptionVisable(boolean show){
        try{

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * Getters and Setters.
     *
     */


    @Override
    public Context getContext() {
        return context;
    }

   /* public TextView getTitle() {
        return title;
    }

   /* public ListView getListView() {
        return listView;
    }*/


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
