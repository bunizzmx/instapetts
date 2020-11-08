package com.bunizz.instapetts.fragments.retos_eventos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.FilterBean;
import com.bunizz.instapetts.fragments.profile.adapters.AdapterGridPostsProfile;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    ArrayList<FilterBean> list_filters;

    public ArrayList<FilterBean> getList_filters() {
        return list_filters;
    }

    public void setList_filters(ArrayList<FilterBean> list_filters) {
        this.list_filters = list_filters;
    }

    public FilterAdapter() {
    }

    public FilterAdapter(Context context) {
        this.context = context;
        list_filters = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter,parent,false);
        return new filterHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        filterHolder h = (filterHolder)holder;
        h.text_filter.setText(list_filters.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        return list_filters.size();
    }

    public class filterHolder extends  RecyclerView.ViewHolder{
       TextView text_filter;
        public filterHolder(@NonNull View itemView) {
            super(itemView);
            text_filter = itemView.findViewById(R.id.text_filter);
        }
    }
}
