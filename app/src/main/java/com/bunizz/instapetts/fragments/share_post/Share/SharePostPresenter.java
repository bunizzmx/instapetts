package com.bunizz.instapetts.fragments.share_post.Share;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.LocationServices;

public class SharePostPresenter implements SharePostContract.Presenter {
    private SharePostContract.View mView;
    private Context mContext;


    SharePostPresenter(SharePostContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
    }


    @Override
    public void getLocation() {
        LocationServices.getFusedLocationProviderClient(mContext).getLastLocation().addOnSuccessListener(location -> {
            if(location!=null){
                Log.e("LOCALIZACION","-->" + location.getLatitude()  + "/" + location.getLongitude());
                mView.showLocation(location.getLatitude()  + "/" + location.getLongitude());
            }
        });
    }
}