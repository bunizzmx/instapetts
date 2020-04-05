package com.bunizz.instapetts.activitys.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.PetsResponse;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private Context mContext;
    WebServices apiService;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    FollowsHelper followsHelper;
    PetHelper petHelper;
    int RETRY_PETS=0;
    private CompositeDisposable disposable = new CompositeDisposable();

    MainPresenter(MainContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        myStoryHelper = new MyStoryHelper(context);
        followsHelper = new FollowsHelper(mContext);
        petHelper = new PetHelper(mContext);
        db = App.getIntanceFirestore();
    }

    @Override
    public void UpdateProfile(UserBean userBean) {
        disposable.add(
                apiService
                        .newUser(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponseLogin>() {
                            @Override
                            public void onSuccess(SimpleResponseLogin user) {

                                mView.psuccessProfileUpdated();
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","UPDATED PROFILE");
                            }
                        }));
    }

    @Override
    public void saveMyStory(HistoriesBean historiesBean) {
        myStoryHelper.saveMyStory(historiesBean);
        disposable.add(
                apiService
                        .newstory(historiesBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse user) {
                               // mView.psuccessProfileUpdated();
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","UPDATED PROFILE");
                            }
                        }));
    }

    @Override
    public void followUser(UserBean userBean) {
        Map<String,Object> followUserData = new HashMap<>();
        followUserData.put("name_user",userBean.getName_user());
        followUserData.put("url_photo_user",userBean.getPhoto_user());
        followUserData.put("uuid_user",userBean.getUuid());
        followUserData.put("id_user",userBean.getId());
        followUserData.put("name_nip_user",userBean.getName_user());
        followUserData.put("token",userBean.getToken());
        followsHelper.saveNewFriend(userBean);
        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDOS)
                .document(String.valueOf(userBean.getUuid()))
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
        db.collection(FIRESTORE.R_FOLLOWS).document(String.valueOf(userBean.getUuid())).collection(FIRESTORE.SEGUIDORES)
                .document(App.read(PREFERENCES.UUID,"INVALID"))
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

    @Override
    public void downloadMyPets(UserBean userBean) {
        disposable.add(
                apiService
                        .getPets(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<PetsResponse>() {
                            @Override
                            public void onSuccess(PetsResponse pets) {
                                    mView.saveMyPets(pets.getList_pets());
                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY_PETS <3) {
                                    RETRY_PETS ++;
                                    mView.onError(1);
                                }else{
                                    RETRY_PETS =0;
                                }
                            }
                        }));
    }

    @Override
    public void have_pets() {
        ArrayList<PetBean> petBeans = new ArrayList<>();
        petBeans.addAll(petHelper.getMyPets());
        if(petBeans.size()>0)
            mView.havePetsResult(true);
        else
            mView.havePetsResult(false);
    }

    @Override
    public void update_token(UserBean userBean) {
        disposable.add(
                apiService
                        .update_token(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse pets) {
                              Log.e("TOKEN_ACTUALIZADO","SI");
                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY_PETS <3) {
                                    RETRY_PETS ++;
                                    mView.onError(1);
                                }else{
                                    RETRY_PETS =0;
                                }
                            }
                        }));
    }
}
