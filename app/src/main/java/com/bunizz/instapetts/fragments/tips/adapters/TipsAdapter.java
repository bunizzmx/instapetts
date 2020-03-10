package com.bunizz.instapetts.fragments.tips.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.TipsBean;
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
        notifyDataSetChanged();
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
                TipsBean data_parsed = (TipsBean) data.get(position);
                h.boty_tip_top.setText(data_parsed.getBody_tip().substring(0,250) + "...");
                h.title_tip_top.setText(data_parsed.getTitle_tip());
                break;
            default:
                TipsHolder f = (TipsHolder)holder;
                TipsBean data_parsed_n = (TipsBean) data.get(position);
                Glide.with(context).load(data_parsed_n.getPhoto_tumbh_tip()).into(f.image_tip_simple);
                f.root_tip_simple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener!=null){
                            Bundle b = new Bundle();
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_TIP_DETAIL,b);
                        }
                    }
                });
                if(data_parsed_n.getBody_tip().length() > 200)
                    f.body_tip_short.setText(data_parsed_n.getBody_tip().substring(0,150) + "...");
                else
                    f.body_tip_short.setText(data_parsed_n.getBody_tip());
                f.title_tip_short.setText(data_parsed_n.getTitle_tip());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class TipsHolder extends RecyclerView.ViewHolder{
RelativeLayout root_tip_simple;
ImageView image_tip_simple;
TextView body_tip_short;
TextView title_tip_short;
        public TipsHolder(@NonNull View itemView) {
            super(itemView);
            root_tip_simple = itemView.findViewById(R.id.root_tip_simple);
            image_tip_simple = itemView.findViewById(R.id.image_tip_simple);
            body_tip_short = itemView.findViewById(R.id.body_tip_short);
            title_tip_short = itemView.findViewById(R.id.title_tip_short);
        }
    }

    public class TipsTopHolder extends RecyclerView.ViewHolder{
     TextView boty_tip_top,title_tip_top;
        public TipsTopHolder(@NonNull View itemView) {
            super(itemView);
            boty_tip_top = itemView.findViewById(R.id.boty_tip_top);
            title_tip_top = itemView.findViewById(R.id.title_tip_top);
        }
    }
}
