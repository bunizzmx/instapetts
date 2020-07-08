package com.bunizz.instapetts.activitys.PlayVideo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.bunizz.instapetts.web.parameters.PostLikeBean;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PlayVideoPresenter implements PlayVideoContract.Presenter {


    private PlayVideoContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    int RETRY =0;
    FirebaseFirestore db;
    LikePostHelper likePostHelper;
    SavedPostHelper savedPostHelper;
    IdsUsersHelper idsUsersHelper;
    PlayVideoPresenter(PlayVideoContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        likePostHelper = new LikePostHelper(this.mContext);
        savedPostHelper = new SavedPostHelper(this.mContext);
        idsUsersHelper = new IdsUsersHelper(this.mContext);
        db = App.getIntanceFirestore();
    }

    @Override
    public void likeVideo(PostActions postActions) {
        boolean existsin_db = likePostHelper.saveLikePost(postActions.getId_post());
        if(!existsin_db) {
            PostLikeBean postLikeBean = new PostLikeBean();
            postLikeBean.setId_post(postActions.getId_post());
            postLikeBean.setId_user(postActions.getId_usuario());
            postLikeBean.setType_event(Integer.parseInt(postActions.getAcccion()));
            postLikeBean.setTarget("NEW_LIKE");
            disposable.add(
                    apiService.like_posts(postLikeBean)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                @Override
                                public void onSuccess(SimpleResponse responsePost) {
                                    if (responsePost != null) {
                                        if (responsePost.getCode_response() == 200) {
                                            mView.LikeSuccess();
                                            Map<String, Object> data_notification = new HashMap<>();
                                            data_notification.put("TOKEN",responsePost.getResult_data_extra());
                                            data_notification.put("ID_RECURSO",postActions.getId_post());
                                            data_notification.put("TYPE_NOTIFICATION",0);
                                            data_notification.put("NAME_REMITENTE", App.read(PREFERENCES.NAME_USER,"USER"));
                                            data_notification.put("ID_REMITENTE",App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                                            data_notification.put("URL_EXTRA",postActions.getExtra());
                                            data_notification.put("FOTO_REMITENTE",App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
                                            data_notification.put("FECHA",App.formatDateGMT(new Date()));
                                            db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document(""+postActions.getId_usuario())
                                                    .collection(FIRESTORE.COLLECTION_NOTIFICATIONS)
                                                    .document()
                                                    .set(data_notification)
                                                    .addOnFailureListener(e -> {})
                                                    .addOnCompleteListener(task -> {})
                                                    .addOnSuccessListener(aVoid -> {});
                                        } else {
                                            RETRY++;
                                            if (RETRY < 3) {
                                                //mView.LikeEror();
                                            }
                                        }
                                    } else {
                                        RETRY++;
                                        if (RETRY < 3) {
                                           // mView.LikeEror();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    RETRY++;
                                    if (RETRY < 3) {
                                       // mView.LikeEror();
                                    } else {
                                        Log.e("NUMBER_POSTS", "-->EROR : " + e.getMessage());
                                    }

                                }
                            })
            );
        }else{
            Log.e("REGISTER_LIKE", "-->YA EXISTE NO LO ENVBIO A WEB : ");
        }
    }

    @Override
    public boolean isLiked(int id_post) {
        return likePostHelper.searchPostById(id_post);
    }

    @Override
    public boolean isSaved(int id_post) {
        return savedPostHelper.searchPostById(id_post);
    }

    @Override
    public void unfollowUser(String uuid_usuario, int id_usuario) {
        db.collection(FIRESTORE.R_FOLLOWS).document(uuid_usuario).collection(FIRESTORE.SEGUIDORES)
                .document(App.read(PREFERENCES.UUID,"INVALID"))
                .delete()
                .addOnSuccessListener(aVoid -> {    Log.e("BORRE_FOLLOW","DE EL"); })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE EL");});
        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDORES)
                .document(uuid_usuario)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.e("BORRE_FOLLOW","DE MI");
                })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE MI");});

        idsUsersHelper.deleteId(id_usuario);
    }

    @SuppressLint("CheckResult")
    @Override
    public void deleteVideo(PostBean postBean) {
        apiService.delete_post(postBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                    @Override
                    public void onSuccess(SimpleResponse response) {
                        Log.e("POST","SUCCESS");
                        if(response!=null){
                            if(response.getCode_response() ==200) {
                                mView.deletePostError(true);
                                Log.e("ID_BORRAR","-->" + postBean.getId_post_from_web());
                                db.collection(FIRESTORE.COLLECTION_COMENTARIOS).document(""+postBean.getId_post_from_web())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.e("POST","COMEMTARIO BORRADO");
                                            }
                                        })
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.e("POST","COMEMTARIO BORRADO 2");
                                            }
                                        });
                            }
                            else
                                mView.deletePostError(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.deletePostError(false);
                    }
                });
    }


    @Override
    public void saveFavorite(PostActions postActions, PostBean postBean) {
        savedPostHelper.savePost(postBean);
        Map<String,Object> data_post_saved = new HashMap<>();
        data_post_saved.put("urls_posts",postBean.getUrls_posts());
        data_post_saved.put("name_pet",postBean.getName_pet());
        data_post_saved.put("name_user",postBean.getName_user());
        data_post_saved.put("url_photo_user",postBean.getUrl_photo_user());
        data_post_saved.put("description",postBean.getDescription());
        data_post_saved.put("date_post",postBean.getDate_post());
        data_post_saved.put("uuid",postBean.getUuid());
        data_post_saved.put("id_usuario",postBean.getId_usuario());
        data_post_saved.put("id_post_from_web",postBean.getId_post_from_web());
        data_post_saved.put("saved",postBean.isSaved());
        data_post_saved.put("liked",postBean.isLiked());
        data_post_saved.put("thumb_video",postBean.getThumb_video());
        data_post_saved.put("address",postBean.getAddress());
        data_post_saved.put("aspect",postBean.getAspect());
        data_post_saved.put("can_comment",postBean.getCan_comment());
        data_post_saved.put("cp",postBean.getCp());
        data_post_saved.put("type_post",postBean.getType_post());
        data_post_saved.put("type_pet",postBean.getType_pet());
        db.collection(FIRESTORE.R_POSTS_SAVED).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.POSTS)
                .document(String.valueOf(postBean.getId_post_from_web()))
                .set(data_post_saved)
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
    public void deleteFavorite(int id_post) {
        savedPostHelper.deleteSavedPost(id_post);
        db.collection(FIRESTORE.R_POSTS_SAVED).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.POSTS)
                .document(""+id_post)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("DELETED_FAVORITE","ON COMPLETE");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("DELETED_FAVORITE","ON SUCCESS");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}