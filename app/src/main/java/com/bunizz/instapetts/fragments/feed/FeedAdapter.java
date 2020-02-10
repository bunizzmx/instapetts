package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.utils.Dots.DotsIndicator;
import com.bunizz.instapetts.utils.ViewPagerAdapter;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> current_images = new ArrayList<>();
    public FeedAdapter(Context context) {
        this.context = context;
        current_images.add("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C1%2FFrase1.jpg?alt=media&token=00df296a-5237-4bc7-a331-4d973c826d57");
        current_images.add("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C1%2FFrase1.jpg?alt=media&token=00df296a-5237-4bc7-a331-4d973c826d57");
        current_images.add("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C1%2FFrase1.jpg?alt=media&token=00df296a-5237-4bc7-a331-4d973c826d57");
        current_images.add("https://firebasestorage.googleapis.com/v0/b/melove-principal/o/C1%2FFrase1.jpg?alt=media&token=00df296a-5237-4bc7-a331-4d973c826d57");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_post,parent,false);
      return new FeedHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FeedHolder h = (FeedHolder)holder;
        ViewPagerAdapter adapter = new ViewPagerAdapter(context,current_images);
        h.list_fotos.setAdapter(adapter);
        h.dots_indicator.setViewPager(h.list_fotos);
    }

    @Override
    public int getItemCount() {
        return 10;
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
}
