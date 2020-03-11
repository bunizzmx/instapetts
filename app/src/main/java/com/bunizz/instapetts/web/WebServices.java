package com.bunizz.instapetts.web;

import android.provider.ContactsContract;

import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.web.responses.ResponseCatalogo;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseTips;
import com.bunizz.instapetts.web.responses.SimpleResponse;

import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServices {

    //USERS //////////////////////////////////////////////////////////
    @POST("test/newusers")
    Single<SimpleResponse> newUser(@Body UserBean user);

    @POST("test/getusers")
    Single<SimpleResponse> getUser(@Body UserBean user);
    //////////////////////////////////////////////////////////////////

    // POSTS //////////////////////////////////////////////////////////
    @POST("test/newposts")
    Single<SimpleResponse> sendPost( @Body PostBean post );

    @POST("test/getposts")
    Single<ResponsePost> getPosts(@Body AutenticateBean autenticateBean);
    ////////////////////////////////////////////////////////////////////


    // STORIES //////////////////////////////////////////////////////////
    @POST("test/newstories")
    Single<SimpleResponse> newStory( @Body PostBean post );
    ////////////////////////////////////////////////////////////////////

    @POST("test/gettips")
    Single<ResponseTips> getTips(@Body AutenticateBean autenticateBean);

    @POST("test/catalogo")
    Single<ResponseCatalogo> getCatalogos(@Body int idPet );
}
