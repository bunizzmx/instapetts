package com.bunizz.instapetts.fragments.search;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.ParameterSearching;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.SearchPetsResponse;
import com.bunizz.instapetts.web.responses.SearchUsersResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchPetPresenter implements SearchPetContract.Presenter {


private SearchPetContract.View mView;
private Context mContext;
private CompositeDisposable disposable = new CompositeDisposable();
private WebServices apiService;

        int RETRY =0;


    SearchPetPresenter(SearchPetContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
        .create(WebServices.class);
        }


    @Override
    public void searchusers(String word_search) {
        ParameterSearching parameterSearching = new ParameterSearching();
        String word;
        parameterSearching.setWord(word_search);
        disposable.add(
                apiService.searchUser(parameterSearching)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SearchUsersResponse>() {
                            @Override
                            public void onSuccess(SearchUsersResponse response) {
                              mView.shoUsersResults(response.getList_users());
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","--->" + e.getMessage());
                            }
                        })
        );
    }

    @Override
    public void searchPets(String word_search) {
        ParameterSearching parameterSearching = new ParameterSearching();
        String word;
        parameterSearching.setWord(word_search);
        disposable.add(
                apiService.searchPets(parameterSearching)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SearchPetsResponse>() {
                            @Override
                            public void onSuccess(SearchPetsResponse response) {
                                mView.shoPetsResults(response.getList_pets());
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","--->" + e.getMessage());
                            }
                        })
        );
    }

    @Override
    public void searchNewUsers() {
        ParameterSearching parameterSearching = new ParameterSearching();
        parameterSearching.setWord("ALL_USERS");
        disposable.add(
                apiService.searchUser(parameterSearching)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SearchUsersResponse>() {
                            @Override
                            public void onSuccess(SearchUsersResponse response) {
                                mView.shoUsersResults(response.getList_users());
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","--->" + e.getMessage());
                            }
                        })
        );
    }
}
