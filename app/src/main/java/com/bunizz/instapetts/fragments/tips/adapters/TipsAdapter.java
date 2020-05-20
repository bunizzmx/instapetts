package com.bunizz.instapetts.fragments.tips.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.beans.AspectBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.UnifiedAddHolder;
import com.bunizz.instapetts.listeners.PlayStopVideoListener;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class TipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private static final int TYPE_TIP_TOP=1;
    private static final int TYPE_TIP_NORMAL = 2;
    private static final int TYPE_VIDEO = 3;
    private static final int TYPE_ADD = 4;
    private RequestManager requestManager_param;
    ArrayList<Object> data = new ArrayList<>();
    changue_fragment_parameters_listener listener;
    PlayStopVideoListener listener_video;

    public PlayStopVideoListener getListener_video() {
        return listener_video;
    }

    public void setListener_video(PlayStopVideoListener listener_video) {
        this.listener_video = listener_video;
    }

    public changue_fragment_parameters_listener getListener() {
        return listener;
    }

    public void setListener(changue_fragment_parameters_listener listener) {
        this.listener = listener;
    }

    public RequestManager getRequestManager() {
        return requestManager_param;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager_param = requestManager;
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
       Object recyclerViewItem = data.get(position);
        if (position ==0) {
            return TYPE_TIP_TOP;
        }else{
            if(recyclerViewItem instanceof UnifiedNativeAd){
                return  TYPE_ADD;
            }else{
                if(recyclerViewItem instanceof TipsBean){
                    TipsBean data_parsed = (TipsBean) recyclerViewItem;
                    if(data_parsed.getType_tip() == 0)
                        return TYPE_TIP_NORMAL;
                    else
                        return TYPE_VIDEO;
                }else
                    return TYPE_TIP_NORMAL;

            }
        }
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
            case TYPE_VIDEO:
                view = getInflatedView(parent, R.layout.item_tips_video);
                return new TipsHolderVideo(view);

            case TYPE_ADD:
                view = getInflatedView(parent, R.layout.ad_unified);
                return new UnifiedAddHolder(view);

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
                h.fecha_tip.setText(App.getInstance().fecha_lenguaje_humano(data_parsed.getFecha_tip().replace("Z","").replace("T"," ")));
                h.num_views.setText("" + data_parsed.getViews_tip());
                h.num_likes.setText(""+ data_parsed.getLikes_tip());
                h.root_tip_simple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener!=null){
                            Bundle b = new Bundle();
                            b.putString("BODY_TIP",data_parsed.getBody_tip());
                            b.putString("TITLE_TIP",data_parsed.getTitle_tip());
                            b.putString("PHOTO_TIP",data_parsed.getPhoto_tip());
                            b.putInt("ID_TIP",data_parsed.getId());
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_TIP_DETAIL,b);
                        }
                    }
                });
                Glide.with(context).load(data_parsed.getPhoto_tumbh_tip()).into(h.top_image_tip);

                break;
            case TYPE_VIDEO:
                TipsHolderVideo hv =(TipsHolderVideo)holder;
                TipsBean data_parsed_video= (TipsBean) data.get(position);
                AspectBean aspectBean = new AspectBean();
                aspectBean = App.getInstance().getAspect("4_5");
                hv.requestManager = requestManager_param;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(aspectBean.getWidth(), aspectBean.getHeight());
                hv.root_multiple_image.setLayoutParams(layoutParams);
                hv.title_tip.setText(data_parsed_video.getTitle_tip());
                Glide.with(context).load(data_parsed_video.getPhoto_tumbh_tip()).into(hv.mediaCoverImage);
                hv.fecha_tip.setText(App.getInstance().fecha_lenguaje_humano(data_parsed_video.getFecha_tip().replace("Z","").replace("T"," ")));
                hv.root_multiple_image.setOnLongClickListener(v -> {
                    listener_video.StopVideo();
                    Intent i = new Intent(context, PlayVideoActivity.class);
                    i.putExtra("TYPE_PLAYER",0);
                    i.putExtra("BEAN", Parcels.wrap(data_parsed_video));
                    context.startActivity(i);
                    return true;
                });
                hv.root_multiple_image.setOnClickListener(v -> {
                    listener_video.MuteVideo();
                });
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
                            b.putString("BODY_TIP",data_parsed_n.getBody_tip());
                            b.putString("TITLE_TIP",data_parsed_n.getTitle_tip());
                            b.putString("PHOTO_TIP",data_parsed_n.getPhoto_tip());
                            b.putInt("ID_TIP",data_parsed_n.getId());
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_TIP_DETAIL,b);
                        }
                    }
                });
                f.fecha_tip.setText(App.getInstance().fecha_lenguaje_humano(data_parsed_n.getFecha_tip().replace("Z","").replace("T"," ")));
                f.num_views.setText("" + data_parsed_n.getViews_tip());
                f.num_likes.setText(""+ data_parsed_n.getLikes_tip());
                if(data_parsed_n.getBody_tip().length() > 200)
                    f.body_tip_short.setText(data_parsed_n.getBody_tip().substring(0,150) + "...");
                else
                    f.body_tip_short.setText(data_parsed_n.getBody_tip());
                f.title_tip_short.setText(data_parsed_n.getTitle_tip());
                break;
            case TYPE_ADD:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) data.get(position);
                populateNativeAdView(nativeAd, ((UnifiedAddHolder) holder).getAdView());
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class TipsHolder extends RecyclerView.ViewHolder{
RelativeLayout root_tip_simple;
ImageView image_tip_simple;
TextView body_tip_short,fecha_tip,num_likes,num_views;
TextView title_tip_short;
        public TipsHolder(@NonNull View itemView) {
            super(itemView);
            root_tip_simple = itemView.findViewById(R.id.root_tip_simple);
            image_tip_simple = itemView.findViewById(R.id.image_tip_simple);
            body_tip_short = itemView.findViewById(R.id.body_tip_short);
            title_tip_short = itemView.findViewById(R.id.title_tip_short);
            fecha_tip = itemView.findViewById(R.id.fecha_tip);
            num_likes = itemView.findViewById(R.id.num_likes);
            num_views = itemView.findViewById(R.id.num_views);
        }
    }

    public class TipsTopHolder extends RecyclerView.ViewHolder{
     TextView boty_tip_top,title_tip_top,fecha_tip,num_likes,num_views;
        LinearLayout root_tip_simple;
        ImageView top_image_tip;
        public TipsTopHolder(@NonNull View itemView) {
            super(itemView);
            root_tip_simple = itemView.findViewById(R.id.root_tip_simple);
            boty_tip_top = itemView.findViewById(R.id.boty_tip_top);
            title_tip_top = itemView.findViewById(R.id.title_tip_top);
            fecha_tip = itemView.findViewById(R.id.fecha_tip);
            num_likes = itemView.findViewById(R.id.num_likes);
            num_views = itemView.findViewById(R.id.num_views);
            top_image_tip = itemView.findViewById(R.id.top_image_tip);
        }
    }

    public class TipsHolderVideo extends RecyclerView.ViewHolder{
        public FrameLayout mediaContainer;
        public ImageView mediaCoverImage, volumeControl;
        public RequestManager requestManager;
        public CardView card_view_mute;
        public TextView title_tip;
        public TextView fecha_tip;
        RelativeLayout root_multiple_image;
        public ProgressBar progressBar;
        public TipsHolderVideo(@NonNull View itemView) {
            super(itemView);
            mediaContainer = itemView.findViewById(R.id.mediaContainer);
            mediaCoverImage = itemView.findViewById(R.id.ivMediaCoverImage);
            volumeControl = itemView.findViewById(R.id.ivVolumeControl);
            card_view_mute = itemView.findViewById(R.id.card_view_mute);
            title_tip = itemView.findViewById(R.id.title_tip);
            fecha_tip = itemView.findViewById(R.id.fecha_tip);
            root_multiple_image = itemView.findViewById(R.id.root_multiple_image);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }



    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        ((Button) adView.getCallToActionView()).setBackground(context.getResources().getDrawable(R.drawable.button_create_acount));
        ((Button) adView.getCallToActionView()).setTextColor(Color.WHITE);
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}
