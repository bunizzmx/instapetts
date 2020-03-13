package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.managers.FeedResponse;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.bunizz.instapetts.web.responses.ResponsePost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    int RETRY =0;

    FeedPresenter(FeedContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        savedPostHelper = new SavedPostHelper(context);
        likePostHelper = new LikePostHelper(context);
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
                                Log.e("NUMBER_POSTS","-->" + responsePost.getList_posts().size());
                                ArrayList<PostBean> post = new ArrayList<>();
                                post.addAll(responsePost.getList_posts());
                                for (int i=0; i<post.size();i++){
                                    if(savedPostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                        post.get(i).setSaved(true);
                                    else
                                        post.get(i).setSaved(false);

                                    if(likePostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                        post.get(i).setLiked(true);
                                    else
                                        post.get(i).setLiked(false);
                                }
                                mView.show_feed(post,responsePost.getList_stories());
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
    }

    @Override
    public void deleteFavorite(int id_post) {
        savedPostHelper.deleteSavedPost(id_post);
    }
}
