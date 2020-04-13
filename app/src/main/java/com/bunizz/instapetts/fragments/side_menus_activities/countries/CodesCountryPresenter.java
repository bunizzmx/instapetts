package com.bunizz.instapetts.fragments.side_menus_activities.countries;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.follows.FollowsContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.ResponseCodesCountries;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CodesCountryPresenter implements CodesCountryContract.Presenter {

    private CodesCountryContract.View mView;
    private Context mContext;
    WebServices apiService;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    FollowsHelper followsHelper;
    PetHelper petHelper;
    int RETRY_PETS=0;
    int RETRY =0;
    private CompositeDisposable disposable = new CompositeDisposable();

    CodesCountryPresenter(CodesCountryContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        myStoryHelper = new MyStoryHelper(context);
        followsHelper = new FollowsHelper(mContext);
        petHelper = new PetHelper(mContext);
        db = App.getIntanceFirestore();
    }


    @Override
    public void getCodesCountry() {
        UserBean userBean = new UserBean();
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        disposable.add(
                apiService.getCodesCountry(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseCodesCountries>() {
                            @Override
                            public void onSuccess(ResponseCodesCountries responsePost) {
                                if(responsePost!=null) {
                                    if(responsePost.getList_codes()!=null){
                                        if(responsePost.getCode_response()==200){
                                            mView.showCodesCountry(responsePost.getList_codes());
                                        }else{
                                                RETRY ++;
                                                if(RETRY < 3) {

                                                    mView.Error();
                                                }else{
                                                    Log.e("CODES_EXISTENTES","--> aaaa");
                                                    mView.noInternet();
                                            }
                                        }
                                    }

                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.Error();
                                }else{
                                    Log.e("CODES_EXISTENTES","--> bbb");
                                   mView.noInternet();
                                }

                            }
                        })
        );
    }
}
