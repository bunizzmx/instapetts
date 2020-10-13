package com.bunizz.instapetts.fragments.vertical_videos;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.db.helpers.PlayVideosHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.ResponsePlayVideos;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.facebook.internal.LockOnGetVariable;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class playVideoPresenter implements  playVideoContract.Presenter{

    private playVideoContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    SavedPostHelper savedPostHelper ;
    LikePostHelper likePostHelper;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    FollowsHelper followsHelper;
    NotificationHelper notificationHelper;
    IdsUsersHelper idsUsersHelper;
    PlayVideosHelper playVideosHelper;




    public playVideoPresenter(playVideoContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        savedPostHelper = new SavedPostHelper(this.mContext);
        likePostHelper = new LikePostHelper(this.mContext);
        myStoryHelper = new MyStoryHelper(this.mContext);
        followsHelper = new FollowsHelper(this.mContext);
        notificationHelper = new NotificationHelper(mContext);
        idsUsersHelper = new IdsUsersHelper(mContext);
        playVideosHelper = new PlayVideosHelper(mContext);
        db = App.getIntanceFirestore();
    }

    @Override
    public void getVideos( int paginador,boolean more,int type_videos) {
        AutenticateBean autenticateBean = new AutenticateBean();
        if(type_videos == 0)
           autenticateBean.setTarget(WEBCONSTANTS.INSTAPETTSTV);
        else
            autenticateBean.setTarget(WEBCONSTANTS.VIDEOS_USERS);

        autenticateBean.setPaginador(paginador);
        disposable.add(
                apiService.getPlayVideos(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePlayVideos>() {
                            @Override
                            public void onSuccess(ResponsePlayVideos responsePost) {
                                Log.e("play_videos","-->" + responsePost.getList_videos().size());
                                if(!more)
                                  mView.showVideos(responsePost.getList_videos());
                                else
                                    mView.showMoreVideos(responsePost.getList_videos());
                            }
                            @Override
                            public void onError(Throwable e) {}
                        })
        );
    }

    @Override
    public void likeVideo(int id_video) {
        playVideosHelper.saveLikePost(id_video);
    }

    @Override
    public boolean is_video_liked(int id_video) {
        return playVideosHelper.searchPostById(id_video);
    }
}
