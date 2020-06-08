package com.bunizz.instapetts.fragments.post.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.FeedAdapterHistories;
import com.bunizz.instapetts.fragments.wizardPets.adapters.TypePetsAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.ViewPagerAdapter;
import com.bunizz.instapetts.utils.double_tap.DoubleTapLikeView;
import com.bunizz.instapetts.utils.loadings.SpinKitView;
import com.bunizz.instapetts.utils.loadings.SpriteFactory;
import com.bunizz.instapetts.utils.loadings.Style;
import com.bunizz.instapetts.utils.loadings.sprite.Sprite;

import java.util.ArrayList;

import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    change_instance_wizard listener;
    ArrayList<String> current_images = new ArrayList<>();

    public change_instance_wizard getListener() {
        return listener;
    }

    public void setListener(change_instance_wizard listener) {
        this.listener = listener;
    }
    ArrayList<Object> data = new ArrayList<>();
    public ListAdapter(Context context) {
        this.context = context;
    }
    Style style = Style.values()[12];
    Sprite drawable = SpriteFactory.create(style);

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_post,parent,false);
        return  new ListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewtype = getItemViewType(position);
               ListHolder f = (ListHolder)holder;
                PostBean data_parsed = (PostBean) data.get(position);
                if(is_multiple(data_parsed.getUrls_posts())) {
                    f.progres_image.setVisibility(View.GONE);
                    f.root_multiple_image.setVisibility(View.VISIBLE);
                    f.single_image.setVisibility(View.GONE);
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context);
                    if (data_parsed.getUrls_posts() != null)
                        adapter.setUris_not_parsed(data_parsed.getUrls_posts());
                    f.list_fotos.setAdapter(adapter);
                    f.dots_indicator.setViewPager(f.list_fotos);

                }else{
                    f.progres_image.setIndeterminateDrawable(drawable);
                    f.progres_image.setColor(context.getResources().getColor(R.color.primary));
                    f.root_multiple_image.setVisibility(View.GONE);
                    f.single_image.setVisibility(View.VISIBLE);
                    Glide.with(context).load(data_parsed.getUrls_posts()).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            f.progres_image.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            f.progres_image.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(f.single_image);

                }
                f.icon_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f.layout_double_tap_like.setVisibility(View.VISIBLE);
                        f.layout_double_tap_like.animate_icon(f.layout_double_tap_like);
                        if(!data_parsed.isLiked()) {
                            f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                            //listener_post.onLike(data_parsed.getId_post_from_web());
                        }else{
                            f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                        }
                    }
                });
                f.root_preview_perfil_click.setOnClickListener(view ->{
                    Bundle b = new Bundle();
                    b.putString(BUNDLES.UUID,data_parsed.getUuid());
                    Log.e("ID_USUARIO_POST","-->" + data_parsed.getId_usuario());
                    b.putInt(BUNDLES.ID_USUARIO,data_parsed.getId_usuario());
                   // listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,b);
                });
                f.name_pet.setText(data_parsed.getName_pet());
                f.name_user_posts.setText(data_parsed.getName_user());
        if(data_parsed.getLikes()>0)
            f.num_likes_posts.setText("a " + data_parsed.getLikes() + " " +  context.getResources().getString(R.string.people_like_this));
        else
            f.num_likes_posts.setText(context.getResources().getString(R.string.first_like));
        if(data_parsed.getDescription().isEmpty()){
                    f.description_posts.setVisibility(View.GONE);
                }else{
                    f.description_posts.setVisibility(View.VISIBLE);
                    f.description_posts.setText(data_parsed.getDescription());
                }

                Glide.with(context).load(data_parsed.getUrl_photo_pet()).into(f.image_pet);
                Glide.with(context).load(data_parsed.getUrl_photo_user()).into(f.mini_user_photo);
                f.date_post.setText(App.getInstance().fecha_lenguaje_humano(data_parsed.getDate_post()));
                f.save_posts.setOnClickListener(view -> {
                    if(data_parsed.isSaved()) {
                        //listener_post.onDisfavorite(data_parsed.getId_post_from_web());
                        f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                    }
                    else {
                        f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_estrella_black));
                       // listener_post.onFavorite(data_parsed.getId_post_from_web(), data_parsed);
                    }
                });
                if(data_parsed.isSaved()){
                    f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_estrella_black));
                }else{
                    f.save_posts.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                }

                if(data_parsed.isLiked()){
                    f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon_black));
                }else{
                    f.icon_like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_corazon));
                }
                f.open_options_posts.setOnClickListener(view -> {
                  //  listener_post.openMenuOptions(data_parsed.getId_post_from_web(), data_parsed.getId_usuario(), data_parsed.getUuid());
                });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{
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
        ImageView save_posts;
        SpinKitView progres_image;
        ImagenCircular mini_user_photo;
        public ListHolder(@NonNull View itemView) {
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
            save_posts = itemView.findViewById(R.id.save_posts);
            progres_image = itemView.findViewById(R.id.progres_image);
            name_user_posts = itemView.findViewById(R.id.name_user_posts);
            num_likes_posts = itemView.findViewById(R.id.num_likes_posts);
            open_options_posts = itemView.findViewById(R.id.open_options_posts);
            mini_user_photo = itemView.findViewById(R.id.mini_user_photo);
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
