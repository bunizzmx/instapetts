package com.bunizz.instapetts.activitys.story_player;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.IdentificadoresHistoriesHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.IdentificadorHistoryParameter;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StoryPlapyerPresenter implements  StoryPlayerContract.Presenter {

    private StoryPlayerContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    IdentificadoresHistoriesHelper helper ;
    int RETRY =0;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    IdsUsersHelper idsUsersHelper;
    StoryPlapyerPresenter(StoryPlayerContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        helper = new IdentificadoresHistoriesHelper(this.mContext);
        db = App.getIntanceFirestore();
        idsUsersHelper = new IdsUsersHelper(mContext);
    }

    @Override
    public void ViewHistory(String identificador, int id_usuario) {
        IdentificadorHistoryParameter identificadorHistoryParameter = new IdentificadorHistoryParameter();
        identificadorHistoryParameter.setIdentificador(identificador);
        identificadorHistoryParameter.setId_usuario(id_usuario);
        identificadorHistoryParameter.setTarget("NEW_VIEW");
        disposable.add(
                apiService.newViewHistoryUser(identificadorHistoryParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                               if(responsePost.getCode_response()== 200){
                                   Log.e("VIEW_HISTORY","SUCCESS");
                               }else
                                   Log.e("VIEW_HISTORY","ERROR");
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                   // mView.peticion_error();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                  //  mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void LikeHistory(String identificador, int id_usuario) {
        IdentificadorHistoryParameter identificadorHistoryParameter = new IdentificadorHistoryParameter();
        identificadorHistoryParameter.setIdentificador(identificador);
        identificadorHistoryParameter.setId_usuario(id_usuario);
        identificadorHistoryParameter.setTarget("NEW_LIKE");
        disposable.add(
                apiService.newLikeHistoryUser(identificadorHistoryParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost.getCode_response()== 200){
                                    Log.e("VIEW_HISTORY","SUCCESS");
                                }else
                                    Log.e("VIEW_HISTORY","ERROR");
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    // mView.peticion_error();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    //  mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public IdentificadoresHistoriesBean getIdentificador(String identificador) {
        return helper.searchIdentidicadorById(identificador);
    }


    @Override
    public void followUser(UserBean userBean) {

        Map<String,Object> followedUserData = new HashMap<>();
        followedUserData.put("url_photo_user",userBean.getPhoto_user_thumbh());
        followedUserData.put("uuid_user",userBean.getUuid());
        followedUserData.put("id_user",userBean.getId());
        followedUserData.put("name_nip_user",userBean.getName_tag());
        followedUserData.put("token",userBean.getToken());
        followedUserData.put("name_user",userBean.getName_user());


        Map<String,Object> followUserData = new HashMap<>();
        followUserData.put("url_photo_user", App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
        followUserData.put("uuid_user",App.read(PREFERENCES.UUID,"INVALID"));
        followUserData.put("id_user",App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        followUserData.put("name_nip_user",App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"));
        followUserData.put("token",userBean.getToken());
        followUserData.put("name_user",App.read(PREFERENCES.NAME_USER,"INVALID"));
        idsUsersHelper.saveId(userBean.getId());
        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDOS)
                .document(userBean.getName_tag())
                .set(followedUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        db.collection(FIRESTORE.R_FOLLOWS).document(String.valueOf(userBean.getUuid())).collection(FIRESTORE.SEGUIDORES)
                .document(App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"))
                .set(followUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public void unfollowUser(String id_document) {
        db.collection(FIRESTORE.R_FOLLOWS).document(id_document).collection(FIRESTORE.SEGUIDORES)
                .document(App.read(PREFERENCES.UUID,"INVALID"))
                .delete()
                .addOnSuccessListener(aVoid -> {    Log.e("BORRE_FOLLOW","DE EL"); })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE EL");});
        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDORES)
                .document(id_document)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.e("BORRE_FOLLOW","DE MI");
                })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE MI");});
    }

    @Override
    public void favoritePet(UserBean userBean, PetBean petBean) {
        Map<String,Object> followPetData = new HashMap<>();
        followPetData.put("name_user",userBean.getName_user());
        followPetData.put("url_photo_user",userBean.getPhoto_user());
        followPetData.put("uuid_user",userBean.getUuid());
        followPetData.put("id_user",userBean.getId());
        followPetData.put("name_nip_user",userBean.getName_user());
        db.collection(FIRESTORE.R_FAVORITES).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.FAVORITES)
                .document(String.valueOf(userBean.getUuid()))
                .set(followPetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }



}
