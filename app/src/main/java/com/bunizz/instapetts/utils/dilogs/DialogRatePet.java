package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.listeners.RatePetListener;
import com.bunizz.instapetts.listeners.change_instance_wizard;

import androidx.appcompat.app.AlertDialog;

public class DialogRatePet extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Context context;
    RatePetListener listener;
    TextView name_pet_rate;
    ImageView image_pet_rate;
    PetBean PETBEAN;
    Button rate_now;
    EditText input_comment_rate_pet;
    RatingBar ratingBar;
    ImageView close_dialog;
    public RatePetListener getListener() {
        return listener;
    }

    public void setListener(RatePetListener listener) {
        this.listener = listener;
    }

    public DialogRatePet(Context context, PetBean petBean){
        this.context = context;
        PETBEAN = petBean;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_rate_pet, null);
        name_pet_rate = dialogView.findViewById(R.id.name_pet_rate);
        input_comment_rate_pet = dialogView.findViewById(R.id.input_comment_rate_pet);
        ratingBar = dialogView.findViewById(R.id.ratingBar);
        close_dialog = dialogView.findViewById(R.id.close_dialog);
        image_pet_rate = dialogView.findViewById(R.id.image_pet_rate);
        rate_now = dialogView.findViewById(R.id.rate_now);
        rate_now.setOnClickListener(view -> {
            if(listener!=null) {
                double rate_number = ratingBar.getRating();
                if(rate_number>0) {
                    listener.onRate(rate_number, input_comment_rate_pet.getText().toString(), Integer.parseInt(petBean.getId_pet()), petBean.getId_propietary(), petBean.getUuid());
                    dismiss();
                }else{
                    Toast.makeText(context,context.getString(R.string.rate_first),Toast.LENGTH_LONG).show();
                }
            }
        });
        close_dialog.setOnClickListener(v -> dismiss());
        Glide.with(this.context).load(PETBEAN.getUrl_photo_tumbh()).into(image_pet_rate);
        name_pet_rate.setText(PETBEAN.getName_pet());
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
