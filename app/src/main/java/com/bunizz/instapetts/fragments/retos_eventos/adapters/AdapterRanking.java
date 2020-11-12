package com.bunizz.instapetts.fragments.retos_eventos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.RankingBean;
import com.bunizz.instapetts.fragments.feed.adapters.FeedAdapterNewsUsers;
import com.bunizz.instapetts.utils.ImagenCircular;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRanking extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    ArrayList<RankingBean> data =   new ArrayList<>();

    public ArrayList<RankingBean> getData() {
        return data;
    }

    public void setData(ArrayList<RankingBean> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public AdapterRanking(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking,parent,false);
        return new RankingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RankingHolder h =(RankingHolder)holder;
        switch (position){
            case 0:
                h.medall_ranking.setVisibility(View.VISIBLE);
                h.card_number_ranking.setCardBackgroundColor(Color.parseColor("#FFA900"));
                h.medall_ranking.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_firts));
                break;
            case 1:
                h.medall_ranking.setVisibility(View.VISIBLE);
                h.card_number_ranking.setCardBackgroundColor(Color.parseColor("#555555"));
                h.medall_ranking.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_second));
                break;
            case 2:
                h.medall_ranking.setVisibility(View.VISIBLE);
                h.card_number_ranking.setCardBackgroundColor(Color.parseColor("#4C3913"));
                h.medall_ranking.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thirth));
                break;
            default:
                h.medall_ranking.setVisibility(View.GONE);
                break;
        }
        h.num_ranking.setText("" + (position + 1));
        h.likes_ranking.setText("" + data.get(position).getLikes());
        h.name_pet_ranking.setText(data.get(position).getName_pet());
        h.name_propietary_pet.setText(data.get(position).getName_user());
        Glide.with(context).load(data.get(position).getUrl_photo_pet()).into(h.image_pet_ranking);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class RankingHolder extends  RecyclerView.ViewHolder{
    CardView card_number_ranking;
    TextView num_ranking;
    TextView name_pet_ranking;
    TextView likes_ranking;
    TextView name_propietary_pet;
    ImageView medall_ranking;
    ImagenCircular image_pet_ranking;
        public RankingHolder(@NonNull View itemView) {
            super(itemView);
            card_number_ranking = itemView.findViewById(R.id.card_number_ranking);
            num_ranking = itemView.findViewById(R.id.num_ranking);
            name_pet_ranking = itemView.findViewById(R.id.name_pet_ranking);
            image_pet_ranking = itemView.findViewById(R.id.image_pet_ranking);
            likes_ranking = itemView.findViewById(R.id.likes_ranking);
            medall_ranking = itemView.findViewById(R.id.medall_ranking);
            name_propietary_pet = itemView.findViewById(R.id.name_propietary_pet);
        }
    }
}
