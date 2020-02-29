package com.bunizz.instapetts.fragments.tips.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;

import java.util.ArrayList;

public class TipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private static final int TYPE_TIP_TOP=1;
    private static final int TYPE_TIP_NORMAL = 2;
    ArrayList<Object> data = new ArrayList<>();
    changue_fragment_parameters_listener listener;

    public changue_fragment_parameters_listener getListener() {
        return listener;
    }

    public void setListener(changue_fragment_parameters_listener listener) {
        this.listener = listener;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public TipsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
       // Object recyclerViewItem = data.get(position);
        if (position ==0) {
            return TYPE_TIP_TOP;
        }
        return TYPE_TIP_NORMAL;

    }


    private View getInflatedView(ViewGroup parent, int resourceLayout){
        return LayoutInflater
                .from(parent.getContext())
                .inflate(resourceLayout, parent, false);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_TIP_TOP:
                view = getInflatedView(parent, R.layout.item_tips_top);
                return new TipsTopHolder(view);

            case TYPE_TIP_NORMAL:
            default:
                view = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_tips_simple,
                        parent, false);
                return new TipsHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewtype = getItemViewType(position);
        switch (viewtype) {
            case TYPE_TIP_TOP:
                TipsTopHolder h = (TipsTopHolder)holder;
                break;
            default:
                TipsHolder f = (TipsHolder)holder;
                f.root_tip_simple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener!=null){
                            Bundle b = new Bundle();
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_TIP_DETAIL,b);
                        }
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class TipsHolder extends RecyclerView.ViewHolder{
RelativeLayout root_tip_simple;
        public TipsHolder(@NonNull View itemView) {
            super(itemView);
            root_tip_simple = itemView.findViewById(R.id.root_tip_simple);
        }
    }

    public class TipsTopHolder extends RecyclerView.ViewHolder{

        public TipsTopHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
