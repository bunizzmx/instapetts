package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.managers.FeedResponse;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.reactivex.functions.Function;

public class FeedPresenter implements FeedContract.Presenter {
    private FeedContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    SavedPostHelper savedPostHelper ;
    LikePostHelper likePostHelper;
    MyStoryHelper myStoryHelper;
    int RETRY =0;
    FirebaseFirestore db;

    FeedPresenter(FeedContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        savedPostHelper = new SavedPostHelper(context);
        likePostHelper = new LikePostHelper(context);
        myStoryHelper = new MyStoryHelper(context);
        db = App.getIntanceFirestore();
    }

    @Override
    public void get_feed() {
        AutenticateBean autenticateBean = new AutenticateBean();
        autenticateBean.setName_user("DEMO");
        autenticateBean.setToken("xxxx");
        disposable.add(
                apiService.getPosts(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePost>() {
                            @Override
                            public void onSuccess(ResponsePost responsePost) {
                                if(responsePost.getList_posts()!=null) {
                                    if(responsePost.getList_posts()!=null)
                                    Log.e("NUMBER_POSTS", "-->" + responsePost.getList_posts().size());
                                    ArrayList<PostBean> post = new ArrayList<>();
                                    post.addAll(responsePost.getList_posts());
                                    for (int i = 0; i < post.size(); i++) {
                                        if (savedPostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                            post.get(i).setSaved(true);
                                        else
                                            post.get(i).setSaved(false);

                                        if (likePostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                            post.get(i).setLiked(true);
                                        else
                                            post.get(i).setLiked(false);
                                    }
                                    mView.show_feed(post, responsePost.getList_stories());
                                }  else{
                                    mView.peticion_error();
                                    }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.peticion_error();
                                }else{
                                    Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                                }

                            }
                        })
        );
    }

    @Override
    public void likePost(PostActions postActions) {
        likePostHelper.saveLikePost(postActions.getId_post());
    }

    @Override
    public void saveFavorite(PostActions postActions,PostBean postBean) {
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
    }

    @Override
    public ArrayList<HistoriesBean> getMyStories() {
      return  myStoryHelper.getMyStories();
    }
}
