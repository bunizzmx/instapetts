package com.bunizz.instapetts.web;

import android.provider.ContactsContract;
import android.provider.UserDictionary;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServices {

    // Register new user
    @FormUrlEncoded
    @POST("notes/user/register")
    Single<PetBean> register(@Field("user") PetBean user);

    // Create note
    @FormUrlEncoded
    @POST("notes/new")
    Single<ContactsContract.CommonDataKinds.Note> createNote(@Field("note") String note);

    // Fetch all notes
    @GET("notes/all")
    Single<List<PostBean>> fetchAllNotes();

    // Update single note
    @FormUrlEncoded
    @PUT("notes/{id}")
    Completable updateNote(@Path("id") int noteId, @Field("note") String note);

    // Delete note
    @DELETE("notes/{id}")
    Completable deleteNote(@Path("id") int noteId);
}
