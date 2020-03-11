package com.bunizz.instapetts.fragments.tips;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseTips;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TipsPresenter implements TipsContract.Presenter {

    private TipsContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;

    TipsPresenter(TipsContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }

    @Override
    public void getTips() {
        AutenticateBean autenticateBean = new AutenticateBean();
        autenticateBean.setName_user("DEMO");
        autenticateBean.setToken("xxxx");
        disposable.add(
                apiService.getTips(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseTips>() {
                            @Override
                            public void onSuccess(ResponseTips responsePost) {
                                mView.showTips(responsePost.getList_tips());
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                            }
                        })
        );
    }
}
