package com.bunizz.instapetts.utils.dilogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.feed.FeedContract;

import java.util.List;

public class DialogShosePet extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Activity context;
    TextView label_body;
    String title_permision;
    RecyclerView chose_pet_list;
    pet_shoe_adapter adapter;



    public DialogShosePet(Activity context){
        this.context = context;
        adapter = new pet_shoe_adapter(context);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_shose_pet, null);
        chose_pet_list = dialogView.findViewById(R.id.chose_pet_list);
        label_body = dialogView.findViewById(R.id.label_shose_pet);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
        prepare_list();
    }

    void prepare_list(){
        chose_pet_list.setLayoutManager(new LinearLayoutManager(context));
        chose_pet_list.setAdapter(adapter);
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


    public class pet_shoe_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        Context context;

        public pet_shoe_adapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chose_pet,parent,false);
            return  new chose_pet_holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        public class chose_pet_holder extends RecyclerView.ViewHolder{

            public chose_pet_holder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


}
