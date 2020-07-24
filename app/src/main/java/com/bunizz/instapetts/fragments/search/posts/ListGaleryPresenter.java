package com.bunizz.instapetts.fragments.search.posts;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.parameters.PostLikeBean;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListGaleryPresenter implements   ListGaleryContract.Presenter {

    ListGaleryContract.View mView;
    Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    private static final String TAG = MainLogin.class.getSimpleName();
    int RETRY =0;
    SavedPostHelper savedPostHelper;
    FirebaseFirestore db;
    LikePostHelper likePostHelper;
    ListGaleryPresenter(ListGaleryContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        savedPostHelper = new SavedPostHelper(mContext);
        db = App.getIntanceFirestore();
        likePostHelper = new LikePostHelper(mContext);
    }

    @Override
    public void getMorePost(int type_Search, int paginador) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setPaginador(paginador);
        postFriendsBean.setId_one(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        switch (type_Search){
            case 0:
                postFriendsBean.setTarget(WEBCONSTANTS.DISCOVER);
                break;
            case 1:
                postFriendsBean.setTarget(WEBCONSTANTS.MORE_VIEWS);
                break;
            case 2:
                postFriendsBean.setTarget(WEBCONSTANTS.RECENT);
                break;
            default:break;
        }

        Log.e("PAGINADOR_MORE","-->" + paginador + "/" + type_Search);

        disposable.add(
                apiService.getPosts(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePost>() {
                            @Override
                            public void onSuccess(ResponsePost responsePost) {
                                if(responsePost.getList_posts()!=null) {
                                    if(responsePost.getList_posts()!=null)
                                        Log.e("NUMBER_POSTS", "-->" + responsePost.getList_posts().size());
                                    ArrayList<PostBean> post = new ArrayList<>();
                                    for (int i =0;i<responsePost.getList_posts().size();i++){
                                        if(responsePost.getList_posts().get(i).getCensored() == 0){
                                            post.add(responsePost.getList_posts().get(i));
                                        }
                                    }
                                    mView.showMorePost(post);
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        //mView.peticionError();
                                    }else{
                                       // mView.noInternet();
                                    }

                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                   // mView.peticionError();
                                }else{
                                   // mView.noInternet();
                                }
                            }
                        })
        );
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
        data_post_saved.put("thumb_video",postBean.getThumb_video());
        data_post_saved.put("aspect",postBean.getAspect());
        data_post_saved.put("type_post",postBean.getType_post());
        data_post_saved.put("duracion",postBean.getDuracion());
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
    public void likePost(PostActions postActions) {
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
                                            Map<String, Object> data_notification = new HashMap<>();
                                            data_notification.put("TOKEN",responsePost.getResult_data_extra());
                                            data_notification.put("ID_RECURSO",postActions.getId_post());
                                            data_notification.put("TYPE_NOTIFICATION",0);
                                            data_notification.put("NAME_REMITENTE",App.read(PREFERENCES.NAME_USER,"USER"));
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
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {}
                            })
            );
        }else{
            Log.e("REGISTER_LIKE", "-->YA EXISTE NO LO ENVBIO A WEB : ");
        }
    }
}
