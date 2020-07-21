package com.bunizz.instapetts.fragments.follows;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.FollowsBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.FollowParameter;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FollowsPresenter implements FollowsContract.Presenter {

    private FollowsContract.View mView;
    private Context mContext;
    WebServices apiService;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    IdsUsersHelper followsHelper;
    PetHelper petHelper;
    int RETRY_PETS=0;
    int RETRY =0;
    String PAGINADOR="-";
    private DocumentSnapshot x_followers;
    private DocumentSnapshot x_followed;
    boolean is_first_pagination =false;
    private CompositeDisposable disposable = new CompositeDisposable();

    FollowsPresenter(FollowsContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        myStoryHelper = new MyStoryHelper(context);
        followsHelper = new IdsUsersHelper(mContext);
        petHelper = new PetHelper(mContext);
        db = App.getIntanceFirestore();
    }


    @Override
    public void getFirstFolowers(String uuid) {
        x_followers = null;
        Log.e("UUID-PETICION","-->"+uuid);
        ArrayList<FollowsBean> followsBeans=new ArrayList<>();
        db.collection(FIRESTORE.R_FOLLOWS).document(uuid).collection(FIRESTORE.SEGUIDORES).limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FollowsBean follow = document.toObject(FollowsBean.class);
                            followsBeans.add(follow);
                            PAGINADOR= document.getId();
                            x_followers = document;
                        }

                        mView.showFirstFollowers(followsBeans);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        RETRY ++;
                        if(RETRY < 3) {
                            mView.Error();
                        }else{
                            mView.noInternet();
                        }
                    }
                });
    }

    @Override
    public void getFirstFollowed(String uuid) {
        Log.e("UUID-PETICION","-->"+uuid);
        ArrayList<FollowsBean> followsBeans=new ArrayList<>();
        db.collection(FIRESTORE.R_FOLLOWS).document(uuid).collection(FIRESTORE.SEGUIDOS).limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FollowsBean follow = document.toObject(FollowsBean.class);
                            followsBeans.add(follow);
                            PAGINADOR= document.getId();
                            x_followed = document;
                        }

                        mView.showFirstFollowed(followsBeans);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        RETRY ++;
                        if(RETRY < 3) {
                            mView.Error();
                        }else{
                            mView.noInternet();
                        }
                    }
                });
    }

    @Override
    public void nextFollowers() {
        ArrayList<FollowsBean> FOLLOWSBEANS = new ArrayList<>();
        Log.e("DOWNLOAD_MORE",":lll");
        FOLLOWSBEANS.clear();
        Query query;
        if (x_followers == null) {
            Log.e("DOWNLOAD_MORE",":x es nulo");
            is_first_pagination = true;
            query = db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID"))
                    .collection(FIRESTORE.SEGUIDORES)
                    .limit(20);
        } else {
            Log.e("DOWNLOAD_MORE",":x no es nulo");
            is_first_pagination = false;
            query = db.collection(FIRESTORE.R_FOLLOWS)
                    .document(App.read(PREFERENCES.UUID,"INVALID"))
                    .collection(FIRESTORE.SEGUIDORES)
                    .startAfter(x_followers)
                    .limit(10);
        }
        query.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                FollowsBean coment = document.toObject(FollowsBean.class);
                FOLLOWSBEANS.add(coment);
                x_followers = document;
            }
            if (is_first_pagination) {
                Log.e("DOWNLOAD_MORE","is first");
                mView.showNextFollowers(FOLLOWSBEANS);
            }
            else {
                Log.e("DOWNLOAD_MORE","more more");
                mView.showNextFollowers(FOLLOWSBEANS);
            }
        }).addOnFailureListener(e ->{
            mView.showNextFollowers(FOLLOWSBEANS);
            Log.e("DOWNLOAD_MORE",":ccccccccc");});
    }

    @Override
    public void nextFollowed() {
        ArrayList<FollowsBean> FOLLOWSBEANS = new ArrayList<>();
        Log.e("DOWNLOAD_MORE",":lll");
        FOLLOWSBEANS.clear();
        Query query;
        if (x_followed == null) {
            Log.e("DOWNLOAD_MORE",":x es nulo");
            is_first_pagination = true;
            query = db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID"))
                    .collection(FIRESTORE.SEGUIDOS)
                    .limit(20);
        } else {
            Log.e("DOWNLOAD_MORE",":x no es nulo");
            is_first_pagination = false;
            query = db.collection(FIRESTORE.R_FOLLOWS)
                    .document(App.read(PREFERENCES.UUID,"INVALID"))
                    .collection(FIRESTORE.SEGUIDOS)
                    .startAfter(x_followed)
                    .limit(10);
        }
        query.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                FollowsBean coment = document.toObject(FollowsBean.class);
                FOLLOWSBEANS.add(coment);
                x_followed = document;
            }
            if (is_first_pagination) {
                Log.e("DOWNLOAD_MORE","is first");
                mView.showNextFollowed(FOLLOWSBEANS);
            }
            else {
                Log.e("DOWNLOAD_MORE","more more");
                mView.showNextFollowed(FOLLOWSBEANS);
            }
        }).addOnFailureListener(e ->{
            mView.showNextFollowed(FOLLOWSBEANS);
            Log.e("DOWNLOAD_MORE",":ccccccccc");});
    }

    @Override
    public void unfollowUser(String uuid,String name_tag,int id_usuario,boolean delete_me_friends) {
        FollowParameter followParameter= new FollowParameter();
        followParameter.setId_user(id_usuario);
        if(delete_me_friends)
           followParameter.setTarget("DELETE_OF_MY_FRIENDS");
        else
            followParameter.setTarget("UNFOLLOW");
        followParameter.setId_my_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        disposable.add(
                apiService.follows(followParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost!=null) {
                                    if(responsePost.getCode_response()==200){
                                        if(delete_me_friends) {
                                            db.collection(FIRESTORE.R_FOLLOWS).document(uuid).collection(FIRESTORE.SEGUIDOS)
                                                    .document(App.read(PREFERENCES.NAME_TAG_INSTAPETTS, "INVALID"))
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.e("BORRE_FOLLOW", "DE EL");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                    })
                                                    .addOnCompleteListener(task -> {
                                                        mView.UnfollowSuccess();
                                                        Log.e("BORRE_FOLLOW", "DE EL");
                                                    });
                                            db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID, "INVALID")).collection(FIRESTORE.SEGUIDORES)
                                                    .document(name_tag)
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.e("BORRE_FOLLOW", "DE MI");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                    })
                                                    .addOnCompleteListener(task -> {
                                                        mView.UnfollowSuccess();
                                                        Log.e("BORRE_FOLLOW", "DE MI");
                                                    });
                                        }else{
                                            followsHelper.deleteId(id_usuario);
                                            db.collection(FIRESTORE.R_FOLLOWS).document(uuid).collection(FIRESTORE.SEGUIDORES)
                                                    .document(App.read(PREFERENCES.NAME_TAG_INSTAPETTS, "INVALID"))
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.e("BORRE_FOLLOW", "DE EL");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                    })
                                                    .addOnCompleteListener(task -> {
                                                        mView.UnfollowSuccess();
                                                        Log.e("BORRE_FOLLOW", "DE EL");
                                                    });
                                            db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID, "INVALID")).collection(FIRESTORE.SEGUIDOS)
                                                    .document(name_tag)
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.e("BORRE_FOLLOW", "DE MI");
                                                    })
                                                    .addOnFailureListener(e -> {
                                                    })
                                                    .addOnCompleteListener(task -> {
                                                        mView.UnfollowSuccess();
                                                        Log.e("BORRE_FOLLOW", "DE MI");
                                                    });
                                        }
                                    }
                                }  else{
                                    mView.UnfollowSuccess();
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.UnfollowSuccess();
                                }else{
                                    Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                                }

                            }
                        })
        );
    }
}
