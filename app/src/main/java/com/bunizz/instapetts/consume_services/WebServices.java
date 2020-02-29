package com.bunizz.instapetts.consume_services;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.managers.FeedResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebServices {


    String BASE_URL = "https://api.cryptonator.com/api/full/";

    @GET("{coin}-usd")
    Call<FeedResponse> getCoinData(@Path("coin") String coin);
}
