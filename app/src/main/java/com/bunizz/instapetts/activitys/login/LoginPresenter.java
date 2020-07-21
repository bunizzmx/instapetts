package com.bunizz.instapetts.activitys.login;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.MainLoginContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.CONST;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.HistoriesBeanResponse;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    LoginContract.View mView;
    Context mContext;
    int RETRY =0;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    private static final String TAG = MainLogin.class.getSimpleName();
    private StorageReference storageReference;
    IdsUsersHelper idsUsersHelper;
    MyStoryHelper myStoryHelper;
    int COUNTER_FRIENDS =0;
    LoginPresenter(LoginContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        storageReference = FirebaseStorage.getInstance(CONST.BUCKET_FILES_BACKUP).getReference();
        idsUsersHelper = new IdsUsersHelper(mContext);
        myStoryHelper = new MyStoryHelper(mContext);
    }
    @Override
    public void RegisterUser(UserBean userBean) {
            disposable.add(
                apiService
                        .newUser(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponseLogin>() {
                            @Override
                            public void onSuccess(SimpleResponseLogin user) {
                                if(user.getData_user()!=null) {
                                    if (user.getCode_response() == 200)
                                        mView.loginCompleted(user.getData_user());
                                    else
                                        mView.isFirstUser(user.getData_user().getId());
                                }else{
                                    mView.registerError();
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                mView.registerError();
                            }
                        }));
    }

    @Override
    public void updateUser(UserBean userBean) {
        disposable.add(
                apiService
                        .newUser(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponseLogin>() {
                            @Override
                            public void onSuccess(SimpleResponseLogin user) {
                                if(user!=null) {
                                    if (user.getCode_response() == 200)
                                        mView.UpdateFirsUserCompleted();
                                    else
                                        mView.isFirstUser(user.getData_user().getId());
                                }else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        updateUser(userBean);
                                    }else{
                                        mView.registerError();
                                    }
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    updateUser(userBean);
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    mView.registerError();
                                }
                            }
                        }));
    }

    @Override
    public void getFileBackup() {

        Log.e("LEO_ARCHIVO_DESCARGADO","----SI");
        StorageReference islandRef = storageReference.child(App.read(PREFERENCES.UUID,"INVALID") + "_BACKUP.txt");
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                idsUsersHelper.cleanTable();
                for(int i =0;i<bytes.length;i++){
                    try{
                        int id = Integer.parseInt(""+(char)bytes[i]);
                        Log.e("VALOR_FILE","NUMERO: "  + id);
                        idsUsersHelper.saveId(id);
                        COUNTER_FRIENDS ++;
                    }catch (Exception e){
                        Log.e("VALOR_FILE","ES UNA COMA"  + (char)bytes[i]);
                    }
                }
                App.write(PREFERENCES.CURRENTS_FRIENDS,COUNTER_FRIENDS);
                COUNTER_FRIENDS =0;
                mView.fileBackupDownloaded();
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mView.fileBackupDownloaded();
            }
        });
    }

    @Override
    public void getMyStories() {
        UserBean userBean = new UserBean();
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        userBean.setTarget("GET_MY");
        disposable.add(
                apiService
                        .getMyStories(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<HistoriesBeanResponse>() {
                            @Override
                            public void onSuccess(HistoriesBeanResponse user) {
                                if(user!=null) {
                                    if(user.getList_stories().size()>0) {
                                        String splitItems[] = user.getList_stories().get(0).getHistorias().split(",");
                                        String splitSubitems[] = splitItems[splitItems.length - 1].split(";");
                                        for (int i = 0; i < splitItems.length; i++) {
                                            myStoryHelper.saveMyStory(new IndividualDataPetHistoryBean(
                                                    splitSubitems[0],
                                                    splitSubitems[1],
                                                    Integer.parseInt(splitSubitems[2]),
                                                    splitSubitems[4],
                                                    splitSubitems[5],
                                                    splitSubitems[6]));
                                        }
                                        mView.HistoriesSaved();
                                    }
                                }
                                else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        updateUser(userBean);
                                    }else{
                                        mView.registerError();
                                    }
                                }

                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    updateUser(userBean);
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                    mView.registerError();
                                }
                            }
                        }));
    }
}
