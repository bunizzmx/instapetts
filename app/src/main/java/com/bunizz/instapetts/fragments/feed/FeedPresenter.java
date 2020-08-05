package com.bunizz.instapetts.fragments.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.AutenticateBean;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.LikePostHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.NotificationHelper;
import com.bunizz.instapetts.db.helpers.SavedPostHelper;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.managers.FeedResponse;
import com.bunizz.instapetts.web.parameters.PostActions;
import com.bunizz.instapetts.web.parameters.PostFriendsBean;
import com.bunizz.instapetts.web.parameters.PostLikeBean;
import com.bunizz.instapetts.web.responses.ResponsePost;
import com.bunizz.instapetts.web.responses.ResponsePostRecomended;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.reactivex.functions.Function;

public class FeedPresenter implements FeedContract.Presenter {
    private FeedContract.View mView;
    private Context mContext;
    private CompositeDisposable disposable = new CompositeDisposable();
    private WebServices apiService;
    SavedPostHelper savedPostHelper ;
    LikePostHelper likePostHelper;
    MyStoryHelper myStoryHelper;
    int RETRY =0;
    FirebaseFirestore db;
    FollowsHelper followsHelper;
    NotificationHelper notificationHelper;
    IdsUsersHelper idsUsersHelper;

    FeedPresenter(FeedContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        savedPostHelper = new SavedPostHelper(this.mContext);
        likePostHelper = new LikePostHelper(this.mContext);
        myStoryHelper = new MyStoryHelper(this.mContext);
        followsHelper = new FollowsHelper(this.mContext);
        notificationHelper = new NotificationHelper(mContext);
        idsUsersHelper = new IdsUsersHelper(mContext);
        db = App.getIntanceFirestore();
    }

