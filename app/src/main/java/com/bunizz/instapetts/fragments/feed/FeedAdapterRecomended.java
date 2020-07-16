package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.listeners.SelectUserListener;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedAdapterRecomended extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Object> data = new ArrayList<>();
    Context context;
    SelectUserListener listener;

    public SelectUserListener getListener() {
        return listener;
    }

    public void setListener(SelectUserListener listener) {
        this.listener = listener;
    }

    public FeedAdapterRecomended(Context context) {
        this.context = context;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_post_recomended,parent,false);
        return new RecomendedHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecomendedHolder h =(RecomendedHolder)holder;
        PostBean data_parsed = (PostBean) data.get(position);
        if(data_parsed.getType_post() !=1) {
            if (is_multiple(data_parsed.getUrls_posts())) {
                String splits[] = data_parsed.getUrls_posts().split(",");
                Glide.with(context).load(data_parsed.getThumb_video()).into(h.image_post_recomended);
                h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_album));
            } else {
                h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_camera));
                Glide.with(context).load(data_parsed.getThumb_video()).into(h.image_post_recomended);
            }
        }else{
            h.multiple_images_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
            Glide.with(context).load(data_parsed.getThumb_video()).into(h.image_post_recomended);
        }
        Glide.with(context).load(data_parsed.getUrl_photo_user()).into(h.image_user_recomended);

        if(data_parsed.getName_user().trim().length() > 20)
           h.name_user_recomended.setText(data_parsed.getName_user().substring(0,18) + "...");
        else
            h.name_user_recomended.setText(data_parsed.getName_user());
        if(App.getInstance().fecha_lenguaje_humano(data_parsed.getDate_post()).contains(context.getString(R.string.hace))){
            h.date_user_recomended.setText( App.getInstance().fecha_lenguaje_humano(data_parsed.getDate_post()));
        }else{
            h.date_user_recomended.setText(context.getString(R.string.hace) + " "  + App.getInstance().fecha_lenguaje_humano(data_parsed.getDate_post()));
        }

        h.root_simple_recomended_user.setOnClickListener(view -> listener.onSelectUser(data_parsed.getId_usuario(),data_parsed.getUuid()));

        switch (data_parsed.getType_pet()){
            case 1:
                h.icon_type_mascota.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perro));
                break;
            case 2:
                h.icon_type_mascota.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gato));
                break;
            case 3:
                h.icon_type_mascota.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_perico));
                break;
            case 4:
                h.icon_type_mascota.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_conejo));
                break;
            case 5:
                h.icon_type_mascota.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mascota_hamster));
                break;
            case 6:break;
            case 7:break;
            case 8:break;
            case 9:break;
            case 10:break;
            case 11:break;
            case 12:break;
            case 13:break;
            case 14:break;
            case 15:break;
            case 16:break;
            default:break;

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecomendedHolder extends RecyclerView.ViewHolder{
       ImageView image_post_recomended,multiple_images_posts,icon_type_mascota;
       ImagenCircular image_user_recomended;
       TextView name_user_recomended,date_user_recomended;
       RelativeLayout root_simple_recomended_user;
        public RecomendedHolder(@NonNull View itemView) {
            super(itemView);
            multiple_images_posts =itemView.findViewById(R.id.multiple_images_posts);
            image_post_recomended = itemView.findViewById(R.id.image_post_recomended);
            image_user_recomended = itemView.findViewById(R.id.image_user_recomended);
            name_user_recomended = itemView.findViewById(R.id.name_user_recomended);
            root_simple_recomended_user = itemView.findViewById(R.id.root_simple_recomended_user);
            icon_type_mascota = itemView.findViewById(R.id.icon_type_mascota);
            date_user_recomended = itemView.findViewById(R.id.date_user_recomended);
        }
    }

    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }
}
