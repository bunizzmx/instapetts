package com.bunizz.instapetts.activitys.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.Splash;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.share_post.ShareActivity;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.Utilities;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.first_user.FragmentFirstUser;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.fragments.profile.FragmentEditProfileUser;
import com.bunizz.instapetts.fragments.profile.FragmentProfileUserPet;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.login_listener;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.services.DownloadsService;
import com.bunizz.instapetts.services.ImageService;
import com.bunizz.instapetts.utils.AndroidIdentifier;
import com.bunizz.instapetts.utils.dilogs.DialogLoanding;
import com.bunizz.instapetts.web.CONST;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_LOGUEDD;

public class LoginActivity extends AppCompatActivity implements change_instance, login_listener, uploads,LoginContract.View {

    private Stack<FragmentElement> stack_login;
    private Stack<FragmentElement> stack_sigin;
    private Stack<FragmentElement> stack_main_login;
    private Stack<FragmentElement> stack_first_user;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;
    LoginPresenter presenter;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    static final int NEW_PHOTO_UPLOADED= 3;
    RxPermissions rxPermissions ;
    DialogLoanding dialogLoanding ;
    Intent intent_service ;
    FirebaseUser user ;
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        changeStatusBarColor(R.color.white);
        intent_service = new Intent(this, DownloadsService.class);
        dialogLoanding = new DialogLoanding(this);
        stack_sigin = new Stack<>();
        stack_main_login = new Stack<>();
        stack_login = new Stack<>();
        stack_first_user = new Stack<>();
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        rxPermissions = new RxPermissions(this);
        presenter = new LoginPresenter(this,this);
        setupFirstFragment();
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    }
    private void setupFirstFragment() {
        if(App.read(PREFERENCES.IS_FIRST_USER,false)){
            mCurrentFragment = new FragmentElement<>(null, FragmentFirstUser.newInstance(), FragmentElement.INSTANCE_NEW_USER_CONFIG, true);
            config_first_user(mCurrentFragment);
            permision_location();
        }else{
            mCurrentFragment = new FragmentElement<>(null, MainLogin.newInstance(), FragmentElement.INSTANCE_MAIN_LOGIN, true);
            change_main(mCurrentFragment);
            permision_location();
        }

    }

    private void change_login(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_login.size()<=0){stack_login.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_main(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_main_login.size()<=0){stack_main_login.push(mCurrentFragment);}
        }
        inflateFragment();
    }

    private void config_first_user(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_first_user.size()<=0){stack_first_user.push(mCurrentFragment);}
        }
        inflateFragment();
    }

    private void change_sigin(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_sigin.size()<=0){stack_sigin.push(mCurrentFragment);}
        }
        inflateFragment();
    }

    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_LOGIN) {
            if (stack_login.size() == 0) {
                change_login(new FragmentElement<>("", FragmentLogin.newInstance(), FragmentElement.INSTANCE_LOGIN));
            } else {
                change_login(stack_login.pop());
            }
        }else if(intanceType == FragmentElement.INSTANCE_SIGIN) {
            if (stack_sigin.size() == 0) {
                change_sigin(new FragmentElement<>("", FragmentSigin.newInstance(), FragmentElement.INSTANCE_SIGIN));
            } else {
                change_sigin(stack_sigin.pop());
            }

        }
        else if(intanceType == FragmentElement.INSTANCE_MAIN_LOGIN) {
            if (stack_main_login.size() == 0) {
                change_main(new FragmentElement<>("", MainLogin.newInstance(), FragmentElement.INSTANCE_MAIN_LOGIN));
            } else {
                change_main(stack_main_login.pop());
            }
        }

        else if(intanceType == FragmentElement.INSTANCE_NEW_USER_CONFIG) {
            if (stack_first_user.size() == 0) {
                config_first_user(new FragmentElement<>("", FragmentFirstUser.newInstance(), FragmentElement.INSTANCE_NEW_USER_CONFIG));
            } else {
                config_first_user(stack_first_user.pop());
            }
        }



    }

    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(mOldFragment!=null) {
                if (mCurrentFragment.getFragment().isAdded()) {
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_right, R.anim.exit_left)
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .show(mCurrentFragment.getFragment()).commit();
                } else {
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right)
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .add(R.id.root_login, mCurrentFragment.getFragment()).commit();
                }

            }else{
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_login, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {}
    }

    @Override
    public void change(int fragment_element) {
        changeOfInstance(fragment_element);
    }

    @Override
    public void onback() {
        changeOfInstance(FragmentElement.INSTANCE_MAIN_LOGIN);
    }

    @Override
    public void open_sheet(PetBean petBean, int is_me) {

    }




    @Override
    public void open_wizard_pet() {

    }

    @Override
    public void onBackPressed() {
            changeOfInstance(FragmentElement.INSTANCE_MAIN_LOGIN);
    }

    @Override
    public void onLoginSuccess(boolean success) {

    }

    @Override
    public void loginWithGmail() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void loginWithFacebook() {
        permision_location();
    }


    @SuppressLint("CheckResult")
    void permision_location(){
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        getLocation();
                    }
                });
    }
    @Override
    public void loginWithEmail(String correo, String password) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("CURRENT_USER","-->" + user.getEmail());

            mAuth.signInWithEmailAndPassword(correo, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()) {
                                App.write(PREFERENCES.UUID, user.getUid());
                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, instanceIdResult -> {
                                    String token = instanceIdResult.getToken();
                                    App.write(PREFERENCES.TOKEN, token);
                                    generate_user_bean();
                                });
                            }else{
                                Toast.makeText(LoginActivity.this, "VERIFICA TU CORREO", Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("EEROR_LOGIN", "-->" + e.getMessage());
                        if (e.getMessage().contains("badly formatted")) {
                            Toast.makeText(LoginActivity.this, "Correo no valido", Toast.LENGTH_LONG).show();
                        } else if (e.getMessage().contains("There is no user")) {
                            Toast.makeText(LoginActivity.this, "Usuario no existe", Toast.LENGTH_LONG).show();
                        } else if (e.getMessage().contains("The password is invalid")) {
                            Toast.makeText(LoginActivity.this, "Revisa tus credenciales", Toast.LENGTH_LONG).show();
                        }

                    });
        permision_location();

       /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(correo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG_IDENTITY", "User email address updated.");
                        }
                    }
                });
        */
    }

    @Override
    public void sigInWithEmail(String correo, String password) {
        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user!=null){
                            App.write(PREFERENCES.NAME_USER,user.getDisplayName());
                        }
                        user.sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,"SE ENVIO UN CORREO DE VERIFICACION",Toast.LENGTH_LONG).show();
                                        changeOfInstance(FragmentElement.INSTANCE_MAIN_LOGIN);
                                    }
                                });
                    } else {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                App.getInstance().vibrate();
                Log.e("EEROR_LOGIN","-->" + e.getMessage());
                if(e.getMessage().contains("badly formatted")){
                    Toast.makeText(LoginActivity.this, "Correo no valido", Toast.LENGTH_LONG).show();
                }else if(e.getMessage().contains("There is no user")){
                    Toast.makeText(LoginActivity.this, "Usuario no existe" , Toast.LENGTH_LONG).show();
                }else if(e.getMessage().contains("The password is invalid")){
                    Toast.makeText(LoginActivity.this, "Revisa tus credenciales" , Toast.LENGTH_LONG).show();
                }
                else if(e.getMessage().contains("use by another account")){
                    Toast.makeText(LoginActivity.this, "Usuario ya existente" , Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else if(requestCode == NEW_PHOTO_UPLOADED){
            if(data!=null) {
                String url =  data.getStringExtra(BUNDLES.URI_FOTO);
                if (mCurrentFragment.getFragment() instanceof FragmentFirstUser) {
                    ((FragmentFirstUser) mCurrentFragment.getFragment()).change_image_profile(url);
                }
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            permision_location();
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
            Log.e("ERROR_LOGIN","-->TODO BIEN"  );
        } catch (ApiException e) {
            Log.e("ERROR_LOGIN","-->" + e.getMessage());
            // updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        App.write(PREFERENCES.UUID,user.getUid());
                        Log.e("ERROR_LOGIN","-->TODO BIEN" +user.getUid());
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                            String token = instanceIdResult.getToken();
                            App.write(PREFERENCES.TOKEN,token);
                            Log.e("ERROR_LOGIN","-->TODO BIEN" +token);
                            generate_user_bean();
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Intente de nuevo", Toast.LENGTH_LONG).show();

                    }
                });
    }

    void generate_user_bean(){
        dialogLoanding.show();
        UserBean userBean = new UserBean();
        userBean.setDescripcion("-");
        userBean.setIds_pets("-");
        userBean.setName_user("-");
        userBean.setLat(App.read(PREFERENCES.LAT,(float)0));
        userBean.setLon(App.read(PREFERENCES.LON,(float)0));
        userBean.setToken(App.read(PREFERENCES.TOKEN,"INVALID"));
        userBean.setTarget("LOGIN");
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        userBean.setAndroid_id(Utilities.Md5Hash(new AndroidIdentifier(LoginActivity.this).generateCombinationID()));
        userBean.setPhone_user("0000000");
        userBean.setPhoto_user(App.read(PREFERENCES.FOTO_PROFILE_USER,"INVALID"));
        presenter.RegisterUser(userBean);
    }


    void getLocation(){
        LocationServices.getFusedLocationProviderClient(LoginActivity.this).getLastLocation().addOnSuccessListener(location -> {
            if(location!=null){
                Log.e("LOCALIZACION","-->" + location.getLatitude()  + "/" + location.getLongitude());
                if(location!=null){
                    App.write(PREFERENCES.LAT,(float)location.getLatitude());
                    App.write(PREFERENCES.LON,(float)location.getLongitude());
                }
            }
        });
    }

    @Override
    public void registerCompleted() {
           startService(intent_service);
            App.write(IS_LOGUEDD,true);
            Intent i ;
            i = new Intent(LoginActivity.this, Main.class);
            startActivity(i);

    }

    @Override
    public void loginCompleted(UserBean userBean) {
        startService(intent_service);
        App.write(PREFERENCES.FOTO_PROFILE_USER,userBean.getPhoto_user());
        App.write(PREFERENCES.FOTO_PROFILE_USER_THUMBH,userBean.getPhoto_user_thumbh());
        App.write(PREFERENCES.DESCRIPCCION,userBean.getDescripcion());
        App.write(PREFERENCES.NAME_USER,userBean.getName_user());
        App.write(PREFERENCES.ID_USER_FROM_WEB,userBean.getId());
        App.write(IS_LOGUEDD,true);
        Intent i ;
        i = new Intent(LoginActivity.this, Main.class);
        i.putExtra(BUNDLES.DOWNLOADS_INFO,1);
        startActivity(i);
    }

    @Override
    public void registerError() {

    }

    @Override
    public void isFirstUser(int id_from_web) {
        App.write(PREFERENCES.ID_USER_FROM_WEB,id_from_web);
        App.write(PREFERENCES.IS_FIRST_USER,true);
        try {
            dialogLoanding.dismiss();
        }catch (Exception e){}
        changeOfInstance(FragmentElement.INSTANCE_NEW_USER_CONFIG);
    }

    @Override
    public void UpdateFirsUserCompleted() {
        startService(intent_service);
        try {
            dialogLoanding.dismiss();
        }catch (Exception e){}
        App.write(IS_LOGUEDD,true);
        Intent i ;
        i = new Intent(LoginActivity.this, Main.class);
        startActivity(i);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onImageProfileUpdated() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        Intent i = new Intent(LoginActivity.this, ShareActivity.class);
                        i.putExtra("FROM","PROFILE_PHOTO");
                        startActivityForResult(i,NEW_PHOTO_UPLOADED);
                    } else {
                        App.getInstance().show_dialog_permision(LoginActivity.this,getResources().getString(R.string.permision_storage),
                                getResources().getString(R.string.permision_storage_body),0);
                    }
                });
    }

    @Override
    public void setResultForOtherChanges(String url) {

    }

    @Override
    public void UpdateProfile(Bundle bundle) {
        App.write(PREFERENCES.FOTO_PROFILE_USER,App.getInstance().make_uri_bucket_profile());
        upDateUserBean();
        ArrayList<String> uri_profile = new ArrayList<>();
        uri_profile.add(bundle.getString(BUNDLES.PHOTO_LOCAL));
        Intent intent = new Intent(LoginActivity.this, ImageService.class);
        intent.putStringArrayListExtra(ImageService.INTENT_KEY_NAME, uri_profile);
        intent.putExtra(BUNDLES.NOTIFICATION_TIPE,1);
        intent.putExtra(ImageService.INTENT_TRANSFER_OPERATION, ImageService.TRANSFER_OPERATION_UPLOAD);
        startService(intent);
    }

    @Override
    public void new_pet() {

    }


    void upDateUserBean(){
        UserBean userBean = new UserBean();
        userBean.setDescripcion(App.read(PREFERENCES.DESCRIPCCION,"INVALID"));
        userBean.setName_user(App.read(PREFERENCES.NAME_USER,"INVALID"));
        userBean.setPhoto_user(App.read(PREFERENCES.FOTO_PROFILE_USER,"INVALID"));
        userBean.setPhoto_user_thumbh(App.read(PREFERENCES.FOTO_PROFILE_USER_THUMBH,"INVALID"));
        userBean.setUuid(App.read(PREFERENCES.UUID,"INVALID"));
        userBean.setTarget("UPDATE");
        try {
            dialogLoanding.show();
        }catch (Exception e){}
        presenter.updateUser(userBean);
    }


}
