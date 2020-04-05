package com.bunizz.instapetts.web;

import android.provider.ContactsContract;

import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.web.parameters.CatalogoParameters;
import com.bunizz.instapetts.web.parameters.FollowParameter;
import com.bunizz.instapetts.web.parameters.ParameterSearching;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.parameters.PostLikeBean;
import com.bunizz.instapetts.web.responses.PetsResponse;
import com.bunizz.instapetts.web.responses.ResponseCatalogo;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseProfileUser;
import com.bunizz.instapetts.web.responses.ResponseTips;
import com.bunizz.instapetts.web.responses.SearchPetsResponse;
import com.bunizz.instapetts.web.responses.SearchUsersResponse;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;

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
    Single<SimpleResponseLogin> newUser(@Body UserBean user);

    @POST("test/newusers")
    Single<SimpleResponse> update_token(@Body UserBean user);

    @POST("test/getinfouser")
    Single<ResponseProfileUser> getInfoUser(@Body UserBean user);


    //////////////////////////////////////////////////////////////////

    // POSTS //////////////////////////////////////////////////////////
    @POST("test/newposts")
    Single<SimpleResponse> sendPost( @Body PostBean post );

    @POST("test/newposts")
    Single<SimpleResponse> delete_post( @Body PostBean post );

    @POST("test/newposts")
    Single<SimpleResponse> like_posts( @Body PostLikeBean postLikeBean );

    @POST("test/getposts")
    Single<ResponsePost> getPosts(@Body PostFriendsBean postFriendsBean);
    ////////////////////////////////////////////////////////////////////


    // STORIES //////////////////////////////////////////////////////////
    @POST("test/newstories")
    Single<SimpleResponse> newStory( @Body PostBean post );
    ////////////////////////////////////////////////////////////////////

    @POST("test/gettips")
    Single<ResponseTips> getTips(@Body AutenticateBean autenticateBean);

    @POST("test/catalogo")
    Single<ResponseCatalogo> getCatalogos(@Body CatalogoParameters catalogoParameters);

    // PETS //////////////////////////////////////////////////////////
    @POST("test/newpets")
    Single<SimpleResponse> newPet( @Body PetBean pet );

    @POST("test/getpets")
    Single<PetsResponse> getPets(@Body UserBean userBean);
    ////////////////////////////////////////////////////////////////////


    // PETS //////////////////////////////////////////////////////////
    @POST("test/serachusers")
    Single<SearchUsersResponse> searchUser(@Body ParameterSearching parameterSearching );
    @POST("test/searchpets")
    Single<SearchPetsResponse> searchPets(@Body ParameterSearching parameterSearching );
    ////////////////////////////////////////////////////////////////////


    @POST("test/newstories")
    Single<SimpleResponse> newstory(@Body HistoriesBean historiesBean );


    @POST("test/follows")
    Single<SimpleResponse> follows(@Body FollowParameter followParameter );


}
