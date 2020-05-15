package com.bunizz.instapetts.fragments.login.first_user;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.ParameterAvailableNames;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponseNamesAvailables;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FirstUserPresenter implements FirstUserContract.Presenter {

    private FirstUserContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    int RETRY =0;

    FirstUserPresenter(FirstUserContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }


    @Override
    public void getNameAvailable(String name) {
        ParameterAvailableNames parameterAvailableNames = new ParameterAvailableNames();
        parameterAvailableNames.setName(name);
        parameterAvailableNames.setTarget("GET");
        disposable.add(
                apiService.getNamesAvailables(parameterAvailableNames)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseNamesAvailables>() {
                            @Override
                            public void onSuccess(ResponseNamesAvailables responsePost) {
                                if(responsePost.getCode_response() == 200) {
                                    if(responsePost.getAvailable() == 1){
                                       mView.showUsersAvailables(true);
                                    }else
                                        mView.showUsersAvailables(false);
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        //mView.peticion_error();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                   // mView.peticion_error();
                                }else{
                                    Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                                }

                            }
                        })
        );
    }
}
