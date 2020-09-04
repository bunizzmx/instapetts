package com.bunizz.instapetts.utils.dilogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.listeners.available_name_listener;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class DialogFirstUser extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = false;
    private boolean isLocked = false;
    Context context;
    Button add_other_pet;
    String title_permision;
    EditText configure_name;
    ImageView close_dialog;
    ImageView image_userd_edit;
    Button save_info_perfil;
    ImageView status_icon_name_tag;
    TextView label_tag_instapets;
    EditText descripcion_user;
    EditText user_instapetts;
    available_name_listener listener;
    public available_name_listener getListener() {
        return listener;
    }

    public void setListener(available_name_listener listener) {
        this.listener = listener;
    }

    @SuppressLint("CheckResult")
    public DialogFirstUser(Context context){
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_first_user, null);
        image_userd_edit = dialogView.findViewById(R.id.image_userd_edit);
        configure_name = dialogView.findViewById(R.id.configure_name);
        save_info_perfil = dialogView.findViewById(R.id.save_info_perfil);
        status_icon_name_tag = dialogView.findViewById(R.id.status_icon_name_tag);
        label_tag_instapets = dialogView.findViewById(R.id.label_tag_instapets);
        descripcion_user = dialogView.findViewById(R.id.descripcion_user);
        user_instapetts = dialogView.findViewById(R.id.user_instapetts);
        Glide.with(getContext()).load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_userd_edit);
        configure_name.setText(App.read(PREFERENCES.NAME_USER,""));
        RxTextView.textChanges(user_instapetts)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if(charSequence.toString().trim().length() > 3) {
                        status_icon_name_tag.setVisibility(View.GONE);
                        label_tag_instapets.setVisibility(View.GONE);
                        listener.name(charSequence.toString().trim());
                    }
                    else {
                        label_tag_instapets.setVisibility(View.VISIBLE);
                        label_tag_instapets.setText(getContext().getString(R.string.more_caracteres));
                    }
                });

        save_info_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!descripcion_user.getText().toString().isEmpty() && !configure_name.getText().toString().isEmpty() && user_instapetts.getText().toString().trim().length() > 3) {
                    String URI_FINAL = App.getInstance().make_uri_bucket_profile();
                    String URI_FINAL_THUMBH = App.getInstance().make_uri_bucket_profile_tumbh();
                    App.write(PREFERENCES.DESCRIPCCION, descripcion_user.getText().toString());
                    App.write(PREFERENCES.FOTO_PROFILE_USER, URI_FINAL);
                    App.write(PREFERENCES.FOTO_PROFILE_USER_THUMBH, URI_FINAL_THUMBH);
                    App.write(PREFERENCES.NAME_USER, configure_name.getText().toString());
                    Bundle b = new Bundle();
                    b.putString("DESCRIPCION", descripcion_user.getText().toString());
                    b.putString("PHOTO", URI_FINAL);
                    b.putString("PHOTO_LOCAL", App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
                    App.write("NAME_TAG_INSTAPETTS",user_instapetts.getText().toString().trim());
                    listener.saveInfo(b);
                }else{
                    if(descripcion_user.getText().toString().length() < 2){
                        Toast.makeText(getContext(),getContext().getString(R.string.name_invalid),Toast.LENGTH_LONG).show();
                    }
                    if(user_instapetts.getText().toString().trim().length() <= 3){
                        status_icon_name_tag.setVisibility(View.VISIBLE);
                        status_icon_name_tag.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_no_available));
                        label_tag_instapets.setVisibility(View.VISIBLE);
                        label_tag_instapets.setText(getContext().getString(R.string.more_caracteres));
                    }
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
    }

    public void is_name_valid(boolean valid){
        Log.e("AVAILABLE_NAME","--->:" + valid);
        if(valid)
            status_icon_name_tag.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_available));
        else
            status_icon_name_tag.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_no_available));
        status_icon_name_tag.setVisibility(View.VISIBLE);
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
