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
import com.bunizz.instapetts.fragments.post.PostGaleryContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponseEventos;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseRankings;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EventosPresenter  implements  EventosContract.Presenter{


    EventosContract.View mView;
        Context mContext;
        private CompositeDisposable disposable = new CompositeDisposable();
        private WebServices apiService;
        private static final String TAG = MainLogin.class.getSimpleName();
        int RETRY =0;
        SavedPostHelper savedPostHelper;
        FirebaseFirestore db;
        LikePostHelper likePostHelper;
    EventosPresenter(EventosContract.View view, Context context) {
            this.mView = view;
            this.mContext = context;
            apiService = ApiClient.getClient(context)
                    .create(WebServices.class);
            savedPostHelper = new SavedPostHelper(mContext);
            db = App.getIntanceFirestore();
            likePostHelper = new LikePostHelper(mContext);
        }

    @Override
    public void getEventosPosts(int type_Search, int paginador, int id_usuario, int filter) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setPaginador(paginador);
        postFriendsBean.setId_one(id_usuario);
        postFriendsBean.setFilter(filter);
        switch (0){
            case 0:
                postFriendsBean.setTarget(WEBCONSTANTS.DISCOVER);
                break;
            case 1:
                postFriendsBean.setTarget(WEBCONSTANTS.MORE_VIEWS);
                break;
            case 2:
                postFriendsBean.setTarget(WEBCONSTANTS.RECENT);
                break;
            case 3:
                postFriendsBean.setTarget(WEBCONSTANTS.ONE);
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
                                    ArrayList<PostBean> post = new ArrayList<>();
                                    for (int i =0;i<responsePost.getList_posts().size();i++){
                                        if(responsePost.getList_posts().get(i).getCensored() == 0){
                                            post.add(responsePost.getList_posts().get(i));
                                        }
                                    }
                                    mView.showEventosPost(post);
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        //mView.peticionError();
                                    }else{
                                        // mView.noInternet();
                                    }

                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    // mView.peticionError();
                                }else{
                                    // mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void getEventos(int type_Search, int paginador, int id_usuario, int filter) {
        AutenticateBean postFriendsBean = new AutenticateBean();
        postFriendsBean.setPaginador(paginador);
        postFriendsBean.setTarget(WEBCONSTANTS.GET);
        disposable.add(
                apiService.getEventos(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseEventos>() {
                            @Override
                            public void onSuccess(ResponseEventos responsePost) {
                                if(responsePost.getList_Eventos()!=null) {
                                    ArrayList<EventBean> post = new ArrayList<>();
                                    post.addAll(responsePost.getList_Eventos());
                                    mView.showEventos(post);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                        })
        );
    }

    @Override
    public void getCurrentRanking() {
        AutenticateBean postFriendsBean = new AutenticateBean();
        postFriendsBean.setTarget(WEBCONSTANTS.RANKING);
        disposable.add(
                apiService.getRanking(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseRankings>() {
                            @Override
                            public void onSuccess(ResponseRankings responsePost) {
                                if(responsePost.getList_rankings()!=null) {
                                    ArrayList<RankingBean> rankings = new ArrayList<>();
                                    rankings.addAll(responsePost.getList_rankings());
                                    mView.showRanking(rankings);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                        })
        );
    }
}
