package com.bunizz.instapetts.activitys.main;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean$$Parcelable;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.FIRESTORE;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.constantes.WEBCONSTANTS;
import com.bunizz.instapetts.db.helpers.FollowsHelper;
import com.bunizz.instapetts.db.helpers.IdentificadoresHistoriesHelper;
import com.bunizz.instapetts.db.helpers.IdsUsersHelper;
import com.bunizz.instapetts.db.helpers.MyStoryHelper;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.share_post.Share.SharePostContract;
import com.bunizz.instapetts.web.ApiClient;
import com.bunizz.instapetts.web.CONST;
import com.bunizz.instapetts.web.WebServices;
import com.bunizz.instapetts.web.parameters.IdentificadorHistoryParameter;
import com.bunizz.instapetts.web.responses.IdentificadoresHistoriesResponse;
import com.bunizz.instapetts.web.responses.PetsResponse;
import com.bunizz.instapetts.web.responses.SimpleResponse;
import com.bunizz.instapetts.web.responses.SimpleResponseLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private Context mContext;
    WebServices apiService;
    MyStoryHelper myStoryHelper;
    FirebaseFirestore db;
    IdentificadoresHistoriesHelper identificadoresHistoriesHelper ;
    private StorageReference storageReference;
    PetHelper petHelper;
    int RETRY_PETS=0;
    private CompositeDisposable disposable = new CompositeDisposable();
    StorageMetadata metadata;
    IdsUsersHelper idsUsersHelper;
    MainPresenter(MainContract.View view, Context context) {
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
    public void unfollowUser(String id_document) {
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
    public void favoritePet(UserBean userBean, PetBean petBean) {
        Map<String,Object> followPetData = new HashMap<>();
        followPetData.put("name_user",userBean.getName_user());
        followPetData.put("url_photo_user",userBean.getPhoto_user());
        followPetData.put("uuid_user",userBean.getUuid());
        followPetData.put("id_user",userBean.getId());
        followPetData.put("name_nip_user",userBean.getName_user());
        db.collection(FIRESTORE.R_FAVORITES).document(App.read(PREFERENCES.UUID,"INVALID")).collection(FIRESTORE.FAVORITES)
                .document(String.valueOf(userBean.getUuid()))
                .set(followPetData)
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
    public void downloadMyPets(UserBean userBean) {
        disposable.add(
                apiService
                        .getPets(userBean)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<PetsResponse>() {
                            @Override
                            public void onSuccess(PetsResponse pets) {
                                    mView.saveMyPets(pets.getList_pets());
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
                                if(response.getCode_response() == 200){
                                    if(response.getIdentificadores().size()>0){
                                        Log.e("IDENTIFICADORES_MIOIS","si encontre:");
                                        identificadoresHistoriesBeans.addAll(response.getIdentificadores());
                                        for (int i=0;i<response.getIdentificadores().size();i++){
                                            identificadoresHistoriesHelper.saveIdentificador(response.getIdentificadores().get(i));
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
}
