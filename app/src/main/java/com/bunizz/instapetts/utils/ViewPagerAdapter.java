package com.bunizz.instapetts.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> image_urls;

    public ViewPagerAdapter(Context context, ArrayList<String> image_urls) {
        this.context = context;
        this.image_urls = image_urls;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C18%2F15cb3831f0b9426c484d380f0ab1afac.jpg?alt=media&token=042d5974-e96c-4bcc-9fa2-f657fe2167af").centerCrop().into(imageView);
        container.addView(imageView);
        return  imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View)object);
    }
}
