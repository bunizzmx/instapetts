package com.bunizz.instapetts.fragments.info;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.LikesTipsHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.tips.detail.DetailContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.EventsPetsBean;
import com.bunizz.instapetts.web.parameters.EventsTipsBean;
import com.bunizz.instapetts.web.responses.SimpleResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class InfoPetPresenter implements  InfoPetContract.Presenter {

    private InfoPetContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    int RETRY =0;
    PetHelper helper;


    InfoPetPresenter(InfoPetContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        helper = new PetHelper( this.mContext);
    }

    @Override
    public void delete(int id_pet) {
        helper.deletePet(id_pet);
        EventsPetsBean eventsPetsBean = new EventsPetsBean();
        eventsPetsBean.setTarget(WEBCONSTANTS.DELETE);
        eventsPetsBean.setId_pet(id_pet);
        eventsPetsBean.setId_propietary(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        disposable.add(
                apiService.deletePet(eventsPetsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost.getCode_response()==200) {
                                      mView.petDeleted();
                                }else{ }
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                        })
        );
    }

    @Override
    public void ratePet(int id_pet, int stars) {
        EventsPetsBean eventsPetsBean = new EventsPetsBean();
        eventsPetsBean.setTarget("RATE");
        eventsPetsBean.setId_pet(id_pet);
        eventsPetsBean.setStars(stars);
        disposable.add(
                apiService.ratePet(eventsPetsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost.getCode_response()==200) {

                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        //mView.peticionError();
                                    }else{
                                        Log.e("NO_INTERNET","-->request" );
                                        // mView.noInternet();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    //mView.peticionError();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    //mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void updatePet(PetBean petBean) {
        petBean.setTarget(WEBCONSTANTS.UPDATE);
        disposable.add(
                apiService.updatePet(petBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost.getCode_response()==200) {
                                    mView.petUpdated();
                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        //mView.peticionError();
                                    }else{
                                        Log.e("NO_INTERNET","-->request" );
                                        // mView.noInternet();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    //mView.peticionError();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    //mView.noInternet();
                                }
                            }
                        })
        );
    }
}
