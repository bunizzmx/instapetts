package com.bunizz.instapetts.fragments.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;
import static com.bunizz.instapetts.utils.trimVideo.utils.UIThreadUtil.runOnUiThread;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> current_images = new ArrayList<>();
    ArrayList<Object> data = new ArrayList<>();
    changue_fragment_parameters_listener listener;
    long timeWhenDown =0;
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


    @SuppressLint("ClickableViewAccessibility")
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
                PostBean data_parsed = (PostBean) data.get(position);
                if(is_multiple(data_parsed.getUrls_posts())) {
                    f.root_multiple_image.setVisibility(View.VISIBLE);
                    f.single_image.setVisibility(View.GONE);
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context);
                    if (data_parsed.getUrls_posts() != null)
                        adapter.setUris_not_parsed(data_parsed.getUrls_posts());
                    f.list_fotos.setAdapter(adapter);
                    f.dots_indicator.setViewPager(f.list_fotos);

                }else{
                    f.root_multiple_image.setVisibility(View.GONE);
                    f.single_image.setVisibility(View.VISIBLE);
                    Glide.with(context).load(data_parsed.getUrls_posts()).into(f.single_image);
                }
                f.icon_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("LIKE_ROO","ccccccccccccc");
                        f.layout_double_tap_like.setVisibility(View.VISIBLE);
                        f.layout_double_tap_like.animate_icon(f.layout_double_tap_like);
                    }
                });
                f.root_preview_perfil_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,null);
                    }
                });
                f.name_pet.setText(data_parsed.getName_user());
                f.description_posts.setText(data_parsed.getDescription());
                Glide.with(context).load(data_parsed.getUrl_photo_user()).into(f.image_pet);

                f.date_post.setText(App.fecha_lenguaje_humano(data_parsed.getDate_post()));


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
        RelativeLayout root_multiple_image;
        ImageView single_image,icon_like;
        TextView date_post;
        DoubleTapLikeView layout_double_tap_like;
        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            root_preview_perfil_click = itemView.findViewById(R.id.root_preview_perfil_click);
            dots_indicator = itemView.findViewById(R.id.dots_indicator);
            list_fotos = itemView.findViewById(R.id.list_fotos);
            image_pet = itemView.findViewById(R.id.image_pet);
            description_posts = itemView.findViewById(R.id.description_posts);
            name_pet = itemView.findViewById(R.id.name_pet);
            root_multiple_image = itemView.findViewById(R.id.root_multiple_image);
            single_image = itemView.findViewById(R.id.single_image);
            date_post = itemView.findViewById(R.id.date_post);
            layout_double_tap_like = itemView.findViewById(R.id.layout_double_tap_like);
            icon_like = itemView.findViewById(R.id.icon_like);
        }
    }


    public class HistoriesHolder extends RecyclerView.ViewHolder{
        RecyclerView list_histories;
        public HistoriesHolder(@NonNull View itemView) {
            super(itemView);
            list_histories = itemView.findViewById(R.id.list_histories);
        }
    }

    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }
}
