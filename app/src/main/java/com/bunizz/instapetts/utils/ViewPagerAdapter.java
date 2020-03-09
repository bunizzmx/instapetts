package com.bunizz.instapetts.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> image_urls = new ArrayList<>();
    String uris_not_parsed ="";

    public String getUris_not_parsed() {
        return uris_not_parsed;
    }

    public void setUris_not_parsed(String uris_not_parsed) {
        this.uris_not_parsed = uris_not_parsed;
        image_urls.clear();
        String parsed[]  = uris_not_parsed.split(",");
        if(parsed.length == 1 && uris_not_parsed.length() > 0 ){
            Log.e("DATA_PARSED","solo es una");
            image_urls.add(uris_not_parsed);
        }else{
            Log.e("DATA_PARSED","son varias");
            for (int i =0;i< parsed.length;i++){
                image_urls.add(parsed[i]);
            }
        }
        notifyDataSetChanged();

    }

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<String> getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(ArrayList<String> image_urls) {
        this.image_urls = image_urls;
    }

    @Override
    public int getCount() {
        return image_urls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(image_urls.get(position)).into(imageView);
        container.addView(imageView);
        return  imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View)object);
    }
}
