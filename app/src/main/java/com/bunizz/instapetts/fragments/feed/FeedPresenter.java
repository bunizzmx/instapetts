package com.bunizz.instapetts.fragments.feed;

import android.content.Context;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.consume_services.ApiClient;
import com.bunizz.instapetts.consume_services.WebServices;
import com.bunizz.instapetts.managers.FeedResponse;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedPresenter implements FeedContract.Presenter {
    private FeedContract.View mView;
    private Context mContext;


    FeedPresenter(FeedContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
    }

    @Override
    public void get_feed() {
      WebServices apiservice = ApiClient.getClient().create(WebServices.class);
        Call<FeedResponse> call = apiservice.getCoinData("xxxx");
        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                mView.show_feed(response.body().getData_response());

            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {

            }
        });
    }
}
