package com.bunizz.instapetts.fragments.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bunizz.instapetts.App;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.wizardPets.WizardPetActivity;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.feed.FeedContract;
import com.bunizz.instapetts.listeners.RatePetListener;
import com.bunizz.instapetts.listeners.conexion_listener;
import com.bunizz.instapetts.listeners.delete;
import com.bunizz.instapetts.listeners.uploads;
import com.bunizz.instapetts.services.ImageService;
import com.bunizz.instapetts.utils.ImagenCircular;
import com.bunizz.instapetts.utils.dilogs.DialogDeletes;
import com.bunizz.instapetts.utils.dilogs.DialogPreviewImage;
import com.bunizz.instapetts.utils.dilogs.DialogRatePet;
import com.bunizz.instapetts.utils.snackbar.SnackBar;

import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bunizz.instapetts.constantes.BUNDLES.PETBEAN;

public class InfoPetFragment extends Fragment implements InfoPetContract.View {


    public static InfoPetFragment newInstance() {
        return new InfoPetFragment();
    }

    String ID_PET = "";
    String NAME_PET = "";
    PetHelper petHelper;
    PetBean petBean;

    @BindView(R.id.pet_name_profile)
    TextView pet_name_profile;

    @BindView(R.id.name_property_pet_profile)
    TextView name_property_pet_profile;

    @BindView(R.id.descripcion_pet_profile)
    TextView descripcion_pet_profile;

    @BindView(R.id.peso_pet_profile)
    TextView peso_pet_profile;

    @BindView(R.id.image_pet_info)
    ImagenCircular image_pet_info;

    @BindView(R.id.icon_star_rated)
    ImageView icon_star_rated;

    @BindView(R.id.stars_pet)
    TextView stars_pet;

    @BindView(R.id.delete_trash)
    RelativeLayout delete_trash;

    @BindView(R.id.acerca_de_pet)
    TextView acerca_de_pet;

    @BindView(R.id.name_raza_pet)
    TextView name_raza_pet;

    @BindView(R.id.edad_pet)
    TextView edad_pet;

    @BindView(R.id.icon_type_pet_info_pet)
    ImageView icon_type_pet_info_pet;

    @BindView(R.id.icon_genero_pet)
    ImageView icon_genero_pet;

    @BindView(R.id.rate_pet_card)
    CardView rate_pet_card;

    @BindView(R.id.edit_photo_pet)
    CardView edit_photo_pet;

    @BindView(R.id.root_imagen_and_pencil)
    RelativeLayout root_imagen_and_pencil;

    @BindView(R.id.new_peso_pet)
    EditText new_peso_pet;

    @BindView(R.id.new_descripcion_pet)
    EditText new_descripcion_pet;
    InfoPetPresenter presenter;
    boolean IN_EDICION = false;

    uploads listener;

