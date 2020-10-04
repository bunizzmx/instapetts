package com.bunizz.instapetts.web;

import android.provider.ContactsContract;

import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PlayVideos;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.ReportBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.web.parameters.CatalogoParameters;
import com.bunizz.instapetts.web.parameters.EventsPetsBean;
import com.bunizz.instapetts.web.parameters.EventsTipsBean;
import com.bunizz.instapetts.web.parameters.FollowParameter;
import com.bunizz.instapetts.web.parameters.IdentificadorHistoryParameter;
import com.bunizz.instapetts.web.parameters.ParameterAvailableNames;
import com.bunizz.instapetts.web.parameters.ParameterSearching;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.parameters.PostLikeBean;
import com.bunizz.instapetts.web.responses.HistoriesBeanResponse;
import com.bunizz.instapetts.web.responses.IdentificadoresHistoriesResponse;
import com.bunizz.instapetts.web.responses.PetsResponse;
import com.bunizz.instapetts.web.responses.ResponseCatalogo;
import com.bunizz.instapetts.web.responses.ResponseCodesCountries;
import com.bunizz.instapetts.web.responses.ResponseListReports;
import com.bunizz.instapetts.web.responses.ResponseNamesAvailables;
import com.bunizz.instapetts.web.responses.ResponsePlayVideos;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponsePostRecomended;
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

    @POST("test/newusers")
    Single<SimpleResponse> updateConexion(@Body UserBean user);

    @POST("test/getinfouser")
    Single<ResponseProfileUser> getInfoUser(@Body UserBean user);


    //////////////////////////////////////////////////////////////////

    // POSTS //////////////////////////////////////////////////////////
    @POST("test/newposts")
    Single<SimpleResponse> sendPost( @Body PostBean post );

    @POST("test/newposts")
    Single<SimpleResponse> sendPostInstapettsttv( @Body PlayVideos post );

    @POST("test/newposts")
    Single<SimpleResponse> delete_post( @Body PostBean post );

    @POST("test/newposts")
    Single<SimpleResponse> like_posts( @Body PostLikeBean postLikeBean );

    @POST("test/getposts")
    Single<ResponsePost> getPosts(@Body PostFriendsBean postFriendsBean);


    @POST("test/getposts")
    Single<ResponsePostRecomended> getPostsRecomended(@Body PostFriendsBean postFriendsBean);
    ////////////////////////////////////////////////////////////////////

    @POST("test/getplayvideos")
    Single<ResponsePlayVideos> getPlayVideos(@Body AutenticateBean postFriendsBean);


    // STORIES //////////////////////////////////////////////////////////
    @POST("test/newstories")
    Single<SimpleResponse> newStory( @Body HistoriesBean historiesBean );
    @POST("test/newstories")
    Single<HistoriesBeanResponse> getMyStories(@Body UserBean userBean );
    ////////////////////////////////////////////////////////////////////

    @POST("test/gettips")
    Single<ResponseTips> getTips(@Body AutenticateBean autenticateBean);

    @POST("test/newtips")
    Single<SimpleResponse> newLikeTip(@Body EventsTipsBean eventsTipsBean);

    @POST("test/newtips")
    Single<SimpleResponse> newViewTip(@Body EventsTipsBean eventsTipsBean);

    @POST("test/catalogo")
    Single<ResponseCatalogo> getCatalogos(@Body CatalogoParameters catalogoParameters);

    // PETS //////////////////////////////////////////////////////////
    @POST("test/newpets")
    Single<SimpleResponse> newPet( @Body PetBean pet );

    @POST("test/newpets")
    Single<SimpleResponse> deletePet( @Body EventsPetsBean eventsPetsBean );

    @POST("test/newpets")
    Single<SimpleResponse> ratePet( @Body EventsPetsBean eventsPetsBean );
    @POST("test/newpets")
    Single<SimpleResponse> updatePet( @Body PetBean petBean );

    @POST("test/getpets")
    Single<PetsResponse> getPets(@Body UserBean userBean);
    ////////////////////////////////////////////////////////////////////


    // PETS //////////////////////////////////////////////////////////
    @POST("test/serachusers")
    Single<SearchUsersResponse> searchUser(@Body ParameterSearching parameterSearching );
    @POST("test/searchpets")
    Single<SearchPetsResponse> searchPets(@Body ParameterSearching parameterSearching );
    ////////////////////////////////////////////////////////////////////



    @POST("test/follows")
    Single<SimpleResponse> follows(@Body FollowParameter followParameter );

    @POST("test/getcodescountry")
    Single<ResponseCodesCountries> getCodesCountry(@Body UserBean user);



    @POST("test/ids-histories")
    Single<SimpleResponse> newViewHistoryUser(@Body IdentificadorHistoryParameter identificadorHistoryParameter);

    @POST("test/ids-histories")
    Single<SimpleResponse> newLikeHistoryUser(@Body IdentificadorHistoryParameter identificadorHistoryParameter);

    @POST("test/ids-histories")
    Single<IdentificadoresHistoriesResponse> getIdentificadoresHistories(@Body IdentificadorHistoryParameter identificadorHistoryParameter);


    @POST("test/userstags")
    Single<ResponseNamesAvailables> getNamesAvailables(@Body ParameterAvailableNames parameterAvailableNames);


    @POST("test/reports")
    Single<ResponseListReports> getListReports(@Body UserBean userBean);


    @POST("test/reports")
    Single<SimpleResponse> senReport(@Body ReportBean reportBean);



}
