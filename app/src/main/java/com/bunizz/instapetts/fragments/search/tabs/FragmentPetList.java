package com.bunizz.instapetts.fragments.search.tabs;

import android.content.Context;
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
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.feed.FeedFragment;
import com.bunizz.instapetts.fragments.search.SearchPetAdapter;
import com.bunizz.instapetts.listeners.change_instance;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPetList extends Fragment {



    @BindView(R.id.list_pets_search_result)
    RecyclerView list_pets_search_result;

    change_instance listener;
    SearchPetAdapter adapter;

    ArrayList<Object> data = new ArrayList<>();

    public void setData(ArrayList<Object> data) {
        Log.e("REFRESH_DATA_SEARCH","--> data:" + data.size());
        this.data = data;
        adapter.setData(data);
    }

    public static FragmentPetList newInstance() {
        return new FragmentPetList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchPetAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fargment_pet_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_pets_search_result.setLayoutManager(new LinearLayoutManager(getContext()));
        list_pets_search_result.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
    }
}