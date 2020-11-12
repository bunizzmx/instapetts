package com.bunizz.instapetts.fragments.retos_eventos;

import android.content.Context;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.EventBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.RankingBean;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponseDetailEventPolitic;
import com.bunizz.instapetts.web.responses.ResponseEventos;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseRankings;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailEventosPresenter implements  DetailEventosContract.Presenter{


    DetailEventosContract.View mView;
        Context mContext;
        private CompositeDisposable disposable = new CompositeDisposable();
        private WebServices apiService;
        private static final String TAG = MainLogin.class.getSimpleName();
        int RETRY =0;
        SavedPostHelper savedPostHelper;
        FirebaseFirestore db;
        LikePostHelper likePostHelper;
    DetailEventosPresenter(DetailEventosContract.View view, Context context) {
            this.mView = view;
            this.mContext = context;
            apiService = ApiClient.getClient(context)
                    .create(WebServices.class);
        }

    @Override
    public void getPoliticasDetailReto(int id_politica) {
        AutenticateBean postFriendsBean = new AutenticateBean();
        postFriendsBean.setTarget(WEBCONSTANTS.POLITICAS_EVENT);
        postFriendsBean.setId_resource(""+id_politica);
        disposable.add(
                apiService.getPoliticEvent(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseDetailEventPolitic>() {
                            @Override
                            public void onSuccess(ResponseDetailEventPolitic responsePost) {
                                if(responsePost.getPolitica()!=null) {
                                    mView.showPolitica(responsePost.getPolitica());
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                        })
        );
    }
}
