package com.bunizz.instapetts.activitys.searchingPets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.wizardPets.adapters.SearchRazaAdapter;
import com.bunizz.instapetts.listeners.change_instance_wizard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchPetActivity extends Activity {

    @BindView(R.id.searching_results_list_pets)
    RecyclerView list_pets;

    SearchPetAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_searching_pet);
        ButterKnife.bind(this);
        changeStatusBarColor(R.color.white);
        adapter = new SearchPetAdapter(this);
        list_pets.setLayoutManager(new LinearLayoutManager(this));
        list_pets.setAdapter(adapter);

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

}