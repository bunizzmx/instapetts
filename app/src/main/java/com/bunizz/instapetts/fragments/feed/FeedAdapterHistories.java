package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.listeners.open_camera_histories_listener;
import com.bunizz.instapetts.activitys.story_player.StoryPlayer;
import com.bunizz.instapetts.utils.ImagenCircular;

import org.parceler.Parcels;

import java.util.ArrayList;


public class FeedAdapterHistories extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<HistoriesBean> historiesBeans = new ArrayList<>();
    open_camera_histories_listener listener;

    public ArrayList<HistoriesBean> getHistoriesBeans() {
        return historiesBeans;
    }

    public void setHistoriesBeans(ArrayList<HistoriesBean> historiesBeans) {
        if(historiesBeans!=null) {
            this.historiesBeans.addAll(historiesBeans);
            notifyDataSetChanged();
        }
    }

    public open_camera_histories_listener getListener() {
        return listener;
    }

    public void setListener(open_camera_histories_listener listener) {
        this.listener = listener;
    }

    public FeedAdapterHistories(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return  new FeedHistoriesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FeedHistoriesHolder h =(FeedHistoriesHolder)holder;
        if(position == 0){
            h.name_pet_item.setText(context.getResources().getString(R.string.you_history));
            if(historiesBeans.get(position)!=null){
                if(historiesBeans.get(position).getHistorias()!=null){
                    h.profile_background.setOnClickListener(view -> {
                            Intent i = new Intent(context, StoryPlayer.class);
                            i.putExtra("sliders", Parcels.wrap(historiesBeans));
                            i.putExtra("SELECTED_POSITION", position);
                            context.startActivity(i);
                    });
                    h.image_pet_history.setVisibility(View.VISIBLE);
                    String splitItems[] = historiesBeans.get(position).getHistorias().split(",");
                    String splitSubitems[] = splitItems[splitItems.length-1].split(";");
                    Glide.with(context).load(App.getInstance().getBucketUriHistorie(splitSubitems[4])).into(h.profile_background);
                    h.add_story_icon.setVisibility(View.GONE);
                }else{
                    h.add_story_icon.setVisibility(View.VISIBLE);
                    h.image_pet_history.setVisibility(View.GONE);
                    h.image_pet_history.setStroke_separate(false);
                    h.image_pet_history.setBorderColor(Color.WHITE);
                    h.profile_background.setOnClickListener(view -> {
                        if(listener!=null){
                            listener.open();
                        }

                    });

                }
            }else{
                h.image_pet_history.setVisibility(View.GONE);
            }
            Glide.with(context).load(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID")).placeholder(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.image_pet_history);
        }else{
            h.add_story_icon.setVisibility(View.GONE);
            h.image_pet_history.setVisibility(View.VISIBLE);
            String splitItems[] = historiesBeans.get(position).getHistorias().split(",");
            String splitSubitems[] = splitItems[splitItems.length-1].split(";");
            Glide.with(context).load(App.getInstance().getBucketUriHistorie(splitSubitems[4])).into(h.profile_background);
            Glide.with(context).load(historiesBeans.get(position).getPhoto_user()).into(h.image_pet_history);
            h.profile_background.setOnClickListener(view -> {
                Intent i = new Intent(context, StoryPlayer.class);
                if(historiesBeans.get(0).getHistorias()!=null && !historiesBeans.get(position).getHistorias().isEmpty() ) {
                    Log.e("CONTENIDO_HISTORY","-->A:" + historiesBeans.get(position).getHistorias());
                    i.putExtra("sliders", Parcels.wrap(historiesBeans));
                    i.putExtra("SELECTED_POSITION", position);
                }
                else{
                    Log.e("CONTENIDO_HISTORY","-->B:" + historiesBeans.get(position).getHistorias());
                    i.putExtra("SELECTED_POSITION", position - 1);
                    i.putExtra("sliders", Parcels.wrap(historiesBeans));
                }

                context.startActivity(i);
            });
            if(historiesBeans.get(position).getName_user().length() >10) {
                h.name_pet_item.setText(historiesBeans.get(position).getName_user().substring(0,9) + "...");
            }else{
                h.name_pet_item.setText(historiesBeans.get(position).getName_user());
            }
        }

    }

    @Override
    public int getItemCount() {
        return historiesBeans.size();
    }

    public class FeedHistoriesHolder extends RecyclerView.ViewHolder{
     ImagenCircular image_pet_history;
     TextView name_pet_item;
     ImageView profile_background;
     ImageView add_story_icon;
        public FeedHistoriesHolder(@NonNull View itemView) {
            super(itemView);
            image_pet_history = itemView.findViewById(R.id.image_pet_history);
            name_pet_item = itemView.findViewById(R.id.name_pet_item);
            profile_background = itemView.findViewById(R.id.profile_background);
            add_story_icon = itemView.findViewById(R.id.add_story_icon);
        }
    }


}
