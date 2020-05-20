package com.bunizz.instapetts.fragments.comentarios;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.CommentariosBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.ComentariosHelper;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.PostLikeBean;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ComentariosPresenter implements ComentariosContract.Presenter {

    private ComentariosContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    int RETRY =0;
    FirebaseFirestore db;
    ComentariosHelper comentariosHelper;
    String PAGINADOR="-";
    private DocumentSnapshot x;
    boolean is_first_pagination =false;
    ComentariosPresenter(ComentariosContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        comentariosHelper = new ComentariosHelper(this.mContext);
        db = App.getIntanceFirestore();
    }


    @Override
    public void comment(CommentariosBean commentariosBean) {
        Map<String,Object> data = new HashMap<>();
        data.put("commentario",commentariosBean.getCommentario());
        data.put("fecha_comentario",commentariosBean.getFecha_comentario());
        data.put("name_user",commentariosBean.getName_user());
        data.put("foto_user",commentariosBean.getFoto_user());
        data.put("id_user",commentariosBean.getId_user());
        data.put("id_post",commentariosBean.getId_post());
        data.put("likes",commentariosBean.getLikes());

        db.collection(FIRESTORE.COLLECTION_COMENTARIOS).document(""+commentariosBean.getId_post()).collection(FIRESTORE.COLLECTION_COMENTARIOS_SUBCOLECCION)
                .document(App.formatDateComments(new Date()))
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            PostLikeBean postLikeBean = new PostLikeBean();
            postLikeBean.setId_post(commentariosBean.getId_post());
            postLikeBean.setId_user(commentariosBean.getId_user());
            postLikeBean.setType_event(1);
            postLikeBean.setTarget("NEW_COMMENT");
            disposable.add(
                    apiService.like_posts(postLikeBean)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                @Override
                                public void onSuccess(SimpleResponse responsePost) {
                                    if (responsePost != null) {
                                        if (responsePost.getCode_response() == 200) {
                                            //mView.LikeSuccess();
                                         /*   Map<String, Object> data_notification = new HashMap<>();
                                            data_notification.put("TOKEN",responsePost.getResult_data_extra());
                                            data_notification.put("ID_RECURSO",postActions.getId_post());
                                            data_notification.put("TYPE_NOTIFICATION",0);
                                            data_notification.put("NAME_REMITENTE",App.read(PREFERENCES.NAME_USER,"USER"));
                                            data_notification.put("ID_REMITENTE",App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                                            data_notification.put("URL_EXTRA",postActions.getExtra());
                                            data_notification.put("FOTO_REMITENTE",App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
                                            db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document()
                                                    .set(data_notification)
                                                    .addOnFailureListener(e -> {})
                                                    .addOnCompleteListener(task -> {})
                                                    .addOnSuccessListener(aVoid -> {});
                                                    */

                                        } else {
                                            RETRY++;
                                            if (RETRY < 3) {
                                                //mView.LikeEror();
                                            }
                                        }
                                    } else {
                                        RETRY++;
                                        if (RETRY < 3) {
                                            //mView.LikeEror();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    RETRY++;
                                    if (RETRY < 3) {
                                        //mView.LikeEror();
                                    } else {
                                        Log.e("NUMBER_POSTS", "-->EROR : " + e.getMessage());
                                    }

                                }
                            })
            );
    }

    @Override
    public void getComentarios(int id_post) {
        ArrayList<CommentariosBean> COMMENTARIOS = new ArrayList<>();
        db.collection(FIRESTORE.COLLECTION_COMENTARIOS).document(""+id_post).collection(FIRESTORE.COLLECTION_COMENTARIOS_SUBCOLECCION)
                .limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CommentariosBean commentario = document.toObject(CommentariosBean.class);
                            commentario.setId_document(document.getId());
                            if(comentariosHelper.isLikedComment(document.getId()))
                                commentario.setIs_liked(true);
                            else
                                commentario.setIs_liked(false);
                            COMMENTARIOS.add(commentario);
                            PAGINADOR= commentario.getId_document();
                            x = document;
                        }
                        mView.showComments(COMMENTARIOS);
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

    @Override
    public void likeComment(int id_post, String id_document) {
        if(!comentariosHelper.isLikedComment(id_document)) {
            db.collection(FIRESTORE.COLLECTION_COMENTARIOS).document("" + id_post)
                    .collection(FIRESTORE.COLLECTION_COMENTARIOS_SUBCOLECCION)
                    .document(id_document)
                    .update("likes", FieldValue.increment(1))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           comentariosHelper.saveNewLikeComment(id_document);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            comentariosHelper.saveNewLikeComment(id_document);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else{
            Log.e("LIKE_COMMENT","ya no lo envio porque lo guarde");
        }
    }

    @Override
    public void loadNextComments(int id_post) {
        ArrayList<CommentariosBean> COMMENTARIOS = new ArrayList<>();
        Log.e("DOWNLOAD_MORE",":lll");
        COMMENTARIOS.clear();
        Query query;
        if (x == null) {
            Log.e("DOWNLOAD_MORE",":x es nulo");
            is_first_pagination = true;
            query = db.collection(FIRESTORE.COLLECTION_COMENTARIOS).document("" + id_post)
                    .collection(FIRESTORE.COLLECTION_COMENTARIOS_SUBCOLECCION)
                    .limit(20);
        } else {
            Log.e("DOWNLOAD_MORE",":x no es nulo");
            is_first_pagination = false;
            query = db.collection(FIRESTORE.COLLECTION_COMENTARIOS)
                    .document(""+id_post)
                    .collection(FIRESTORE.COLLECTION_COMENTARIOS_SUBCOLECCION)
                    .startAfter(x)
                    .limit(20);
        }
        query.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                CommentariosBean coment = document.toObject(CommentariosBean.class);
                COMMENTARIOS.add(coment);
                x = document;
            }
            if (is_first_pagination) {
                Log.e("DOWNLOAD_MORE","is first");
                mView.showNextComments(COMMENTARIOS);
            }
            else {
                Log.e("DOWNLOAD_MORE","more more");
                mView.showNextComments(COMMENTARIOS);
            }
        }).addOnFailureListener(e ->{
            mView.showNextComments(COMMENTARIOS);
            Log.e("DOWNLOAD_MORE",":ccccccccc");});
    }
}
