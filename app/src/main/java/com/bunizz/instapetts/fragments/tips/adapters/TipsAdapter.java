package com.bunizz.instapetts.fragments.tips.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;

import java.util.ArrayList;

public class TipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private static final int TYPE_TIP_TOP=1;
    private static final int TYPE_TIP_NORMAL = 2;
    ArrayList<Object> data = new ArrayList<>();

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
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_tips_simple,
                        parent, false);
                return new TipsHolder(unifiedNativeLayoutView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class TipsHolder extends RecyclerView.ViewHolder{

        public TipsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class TipsTopHolder extends RecyclerView.ViewHolder{

        public TipsTopHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
