package com.bunizz.instapetts.fragments.search.posts;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.profile.ProfileUserContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponsePost;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PostPublicsPresenter implements   PostPublicsContract.Presenter {

    PostPublicsContract.View mView;
    Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    private static final String TAG = MainLogin.class.getSimpleName();
    int RETRY =0;
    PostPublicsPresenter(PostPublicsContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }

    @Override
    public void getPostPublics(int type_search) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setPaginador(-1);
        postFriendsBean.setId_one(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        switch (type_search){
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
                                    mView.showPosts(post);
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.peticionError();
                                    }else{
                                        mView.noInternet();
                                    }

                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                               RETRY ++;
                                if(RETRY < 3) {
                                    mView.peticionError();
                                }else{
                                   mView.noInternet();
                                }
                            }
                        })
        );
    }
}
