package com.bunizz.instapetts.fragments.feed;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.managers.FeedResponse;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.reactivex.functions.Function;

public class FeedPresenter implements FeedContract.Presenter {
    private FeedContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;

    FeedPresenter(FeedContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }

    @Override
    public void get_feed() {
        disposable.add(
                apiService.fetchAllNotes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<PostBean>, List<PostBean>>() {
                            @Override
                            public List<PostBean> apply(List<PostBean> notes) throws Exception {
                                // TODO - note about sort
                                Collections.sort(notes, new Comparator<PostBean>() {
                                    @Override
                                    public int compare(PostBean n1, PostBean n2) {
                                        return 1 - 2;
                                    }
                                });
                                return notes;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<List<PostBean>>() {
                            @Override
                            public void onSuccess(List<PostBean> notes) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                //Log.e(TAG, "onError: " + e.getMessage());
                               // showError(e);
                            }
                        })
        );
    }
}
