package com.bunizz.instapetts.fragments.login.first_user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.search.tabs.pets.FragmentPetList;
import com.bunizz.instapetts.fragments.search.tabs.users.FragmentPopietaryList;
import com.bunizz.instapetts.listeners.change_instance;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.web.CONST;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FragmentFirstUser extends Fragment implements  FirstUserContract.View{



    change_instance listener;
    uploads listener_uploads;

    @BindView(R.id.configure_name)
    EditText configure_name;

    @BindView(R.id.descripcion_user)
    EditText descripcion_user;

    @BindView(R.id.user_instapetts)
    EditText user_instapetts;

    @BindView(R.id.image_userd_edit)
    ImageView image_userd_edit;

    @BindView(R.id.status_icon_name_tag)
    ImageView status_icon_name_tag;

    @BindView(R.id.label_tag_instapets)
    TextView label_tag_instapets;



    String URL_UPDATED="INVALID";
    String URL_LOCAL="INVALID";

    FirstUserPresenter firstUserPresenter;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.change_photo)
    void change_photo()
    {
        App.write(PREFERENCES.FROM_PICKER,"PROFILE");
        listener_uploads.onImageProfileUpdated("PROFILE_PHOTO");
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.save_info_perfil)
    void save_info_perfil()
    {
        if(!descripcion_user.getText().toString().isEmpty() && !configure_name.getText().toString().isEmpty() && !URL_LOCAL.equals("INVALID") && user_instapetts.getText().toString().trim().length() > 3) {
            String URI_FINAL = App.getInstance().make_uri_bucket_profile();
            String URI_FINAL_THUMBH = App.getInstance().make_uri_bucket_profile_tumbh();
            App.write(PREFERENCES.DESCRIPCCION, descripcion_user.getText().toString());
            App.write(PREFERENCES.FOTO_PROFILE_USER, URI_FINAL);
            App.write(PREFERENCES.FOTO_PROFILE_USER_THUMBH, URI_FINAL_THUMBH);
            App.write(PREFERENCES.NAME_USER, configure_name.getText().toString());
            Bundle b = new Bundle();
            b.putString("DESCRIPCION", descripcion_user.getText().toString());
            b.putString("PHOTO", URI_FINAL);
            b.putString("PHOTO_LOCAL", URL_LOCAL);
            App.write("NAME_TAG_INSTAPETTS",user_instapetts.getText().toString().trim());
            listener_uploads.UpdateProfile(b);
        }else{
            if(descripcion_user.getText().toString().length() < 5){
                Toast.makeText(getContext(),getContext().getString(R.string.name_invalid),Toast.LENGTH_LONG).show();
            }
            if(URL_LOCAL.equals("INVALID")){
                Toast.makeText(getContext(),getContext().getString(R.string.chose_photo),Toast.LENGTH_LONG).show();
            }
            if(user_instapetts.getText().toString().trim().length() <= 3){
                status_icon_name_tag.setVisibility(View.VISIBLE);
                status_icon_name_tag.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_no_available));
                label_tag_instapets.setVisibility(View.VISIBLE);
                label_tag_instapets.setText(getContext().getString(R.string.more_caracteres));
            }
        }
    }



    public static FragmentFirstUser newInstance() {
        return new FragmentFirstUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstUserPresenter = new FirstUserPresenter(this,getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_user, container, false);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(  App.read(PREFERENCES.NAME_USER,"-").equals("-")){
        }else{
            configure_name.setText(App.read(PREFERENCES.NAME_USER,"-"));
        }

        RxTextView.textChanges(user_instapetts)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    Log.e("CHAR_SEARCH",charSequence.toString());
                    if(charSequence.toString().trim().length() > 3) {
                        status_icon_name_tag.setVisibility(View.GONE);
                        label_tag_instapets.setVisibility(View.GONE);
                        firstUserPresenter.getNameAvailable(charSequence.toString().trim());
                    }
                    else {
                        label_tag_instapets.setVisibility(View.VISIBLE);
                        label_tag_instapets.setText(getContext().getString(R.string.more_caracteres));
                    }
                });


        user_instapetts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TEXT_CAMBIADO","-->:" + s + "/" + start + "/" + before +"/" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void change_image_profile(String url){
        URL_LOCAL = url;
        String splits[] = url.split("/");
        int index = splits.length;
        URL_UPDATED = splits[index - 1];
        Glide.with(getContext()).load(url).into(image_userd_edit);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
        listener_uploads =(uploads)context;
    }

    @Override
    public void showUsersAvailables(boolean available) {
        Log.e("AVAILABLE_NAME","--->:" + available);
        if(available)
            status_icon_name_tag.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_available));
        else
            status_icon_name_tag.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_no_available));
        status_icon_name_tag.setVisibility(View.VISIBLE);
    }
}
