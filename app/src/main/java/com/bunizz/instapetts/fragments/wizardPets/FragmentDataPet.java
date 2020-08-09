package com.bunizz.instapetts.fragments.wizardPets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.constantes.BUNDLES;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.change_instance_wizard;
import com.bunizz.instapetts.listeners.process_save_pet_listener;
import com.bunizz.instapetts.utils.datepicker.date.DatePickerDialogFragment;
import com.bunizz.instapetts.utils.rulepicker.RulerValuePicker;
import com.bunizz.instapetts.utils.rulepicker.RulerValuePickerListener;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bunizz.instapetts.utils.TYPE_PETS.CONEJO;
import static com.bunizz.instapetts.utils.TYPE_PETS.GATO;
import static com.bunizz.instapetts.utils.TYPE_PETS.HAMSTER;
import static com.bunizz.instapetts.utils.TYPE_PETS.OTRO;
import static com.bunizz.instapetts.utils.TYPE_PETS.PAJARO;
import static com.bunizz.instapetts.utils.TYPE_PETS.PERRO;

public class FragmentDataPet extends Fragment {

    @BindView(R.id.height_ruler_picker)
    RulerValuePicker     height_ruler_picker;

    @BindView(R.id.txt_peso_picker)
    TextView txt_peso_picker;

    @BindView(R.id.factor_peso)
    TextView factor_peso;

    @BindView(R.id.card_age)
    CardView card_age;

    @BindView(R.id.date_selected)
    TextView date_selected;

    @BindView(R.id.card_hembra)
    CardView card_hembra;

    @BindView(R.id.card_macho)
    CardView card_macho;

    @BindView(R.id.icon_hembra)
    ImageView icon_hembra;

    @BindView(R.id.icon_macho)
    ImageView icon_macho;
    int GENERO =0;
    DatePickerDialogFragment datePickerDialogFragment;

    String FECHA_CUMPLEAN ="NULO";


    @SuppressLint("MissingPermission")
    @OnClick(R.id.back_to_main)
    void back_to_main()
    {
        getActivity().onBackPressed();
    }

    @SuppressLint("MissingPermission")
    @OnClick(R.id.next_finalize)
    void finalice_pet()
    {
        if(listener!=null){
            Bundle b = new Bundle();
            b.putString(BUNDLES.PESO_PET,String.valueOf(height_ruler_picker.getCurrentValue()));
            if(GENERO == 1)
            b.putString(BUNDLES.GENERO_PET,"M");
            else if(GENERO == 2)
              b.putString(BUNDLES.GENERO_PET,"H");

            b.putString(BUNDLES.EDAD_PET,FECHA_CUMPLEAN);
            listener_pet_config.SaveDataPet(b,3);
            if(GENERO == 0){
                Toast.makeText(getContext(),getContext().getString(R.string.seleciona_genero),Toast.LENGTH_LONG).show();
            }else{
                if(date_selected.getText().toString().isEmpty() || FECHA_CUMPLEAN.equals("NULO"))
                    Toast.makeText(getContext(),getContext().getString(R.string.selecciona_fecha),Toast.LENGTH_LONG).show();
                else {
                    listener_pet_config.SaveDataPet(b,3);
                    listener.onchange(FragmentElement.INSTANCE_FINAL_CONFIG_PET, null);
                }
            }
        }
    }





    int tipe_pet=0;
    String FACTOR = "kg";
    process_save_pet_listener listener_pet_config;
    change_instance_wizard listener;


    public static FragmentDataPet newInstance() {
        return new FragmentDataPet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if(bundle!=null){
            tipe_pet = bundle.getInt("DOG");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_pets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        height_ruler_picker.setIndicatorHeight(0.5f, 0.2f);
        height_ruler_picker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(final int selectedValue) {
                txt_peso_picker.setText(selectedValue + "");
            }

            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                txt_peso_picker.setText(selectedValue + "");
            }
        });
        switch (tipe_pet){
            case PERRO:
                height_ruler_picker.setMinMaxValue(1,30);
                height_ruler_picker.selectValue(5);
                break;
            case GATO:
                height_ruler_picker.setMinMaxValue(500,5000);
                height_ruler_picker.selectValue(500);
                break;
            case PAJARO:
                height_ruler_picker.setMinMaxValue(10,1000);
                height_ruler_picker.selectValue(100);
                break;
            case CONEJO:
                height_ruler_picker.setMinMaxValue(1,10);
                height_ruler_picker.selectValue(3);
                break;
            case HAMSTER:
                height_ruler_picker.setMinMaxValue(100,1000);
                height_ruler_picker.selectValue(200);
                break;
            default:
                height_ruler_picker.setMinMaxValue(1,50);
                height_ruler_picker.selectValue(10);
                break;
        }

        factor_peso.setText(FACTOR);
        card_age.setOnClickListener(view1 -> {
            datePickerDialogFragment= new DatePickerDialogFragment();
            datePickerDialogFragment.setOnDateChooseListener((year, month, day) -> {
                date_selected.setVisibility(View.VISIBLE);
                date_selected.setText(year + "-" + month + "-" + day);
                FECHA_CUMPLEAN = year+"-"+month+"-"+day;
            });
            datePickerDialogFragment.show(getFragmentManager(), "DatePickerDialogFragment");
        });

        card_hembra.setOnClickListener(view13 -> {
            GENERO = 2;
            card_hembra.setCardBackgroundColor(getContext().getResources().getColor(R.color.primary));
            card_macho.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
            icon_hembra.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_hembra_white));
            icon_macho.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_macho));
        });

        card_macho.setOnClickListener(view12 -> {
            GENERO = 1;
            card_macho.setCardBackgroundColor(getContext().getResources().getColor(R.color.primary));
            card_hembra.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
            icon_hembra.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_hembra));
            icon_macho.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_macho_white));
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance_wizard) context;
        listener_pet_config =(process_save_pet_listener)context;
    }
}
