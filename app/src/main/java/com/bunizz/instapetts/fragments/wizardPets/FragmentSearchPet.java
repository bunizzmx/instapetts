package com.bunizz.instapetts.fragments.wizardPets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.activitys.main.Main;
import com.bunizz.instapetts.activitys.wizardPets.WizardPetActivity;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.wizardPets.adapters.SearchRazaAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSearchPet extends Fragment {


    @BindView(R.id.list_words_search)
    RecyclerView list_words_search;

    @SuppressLint("MissingPermission")
    @OnClick(R.id.layout_no_se_raza)
    void layout_no_se_raza()
    {
        if(listener!=null){
            Bundle b = new Bundle();
            listener.onchange(FragmentElement.INSTANCE_DATA_PET,b);
        }
        //changeOfInstance(FragmentElement.INSTANCE_PROFILE_PET);
    }


    SearchRazaAdapter adapter;
    change_instance_wizard listener;

    public static FragmentSearchPet newInstance() {
        return new FragmentSearchPet();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchRazaAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_raza_pet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_words_search.setLayoutManager(new LinearLayoutManager(getContext()));
        list_words_search.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance_wizard) context;
    }
}
