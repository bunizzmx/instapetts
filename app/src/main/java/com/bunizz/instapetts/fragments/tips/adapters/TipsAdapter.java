package com.bunizz.instapetts.fragments.tips.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity;
import com.bunizz.instapetts.activitys.PlayVideo.PlayVideoActivity2;
import com.bunizz.instapetts.beans.AspectBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.TipsBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.UnifiedAddHolder;
import com.bunizz.instapetts.listeners.PlayStopVideoListener;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;

public class TipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private static final int TYPE_TIP_TOP=1;
    private static final int TYPE_TIP_NORMAL = 2;
    private static final int TYPE_VIDEO = 3;
    private static final int TYPE_ADD = 4;
    private static final int TYPE_HELPS = 5;
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
        this.data.clear();
        this.data.addAll(data);
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
            }else if(recyclerViewItem instanceof PostBean){
                return TYPE_HELPS;
            }
            else{
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

            case TYPE_HELPS:
                view = getInflatedView(parent, R.layout.item_feed_post_help);
                return new FeedHolder(view);

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
                Glide.with(context).load(data_parsed.getPhoto_tumbh_tip())
               .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                        .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(h.top_image_tip);

                if(data_parsed.getTip_notice() == 0){
                    h.tip_notice_text.setText(context.getString(R.string.recomendacion_label));
                   h.tipo_tip_notice.setCardBackgroundColor(context.getResources().getColor(R.color.primary));
                }else{
                    h.tip_notice_text.setText(context.getString(R.string.notice));
                    h.tipo_tip_notice.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                }

                break;
            case TYPE_VIDEO:
                TipsHolderVideo hv =(TipsHolderVideo)holder;
                TipsBean data_parsed_video= (TipsBean) data.get(position);
                AspectBean aspectBean = new AspectBean();
                aspectBean = App.getInstance().getAspect(data_parsed_video.getAspect());
                hv.requestManager = requestManager_param;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(aspectBean.getWidth(), aspectBean.getHeight());
                hv.root_multiple_image.setLayoutParams(layoutParams);
                hv.title_tip.setText(data_parsed_video.getTitle_tip());
                Glide.with(context).load(data_parsed_video.getPhoto_tumbh_tip())
                .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                        .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(hv.mediaCoverImage);
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
                hv.tip_notice_text.setText(context.getString(R.string.video));
                hv.tipo_tip_notice.setCardBackgroundColor(context.getResources().getColor(R.color.azul_facebook));
                hv.num_views.setText("" + data_parsed_video.getViews_tip());
                hv.num_likes.setText(""+ data_parsed_video.getLikes_tip());
                break;
                case TYPE_HELPS:
                    PostBean data_parsed_help= (PostBean) data.get(position);
                    FeedHolder help = (FeedHolder)holder;
                    if(!data_parsed_help.getAddress().equals("INVALID")) {
                        help.addres_post.setVisibility(View.VISIBLE);
                        help.addres_post.setText(data_parsed_help.getAddress());
                    }
                    else
                        help.addres_post.setVisibility(View.GONE);

                    help.l_icon_commentar.setOnClickListener(v -> {
                        Bundle b = new Bundle();
                        b.putInt(BUNDLES.ID_POST,data_parsed_help.getId_post_from_web());
                        b.putBoolean(BUNDLES.CAN_COMMENT,true);
                        b.putInt(BUNDLES.TYPE_PET,-999);
                        b.putInt(BUNDLES.ID_USUARIO,data_parsed_help.getId_usuario());
                        listener.change_fragment_parameter(FragmentElement.INSTANCE_COMENTARIOS,b);
                    });

                    if(data_parsed_help.getNum_comentarios() > 0){
                        help.num_comments_layout.setVisibility(View.VISIBLE);
                        help.num_coments.setText(data_parsed_help.getNum_comentarios() + " " +context.getString(R.string.comentarios));
                        help.num_comments_layout.setOnClickListener(v -> {
                            Bundle b = new Bundle();
                            b.putInt(BUNDLES.ID_POST,data_parsed_help.getId_post_from_web());
                            b.putBoolean(BUNDLES.CAN_COMMENT,true);
                            b.putInt(BUNDLES.TYPE_PET,-999);
                            b.putInt(BUNDLES.ID_USUARIO,data_parsed_help.getId_usuario());
                            listener.change_fragment_parameter(FragmentElement.INSTANCE_COMENTARIOS,b);

                        });
                    }else{
                        help.num_comments_layout.setVisibility(View.GONE);
                    }

                    if(is_multiple(data_parsed_help.getUrls_posts())) {
                        help.card_number_indicator.setVisibility(View.VISIBLE);
                        String s[]= data_parsed_help.getUrls_posts().split(",");
                        int splits_number = s.length;
                        help.label_number_indicator.setText("1/"+splits_number);
                        help.progres_image.setVisibility(View.GONE);
                        help.root_multiple_image.setVisibility(View.VISIBLE);
                        help.single_image.setVisibility(View.GONE);
                        ViewPagerAdapter adapter = new ViewPagerAdapter(context);
                        if (data_parsed_help.getUrls_posts() != null)
                            adapter.setUris_not_parsed(data_parsed_help.getUrls_posts());

                        help.list_fotos.setAdapter(adapter);

                        help.list_fotos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                help.label_number_indicator.setText((position + 1) + "/"+splits_number);
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                        help.dots_indicator.setViewPager(help.list_fotos);

                    }else{
                       // help.progres_image.setIndeterminateDrawable(drawable);
                        help.progres_image.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        help.root_multiple_image.setVisibility(View.GONE);
                        help.single_image.setVisibility(View.VISIBLE);
                        Glide.with(context).load(data_parsed_help.getUrls_posts())
                                .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                                .error(context.getResources().getDrawable(R.drawable.ic_holder)).addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                help.progres_image.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                help.progres_image.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(help.single_image);

                    }
                    help.l_icon_like.setOnClickListener(view -> {
                        if(!data_parsed_help.isLiked()) {
                            help.layout_double_tap_like.setVisibility(View.VISIBLE);
                            help.layout_double_tap_like.animate_icon(help.layout_double_tap_like);
                        }
                        if(!data_parsed_help.isLiked()) {
                            data_parsed_help.setLiked(true);
                            help.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                            if(is_multiple(data_parsed_help.getUrls_posts())) {
                                String s[]= data_parsed_help.getUrls_posts().split(",");
                              //  listener_post.onLike(data_parsed_help.getId_post_from_web(), true, data_parsed_help.getId_usuario(),data_parsed_help.getThumb_video());
                            }else{
                               // listener_post.onLike(data_parsed_help.getId_post_from_web(), true, data_parsed_help.getId_usuario(), data_parsed_help.getThumb_video());
                            }
                        }else{
                            data_parsed_help.setLiked(false);
                            help.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                        }
                    });
                    /*help.root_preview_perfil_click.setOnClickListener(view ->{
                        Bundle b = new Bundle();
                        b.putString(BUNDLES.UUID,data_parsed_help.getUuid());
                        Log.e("ID_USUARIO_POST","-->" + data_parsed_help.getId_usuario());
                        b.putInt(BUNDLES.ID_USUARIO,data_parsed_help.getId_usuario());
                        listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,b);
                    });*/
                    help.name_pet.setText(data_parsed_help.getName_pet());
                    help.name_user_posts.setText(data_parsed_help.getName_user());
                    if(data_parsed_help.getLikes()>0)
                        help.num_likes_posts.setText( "" +data_parsed_help.getLikes() + " " + context.getResources().getString(R.string.likes));
                    else
                        help.num_likes_posts.setText(context.getResources().getString(R.string.first_like));
                    if(data_parsed_help.getDescription().isEmpty()){
                        help.description_posts.setVisibility(View.GONE);
                    }else{
                        help.description_posts.setVisibility(View.VISIBLE);
                        help.description_posts.setText(data_parsed_help.getDescription());
                    }

                    Glide.with(context).load(context.getResources().getDrawable(R.drawable.logo))
                            .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                            .error(context.getResources().getDrawable(R.drawable.ic_holder))
                            .into(help.image_pet);
                    Glide.with(context).load(data_parsed_help.getUrl_photo_user())
                            .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                            .error(context.getResources().getDrawable(R.drawable.ic_holder))
                            .into(help.mini_user_photo);
                    help.date_post.setText( "Hace " + App.getInstance().fecha_lenguaje_humano(data_parsed_help.getDate_post()));

                    if(data_parsed_help.isLiked()){
                        help.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                    }else{
                        help.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                    }
                  //  help.open_options_posts.setOnClickListener(view -> listener_post.openMenuOptions(data_parsed_help.getId_post_from_web(),data_parsed_help.getId_usuario(),data_parsed_help.getUuid()));
                    break;
            default:
                TipsHolder f = (TipsHolder)holder;
                TipsBean data_parsed_n = (TipsBean) data.get(position);
                Glide.with(context).load(data_parsed_n.getPhoto_tumbh_tip())
                        .placeholder(context.getResources().getDrawable(R.drawable.ic_holder))
                        .error(context.getResources().getDrawable(R.drawable.ic_holder)).into(f.image_tip_simple);
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

                if(data_parsed_n.getTip_notice() == 0){
                    f.tipo_tip_notice.setCardBackgroundColor(context.getResources().getColor(R.color.primary));
                    f.tip_notice_text.setText(context.getString(R.string.recomendacion_label));
                }else{
                    f.tip_notice_text.setText(context.getString(R.string.notice));
                    f.tipo_tip_notice.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                }

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

    public boolean is_multiple(String uris_not_parsed) {
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length > 1)
            return  true;
        else
            return false;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class TipsHolder extends RecyclerView.ViewHolder{
RelativeLayout root_tip_simple;
ImageView image_tip_simple;
TextView body_tip_short,fecha_tip,num_likes,num_views,tip_notice_text;
TextView title_tip_short;
CardView tipo_tip_notice;
        public TipsHolder(@NonNull View itemView) {
            super(itemView);
            root_tip_simple = itemView.findViewById(R.id.root_tip_simple);
            image_tip_simple = itemView.findViewById(R.id.image_tip_simple);
            body_tip_short = itemView.findViewById(R.id.body_tip_short);
            title_tip_short = itemView.findViewById(R.id.title_tip_short);
            fecha_tip = itemView.findViewById(R.id.fecha_tip);
            num_likes = itemView.findViewById(R.id.num_likes);
            num_views = itemView.findViewById(R.id.num_views);
            tipo_tip_notice = itemView.findViewById(R.id.tipo_tip_notice);
            tip_notice_text = itemView.findViewById(R.id.tip_notice_text);
        }
    }

    public class TipsTopHolder extends RecyclerView.ViewHolder{
     TextView boty_tip_top,title_tip_top,fecha_tip,num_likes,num_views,tip_notice_text;
        LinearLayout root_tip_simple;
        ImageView top_image_tip;
        CardView tipo_tip_notice;
        public TipsTopHolder(@NonNull View itemView) {
            super(itemView);
            root_tip_simple = itemView.findViewById(R.id.root_tip_simple);
            boty_tip_top = itemView.findViewById(R.id.boty_tip_top);
            title_tip_top = itemView.findViewById(R.id.title_tip_top);
            fecha_tip = itemView.findViewById(R.id.fecha_tip);
            num_likes = itemView.findViewById(R.id.num_likes);
            num_views = itemView.findViewById(R.id.num_views);
            top_image_tip = itemView.findViewById(R.id.top_image_tip);
            tipo_tip_notice = itemView.findViewById(R.id.tipo_tip_notice);
            tip_notice_text = itemView.findViewById(R.id.tip_notice_text);
        }
    }

    public class TipsHolderVideo extends RecyclerView.ViewHolder{
        public FrameLayout mediaContainer;
        public ImageView mediaCoverImage, volumeControl;
        public RequestManager requestManager;
        public CardView card_view_mute;
        public TextView title_tip;
        public TextView fecha_tip,tip_notice_text;
        RelativeLayout root_multiple_image;
        public ProgressBar progressBar;
        CardView tipo_tip_notice;
        TextView num_likes,num_views;
        public TipsHolderVideo(@NonNull View itemView) {
            super(itemView);
            tipo_tip_notice = itemView.findViewById(R.id.tipo_tip_notice);
            mediaContainer = itemView.findViewById(R.id.mediaContainer);
            mediaCoverImage = itemView.findViewById(R.id.ivMediaCoverImage);
            volumeControl = itemView.findViewById(R.id.ivVolumeControl);
            card_view_mute = itemView.findViewById(R.id.card_view_mute);
            title_tip = itemView.findViewById(R.id.title_tip);
            fecha_tip = itemView.findViewById(R.id.fecha_tip);
            root_multiple_image = itemView.findViewById(R.id.root_multiple_image);
            progressBar = itemView.findViewById(R.id.progressBar);
            tip_notice_text = itemView.findViewById(R.id.tip_notice_text);
            num_likes = itemView.findViewById(R.id.num_likes);
            num_views = itemView.findViewById(R.id.num_views);
        }
    }

    public class FeedHolder extends RecyclerView.ViewHolder{
        ViewPager list_fotos;
        DotsIndicator dots_indicator;
        RelativeLayout root_preview_perfil_click,open_options_posts;
        TextView description_posts;
        ImagenCircular image_pet;
        TextView name_pet,name_user_posts,num_likes_posts;
        RelativeLayout root_multiple_image;
        ImageView single_image,icon_like;
        TextView date_post;
        DoubleTapLikeView layout_double_tap_like;
        SpinKitView progres_image;
        ImagenCircular mini_user_photo;
        TextView addres_post;
        CardView card_number_indicator;
        TextView label_number_indicator;
        ImageView icon_commentar;
        TextView num_coments;
        LinearLayout num_comments_layout;
        CardView l_icon_like,l_icon_commentar;
        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            l_icon_commentar = itemView.findViewById(R.id.l_icon_commentar);
            l_icon_like = itemView.findViewById(R.id.l_icon_like);
            card_number_indicator = itemView.findViewById(R.id.card_number_indicator);
            label_number_indicator = itemView.findViewById(R.id.label_number_indicator);
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
            progres_image = itemView.findViewById(R.id.progres_image);
            name_user_posts = itemView.findViewById(R.id.name_user_posts);
            num_likes_posts = itemView.findViewById(R.id.num_likes_posts);
            open_options_posts = itemView.findViewById(R.id.open_options_posts);
            mini_user_photo = itemView.findViewById(R.id.mini_user_photo);
            addres_post = itemView.findViewById(R.id.addres_post);
            icon_commentar = itemView.findViewById(R.id.icon_commentar);
            num_coments = itemView.findViewById(R.id.num_coments);
            num_comments_layout = itemView.findViewById(R.id.num_comments_layout);
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
