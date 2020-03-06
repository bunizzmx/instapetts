package com.bunizz.instapetts.fragments.share_post.Share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SharePostPresenter implements SharePostContract.Presenter {
    private SharePostContract.View mView;
    private Context mContext;
    WebServices apiService;

    SharePostPresenter(SharePostContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
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

    @SuppressLint("CheckResult")
    void get_algo(){
        apiService.fetchAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<PostBean>>() {
                    @Override
                    public void onSuccess(List<PostBean> notes) {
                        // Received all notes
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                    }
                });
    }
}