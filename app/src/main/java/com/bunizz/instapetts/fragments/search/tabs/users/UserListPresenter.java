package com.bunizz.instapetts.fragments.search.tabs.users;

import android.content.Context;

import com.bunizz.instapetts.beans.SearcRecentBean;
import com.bunizz.instapetts.db.helpers.SearchResentHelper;
import com.bunizz.instapetts.fragments.search.tabs.pets.PetListContract;

public class UserListPresenter implements UserListContract.Presenter {


    private UserListContract.View mView;
    private Context mContext;
    SearchResentHelper searchResentHelper;

    int RETRY =0;
    UserListPresenter(UserListContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        searchResentHelper = new SearchResentHelper(mContext);
    }


    @Override
    public void saveSearch(SearcRecentBean searcRecentBean) {
        searchResentHelper.saveSearch(searcRecentBean);
    }

    @Override
    public void deleteRecent(int id) {
        searchResentHelper.deleteSearch(id,true);
        mView.deleteSucess();
    }
}