    @Override
    public void get_feed(boolean one_user,int id_one) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setPaginador(-1);
        if(one_user){
            postFriendsBean.setId_one(id_one);
            postFriendsBean.setTarget("ONE");
        }else{
            postFriendsBean.setTarget("MULTIPLE");
            postFriendsBean.setIds(idsUsersHelper.getMyFriendsForPost());
            postFriendsBean.setIds_h(idsUsersHelper.getMyFriendsForPost());
        }
        postFriendsBean.setPaginador(-999);
        disposable.add(
                apiService.getPosts(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePost>() {
                            @Override
                            public void onSuccess(ResponsePost responsePost) {
                                if(responsePost.getList_posts()!=null) {
                                    if(responsePost.getList_posts()!=null)
                                    Log.e("NUMBER_POSTS", "-->" + responsePost.getList_posts().size());
                                    ArrayList<PostBean> post = new ArrayList<>();
                                    for (int i =0;i<responsePost.getList_posts().size();i++){
                                        if(responsePost.getList_posts().get(i).getCensored() == 0){
                                            post.add(responsePost.getList_posts().get(i));
                                        }
                                    }
                                    for (int i = 0; i < post.size(); i++) {
                                        if (savedPostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                            post.get(i).setSaved(true);
                                        else
                                            post.get(i).setSaved(false);

                                        if (likePostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                            post.get(i).setLiked(true);
                                        else
                                            post.get(i).setLiked(false);
                                    }
                                    if(responsePost.getCode_response()==200) {
                                        if(post.size()>0)
                                            mView.show_feed(post, responsePost.getList_stories());
                                        else {
                                            Log.e("GET_FEED","RECOMENDED");
                                            geet_feed_recomended(false, App.read(PREFERENCES.ID_USER_FROM_WEB, 0));
                                        }

                                    }else{
                                        Log.e("NO_INTERNET","--> SUS AMIGOS AUN NO PUBLICAN" );
                                        mView.show_feed_recomended(post,null);
                                    }
                                }  else{
                                    RETRY ++;
                                    if(RETRY < 3) {
                                        mView.peticion_error();
                                    }else{
                                        Log.e("NO_INTERNET","--> tries alcanzados" );
                                        mView.noInternet();
                                    }
                                    }
                            }
                            @Override
                            public void onError(Throwable e) {
                                RETRY ++;
                                if(RETRY < 3) {
                                    mView.peticion_error();
                                }else{
                                    Log.e("NO_INTERNET","-->error" );
                                  mView.noInternet();
                                }
                            }
                        })
        );
    }

    @Override
    public void get_next_feed(boolean one_user, int id_one, int paginador) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setPaginador(-1);
        if(one_user){
            postFriendsBean.setId_one(id_one);
            postFriendsBean.setTarget("ONE");
        }else{
            postFriendsBean.setTarget("MULTIPLE");
            postFriendsBean.setIds(idsUsersHelper.getMyFriendsForPost());
            postFriendsBean.setIds_h(idsUsersHelper.getMyFriendsForPost());
        }
        postFriendsBean.setPaginador(paginador);
        disposable.add(
                apiService.getPosts(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePost>() {
                            @Override
                            public void onSuccess(ResponsePost responsePost) {
                                if(responsePost.getList_posts()!=null) {
                                    if(responsePost.getList_posts()!=null)
                                        Log.e("NUMBER_POSTS", "-->" + responsePost.getList_posts().size());
                                    ArrayList<PostBean> post = new ArrayList<>();
                                    for (int i =0;i<responsePost.getList_posts().size();i++){
                                        if(responsePost.getList_posts().get(i).getCensored() == 0){
                                            post.add(responsePost.getList_posts().get(i));
                                        }
                                    }
                                    for (int i = 0; i < post.size(); i++) {
                                        if (savedPostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                            post.get(i).setSaved(true);
                                        else
                                            post.get(i).setSaved(false);

                                        if (likePostHelper.searchPostById(post.get(i).getId_post_from_web()))
                                            post.get(i).setLiked(true);
                                        else
                                            post.get(i).setLiked(false);
                                    }

                                    if(responsePost.getCode_response()==200) {
                                            mView.show_next_feed(post);
                                    }else{
                                        Log.e("NO_INTERNET","--> SUS AMIGOS AUN NO PUBLICAN" );
                                        mView.show_next_feed(post);
                                    }
                                }else{

                                }
                            }
                            @Override
                            public void onError(Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void unfollowUser(String  uuid_usuario,int id_usuario) {
        db.collection(FIRESTORE.R_FOLLOWS).document(uuid_usuario).collection(FIRESTORE.SEGUIDORES)
                .document(App.read(PREFERENCES.UUID,"INVALID"))
                .delete()
                .addOnSuccessListener(aVoid -> {    Log.e("BORRE_FOLLOW","DE EL"); })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE EL");});
        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDORES)
                .document(uuid_usuario)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.e("BORRE_FOLLOW","DE MI");
                })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE MI");});

        idsUsersHelper.deleteId(id_usuario);

    }

    @Override
    public void geet_feed_recomended(boolean one_user, int id_one) {
        PostFriendsBean postFriendsBean = new PostFriendsBean();
        postFriendsBean.setId_one(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
         postFriendsBean.setTarget(WEBCONSTANTS.DISCOVER_RECOMENDED);
        disposable.add(
                apiService.getPostsRecomended(postFriendsBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponsePostRecomended>() {
                            @Override
                            public void onSuccess(ResponsePostRecomended responsePost) {
                                if(responsePost.getList_posts()!=null) {
                                    ArrayList<PostBean> post = new ArrayList<>();
                                    ArrayList<UserBean>users = new ArrayList<>();
                                    for (int i =0;i<responsePost.getList_posts().size();i++){
                                        if(responsePost.getList_posts().get(i).getCensored() == 0){
                                            post.add(responsePost.getList_posts().get(i));
                                        }
                                    }
                                    for (int i =0;i<responsePost.getList_users().size();i++){
                                        users.add(responsePost.getList_users().get(i));

                                    }

                                    mView.show_feed_recomended(post,users);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                mView.noInternet();
                            }
                        })
        );
    }

    @Override
    public void likePost(PostActions postActions) {
        boolean existsin_db = likePostHelper.saveLikePost(postActions.getId_post());
        if(!existsin_db) {
            PostLikeBean postLikeBean = new PostLikeBean();
            postLikeBean.setId_post(postActions.getId_post());
            postLikeBean.setId_user(postActions.getId_usuario());
            postLikeBean.setType_event(Integer.parseInt(postActions.getAcccion()));
            postLikeBean.setTarget("NEW_LIKE");
            disposable.add(
                    apiService.like_posts(postLikeBean)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                @Override
                                public void onSuccess(SimpleResponse responsePost) {
                                    if (responsePost != null) {
                                        if (responsePost.getCode_response() == 200) {
                                            mView.LikeSuccess();
                                            Map<String, Object> data_notification = new HashMap<>();
                                            data_notification.put("TOKEN",responsePost.getResult_data_extra());
                                            data_notification.put("ID_RECURSO",postActions.getId_post());
                                            data_notification.put("TYPE_NOTIFICATION",0);
                                            data_notification.put("NAME_REMITENTE",App.read(PREFERENCES.NAME_USER,"USER"));
                                            data_notification.put("ID_REMITENTE",App.read(PREFERENCES.ID_USER_FROM_WEB,0));
                                            data_notification.put("URL_EXTRA",postActions.getExtra());
                                            data_notification.put("FOTO_REMITENTE",App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
                                            data_notification.put("FECHA",App.formatDateGMT(new Date()));
                                            db.collection(FIRESTORE.COLLECTION_NOTIFICATIONS).document(""+postActions.getId_usuario())
                                                    .collection(FIRESTORE.COLLECTION_NOTIFICATIONS)
                                                    .document()
                                                    .set(data_notification)
                                                    .addOnFailureListener(e -> {})
                                                    .addOnCompleteListener(task -> {})
                                                    .addOnSuccessListener(aVoid -> {});


                                        } else {
                                            RETRY++;
                                            if (RETRY < 3) {
                                                mView.LikeEror();
                                            }
                                        }
                                    } else {
                                        RETRY++;
                                        if (RETRY < 3) {
                                            mView.LikeEror();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    RETRY++;
                                    if (RETRY < 3) {
                                        mView.LikeEror();
                                    } else {
                                        Log.e("NUMBER_POSTS", "-->EROR : " + e.getMessage());
                                    }

                                }
                            })
            );
        }else{
            Log.e("REGISTER_LIKE", "-->YA EXISTE NO LO ENVBIO A WEB : ");
        }
    }

    @Override
    public void saveFavorite(PostActions postActions,PostBean postBean) {
        savedPostHelper.savePost(postBean);
        Map<String,Object> data_post_saved = new HashMap<>();
        data_post_saved.put("urls_posts",postBean.getUrls_posts());
        data_post_saved.put("name_pet",postBean.getName_pet());
        data_post_saved.put("name_user",postBean.getName_user());
        data_post_saved.put("url_photo_user",postBean.getUrl_photo_user());
        data_post_saved.put("description",postBean.getDescription());
        data_post_saved.put("date_post",postBean.getDate_post());
        data_post_saved.put("uuid",postBean.getUuid());
        data_post_saved.put("id_usuario",postBean.getId_usuario());
        data_post_saved.put("id_post_from_web",postBean.getId_post_from_web());
        data_post_saved.put("saved",postBean.isSaved());
        data_post_saved.put("liked",postBean.isLiked());
        data_post_saved.put("thumb_video",postBean.getThumb_video());
        data_post_saved.put("address",postBean.getAddress());
        data_post_saved.put("aspect",postBean.getAspect());
        data_post_saved.put("can_comment",postBean.getCan_comment());
        data_post_saved.put("cp",postBean.getCp());
        data_post_saved.put("type_post",postBean.getType_post());
        data_post_saved.put("type_pet",postBean.getType_pet());
        data_post_saved.put("thumb_video",postBean.getThumb_video());
        data_post_saved.put("aspect",postBean.getAspect());
        data_post_saved.put("type_post",postBean.getType_post());
        data_post_saved.put("duracion",postBean.getDuracion());
        db.collection(FIRESTORE.R_POSTS_SAVED).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.POSTS)
                .document(String.valueOf(postBean.getId_post_from_web()))
                .set(data_post_saved)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public void deleteFavorite(int id_post) {
        savedPostHelper.deleteSavedPost(id_post);
        db.collection(FIRESTORE.R_POSTS_SAVED).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.POSTS)
                .document(""+id_post)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("DELETED_FAVORITE","ON COMPLETE");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("DELETED_FAVORITE","ON SUCCESS");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void deletePost(PostBean postBean) {
        apiService.delete_post(postBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                    @Override
                    public void onSuccess(SimpleResponse response) {
                        Log.e("POST","SUCCESS");
                     if(response!=null){
                        if(response.getCode_response() ==200) {
                            mView.deletePostError(true);
                        }
                        else
                            mView.deletePostError(false);
                     }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.deletePostError(false);
                    }
                });
    }

    @Override
    public HistoriesBean getMyStories() {
        String concat_stories="";
        HistoriesBean bean = new HistoriesBean();
        ArrayList<IndividualDataPetHistoryBean> historiesBeans = new ArrayList<>();
        historiesBeans.addAll(myStoryHelper.getMyStories());
        for(int i =0; i< historiesBeans.size();i++){
            concat_stories +=
                    historiesBeans.get(i).getName_pet()+";" +
                            historiesBeans.get(i).getPhoto_pet() +";" +
                            historiesBeans.get(i).getId_pet() +";" +
                            historiesBeans.get(i).getTumbh_video() + ";" +
                            historiesBeans.get(i).getUrl_photo() + ";" +
                            historiesBeans.get(i).getIdentificador() +";"+
                             historiesBeans.get(i).getDate_story() +",";
        }
        bean.setHistorias(concat_stories);
        bean.setName_user(App.read(PREFERENCES.NAME_USER,"INVALID"));
        bean.setPhoto_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
        bean.setId_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        return  bean;
    }

    @Override
    public void haveNotificatiosn() {
        if(notificationHelper.getNoViewsNotifications()){
            mView.showBadge(true);
        }
        else
            mView.showBadge(false);
    }
}
