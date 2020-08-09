package com.bunizz.instapetts.fragments.reports;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.ReportBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.fragments.search.SearchPetContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.ParameterSearching;
import com.bunizz.instapetts.web.responses.ResponseListReports;
import com.bunizz.instapetts.web.responses.SearchUsersResponse;
import com.bunizz.instapetts.web.responses.SimpleResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ReportPresenter implements ReportsContract.Presenter {

    private ReportsContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;

    int RETRY =0;


    ReportPresenter(ReportsContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
    }


    @Override
    public void getList() {
        UserBean user = new UserBean();
        user.setTarget(WEBCONSTANTS.GET);
        user.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        user.setIdioma(App.read(PREFERENCES.IDIOMA,"es"));
        disposable.add(
                apiService.getListReports(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseListReports>() {
                            @Override
                            public void onSuccess(ResponseListReports response) {
                                mView.showListReports(response.getList_reports());
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","--->" + e.getMessage());
                            }
                        })
        );
    }

    @Override
    public void SendReport(ReportBean reportBean) {
        disposable.add(
                apiService.senReport(reportBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse response) {
                                if(response!=null){
                                    if(response.getCode_response()==200){
                                        mView.reportSended();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("ERROR","--->" + e.getMessage());
                            }
                        })
        );
    }
}
