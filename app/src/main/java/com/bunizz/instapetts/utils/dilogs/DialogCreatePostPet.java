package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.listeners.delete;
import com.bunizz.instapetts.listeners.open_sheet_listener;

import androidx.appcompat.app.AlertDialog;

public class DialogCreatePostPet extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Context context;
    Button create_pet_button;
    CheckBox check_no_mostrar;
    open_sheet_listener listener;

    public open_sheet_listener getListener() {
        return listener;
    }

    public void setListener(open_sheet_listener listener) {
        this.listener = listener;
    }

    public DialogCreatePostPet(Context context){
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_create_post_pet, null);
        check_no_mostrar = dialogView.findViewById(R.id.check_no_mostrar);
        check_no_mostrar.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                App.write(BUNDLES.NO_MOSTRAR_DIALOGO_PET,2);
                dismiss();
                Log.e("ESTATUS_CHECK","CHECKEADO");
            }else{
                App.write(BUNDLES.NO_MOSTRAR_DIALOGO_PET,1);
                dismiss();
                Log.e("ESTATUS_CHECK","QUITADO");
            }
        });
        create_pet_button = dialogView.findViewById(R.id.create_pet_button);
        create_pet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
               if(listener!=null)
                   listener.open_wizard_pet();
            }
        });
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
    }


    @Override
    public void show(){
        dialog.setCancelable(true);
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
