package com.bunizz.instapetts.fragments.wizardPets.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance_wizard;

import java.util.ArrayList;

public class SearchRazaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    ArrayList<RazaBean> data = new ArrayList<>();
    String texto_buscado="";
    change_instance_wizard listener;

    public change_instance_wizard getListener() {
        return listener;
    }

    public void setListener(change_instance_wizard listener) {
        this.listener = listener;
    }

    public ArrayList<RazaBean> getData() {
        return data;
    }

    public void setData(ArrayList<RazaBean> data,String texto_bu) {
        this.data = data;
        this.texto_buscado =texto_bu;
        notifyDataSetChanged();
    }

    public SearchRazaAdapter(Context context) {
        this.context = context;
    }

    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_text_search_raza,parent,false);
        return  new SearchRazaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchRazaHolder h =(SearchRazaHolder)holder;
        String texto = data.get(position).getName_raza_esp().toLowerCase();
        int index = texto.indexOf(texto_buscado.trim().toLowerCase());
        texto_buscado.replace("."," ").toLowerCase().trim().length();
        Spannable wordtoSpan = new SpannableString(texto);
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.parseColor("#ea6060"));
        ForegroundColorSpan backgroundSpan = new ForegroundColorSpan(Color.BLACK);
        if(texto_buscado.length()<=texto.length()) {
            wordtoSpan.setSpan(foregroundSpan, index, index+ texto_buscado.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            h.result_search_raza.setText(wordtoSpan);
        }else{
        }
        h.root_raza_select.setOnClickListener(view -> {
            Bundle b = new Bundle();
            b.putString(BUNDLES.RAZA_PET,data.get(position).getName_raza_esp());
            listener.onchange(FragmentElement.INSTANCE_DATA_PET,b);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchRazaHolder extends RecyclerView.ViewHolder{
         TextView result_search_raza;
         RelativeLayout root_raza_select;
        public SearchRazaHolder(@NonNull View itemView) {
            super(itemView);
            result_search_raza = itemView.findViewById(R.id.result_search_raza);
            root_raza_select = itemView.findViewById(R.id.root_raza_select);
        }
    }
}
