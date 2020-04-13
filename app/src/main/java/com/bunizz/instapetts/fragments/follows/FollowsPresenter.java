package com.bunizz.instapetts.fragments.follows;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.activitys.main.MainContract;
import com.bunizz.instapetts.beans.FollowsBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.responses.PetsResponse;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FollowsPresenter implements FollowsContract.Presenter {

    private FollowsContract.View mView;
    private Context mContext;
    WebServices apiService;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    FollowsHelper followsHelper;
    PetHelper petHelper;
    int RETRY_PETS=0;
    int RETRY =0;
    private CompositeDisposable disposable = new CompositeDisposable();

    FollowsPresenter(FollowsContract.View view, Context context) {
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
    public void getFirstFollows(String uuid) {
        Log.e("UUID-PETICION","-->"+uuid);
        ArrayList<FollowsBean> followsBeans=new ArrayList<>();
        db.collection(FIRESTORE.R_FOLLOWS).document(uuid).collection(FIRESTORE.SEGUIDOS).limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            FollowsBean follow = document.toObject(FollowsBean.class);
                            followsBeans.add(follow);
                        }

                        mView.showFirstFollows(followsBeans);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        RETRY ++;
                        if(RETRY < 3) {
                            mView.Error();
                        }else{
                            mView.noInternet();
                        }
                    }
                });
    }

    @Override
    public void nextFollows() {

    }
}
