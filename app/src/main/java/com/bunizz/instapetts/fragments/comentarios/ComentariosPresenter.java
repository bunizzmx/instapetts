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
import com.bunizz.instapetts.web.parameters.PlayVideoParameters;
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
    public void comment(CommentariosBean commentariosBean,int type_resource_to_comment) {
        Map<String,Object> data = new HashMap<>();
        data.put("commentario",commentariosBean.getCommentario());
        data.put("fecha_comentario",commentariosBean.getFecha_comentario());
        data.put("name_user",commentariosBean.getName_user());
        data.put("foto_user",commentariosBean.getFoto_user());
        data.put("id_user",commentariosBean.getId_user());
        data.put("id_post",commentariosBean.getId_post());
        data.put("likes",commentariosBean.getLikes());
        if(type_resource_to_comment == 1){
            db.collection(FIRESTORE.COLLECTION_COMENTARIOS).document("-"+commentariosBean.getId_post()+"-").collection(FIRESTORE.COLLECTION_COMENTARIOS_SUBCOLECCION)
                    .document(App.formatDateComments(new Date()))
                    .set(data)
                    .addOnSuccessListener(aVoid -> {})
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) { } })
                    .addOnFailureListener(e -> {});
            PostLikeBean postLikeBean = new PostLikeBean();
            postLikeBean.setId_post(commentariosBean.getId_post());
            postLikeBean.setId_user(commentariosBean.getId_user());
            postLikeBean.setType_event(1);

            if(commentariosBean.getHelps_post() == 0)
                postLikeBean.setTarget("NEW_COMMENT");
            else
                postLikeBean.setTarget("NEW_COMMENT_HELPS");

            disposable.add(
                    apiService.like_posts(postLikeBean)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                @Override
                                public void onSuccess(SimpleResponse responsePost) {
                                    if (responsePost != null) {
                                        if (responsePost.getCode_response() == 200) {
                                            if(commentariosBean.getId_destinatario() !=  App.read(PREFERENCES.ID_USER_FROM_WEB,0)){
                                                Log.e("ESTATUS_NOTIFICACION","SE VA A ENVIAR A : " + commentariosBean.getId_user());
                                                Map<String, Object> data_notification = new HashMap<>();
                                                data_notification.put("TOKEN",responsePost.getResult_data_extra());
                                                data_notification.put("ID_RECURSO",commentariosBean.getId_post());
                                                data_notification.put("TYPE_NOTIFICATION",3);
                                                data_notification.put("NAME_REMITENTE",App.read(PREFERENCES.NAME_USER,"INVALID"));
                                                data_notification.put("ID_REMITENTE",App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                                                data_notification.put("URL_EXTRA",commentariosBean.getCommentario());
                                                data_notification.put("FOTO_REMITENTE",App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
                                                data_notification.put("FECHA",App.formatDateGMT(new Date()));
                                                App.getInstance().sendNotification(data_notification,commentariosBean.getId_destinatario());
                                            }
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
        else{
            db.collection(FIRESTORE.COLLECTION_COMENTARIOS_INSTAPETTSTV).document("-"+commentariosBean.getId_post()+"-").collection(FIRESTORE.COLLECTION_COMENTARIOS_INSTAPETTSTV)
                    .document(App.formatDateComments(new Date()))
                    .set(data)
                    .addOnSuccessListener(aVoid -> {})
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            PlayVideoParameters playVideoParameters = new PlayVideoParameters();
                            playVideoParameters.setTarget("COMMENT");
                            playVideoParameters.setId_video(commentariosBean.getId_post());
                            disposable.add(
                                    apiService
                                            .actionsPlayVideos(playVideoParameters)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                                @Override
                                                public void onSuccess(SimpleResponse user) {
                                                    if(user!=null) {
                                                        if (user.getCode_response() == 200)
                                                            Log.e("LIKE_SUCCESS",":)");
                                                        else
                                                            Log.e("LIKE_SUCCESS",":(");
                                                    }else{

                                                    }
                                                }
                                                @Override
                                                public void onError(Throwable e) {

                                                }
                                            }));
                        } })
                    .addOnFailureListener(e -> {});
        }


    }

    @Override
    public void getComentarios(int id_post,int type_resoruce_to_comment) {
        ArrayList<CommentariosBean> COMMENTARIOS = new ArrayList<>();
        String COLLECCTION_REQUEST ="";

        if(type_resoruce_to_comment == 1)
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS;
        else
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS_INSTAPETTSTV;

        db.collection(COLLECCTION_REQUEST).document("-"+id_post+"-").collection(COLLECCTION_REQUEST)
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
    public void deleteComment(int id_post,String document,int type_resource_to_comment) {

        String COLLECCTION_REQUEST ="";

        if(type_resource_to_comment == 1)
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS;
        else
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS_INSTAPETTSTV;



        if((""+id_post)!=null && document!=null) {
            db.collection(COLLECCTION_REQUEST).document("-" + id_post + "-").collection(COLLECCTION_REQUEST)
                    .document(document)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnSuccessListener(aVoid -> {
                        if(type_resource_to_comment ==1){
                            PostLikeBean postLikeBean = new PostLikeBean();
                            postLikeBean.setId_post(id_post);
                            postLikeBean.setTarget("DELETE_COMMENT");
                            disposable.add(
                                    apiService.like_posts(postLikeBean)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                                @Override
                                                public void onSuccess(SimpleResponse responsePost) {
                                                    if (responsePost != null) {
                                                        if (responsePost.getCode_response() == 200) {
                                                            Log.e("DESCONTAR_COMENTARIO", "COMENTARIO DESCONTADO ");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.e("NUMBER_POSTS", "-->EROR : " + e.getMessage());
                                                }
                                            })
                            );
                        }else{
                            PlayVideoParameters playVideoParameters = new PlayVideoParameters();
                            playVideoParameters.setTarget("DELETE");
                            playVideoParameters.setId_video(id_post);
                            disposable.add(
                                    apiService
                                            .actionsPlayVideos(playVideoParameters)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                                @Override
                                                public void onSuccess(SimpleResponse user) {
                                                    if(user!=null) {
                                                        if (user.getCode_response() == 200)
                                                            Log.e("LIKE_SUCCESS",":)");
                                                        else
                                                            Log.e("LIKE_SUCCESS",":(");
                                                    }else{

                                                    }
                                                }
                                                @Override
                                                public void onError(Throwable e) {

                                                }
                                            }));
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else{
            Log.e("ID_DOCUM,ENT","NULO");
        }
    }

    @Override
    public void likeComment(int id_post, String id_document,int type_resource_to_comment) {

        String COLLECCTION_REQUEST ="";

        if(type_resource_to_comment == 1)
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS;
        else
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS_INSTAPETTSTV;


        if(!comentariosHelper.isLikedComment(id_document)) {
            db.collection(COLLECCTION_REQUEST).document("-" + id_post+"-")
                    .collection(COLLECCTION_REQUEST)
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
    public void loadNextComments(int id_post,int type_resoruce_to_comment) {
        ArrayList<CommentariosBean> COMMENTARIOS = new ArrayList<>();
        COMMENTARIOS.clear();
        String COLLECCTION_REQUEST ="";

        if(type_resoruce_to_comment == 1)
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS;
        else
            COLLECCTION_REQUEST =FIRESTORE.COLLECTION_COMENTARIOS_INSTAPETTSTV;

        Query query;
        if (x == null) {
            is_first_pagination = true;
            query = db.collection(COLLECCTION_REQUEST).document("-" + id_post+"-")
                    .collection(COLLECCTION_REQUEST)
                    .limit(20);
        } else {
            is_first_pagination = false;
            query = db.collection(COLLECCTION_REQUEST)
                    .document("-"+id_post+"-")
                    .collection(COLLECCTION_REQUEST)
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
