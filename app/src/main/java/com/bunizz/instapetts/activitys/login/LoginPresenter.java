package com.bunizz.instapetts.activitys.login;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.MainLoginContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    LoginContract.View mView;
    Context mContext;
    int RETRY =0;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    private static final String TAG = MainLogin.class.getSimpleName();

    LoginPresenter(LoginContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }
    @Override
    public void RegisterUser(UserBean userBean) {
            disposable.add(
                apiService
                        .newUser(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponseLogin>() {
                            @Override
                            public void onSuccess(SimpleResponseLogin user) {
                                if(user.getData_user()!=null) {
                                    if (user.getCode_response() == 200)
                                        mView.loginCompleted(user.getData_user());
                                    else
                                        mView.isFirstUser(user.getData_user().getId());
                                }else{
                                    mView.registerError();
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                mView.registerError();
                            }
                        }));
    }

    @Override
    public void updateUser(UserBean userBean) {
        disposable.add(
                apiService
                        .newUser(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponseLogin>() {
                            @Override
                            public void onSuccess(SimpleResponseLogin user) {
                                if(user!=null) {
                                    if (user.getCode_response() == 200)
                                        mView.UpdateFirsUserCompleted();
                                    else
                                        mView.isFirstUser(user.getData_user().getId());
                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        updateUser(userBean);
                                    }else{
                                        mView.registerError();
                                    }
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    updateUser(userBean);
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    mView.registerError();
                                }
                            }
                        }));
    }
}
