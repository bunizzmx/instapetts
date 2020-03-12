package com.bunizz.instapetts.activitys.login;

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
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.login.MainLogin;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.login.sigin.FragmentSigin;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.login_listener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import static com.bunizz.instapetts.constantes.PREFERENCES.IS_LOGUEDD;

public class LoginActivity extends AppCompatActivity implements change_instance, login_listener {

    private Stack<FragmentElement> stack_login;
    private Stack<FragmentElement> stack_sigin;
    private Stack<FragmentElement> stack_main_login;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        changeStatusBarColor(R.color.white);
        stack_sigin = new Stack<>();
        stack_main_login = new Stack<>();
        stack_login = new Stack<>();
        mAuth = FirebaseAuth.getInstance();
        setupFirstFragment();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
        mCurrentFragment = new FragmentElement<>(null, MainLogin.newInstance(), FragmentElement.INSTANCE_MAIN_LOGIN, true);
        change_main(mCurrentFragment);
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
    public void open_sheet() {

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
        if(App.read("IS_NEW_USER",false)){

        }else{
            App.write(IS_LOGUEDD,true);
            Intent i ;
            i = new Intent(LoginActivity.this, Main.class);
            startActivity(i);
        }
    }

    @Override
    public void loginWithGmail() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void loginWithFacebook() {

    }

    @Override
    public void loginWithEmail(String correo, String password) {

        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("LOG_IDENTITY", "Email sent.");
                                            }
                                        }
                                    });
                            //updateUI(user);
                        } else {
                            Log.d("AUJHFKJH", "createUserWithEmail:success" + task.getException().getMessage());
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
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
                        App.write("UUID",user.getUid());
                        Log.e("ERROR_LOGIN","-->TODO BIEN" +user.getUid());
                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                            String token = instanceIdResult.getToken();
                            App.write("TOKEN",token);
                            Log.e("ERROR_LOGIN","-->TODO BIEN" +token);
                        });
                    } else {
                        App.sonar(3);
                        Toast.makeText(LoginActivity.this, "Intente de nuevo", Toast.LENGTH_LONG).show();

                    }
                });
    }

}
