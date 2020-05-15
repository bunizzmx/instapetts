package com.bunizz.instapetts.activitys.story_player;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdentificadoresHistoriesHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.IdentificadorHistoryParameter;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StoryPlapyerPresenter implements  StoryPlayerContract.Presenter {

    private StoryPlayerContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    IdentificadoresHistoriesHelper helper ;
    int RETRY =0;
    StoryPlapyerPresenter(StoryPlayerContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        helper = new IdentificadoresHistoriesHelper(this.mContext);
    }

    @Override
    public void ViewHistory(String identificador, int id_usuario) {
        IdentificadorHistoryParameter identificadorHistoryParameter = new IdentificadorHistoryParameter();
        identificadorHistoryParameter.setIdentificador(identificador);
        identificadorHistoryParameter.setId_usuario(id_usuario);
        identificadorHistoryParameter.setTarget("NEW_VIEW");
        disposable.add(
                apiService.newViewHistoryUser(identificadorHistoryParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                               if(responsePost.getCode_response()== 200){
                                   Log.e("VIEW_HISTORY","SUCCESS");
                               }else
                                   Log.e("VIEW_HISTORY","ERROR");
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                   // mView.peticion_error();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                  //  mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void LikeHistory(String identificador, int id_usuario) {
        IdentificadorHistoryParameter identificadorHistoryParameter = new IdentificadorHistoryParameter();
        identificadorHistoryParameter.setIdentificador(identificador);
        identificadorHistoryParameter.setId_usuario(id_usuario);
        identificadorHistoryParameter.setTarget("NEW_LIKE");
        disposable.add(
                apiService.newLikeHistoryUser(identificadorHistoryParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost.getCode_response()== 200){
                                    Log.e("VIEW_HISTORY","SUCCESS");
                                }else
                                    Log.e("VIEW_HISTORY","ERROR");
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    // mView.peticion_error();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    //  mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public IdentificadoresHistoriesBean getIdentificador(String identificador) {
        return helper.searchIdentidicadorById(identificador);
    }


}
