package com.bunizz.instapetts.fragments.comentarios;

import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.CommentariosBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.db.helpers.ComentariosHelper;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.reactivex.disposables.CompositeDisposable;

public class ComentariosPresenter implements ComentariosContract.Presenter {

    private ComentariosContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    int RETRY =0;
    FirebaseFirestore db;
    ComentariosHelper comentariosHelper;

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
    }

    @Override
    public void getComentarios(int id_post) {
        ArrayList<CommentariosBean> COMMENTARIOS = new ArrayList<>();
        db.collection(FIRESTORE.COLLECTION_COMENTARIOS).document(""+id_post).collection(FIRESTORE.COLLECTION_COMENTARIOS_SUBCOLECCION)
                .limit(10)
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
}
