package com.bunizz.instapetts.fragments.side_menus_activities.postsSaved;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PostPublicsSavedPresenter implements   PostPublicsSavedContract.Presenter {

    PostPublicsSavedContract.View mView;
    Context mContext;
    FirebaseFirestore db;
    private CompositeDisposable disposable = new CompositeDisposable();

    private static final String TAG = MainLogin.class.getSimpleName();

    PostPublicsSavedPresenter(PostPublicsSavedContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        db = App.getIntanceFirestore();
    }

    @Override
    public void getPostPublics() {
         ArrayList<PostBean> POST = new ArrayList<>();
        db.collection(FIRESTORE.R_POSTS_SAVED).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.POSTS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PostBean post = document.toObject(PostBean.class);
                            POST.add(post);
                        }
                        Log.e("POST_FIRESTORE","-->" + POST.size());
                        mView.showPosts(POST);

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
