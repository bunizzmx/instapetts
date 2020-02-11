package com.bunizz.instapetts.fragments.wizardPets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.utils.rulepicker.RulerValuePicker;
import com.bunizz.instapetts.utils.rulepicker.RulerValuePickerListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentDataPet extends Fragment {

    @BindView(R.id.height_ruler_picker)
    RulerValuePicker     height_ruler_picker;

    @BindView(R.id.txt_peso_picker)
    TextView txt_peso_picker;

    public static FragmentDataPet newInstance() {
        return new FragmentDataPet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        height_ruler_picker.selectValue(20);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //listener= (change_instance) context;
    }
}
