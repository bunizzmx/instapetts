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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.SearchPetBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.listeners.chose_pet_listener;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;
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
    ArrayList<PetBean> petBeans = new ArrayList<>();
    chose_pet_listener listener;

    public chose_pet_listener getListener() {
        return listener;
    }

    public void setListener(chose_pet_listener listener) {
        this.listener = listener;
    }

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
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
        adapter.setListener(new chose_pet_listener() {
            @Override
            public void chose(String url_foto, int id_pet,String name_pet) {
                if(listener!=null){
                    listener.chose(url_foto,id_pet,name_pet);
                }
            }

            @Override
            public void request_no_pets() {

            }
        });
        prepare_list();
    }

    void prepare_list(){
        chose_pet_list.setLayoutManager(new LinearLayoutManager(context));
        chose_pet_list.setAdapter(adapter);
    }

    public ArrayList<PetBean> getPetBeans() {
        return petBeans;
    }

    public void setPetBeans(ArrayList<PetBean> petBeans) {
        this.petBeans = petBeans;
        adapter.setPetBeans(this.petBeans);
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
        ArrayList<PetBean> petBeans = new ArrayList<>();
        chose_pet_listener listener;

        public chose_pet_listener getListener() {
            return listener;
        }

        public void setListener(chose_pet_listener listener) {
            this.listener = listener;
        }

        public ArrayList<PetBean> getPetBeans() {
            return petBeans;
        }

        public void setPetBeans(ArrayList<PetBean> petBeans) {
            this.petBeans = petBeans;
            notifyDataSetChanged();
        }

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
            chose_pet_holder h =(chose_pet_holder)holder;
            Glide.with(context).load(petBeans.get(position).getUrl_photo_tumbh()).into(h.pet_chose_list);
            h.name_pet_chose_list.setText(petBeans.get(position).getName_pet());
            h.root_chose_pet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.chose(petBeans.get(position).getUrl_photo_tumbh(),Integer.parseInt(petBeans.get(position).getId_pet()),petBeans.get(position).getName_pet());
                }
            });
        }

        @Override
        public int getItemCount() {
            return petBeans.size();
        }

        public class chose_pet_holder extends RecyclerView.ViewHolder{
              ImagenCircular pet_chose_list;
              TextView name_pet_chose_list;
              CardView root_chose_pet;
            public chose_pet_holder(@NonNull View itemView) {
                super(itemView);
                pet_chose_list = itemView.findViewById(R.id.pet_chose_list);
                name_pet_chose_list = itemView.findViewById(R.id.name_pet_chose_list);
                root_chose_pet = itemView.findViewById(R.id.root_chose_pet);
            }
        }
    }


}
