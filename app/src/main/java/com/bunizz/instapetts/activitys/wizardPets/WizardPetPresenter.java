package com.bunizz.instapetts.activitys.wizardPets;

import android.content.Context;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class WizardPetPresenter implements WizardPetContract.Presenter {

    private WizardPetContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    int RETRY =0;

    WizardPetPresenter(WizardPetContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }

    @Override
    public void newPet(PetBean petBean) {
        disposable.add(
                apiService
                        .newPet(petBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse user) {
                                if(user.getCode_response() ==200)
                                    mView.petSaved(Integer.valueOf(user.getResult_data_extra()));
                                else
                                    mView.petSaved(Integer.valueOf(user.getResult_data_extra()));

                            }
                            @Override
                            public void onError(Throwable e) {
                               // mView.petSaved(Integer.valueOf(user.getResult_data_extra()));
                            }
                        }));
    }
}
