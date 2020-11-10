package com.bunizz.instapetts.fragments.retos_eventos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.feed.adapters.FeedAdapterNewsUsers;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRanking extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

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
                h.card_number_ranking.setCardBackgroundColor(Color.parseColor("#FFA900"));
                break;
            case 1:
                h.card_number_ranking.setCardBackgroundColor(Color.parseColor("#555555"));
                break;
            case 2:
                h.card_number_ranking.setCardBackgroundColor(Color.parseColor("#4C3913"));
                break;
            default:
                break;
        }
        h.num_ranking.setText("" + (position + 1));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class RankingHolder extends  RecyclerView.ViewHolder{
    CardView card_number_ranking;
    TextView num_ranking;
        public RankingHolder(@NonNull View itemView) {
            super(itemView);
            card_number_ranking = itemView.findViewById(R.id.card_number_ranking);
            num_ranking = itemView.findViewById(R.id.num_ranking);
        }
    }
}
