package com.bunizz.instapetts.fragments.previewProfile;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.profile.ProfileUserContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.FollowParameter;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseProfileUser;
import com.bunizz.instapetts.web.responses.SimpleResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PreviewProfileUserPresenter implements   ProfileUserContract.Presenter {

    ProfileUserContract.View mView;
    Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    IdsUsersHelper followsHelper;
    SavedPostHelper savedPostHelper;
    LikePostHelper likePostHelper;
    private static final String TAG = MainLogin.class.getSimpleName();
    int RETRY =0;
    PreviewProfileUserPresenter(ProfileUserContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        followsHelper = new IdsUsersHelper(mContext);
        savedPostHelper = new SavedPostHelper(mContext);
        likePostHelper = new LikePostHelper(mContext);
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
                                    mView.showInfoUser(info.getData_user(),info.getPetsUser());
                                }else{
                                    if(RETRY < 3){
                                        RETRY ++;
                                        mView.Error();
                                    }
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY < 3){
                                    RETRY ++;
                                    mView.Error();
                                }
                            }
                        }));
    }

    @Override
    public void getPostUser(boolean one_user,int id_one,int filter) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setPaginador(-1);
        if(one_user){
            postFriendsBean.setId_one(id_one);
            postFriendsBean.setTarget("ONE");
        }else{
            postFriendsBean.setTarget("MULTIPLE");
            postFriendsBean.setIds(followsHelper.getMyFriendsForPost());
        }
        postFriendsBean.setFilter(filter);
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
                                    mView.ErrorPostUsers();
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

    }

    @Override
    public void follow(int id_user,boolean follow) {
        FollowParameter followParameter= new FollowParameter();
        followParameter.setId_user(id_user);
        if(follow)
            followParameter.setTarget("FOLLOW");
        else {
            followParameter.setTarget("UNFOLLOW");
        }

        followParameter.setId_my_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        disposable.add(
                apiService.follows(followParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost!=null) {
                                    if(responsePost.getCode_response()==200) {
                                        if(follow == false){
                                            followsHelper.deleteId(id_user);
                                        }
                                        mView.successFollow(follow, id_user);
                                    }
                                    else
                                        mView.successFollow(follow,id_user);

                                }  else{
                                    mView.successFollow(follow,id_user);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.successFollow(false,id_user);
                                }else{
                                    Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                                }

                            }
                        })
        );
    }

    @Override
    public boolean is_user_followed(int id_user) {
        if(followsHelper.isMyFriend(id_user))
          return true;
        else
            return false;
    }

    @Override
    public void updateMyPetLocal(PetBean petBean) {
    }
}
