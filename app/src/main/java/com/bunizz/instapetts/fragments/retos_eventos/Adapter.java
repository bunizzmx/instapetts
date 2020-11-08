package com.bunizz.instapetts.fragments.retos_eventos;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.beans.EventBean;

import androidx.cardview.widget.CardView;

/**
 * adapter Created by farshid roohi on 12/12/17.
 */

public class Adapter extends BaseCardViewPagerItem<EventBean> {

    @Override
    public int getLayout() {
        return R.layout.item;
    }

    @Override
    public void bindView(View view, EventBean item) {
        ViewGroup layoutRoot = view.findViewById(R.id.layout_root);
        TextView txtTitle = view.findViewById(R.id.txt_title);
        CardView card_root = view.findViewById(R.id.card_root);
        txtTitle.setText(item.getTitle());
    }
}
