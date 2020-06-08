package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
    LinearLayout layout_delete_algo;
    TextView description_deletes;
    TextView confirm_deletes;
    TextView confirm_decline;

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
        description_deletes = dialogView.findViewById(R.id.description_deletes);
        confirm_deletes = dialogView.findViewById(R.id.confirm_deletes);
        confirm_decline = dialogView.findViewById(R.id.confirm_decline);
        layout_delete_algo = dialogView.findViewById(R.id.layout_delete_algo);
        if(type_dialog == 0) {
            layout_delete_algo.setVisibility(View.GONE);
            text_delete_dialog.setText(getContext().getResources().getString(R.string.delete_button_alt));
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
        }
        else if(type_dialog == 1) {
            layout_delete_algo.setVisibility(View.GONE);
            text_delete_dialog.setText("Eliminar todas");
            delete_now_layout.setOnClickListener(v -> {
                if(listener!=null) {
                    if(type_dialog == 1) {
                        Log.e("EJECUTO_DELETE","SI");
                        listener.delete(true);
                    }else{
                        listener.deleteOne(id);
                    }
                    dismiss();
                }
            });
        }
        else {
            layout_delete_algo.setVisibility(View.VISIBLE);
            text_delete_dialog.setText(getContext().getResources().getString(R.string.delete_pet_title));
            description_deletes.setText(getContext().getResources().getString(R.string.delete_pet_body));
            confirm_deletes.setOnClickListener(v -> listener.delete(true));
            confirm_decline.setOnClickListener(v -> dismiss());
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
