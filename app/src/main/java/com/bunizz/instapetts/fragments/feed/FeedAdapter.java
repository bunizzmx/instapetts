package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> current_images = new ArrayList<>();
    ArrayList<Object> data = new ArrayList<>();
    changue_fragment_parameters_listener listener;

    public changue_fragment_parameters_listener getListener() {
        return listener;
    }

    public void setListener(changue_fragment_parameters_listener listener) {
        this.listener = listener;
    }

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
        notifyDataSetChanged();
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
                ViewPagerAdapter adapter = new ViewPagerAdapter(context);
                PostBean data_parsed = (PostBean) data.get(position);
                if(data_parsed.getUrls_posts()!=null)
                adapter.setUris_not_parsed(data_parsed.getUrls_posts());
                f.list_fotos.setAdapter(adapter);
                f.dots_indicator.setViewPager(f.list_fotos);
                f.root_preview_perfil_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,null);
                    }
                });
                f.name_pet.setText(data_parsed.getName_user());
                f.description_posts.setText(data_parsed.getDescription());
                Glide.with(context).load(data_parsed.getUrl_photo_user()).into(f.image_pet);
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
        RelativeLayout root_preview_perfil_click;
        TextView description_posts;
        ImagenCircular image_pet;
        TextView name_pet;
        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            root_preview_perfil_click = itemView.findViewById(R.id.root_preview_perfil_click);
            dots_indicator = itemView.findViewById(R.id.dots_indicator);
            list_fotos = itemView.findViewById(R.id.list_fotos);
            image_pet = itemView.findViewById(R.id.image_pet);
            description_posts = itemView.findViewById(R.id.description_posts);
            name_pet = itemView.findViewById(R.id.name_pet);

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