    String REFRESH_IMAGE ="INVALID";
    conexion_listener  listener_conexion;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.rate_pet_card)
    void rate_pet_card() {
        if(petBean.getId_propietary() ==  App.read(PREFERENCES.ID_USER_FROM_WEB,0)){
            if(IN_EDICION == false) {
                IN_EDICION = true;
                icon_star_rated.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_available));
            } else{
                icon_star_rated.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_editar));
                IN_EDICION = false;
            }
            if(IN_EDICION) {
                peso_pet_profile.setVisibility(View.GONE);
                descripcion_pet_profile.setVisibility(View.GONE);
                new_peso_pet.setVisibility(View.VISIBLE);
                new_descripcion_pet.setVisibility(View.VISIBLE);
                new_peso_pet.setText("" + petBean.getPeso_pet());
                new_descripcion_pet.setText("" + petBean.getDescripcion_pet());
            }else{
                petBean.setPeso_pet(new_peso_pet.getText().toString());
                petBean.setDescripcion_pet(new_descripcion_pet.getText().toString());
                peso_pet_profile.setText(new_peso_pet.getText().toString() + " kg");
                descripcion_pet_profile.setText(new_descripcion_pet.getText().toString());
                peso_pet_profile.setVisibility(View.VISIBLE);
                descripcion_pet_profile.setVisibility(View.VISIBLE);
                new_peso_pet.setVisibility(View.GONE);
                new_descripcion_pet.setVisibility(View.GONE);
                presenter.updatePet(petBean);
            }
        }else{
            DialogRatePet dialogRatePet = new DialogRatePet(getContext(),petBean);
            dialogRatePet.setListener((rate, comment, id_pet, id_usuario, uuid) -> {
                icon_star_rated.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_estrella_black));
                presenter.ratePet(Integer.valueOf(petBean.getId_pet()),(int)rate);
                listener_conexion.refreshedComplete();
            });
            dialogRatePet.show();

        }

    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.delete_trash)
    void delete_trash() {
        DialogDeletes  delete_pet = new DialogDeletes(getContext(),0,3);
        delete_pet.setListener(new delete() {
            @Override
            public void delete(boolean delete) {
                presenter.delete(Integer.parseInt(petBean.getId_pet()));
                delete_pet.dismiss();
            }
            @Override
            public void deleteOne(int id) {

            }
        });
        delete_pet.show();
    }


    public void refresh_data_on_pet(String url) {
        REFRESH_IMAGE = url;
        Log.e("REFRESH_OFTO_PET","-->A: " + url+"/" + petBean.getName_pet());
        Glide.with(getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
       .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder))
        .error(getContext().getResources().getDrawable(R.drawable.ic_holder))
                .into(image_pet_info);
        UploadImagePet(REFRESH_IMAGE,petBean.getName_pet());
    }



  boolean IS_ME=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        presenter = new InfoPetPresenter(this,getContext());
        if(bundle!=null){
            int is_who = bundle.getInt(BUNDLES.IS_ME);
                    if(is_who == 0)
                        IS_ME = true;
                    else
                        IS_ME = false;
            petBean =  Parcels.unwrap(bundle.getParcelable(PETBEAN));
        }
        petHelper = new PetHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        pet_name_profile.setText(petBean.getName_pet());
        descripcion_pet_profile.setText(petBean.getDescripcion_pet());
        peso_pet_profile.setText(petBean.getPeso_pet() + "kg");
        Log.e("CARGO_TUMBH","perror : " + petBean.getUrl_photo_tumbh());
        Glide.with(getContext()).load(petBean.getUrl_photo_tumbh())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
        .placeholder(getContext().getResources().getDrawable(R.drawable.ic_holder)).error(getContext().getResources().getDrawable(R.drawable.ic_holder)).into(image_pet_info);
        stars_pet.setText(String.format("%.2f", petBean.getRate_pet()));
        Log.e("PENDDD","-->" + petBean.getId_propietary() + "/" + App.read(PREFERENCES.ID_USER_FROM_WEB,0) );
        Log.e("ID_PETXX","-->" + petBean.getId_propietary() + "/" + App.read(PREFERENCES.ID_USER_FROM_WEB,0));
        if(petBean.getId_propietary() == App.read(PREFERENCES.ID_USER_FROM_WEB,0)){
            name_property_pet_profile.setText("@" + App.read(PREFERENCES.NAME_USER,"INVALID"));
            delete_trash.setVisibility(View.VISIBLE);
            icon_star_rated.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_editar));
            edit_photo_pet.setVisibility(View.VISIBLE);
            root_imagen_and_pencil.setOnClickListener(v -> {
                App.write(PREFERENCES.FROM_PICKER,"PROFILE");
                listener.onImageProfileUpdated("PROFILE_PHOTO_PET");
            });
        }else{
            name_property_pet_profile.setText("@" + petBean.getName_propietary());
            rate_pet_card.setVisibility(View.VISIBLE);
            delete_trash.setVisibility(View.GONE);
            edit_photo_pet.setVisibility(View.GONE);
        }
        acerca_de_pet.setText(getContext().getString(R.string.about_pet) + " " +  petBean.getName_pet());
        switch (petBean.getType_pet()){
            case 1:
                icon_type_pet_info_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_perro));
                break;
            case 2:
                icon_type_pet_info_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_gato));
                break;
            case 3:
                icon_type_pet_info_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_mascota_perico));
                break;
            case 4:
                icon_type_pet_info_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_mascota_conejo));
                break;
            case 5:
                icon_type_pet_info_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_mascota_hamster));
                break;
            case 6:
                icon_type_pet_info_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_serpiente));
                break;
            case 7:
                icon_type_pet_info_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_otro));break;
            case 8:break;
            case 9:break;
            case 10:break;
            case 11:break;
            case 12:break;
            case 13:break;
            case 14:break;
            case 15:break;
            case 16:break;
            default:break;

        }
        if(petBean.getRaza_pet() == null ){
            name_raza_pet.setText(getContext().getString(R.string.indeterminate));
        }else{
            if(petBean.getRaza_pet().trim().isEmpty() || petBean.getRaza_pet().equals("undefined")){
                name_raza_pet.setText(getContext().getString(R.string.indeterminate));
            }else{
                name_raza_pet.setText(petBean.getRaza_pet());
            }
        }

        if(petBean.getGenero_pet().equals("M")){
            icon_genero_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_macho));
        }else{
            icon_genero_pet.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_hembra));
        }

        edad_pet.setText(App.getInstance().fecha_lenguaje_humano(petBean.getEdad_pet().replace("T"," ").replace("Z","")));

        image_pet_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPreviewImage dialogPreviewImage = new DialogPreviewImage(getContext(),petBean.getUrl_photo());
                dialogPreviewImage.show();
            }
        });

    }

    @Override
    public void petRated() {

    }

    @Override
    public void petUpdated() {
        Toast.makeText(getActivity(),getContext().getString(R.string.pet_updated),Toast.LENGTH_LONG).show();
    }

    @Override
    public void petDeleted() {
        Log.e("PET_DELETED","SI");
        listener_conexion.message("Mascota eliminada");
        getActivity().onBackPressed();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (uploads) context;
        listener_conexion =(conexion_listener)context;
    }

    void UploadImagePet(String url,String name_pet){
        ArrayList<String> uri_profile = new ArrayList<>();
        uri_profile.add(url);
        Intent intent = new Intent(getActivity(), ImageService.class);
        intent.putStringArrayListExtra(ImageService.INTENT_KEY_NAME, uri_profile);
        intent.putExtra(BUNDLES.NOTIFICATION_TIPE,3);
        intent.putExtra(BUNDLES.NAME_PET,name_pet);
        intent.putExtra(ImageService.INTENT_TRANSFER_OPERATION, ImageService.TRANSFER_OPERATION_UPLOAD);
        getActivity().startService(intent);
        Toast.makeText(getActivity(),getContext().getString(R.string.actualizando_foto),Toast.LENGTH_LONG).show();
        App.getInstance().delete_cache();
    }
}

