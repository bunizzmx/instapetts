package com.bunizz.instapetts.fragments.search.tabs.pets;

import android.content.Context;

import com.bunizz.instapetts.beans.SearcRecentBean;
import com.bunizz.instapetts.db.helpers.SearchResentHelper;
import com.bunizz.instapetts.fragments.search.SearchPetContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;

import io.reactivex.disposables.CompositeDisposable;

public class PetListPresenter implements PetListContract.Presenter {


    private PetListContract.View mView;
    private Context mContext;
    SearchResentHelper searchResentHelper;

    int RETRY =0;
    PetListPresenter(PetListContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        searchResentHelper = new SearchResentHelper(mContext);
    }


    @Override
    public void saveSearch(SearcRecentBean searcRecentBean) {
        searchResentHelper.saveSearch(searcRecentBean);
    }
}
