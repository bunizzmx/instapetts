package com.bunizz.instapetts.fragments.wizardPets;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.db.helpers.SearchRazaHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.CatalogoParameters;
import com.bunizz.instapetts.web.responses.ResponseCatalogo;
import com.bunizz.instapetts.web.responses.ResponsePost;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchPetPresenter implements  SearchPetContract.Presenter {

    private SearchPetContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    SearchRazaHelper helper;
    int RETRY =0;

    SearchPetPresenter(SearchPetContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        helper = SearchRazaHelper.getInstance(context);
    }


    @Override
    public void saveRaza(RazaBean razaBean) {
        helper.saveRaza(razaBean);
    }

    @Override
    public void downloadCatalogo(int type_pet) {
        int idPet = type_pet;
        CatalogoParameters  catalogoParameters = new CatalogoParameters();
        catalogoParameters.setId_raza(idPet);
        disposable.add(
                apiService.getCatalogos(catalogoParameters)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseCatalogo>() {
                            @Override
                            public void onSuccess(ResponseCatalogo responsePost) {

                                if(responsePost!=null){
                                    if(responsePost.getCode_response()==200){
                                        mView.saveRazas(responsePost.getList_catalogo());
                                    }else{
                                        if(RETRY < 3){
                                            mView.onCatalogoError();
                                        }
                                    }
                                }else{
                                    if(RETRY < 3){
                                       mView.onCatalogoError();
                                    }
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY < 3){
                                    mView.onCatalogoError();
                                }
                            }
                        })
        );
    }

    @Override
    public void search_query(String query) {
        ArrayList<RazaBean> rasaz_filter = new ArrayList<>();
        rasaz_filter = helper.getRazas(query);
        mView.showCatalogo(rasaz_filter,query);
    }

    @Override
    public void clean_table() {
       helper.cleanTable();
    }
}
