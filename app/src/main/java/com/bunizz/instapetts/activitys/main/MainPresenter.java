package com.bunizz.instapetts.activitys.main;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.SimpleResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private Context mContext;
    WebServices apiService;
    MyStoryHelper myStoryHelper;
    private CompositeDisposable disposable = new CompositeDisposable();

    MainPresenter(MainContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        myStoryHelper = new MyStoryHelper(context);
    }

    @Override
    public void UpdateProfile(UserBean userBean) {
        disposable.add(
                apiService
                        .newUser(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse user) {
                                mView.psuccessProfileUpdated();
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","UPDATED PROFILE");
                            }
                        }));
    }

    @Override
    public void saveMyStory(HistoriesBean historiesBean) {
        myStoryHelper.saveMyStory(historiesBean);
    }
}
