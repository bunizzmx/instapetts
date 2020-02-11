package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> current_images = new ArrayList<>();
    ArrayList<Object> data = new ArrayList<>();

    private static final int TYPE_POST=1;
    private static final int TYPE_HISTORI = 2;

    public FeedAdapter(Context context,ArrayList<Object> data) {
        this.context = context;
        this.data.addAll(data);
    }


    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public void addData(ArrayList<Object> data){
        this.data.addAll(data);

    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = data.get(position);
        if (recyclerViewItem instanceof HistoriesBean) {
            return TYPE_HISTORI;
        }
        return TYPE_POST;

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
            case TYPE_POST:
                view = getInflatedView(parent, R.layout.item_feed_post);
                return new FeedHolder(view);

            case TYPE_HISTORI:
            default:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_histories_feed,
                        parent, false);
                return new HistoriesHolder(unifiedNativeLayoutView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewtype = getItemViewType(position);
        switch (viewtype) {
            case TYPE_HISTORI:
                HistoriesHolder h = (HistoriesHolder)holder;
                FeedAdapterHistories adapterHistories = new FeedAdapterHistories(context);
                h.list_histories.setAdapter(adapterHistories);
                h.list_histories.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                break;
            default:
                FeedHolder f = (FeedHolder)holder;
                //ImageModel  data_parsed =(ImageModel) list.get(position);
                ViewPagerAdapter adapter = new ViewPagerAdapter(context,current_images);
                f.list_fotos.setAdapter(adapter);
                f.dots_indicator.setViewPager(f.list_fotos);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FeedHolder extends RecyclerView.ViewHolder{
  ViewPager list_fotos;
  DotsIndicator dots_indicator;
        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            dots_indicator = itemView.findViewById(R.id.dots_indicator);
            list_fotos = itemView.findViewById(R.id.list_fotos);
        }
    }


    public class HistoriesHolder extends RecyclerView.ViewHolder{
        RecyclerView list_histories;
        public HistoriesHolder(@NonNull View itemView) {
            super(itemView);
            list_histories = itemView.findViewById(R.id.list_histories);
        }
    }
}
