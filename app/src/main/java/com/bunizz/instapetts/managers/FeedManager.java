package com.bunizz.instapetts.managers;

import android.content.Context;
import android.widget.Toast;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.consume_services.WebServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bunizz.instapetts.consume_services.WebServices.BASE_URL;

public class FeedManager extends  Manager{
    private static  FeedManager feedManager;
    private Context context;
    Retrofit retrofit;

    public FeedManager(Context context) {
        this.context = context;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static FeedManager getInstance(Context context) {
        if (feedManager == null) {
            feedManager = new FeedManager(context);
        }
        return (FeedManager) feedManager.getManagerInstance();
    }


    @Override
    public Manager getManagerInstance() {
        if (getCustomManager() == null) {
            setCustomManager(feedManager);
        }
        return getCustomManager();
    }


    private void geFeed(){

    }



}
