package com.bunizz.instapetts.fragments.login;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class MainLoginPresenter implements  MainLoginContract.Presenter {
    MainLoginContract.View mView;
    Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    private static final String TAG = MainLogin.class.getSimpleName();

    MainLoginPresenter(MainLoginContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }

    @Override
    public void registerUser(PetBean user) {
        /*disposable.add(
                apiService
                        .register(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<PetBean>() {
                            @Override
                            public void onSuccess(PetBean user) {
                                mView.registerCompleted("");
                            }
                            @Override
                            public void onError(Throwable e) {
                                mView.loginError();
                            }
                        }));*/
    }

    @Override
    public void loginUser() {

    }

    @Override
    public void getDataUser() {

    }


    private void showError(Throwable e) {
        String message = "";
        try {
            if (e instanceof IOException) {
                message = "No internet connection!";
            } else if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject jObj = new JSONObject(errorBody);

                message = jObj.getString("error");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (TextUtils.isEmpty(message)) {
            message = "Unknown error occurred! Check LogCat.";
        }
    }
}
