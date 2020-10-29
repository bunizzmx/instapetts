package com.bunizz.instapetts.utils.dilogs;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.wizardPets.PetTtype;
import com.bunizz.instapetts.fragments.wizardPets.adapters.TypePetsAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.process_save_pet_listener;
import com.bunizz.instapetts.utils.ProgressCircle;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by Tony Zaitoun on 5/14/2016.
 */
public class DialogChangeTypePet extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    private boolean showActionIcons = true;
    Activity context;
    int tipo_permision;
    RecyclerView list_type_pet;
    TypePetsAdapter adapter;
    ArrayList<PetTtype> petTtypes = new ArrayList<>();
    process_save_pet_listener listener_pet_config;
    ImageView close_dialog;
    public process_save_pet_listener getListener_pet_config() {
        return listener_pet_config;
    }

    public void setListener_pet_config(process_save_pet_listener listener_pet_config) {
        this.listener_pet_config = listener_pet_config;
    }

    public DialogChangeTypePet(Activity context){
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_change_typepet, null);
        list_type_pet = dialogView.findViewById(R.id.list_type_pet);
        close_dialog = dialogView.findViewById(R.id.close_dialog);

        close_dialog.setOnClickListener(view -> dismiss());
        list_type_pet.setLayoutManager(new GridLayoutManager(context,3));
        adapter = new TypePetsAdapter(getContext());
        adapter.setListener(new change_instance_wizard() {
            @Override
            public void onchange(int type_fragment, Bundle data) {
                if(listener_pet_config!=null){
                    listener_pet_config.SaveDataPet(data,1);
                    dismiss();
                }
            }

            @Override
            public void onpetFinish(boolean pet_saved) {

            }
        });
        petTtypes.add(new PetTtype(R.drawable.ic_perro,getContext().getResources().getString(R.string.pet_type_dog),1));
        petTtypes.add(new PetTtype(R.drawable.ic_gato,getContext().getResources().getString(R.string.pet_type_cat),2));
        petTtypes.add(new PetTtype(R.drawable.ic_mascota_perico,getContext().getResources().getString(R.string.pet_type_ave),3));
        petTtypes.add(new PetTtype(R.drawable.ic_mascota_conejo,getContext().getResources().getString(R.string.pet_type_conejo),4));
        petTtypes.add(new PetTtype(R.drawable.ic_mascota_hamster,getContext().getResources().getString(R.string.pet_type_hamster),5));
        petTtypes.add(new PetTtype(R.drawable.ic_serpiente,getContext().getResources().getString(R.string.reptil),6));
        petTtypes.add(new PetTtype(R.drawable.ic_pez,getContext().getResources().getString(R.string.pez),8));
        petTtypes.add(new PetTtype(R.drawable.ic_cabra,getContext().getResources().getString(R.string.bovinos),9));
        petTtypes.add(new PetTtype(R.drawable.ic_otro,getContext().getResources().getString(R.string.pet_type_otro),7));
        adapter.setPetTtypes(petTtypes);
        list_type_pet.setAdapter(adapter);

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
