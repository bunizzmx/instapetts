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
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private CompositeDisposable disposable = new CompositeDisposable();

    MainPresenter(MainContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        myStoryHelper = new MyStoryHelper(context);
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
    }

    @Override
    public void followUser(UserBean userBean) {
        Map<String,Object> followUserData = new HashMap<>();
        followUserData.put("name_user",userBean.getName_user());
        followUserData.put("url_photo_user",userBean.getPhoto_user());
        followUserData.put("uuid_user",userBean.getUuid());
        followUserData.put("id_user",userBean.getId());
        followUserData.put("name_nip_user",userBean.getName_user());
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
