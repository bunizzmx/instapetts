package com.bunizz.instapetts.fragments.profile;

import android.content.Context;

import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.MainLoginContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.ResponseProfileUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileUserPresenter implements   ProfileUserContract.Presenter {

    ProfileUserContract.View mView;
    Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    private static final String TAG = MainLogin.class.getSimpleName();
    int RETRY =0;
    ProfileUserPresenter(ProfileUserContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
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
                                    mView.showInfoUser(info.getData_user(), info.getPetsUser(), info.getPostsUser());
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
                                //mView.showInfoUser(info.getData_user(),info.getPetsUser(),info.getPostsUser());
                            }
                        }));
    }
}
