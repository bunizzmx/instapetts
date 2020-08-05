package com.bunizz.instapetts.fragments.profile;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.MainLoginContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseProfileUser;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileUserPresenter implements   ProfileUserContract.Presenter {

    ProfileUserContract.View mView;
    Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    private static final String TAG = MainLogin.class.getSimpleName();
    SavedPostHelper savedPostHelper;
    LikePostHelper likePostHelper;
    PetHelper petHelper;
    int RETRY =0;
    ProfileUserPresenter(ProfileUserContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        savedPostHelper = new SavedPostHelper(mContext);
        likePostHelper = new LikePostHelper(mContext);
        petHelper = new PetHelper(this.mContext);
    }

    @Override
    public void getInfoUser(UserBean user) {
    disposable.add(
                apiService
                        .getInfoUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseProfileUser>() {
                            @Override
                            public void onSuccess(ResponseProfileUser info) {
                                if(info.getData_user()!=null) {
                                    if(info.getCode_response()==200) {
                                        mView.showInfoUser(info.getData_user(), info.getPetsUser());
                                    }else{
                                        if(RETRY < 3){
                                            RETRY ++;
                                            mView.Error();
                                        }else
                                            mView.ErrorPostUsers();
                                    }
                                }else{
                                    if(RETRY < 3){
                                        RETRY ++;
                                        mView.Error();
                                    }else
                                        mView.ErrorPostUsers();

                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY < 3){
                                    RETRY ++;
                                    mView.Error();
                                }
                                else
                                    mView.ErrorPostUsers();
                                //mView.showInfoUser(info.getData_user(),info.getPetsUser(),info.getPostsUser());
                            }
                        }));
    }

    @Override
    public void getPostUser(boolean one_user, int id_one,int filter) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setId_one(id_one);
        postFriendsBean.setTarget("ONE");
        postFriendsBean.setFilter(filter);
        postFriendsBean.setPaginador(-999);
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
                                    mView.showPostUser(post);
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.ErrorPostUsers();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.ErrorPostUsers();
                                }else{
                                    Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                                }

                            }
                        })
        );
    }

    @Override
    public void getPostUserPaginate(boolean one_user, int id_one, int filter, int paginador) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setId_one(id_one);
        postFriendsBean.setTarget("ONE");
        postFriendsBean.setFilter(filter);
        postFriendsBean.setPaginador(paginador);
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
                                    mView.showPostUserPaginate(post);
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.ErrorPostUsers();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.ErrorPostUsers();
                                }else{
                                    Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                                }

                            }
                        })
        );
    }

    @Override
    public void follow(int id_user, boolean follow) { }

    @Override
    public boolean is_user_followed(int id_user) {
        return false;
    }

    @Override
    public void updateMyPetLocal(PetBean petBean) {
        petHelper.updateMyPet(petBean);
    }
}
