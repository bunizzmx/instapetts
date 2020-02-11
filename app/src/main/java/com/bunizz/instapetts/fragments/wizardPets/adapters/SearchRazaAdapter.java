package com.bunizz.instapetts.fragments.wizardPets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;

public class SearchRazaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    public SearchRazaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_text_search_raza,parent,false);
        return  new SearchRazaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public class SearchRazaHolder extends RecyclerView.ViewHolder{

        public SearchRazaHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
