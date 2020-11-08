package com.bunizz.instapetts.fragments.retos_eventos;

import androidx.cardview.widget.CardView;

/**
 * Card ViewPager Created by farshid roohi on 12/12/17.
 */

public interface CardElevationMax {

    int MAX_ELEVATION_FACTOR = 20;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
