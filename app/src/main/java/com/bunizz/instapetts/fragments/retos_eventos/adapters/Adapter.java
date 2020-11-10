package com.bunizz.instapetts.fragments.retos_eventos.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.EventBean;

import androidx.cardview.widget.CardView;

/**
 * adapter Created by farshid roohi on 12/12/17.
 */

public class Adapter extends BaseCardViewPagerItem<EventBean> {

    Context context;

    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getLayout() {
        return R.layout.item;
    }

    @Override
    public void bindView(View view, EventBean item) {
        ViewGroup layoutRoot = view.findViewById(R.id.layout_root);
        TextView txtTitle = view.findViewById(R.id.txt_title);
        CardView card_root = view.findViewById(R.id.card_root);
        TextView description_reto = view.findViewById(R.id.description_reto);
        ImageView image_evento = view.findViewById(R.id.image_evento);
        txtTitle.setText(item.getTitle());
        description_reto.setText(item.getDescription());
        Glide.with(context).load(item.getUrl_resource()).into(image_evento);
    }
}
