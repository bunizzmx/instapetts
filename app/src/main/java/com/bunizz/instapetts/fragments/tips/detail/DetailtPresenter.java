package com.bunizz.instapetts.fragments.tips.detail;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.db.helpers.LikesTipsHelper;
import com.bunizz.instapetts.fragments.tips.TipsContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.EventsTipsBean;
import com.bunizz.instapetts.web.responses.ResponseTips;
import com.bunizz.instapetts.web.responses.SimpleResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailtPresenter implements DetailContract.Presenter {

    private DetailContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    int RETRY =0;
    LikesTipsHelper helper;
    DetailtPresenter(DetailContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        helper = new LikesTipsHelper( this.mContext);
    }

    @Override
    public void like(int id_tip) {
        EventsTipsBean autenticateBean = new EventsTipsBean();
        autenticateBean.setTarget("LIKE");
        autenticateBean.setId_tip(id_tip);
        helper.saveLikeTip(id_tip);
        disposable.add(
                apiService.newLikeTip(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost.getCode_response()==200) {

                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        //mView.peticionError();
                                    }else{
                                        Log.e("NO_INTERNET","-->request" );
                                       // mView.noInternet();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    //mView.peticionError();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    //mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void view(int id_tip) {
        EventsTipsBean autenticateBean = new EventsTipsBean();
        autenticateBean.setTarget("VIEW");
        autenticateBean.setId_tip(id_tip);
        disposable.add(
                apiService.newViewTip(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost.getCode_response()==200) {

                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        //mView.peticionError();
                                    }else{
                                        Log.e("NO_INTERNET","-->request" );
                                        // mView.noInternet();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    //mView.peticionError();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    //mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public boolean is_liked(int id_tip) {
       return  helper.searchTipsById(id_tip);
    }
}
