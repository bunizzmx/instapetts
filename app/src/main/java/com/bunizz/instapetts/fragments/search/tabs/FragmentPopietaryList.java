package com.bunizz.instapetts.fragments.search.tabs;

import android.content.Context;
import android.os.Bundle;
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
import com.bunizz.instapetts.beans.PropietaryBean;
import com.bunizz.instapetts.fragments.feed.FeedAdapter;
import com.bunizz.instapetts.fragments.search.SearchPetAdapter;
import com.bunizz.instapetts.listeners.change_instance;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPopietaryList  extends Fragment {



    @BindView(R.id.list_propietary_search_result)
    RecyclerView list_propietary_search_result;

    change_instance listener;
    SearchPetAdapter adapter;

    ArrayList<Object> data = new ArrayList<>();

    public static FragmentPopietaryList newInstance() {
        return new FragmentPopietaryList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data.add(new PropietaryBean("https://t1.ea.ltmcdn.com/es/images/6/4/2/la_educacion_de_un_pit_bull_cachorro_22246_600.jpg","Angel yardani",3));
        data.add(new PropietaryBean("https://www.zooplus.es/magazine/wp-content/uploads/2017/09/fotolia_165277191.jpg","Alan Natanael",3));
        data.add(new PropietaryBean("https://www.micachorro.net/wp-content/uploads/2018/02/cacne-corso.png","Ichxhel Alejnadra",3));
        data.add(new PropietaryBean("https://www.micachorro.net/wp-content/uploads/2018/02/cacne-corso.png","Pedro tlacuache",3));
        adapter = new SearchPetAdapter(getContext());
        adapter.setData(data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_propietary_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        list_propietary_search_result.setLayoutManager(new LinearLayoutManager(getContext()));
        list_propietary_search_result.setAdapter(adapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (change_instance) context;
    }
}