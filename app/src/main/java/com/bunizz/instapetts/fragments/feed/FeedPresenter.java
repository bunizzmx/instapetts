package com.bunizz.instapetts.fragments.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.managers.FeedResponse;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.parameters.PostLikeBean;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.SimpleResponse;
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
    FollowsHelper followsHelper;

    FeedPresenter(FeedContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        savedPostHelper = new SavedPostHelper(this.mContext);
        likePostHelper = new LikePostHelper(this.mContext);
        myStoryHelper = new MyStoryHelper(this.mContext);
        followsHelper = new FollowsHelper(this.mContext);
        db = App.getIntanceFirestore();
    }

    @Override
    public void get_feed(boolean one_user,int id_one) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        if(one_user){
            postFriendsBean.setId_one(id_one);
            postFriendsBean.setTarget("ONE");
        }else{
            postFriendsBean.setTarget("MULTIPLE");
            postFriendsBean.setIds(followsHelper.getMyFriendsForPost());
            postFriendsBean.setIds_h(followsHelper.getMyFriendsForPost());
        }
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
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.peticion_error();
                                    }else{
                                        Log.e("NO_INTERNET","--> tries alcanzados" );
                                        mView.noInternet();
                                    }
                                    }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.peticion_error();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                  mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void geet_feed_recomended(boolean one_user, int id_one) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setId_one(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
         postFriendsBean.setTarget("DISCOVER");
        disposable.add(
                apiService.getPosts(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePost>() {
                            @Override
                            public void onSuccess(ResponsePost responsePost) {
                                if(responsePost.getList_posts()!=null) {
                                    if(responsePost.getList_posts()!=null)
                                        Log.e("NUMBER_POSTS_RECOMENDED", "-->" + responsePost.getList_posts().size());
                                    ArrayList<PostBean> post = new ArrayList<>();
                                    post.addAll(responsePost.getList_posts());
                                    mView.show_feed_recomended(post);
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.peticion_error();
                                    }
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
        boolean existsin_db = likePostHelper.saveLikePost(postActions.getId_post());
        if(!existsin_db) {
            PostLikeBean postLikeBean = new PostLikeBean();
            postLikeBean.setId_post(postActions.getId_post());
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
                                        } else {
                                            RETRY++;
                                            if (RETRY < 3) {
                                                mView.LikeEror();
                                            }
                                        }
                                    } else {
                                        RETRY++;
                                        if (RETRY < 3) {
                                            mView.LikeEror();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    RETRY++;
                                    if (RETRY < 3) {
                                        mView.LikeEror();
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

    @SuppressLint("CheckResult")
    @Override
    public void deletePost(PostBean postBean) {
        apiService.delete_post(postBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                    @Override
                    public void onSuccess(SimpleResponse response) {
                        Log.e("POST","SUCCESS");
                     if(response!=null){
                        if(response.getCode_response() ==200)
                            mView.deletePostError(true);
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
    public HistoriesBean getMyStories() {
        HistoriesBean bean = new HistoriesBean();
        ArrayList<IndividualDataPetHistoryBean> historyBeans = new ArrayList<>();
        historyBeans.addAll(myStoryHelper.getMyStories());
        bean.setHistories(historyBeans);
        bean.setName_user(App.read(PREFERENCES.NAME_USER,"INVALID"));
        bean.setUrl_photo_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
        bean.setId_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        return  bean;
    }
}
