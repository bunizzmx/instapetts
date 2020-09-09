package com.bunizz.instapetts.fragments.tips;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.fragments.profile.adapters.PetsPropietaryAdapter;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponseTips;
import com.google.firebase.firestore.DocumentSnapshot;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TipsPresenter implements TipsContract.Presenter {

    private TipsContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    PetHelper petHelper;
    int RETRY =0;
    public TipsPresenter(TipsContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        petHelper = new PetHelper(mContext);
    }

    @Override
    public void getTips() {
        AutenticateBean autenticateBean = new AutenticateBean();
        autenticateBean.setName_user("DEMO");
        autenticateBean.setToken("xxxx");
        autenticateBean.setPaginador(-999);
        autenticateBean.setIdioma(App.read(PREFERENCES.IDIOMA,"es"));
        disposable.add(
                apiService.getTips(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseTips>() {
                            @Override
                            public void onSuccess(ResponseTips responsePost) {
                                if(responsePost.getCode_response()==200) {
                                    mView.showTips(responsePost.getList_tips(),responsePost.getHelps());
                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.peticionError();
                                    }else{
                                        Log.e("NO_INTERNET","-->request" );
                                        mView.noInternet();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.peticionError();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void getMoreTips(int paginador) {
        AutenticateBean autenticateBean = new AutenticateBean();
        autenticateBean.setName_user("DEMO");
        autenticateBean.setToken("xxxx");
        autenticateBean.setPaginador(paginador);
        autenticateBean.setIdioma(App.read(PREFERENCES.IDIOMA,"es"));
        disposable.add(
                apiService.getTips(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseTips>() {
                            @Override
                            public void onSuccess(ResponseTips responsePost) {
                                if(responsePost.getCode_response()==200) {
                                    mView.showMoreTips(responsePost.getList_tips());
                                }else{
                                    mView.showMoreTips(responsePost.getList_tips());
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                             }
                        })
        );
    }

    @Override
    public void getTipsForMyPets() {
        AutenticateBean autenticateBean = new AutenticateBean();
        autenticateBean.setName_user(App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"));
        autenticateBean.setToken("xxxx");
        autenticateBean.setPaginador(-999);
        autenticateBean.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        autenticateBean.setTarget("FOR_MY_PETS");
        autenticateBean.setIdioma(App.read(PREFERENCES.IDIOMA,"es"));
        disposable.add(
                apiService.getTips(autenticateBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseTips>() {
                            @Override
                            public void onSuccess(ResponseTips responsePost) {
                                if(responsePost.getCode_response()==200) {
                                    mView.showTips(responsePost.getList_tips(),responsePost.getHelps());
                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.peticionError();
                                    }else{
                                        Log.e("NO_INTERNET","-->request" );
                                        mView.noInternet();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.peticionError();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void havePets() {
        if(petHelper.getMyPets().size()>0){
            mView.have_pets(true);
        }else{
            mView.have_pets(false);
        }
    }
}
