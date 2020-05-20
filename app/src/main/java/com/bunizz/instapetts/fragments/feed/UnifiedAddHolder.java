package com.bunizz.instapetts.fragments.feed;

import android.view.View;

import com.bunizz.instapetts.R;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import androidx.recyclerview.widget.RecyclerView;

public class UnifiedAddHolder extends RecyclerView.ViewHolder {
    private UnifiedNativeAdView adView;

    public UnifiedNativeAdView getAdView() {
        return adView;
    }

    public UnifiedAddHolder(View view) {
        super(view);
        adView =  view.findViewById(R.id.ad_view);
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
    }
}
