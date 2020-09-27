package com.bunizz.instapetts.activitys.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.BuildConfig;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.login.LoginActivity;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdentificadoresHistoriesHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.db.helpers.SearchResentHelper;
import com.bunizz.instapetts.db.helpers.TempPostVideoHelper;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostContract;
import com.bunizz.instapetts.utils.AndroidIdentifier;
import com.bunizz.instapetts.utils.snackbar.SnackBar;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.CONST;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.FollowParameter;
import com.bunizz.instapetts.web.parameters.IdentificadorHistoryParameter;
import com.bunizz.instapetts.web.parameters.ParameterAvailableNames;
import com.bunizz.instapetts.web.responses.IdentificadoresHistoriesResponse;
import com.bunizz.instapetts.web.responses.PetsResponse;
import com.bunizz.instapetts.web.responses.ResponseNamesAvailables;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private Activity mContext;
    WebServices apiService;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    SearchResentHelper searchResentHelper;
    IdentificadoresHistoriesHelper identificadoresHistoriesHelper ;
    private StorageReference storageReference;
    PetHelper petHelper;
    int RETRY_PETS=0;
    IdsUsersHelper idsUsersHelper;
    private CompositeDisposable disposable = new CompositeDisposable();
    StorageMetadata metadata;
    TempPostVideoHelper tempPostVideoHelper;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;

    MainPresenter(MainContract.View view, Activity context) {
        this.mView = view;
        this.mContext = context;
        apiService = ApiClient.getClient(context)
                .create(WebServices.class);
        myStoryHelper = new MyStoryHelper(context);
        idsUsersHelper = new IdsUsersHelper(mContext);
        identificadoresHistoriesHelper = new IdentificadoresHistoriesHelper(mContext);
        petHelper = new PetHelper(mContext);
        db = App.getIntanceFirestore();
        storageReference = FirebaseStorage.getInstance(CONST.BUCKET_FILES_BACKUP).getReference();
        tempPostVideoHelper = new TempPostVideoHelper(mContext);
        searchResentHelper = new SearchResentHelper(mContext);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);
    }

    @Override
    public void UpdateProfile(UserBean userBean) {
        disposable.add(
                apiService
                        .newUser(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponseLogin>() {
                            @Override
                            public void onSuccess(SimpleResponseLogin user) {

                                mView.psuccessProfileUpdated();
                            }
                            @Override
                            public void onError(Throwable e) {
                                mView.noWifi();
                                Log.e("ERROR","UPDATED PROFILE");
                            }
                        }));
    }

    @Override
    public void saveMyStory(IndividualDataPetHistoryBean history) {
        String concat_stories="";
        myStoryHelper.saveMyStory(history);
        HistoriesBean historiesBean = new HistoriesBean();
        ArrayList<IndividualDataPetHistoryBean> lista = new ArrayList<>();
        lista = myStoryHelper.getMyStories();
        for(int i =0; i< lista.size();i++){
            if(i<lista.size()) {
                concat_stories +=
                        lista.get(i).getName_pet() + ";" +
                                lista.get(i).getPhoto_pet() + ";" +
                                lista.get(i).getId_pet() + ";" +
                                lista.get(i).getTumbh_video() + ";" +
                                lista.get(i).getUrl_photo() + ";" +
                                lista.get(i).getIdentificador() + ";" +
                                lista.get(i).getDate_story() +",";
            }else{
                concat_stories +=
                        lista.get(i).getName_pet() + ";" +
                                lista.get(i).getPhoto_pet() + ";" +
                                lista.get(i).getId_pet() + ";" +
                                lista.get(i).getTumbh_video() + ";" +
                                lista.get(i).getUrl_photo() + ";" +
                                lista.get(i).getIdentificador() + ";" +
                                lista.get(i).getDate_story();
            }
         }
        historiesBean.setId_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        historiesBean.setName_user(App.read(PREFERENCES.NAME_USER,"INVALID"));
        historiesBean.setPhoto_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
        historiesBean.setUltima_fecha(App.formatDateGMT(new Date()));
        historiesBean.setHistorias(concat_stories);
        if(lista.size()==1)
            historiesBean.setTarget(WEBCONSTANTS.NEW);
        else if(lista.size() <= 0)
            historiesBean.setTarget(WEBCONSTANTS.DELETE);
        else
            historiesBean.setTarget(WEBCONSTANTS.UPDATE);

        disposable.add(
                apiService
                        .newStory(historiesBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse user) {
                               // mView.psuccessProfileUpdated();
                            }
                            @Override
                            public void onError(Throwable e) {
                                mView.noWifi();
                                Log.e("ERROR","UPDATED PROFILE");
                            }
                        }));
    }

    @Override
    public void followUser(UserBean userBean) {

        Map<String,Object> followedUserData = new HashMap<>();
        followedUserData.put("url_photo_user",userBean.getPhoto_user_thumbh());
        followedUserData.put("uuid_user",userBean.getUuid());
        followedUserData.put("id_user",userBean.getId());
        followedUserData.put("name_nip_user",userBean.getName_tag());
        followedUserData.put("token",userBean.getToken());
        followedUserData.put("name_user",userBean.getName_user());


        Map<String,Object> followUserData = new HashMap<>();
        followUserData.put("url_photo_user",App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
        followUserData.put("uuid_user",App.read(PREFERENCES.UUID,"INVALID"));
        followUserData.put("id_user",App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        followUserData.put("name_nip_user",App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"));
        followUserData.put("token",userBean.getToken());
        followUserData.put("name_user",App.read(PREFERENCES.NAME_USER,"INVALID"));
        idsUsersHelper.saveId(userBean.getId());
            db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDOS)
                    .document(userBean.getName_tag())
                    .set(followedUserData)
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
        db.collection(FIRESTORE.R_FOLLOWS).document(String.valueOf(userBean.getUuid())).collection(FIRESTORE.SEGUIDORES)
                .document(App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"))
                .set(followUserData)
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
    public void unfollowUser(String id_document,int id_usuario,String name_tag) {

        FollowParameter followParameter= new FollowParameter();
        followParameter.setId_user(id_usuario);
        followParameter.setTarget(WEBCONSTANTS.UNFOLLOW);
        followParameter.setId_my_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        disposable.add(
                apiService.follows(followParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost!=null) {
                                    if(responsePost.getCode_response()==200){
                                        idsUsersHelper.deleteId(id_usuario);
                                        db.collection(FIRESTORE.R_FOLLOWS).document(id_document).collection(FIRESTORE.SEGUIDORES)
                                                .document(App.read(PREFERENCES.NAME_TAG_INSTAPETTS,"INVALID"))
                                                .delete()
                                                .addOnSuccessListener(aVoid -> {    Log.e("BORRE_FOLLOW","DE EL"); })
                                                .addOnFailureListener(e -> { })
                                                .addOnCompleteListener(task -> {
                                                    Log.e("BORRE_FOLLOW","DE EL");});
                                        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDOS)
                                                .document(name_tag)
                                                .delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.e("BORRE_FOLLOW","DE MI");
                                                })
                                                .addOnFailureListener(e -> { })
                                                .addOnCompleteListener(task -> {
                                                    Log.e("BORRE_FOLLOW","DE MI");});
                                    }
                                }  else{
                                    Toast.makeText(mContext,mContext.getString(R.string.no_posible_delete),Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext,mContext.getString(R.string.no_posible_delete),Toast.LENGTH_SHORT).show();
                            }
                        })
        );


        db.collection(FIRESTORE.R_FOLLOWS).document(id_document).collection(FIRESTORE.SEGUIDORES)
                .document(App.read(PREFERENCES.UUID,"INVALID"))
                .delete()
                .addOnSuccessListener(aVoid -> {    Log.e("BORRE_FOLLOW","DE EL"); })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE EL");});
        db.collection(FIRESTORE.R_FOLLOWS).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.SEGUIDORES)
                .document(id_document)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.e("BORRE_FOLLOW","DE MI");
                })
                .addOnFailureListener(e -> { })
                .addOnCompleteListener(task -> {    Log.e("BORRE_FOLLOW","DE MI");});
    }



    @Override
    public void downloadMyPets(UserBean userBean) {
        disposable.add(
                apiService
                        .getPets(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<PetsResponse>() {
                            @Override
                            public void onSuccess(PetsResponse pets) {
                                Log.e("PETS_DOWNLOADES","->>" + pets.getList_pets().size());
                                    mView.saveMyPets(pets.getList_pets());
                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY_PETS <3) {
                                    RETRY_PETS ++;
                                    mView.onError(1);
                                }else{
                                    mView.noWifi();
                                    RETRY_PETS =0;
                                }
                            }
                        }));
    }

    @Override
    public void have_pets() {
        ArrayList<PetBean> petBeans = new ArrayList<>();
        petBeans.addAll(petHelper.getMyPets());
        if(petBeans.size()>0)
            mView.havePetsResult(true);
        else
            mView.havePetsResult(false);
    }

    @Override
    public void update_token(UserBean userBean) {
        disposable.add(
                apiService
                        .update_token(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse pets) {
                              Log.e("TOKEN_ACTUALIZADO","SI");
                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY_PETS <3) {
                                    RETRY_PETS ++;
                                    mView.onError(1);
                                }else{
                                    RETRY_PETS =0;
                                }
                            }
                        }));
    }

    @Override
    public void logout() {
        myStoryHelper.cleanTable();
        petHelper.cleanTable();
    }

    @Override
    public void getIdentificadoresHistories() {
        IdentificadorHistoryParameter identificadorHistoryParameter  = new IdentificadorHistoryParameter();
        identificadorHistoryParameter.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        identificadorHistoryParameter.setIdentificador("-");
        identificadorHistoryParameter.setTarget("GET_IDENTIFICADORES");
        disposable.add(
                apiService
                        .getIdentificadoresHistories(identificadorHistoryParameter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<IdentificadoresHistoriesResponse>() {
                            @Override
                            public void onSuccess(IdentificadoresHistoriesResponse response) {
                                ArrayList<IdentificadoresHistoriesBean> identificadoresHistoriesBeans = new ArrayList<>();
                                ArrayList<Integer> ids = new ArrayList<>();
                                if(response.getCode_response() == 200){
                                    if(response.getIdentificadores().size()>0){
                                        Log.e("IDENTIFICADORES_MIOIS","si encontre:");
                                        identificadoresHistoriesBeans.addAll(response.getIdentificadores());
                                        for (int i=0;i<response.getIdentificadores().size();i++){
                                            identificadoresHistoriesHelper.saveIdentificador(response.getIdentificadores().get(i));
                                        }
                                    }
                                    if(response.getIds_users()!=null)
                                    {
                                        if(response.getIds_users().size()>0){
                                            ids.clear();
                                            ids.addAll(response.getIds_users());
                                            for (int i =0;i<ids.size();i++){
                                                Log.e("ME_AUTOELIMINARON"," :( ->" + ids.get(i));
                                                idsUsersHelper.deleteId(ids.get(i));
                                            }
                                            FollowParameter followParameter= new FollowParameter();
                                            followParameter.setId_user(0);
                                            followParameter.setTarget("DELETE_DELETE_OF_FRIENDS");
                                            followParameter.setId_my_user(App.read(PREFERENCES.ID_USER_FROM_WEB,0));

                                            disposable.add(
                                                    apiService.follows(followParameter)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                                                                @Override
                                                                public void onSuccess(SimpleResponse responsePost) {
                                                                    if(responsePost!=null) {
                                                                        if(responsePost.getCode_response()==200){
                                                                            Log.e("NUMBER_POSTS","-->boroo  is eliminacioses : ");
                                                                        }
                                                                    }
                                                                }
                                                                @Override
                                                                public void onError(Throwable e) {
                                                                        Log.e("NUMBER_POSTS","-->EROR : " + e.getMessage());
                                                                }
                                                            })
                                            );
                                        }
                                    }

                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                if(RETRY_PETS <3) {
                                    RETRY_PETS ++;
                                    //mView.onError(1);
                                }else{
                                    RETRY_PETS =0;
                                }
                            }
                        }));
    }

    @Override
    public ArrayList<Integer> getIdsFolows() {
        return idsUsersHelper.getMyFriendsForPost();
    }

    @Override
    public void sendFileBackup() {
        Log.e("SEND_FILE","TRUE");
        UploadTask uploadTask;
        metadata = new StorageMetadata.Builder()
                .setContentType("text/txt")
                .build();
        String filename = App.read(PREFERENCES.UUID,"INVALID") + "_BACKUP.txt";
        final StorageReference reference = storageReference.child(filename);
        uploadTask  = reference.putFile(Uri.fromFile(new File(mContext.getFilesDir()+"/" + App.read(PREFERENCES.UUID,"INVALID")+".txt")),metadata);
        uploadTask.addOnFailureListener(exception -> {}).addOnSuccessListener(taskSnapshot -> {
        }).addOnProgressListener(taskSnapshot -> {});
        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return reference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                App.write(PREFERENCES.FECHA_BACKUP_FILE,App.formatDateSimple(new Date()));
            }
        });
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
                    }catch (Exception e){
                        Log.e("VALOR_FILE","ES UNA COMA"  + (char)bytes[i]);
                    }
                }
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public void delete_data() {
        petHelper.cleanTable();
        idsUsersHelper.cleanTable();
        myStoryHelper.cleanTable();
        searchResentHelper.cleanTable();
    }

    @Override
    public void isAdsActive() {
        DocumentReference docRef = db.collection("INFO_APP").document("INFO");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {
                            if(BuildConfig.BUILD_TYPE.equals("debug")){
                                if (!document.getString("VERSION_DEBUG").equals(BuildConfig.VERSION_NAME)) {
                                    mView.UpdateAvailable(document.getString("VERSION_DEBUG"));
                                }
                            }else{
                                if (!document.getString("VERSION").equals(BuildConfig.VERSION_NAME)) {
                                    mView.UpdateAvailable(document.getString("VERSION"));
                                }
                            }
                            mView.setActivateAds(document.getBoolean("ADS"));
                        }catch (Exception nulo){
                            Log.e("DATOS_NULOS","si");
                        }
                    } else {
                     mView.setActivateAds(false);
                    }
                } else {
                   mView.setActivateAds(false);
                }
            }
        });
    }

    @Override
    public void updateConexion() {
        UserBean userBean = new UserBean();
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        userBean.setLat(App.read(PREFERENCES.LAT,0f));
        userBean.setLon(App.read(PREFERENCES.LON,0f));
        userBean.setCp(App.read(PREFERENCES.CP,0));
        userBean.setTarget("ULTIMA_CONEXION");

        disposable.add(
                apiService.updateConexion(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost!=null) {
                                    if(responsePost.getCode_response()==200){
                                        Log.e("CONEXION_UPDATED","CORRECTO ");
                                    }else if(responsePost.getCode_response()==100){
                                        mView.login_invalid();
                                    }
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("CONEXION_UPDATED","-->EROR : " + e.getMessage());
                            }
                        })
        );
    }



    @SuppressLint("CheckResult")
    @Override
    public void sendPostVideo(PostBean postBean) {
        postBean.setId_usuario(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        postBean.setCp(App.read(PREFERENCES.CP,0));
        postBean.setName_user(App.read(PREFERENCES.NAME_USER,""));
        if(App.read(PREFERENCES.ALLOW_LOCATION_POST,true))
            postBean.setAddress(App.read(PREFERENCES.ADDRESS_USER,"INVALID"));
        postBean.setUuid(App.read(PREFERENCES.UUID,""));
        postBean.setUrl_photo_user(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,""));
        postBean.setCensored(0);
        postBean.setType_post(1);
        postBean.setDate_post(App.formatDateGMT(new Date()));
        postBean.setLikes(0);
        postBean.setSaved(false);
        postBean.setLat(App.read(PREFERENCES.LAT,0f));
        postBean.setLon(App.read(PREFERENCES.LON,0f));
        postBean.setTarget(WEBCONSTANTS.NEW);
        apiService.sendPost(postBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                    @Override
                    public void onSuccess(SimpleResponse response) {
                        tempPostVideoHelper.cleanTable();
                    }

                    @Override
                    public void onError(Throwable e) {
                        tempPostVideoHelper.cleanTable();
                    }
                });

    }

    @Override
    public void getPostVideo() {
        mView.sendPostVideoView(tempPostVideoHelper.getPostVideo());
    }

    @Override
    public void updateLocations() {
        UserBean userBean = new UserBean();
        userBean.setId(App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        userBean.setLat(App.read(PREFERENCES.LAT,0f));
        userBean.setLon(App.read(PREFERENCES.LON,0f));
        userBean.setCp(App.read(PREFERENCES.CP,0));
        userBean.setTarget("UPDATE_LOCATIONS");
        Log.e("CONEXION_UPDATED","UPDATE_LOCATIONS ");
        disposable.add(
                apiService.updateConexion(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SimpleResponse>() {
                            @Override
                            public void onSuccess(SimpleResponse responsePost) {
                                if(responsePost!=null) {
                                         App.write(PREFERENCES.LOCATION_CHANGED,false);
                                         Log.e("CONEXION_UPDATED","CORRECTO desde web ");
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.e("CONEXION_UPDATED","-->EROR : " + e.getMessage());
                            }
                        })
        );
    }

     public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mContext, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                App.write(PREFERENCES.UUID,user.getUid());
                                App.write(PREFERENCES.NAME_USER,user.getDisplayName());
                                App.write(PREFERENCES.FOTO_PROFILE_USER_THUMBH,String.valueOf(user.getPhotoUrl()));
                                App.write(PREFERENCES.FOTO_PROFILE_USER, String.valueOf(user.getPhotoUrl()));
                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(mContext, instanceIdResult -> {
                                    String token = instanceIdResult.getToken();
                                    App.write(PREFERENCES.TOKEN,token);
                                    generate_user_bean();
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mView.messageLogin("Error desconocido");
                                    }
                                });
                            } else {
                                mView.messageLogin("Error desconocido");
                            }
                        }
                )
                .addOnFailureListener(e -> {
                    if (e.getMessage().contains("network error")){
                        mView.messageLogin(mContext.getString(R.string.no_wifi));
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                mView.messageLogin(mContext.getString(R.string.completed));
            }
        })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        mView.messageLogin("Cancelaste esta accion");
                    }
                });
    }

    @Override
    public void startGoggleSignin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mContext.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            App.write(PREFERENCES.UUID, user.getUid());
                            App.write(PREFERENCES.NAME_USER,user.getDisplayName());
                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(mContext, instanceIdResult -> {
                                String token = instanceIdResult.getToken();
                                App.write(PREFERENCES.TOKEN,token);
                                generate_user_bean();
                            });
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.try_again), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void generate_user_bean(){
        UserBean userBean = new UserBean();
        userBean.setDescripcion("-");
        userBean.setIds_pets("-");
        userBean.setName_user("-");
        userBean.setLat(App.read(PREFERENCES.LAT,(float)0));
        userBean.setLon(App.read(PREFERENCES.LON,(float)0));
        userBean.setToken(App.read(PREFERENCES.TOKEN,"INVALID"));
        userBean.setTarget("LOGIN");
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        userBean.setAndroid_id(Utilities.Md5Hash(new AndroidIdentifier(mContext).generateCombinationID()));
        userBean.setPhone_user("0000000");
        userBean.setPhoto_user(App.read(PREFERENCES.FOTO_PROFILE_USER,"INVALID"));
        RegisterUser(userBean);
    }

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
                                        mView.loginInvitadoCompleted(user.getData_user());
                                    else
                                        mView.loginFirstUserInvitado(user.getData_user().getId());
                                }else{
                                 //   mView.registerError();
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                mView.noWifi();
                                //mView.registerError();
                            }
                        }));
    }


    @Override
    public void getNameAvailable(String name) {
        ParameterAvailableNames parameterAvailableNames = new ParameterAvailableNames();
        parameterAvailableNames.setName(name);
        parameterAvailableNames.setTarget("GET");
        disposable.add(
                apiService.getNamesAvailables(parameterAvailableNames)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseNamesAvailables>() {
                            @Override
                            public void onSuccess(ResponseNamesAvailables responsePost) {
                                if(responsePost.getCode_response() == 200) {
                                    if(responsePost.getAvailable() == 1){
                                        mView.showUsersAvailables(true);
                                    }else
                                        mView.showUsersAvailables(false);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {

                            }
                        })
        );
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
                                        mView.completeInfoInvitado();
                                    else
                                        mView.completeInfoInvitado();
                                }else{

                                }
                            }
                            @Override
                            public void onError(Throwable e) {

                            }
                        }));
    }


}
