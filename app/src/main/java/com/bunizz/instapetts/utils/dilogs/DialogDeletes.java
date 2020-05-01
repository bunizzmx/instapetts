package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.listeners.delete;
import com.bunizz.instapetts.utils.ImagenCircular;

import androidx.appcompat.app.AlertDialog;

public class DialogDeletes extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Context context;
    LinearLayout delete_now_layout;
    delete listener;
    TextView text_delete_dialog;

    public delete getListener() {
        return listener;
    }

    public void setListener(delete listener) {
        this.listener = listener;
    }

    public DialogDeletes(Context context,int id,int type_dialog){
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_deletes, null);
        delete_now_layout = dialogView.findViewById(R.id.delete_now_layout);
        text_delete_dialog = dialogView.findViewById(R.id.text_delete_dialog);
        if(type_dialog == 0)
            text_delete_dialog.setText("Eliminar");
        else
            text_delete_dialog.setText("Eliminar todas");

        delete_now_layout.setOnClickListener(v -> {
            if(listener!=null) {
                if(type_dialog == 0) {
                    listener.delete(true);
                }else{
                    listener.deleteOne(id);
                }
                dismiss();
            }
        });
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
